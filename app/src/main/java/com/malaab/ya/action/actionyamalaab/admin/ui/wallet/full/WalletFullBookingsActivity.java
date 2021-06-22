package com.malaab.ya.action.actionyamalaab.admin.ui.wallet.full;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.BookingPlayer;
import com.malaab.ya.action.actionyamalaab.admin.events.OnEventItemClicked;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.details.UsersDetailsActivity;
import com.malaab.ya.action.actionyamalaab.admin.utils.ActivityUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;
import com.malaab.ya.action.actionyamalaab.admin.utils.DateTimeUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WalletFullBookingsActivity extends BaseActivity implements WalletFullBookingsMvpView {

    @BindView(R.id.header_txt_title)
    TextView header_txt_title;
    @BindView(R.id.header_btn_back)
    AppCompatImageButton header_btn_back;
    @BindView(R.id.header_btn_notifications)
    AppCompatImageButton header_btn_notifications;

    @BindView(R.id.mCollapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.txt_total)
    TextView txt_total;
    @BindView(R.id.txt_totalBooking)
    TextView txt_totalBooking;
    @BindView(R.id.mToolbar)
    Toolbar mToolbar;

    @BindView(R.id.pBar_loading)
    public ProgressBar pBar_loading;

    @BindView(R.id.rv_items)
    public RecyclerView rv_items;

    @Inject
    WalletFullBookingsMvpPresenter<WalletFullBookingsMvpView> mPresenter;

    private boolean isLoaded = false;
    private long startDatetime, endDatetime;
    private int week = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_full_bookings);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
    }

    @Override
    protected void initUI() {
        setSupportActionBar(mToolbar);

        header_btn_back.setVisibility(View.VISIBLE);
        header_btn_notifications.setVisibility(View.INVISIBLE); /* Just To fix UI (to center Title) */
        header_txt_title.setText(R.string.full);

        txt_total.setText(String.format(Locale.ENGLISH, getString(R.string.price), 0.00));
        txt_totalBooking.setText( 0+ " حجزوات" );

        mCollapsingToolbarLayout.setTitle(getString(R.string.current_week));

        rv_items.setHasFixedSize(true);
        rv_items.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        /* To enable smooth scroll on RecyclerView */
        ViewCompat.setNestedScrollingEnabled(rv_items, false);
    }


    @OnClick(R.id.header_btn_back)
    public void goBack() {
        onBackPressed();
    }

    @OnClick(R.id.btn_next)
    public void getNextWeek() {
        week++;

        startDatetime = DateTimeUtils.getWeekStartDate(week);
        endDatetime = DateTimeUtils.getWeekEndDate(week);

        mPresenter.getWalletFullBookings(startDatetime, endDatetime);

        if (DateTimeUtils.isDateBetweenDates(DateTimeUtils.getCurrentDatetime(), startDatetime, endDatetime)) {
            mCollapsingToolbarLayout.setTitle(getString(R.string.current_week));
        } else {
            mCollapsingToolbarLayout.setTitle(DateTimeUtils.getDatetime(startDatetime, DateTimeUtils.PATTERN_DATE_4, Locale.ENGLISH) + "  " + DateTimeUtils.getDatetime(endDatetime, DateTimeUtils.PATTERN_DATE_4, Locale.ENGLISH));
        }
    }

    @OnClick(R.id.btn_previous)
    public void getPreviousWeek() {
        week--;

        startDatetime = DateTimeUtils.getWeekStartDate(week);
        endDatetime = DateTimeUtils.getWeekEndDate(week);

        mPresenter.getWalletFullBookings(startDatetime, endDatetime);

        if (DateTimeUtils.isDateBetweenDates(DateTimeUtils.getCurrentDatetime(), startDatetime, endDatetime)) {
            mCollapsingToolbarLayout.setTitle(getString(R.string.current_week));
        } else {
            mCollapsingToolbarLayout.setTitle(DateTimeUtils.getDatetime(startDatetime, DateTimeUtils.PATTERN_DATE_4, Locale.ENGLISH) + "  " + DateTimeUtils.getDatetime(endDatetime, DateTimeUtils.PATTERN_DATE_4, Locale.ENGLISH));
        }
    }


    @Override
    public void showLoading() {
        rv_items.setVisibility(View.GONE);
        pBar_loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pBar_loading.setVisibility(View.GONE);
    }


    @Override
    public void onGetWalletFullBookings(List<Booking> bookings, float total,int totalBookings) {
        txt_total.setText(String.format(Locale.ENGLISH, getString(R.string.price), total));
        txt_totalBooking.setText( totalBookings+ " حجزوات" );

        WalletFullBookingsAdapter adapter = new WalletFullBookingsAdapter(bookings,this);
        rv_items.setAdapter(adapter);

        rv_items.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGetWalletFullBookingsEmpty() {
        txt_total.setText(String.format(Locale.ENGLISH, getString(R.string.price), 0.00));
        txt_totalBooking.setText( 0+ " حجزوات" );

        rv_items.setAdapter(null);
        rv_items.setVisibility(View.GONE);
    }


    @Override
    public void onNoInternetConnection() {
        onError(R.string.error_no_connection);
    }


    @Override
    public void onResume() {
        super.onResume();

        if (!isLoaded) {
            isLoaded = true;

            startDatetime = DateTimeUtils.getCurrentWeekStartDate();
            int timeLeft=100*60*60* Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            startDatetime=startDatetime-timeLeft;
            endDatetime = DateTimeUtils.getCurrentWeekEndDate();

            mPresenter.getWalletFullBookings(startDatetime, endDatetime);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventItemClicked(final OnEventItemClicked event) {
        Log.d("ttt","event");

        if (event.getItem() instanceof BookingPlayer) {
            Bundle bundle = new Bundle();
            Log.d("ttt","event.getItem() instanceof BookingPlayer");

            bundle.putString(Constants.INTENT_KEY_USER_UID, ((BookingPlayer) event.getItem()).uId);
            ActivityUtils.goTo(this, UsersDetailsActivity.class, false, bundle);

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        rv_items.setAdapter(null);
        mPresenter.onDetach();
        super.onDestroy();
    }
}

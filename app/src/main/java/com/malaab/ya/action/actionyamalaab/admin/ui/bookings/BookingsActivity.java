package com.malaab.ya.action.actionyamalaab.admin.ui.bookings;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.custom.rtlpager.RTLPagerAdapter;
import com.malaab.ya.action.actionyamalaab.admin.custom.rtlpager.RTLViewPager;
import com.malaab.ya.action.actionyamalaab.admin.custom.rtlpager.Tab;
import com.malaab.ya.action.actionyamalaab.admin.events.OnEventFilterByDatetime;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.bookings.full.FullBookingsFragment;
import com.malaab.ya.action.actionyamalaab.admin.ui.bookings.individual.IndividualBookingsFragment;
import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;
import com.malaab.ya.action.actionyamalaab.admin.utils.DateTimeUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BookingsActivity extends BaseActivity implements BookingsMvpView, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.header_txt_title)
    TextView header_txt_title;
    @BindView(R.id.header_btn_back)
    AppCompatImageButton header_btn_back;
    @BindView(R.id.header_btn_notifications)
    AppCompatImageButton header_btn_notifications;

    @BindView(R.id.tl_container)
    public TabLayout tl_container;
    @BindView(R.id.vp_content)
    public RTLViewPager vp_content;

    @BindView(R.id.fab_date)
    public FloatingActionButton fab_date;

    @Inject
    BookingsPresenter<BookingsMvpView> mPresenter;

    private static final int TAB_INDIVIDUAL = 1;
    private static final int TAB_Full = 2;

    private DatePickerDialog datePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
    }

    @Override
    protected void initUI() {
        header_btn_back.setVisibility(View.VISIBLE);
        header_btn_notifications.setVisibility(View.INVISIBLE); /* Just To fix UI (to center Title) */
        header_txt_title.setText(R.string.title_bookings_manage);

        Tab[] mTabs = new Tab[]{
                new Tab(TAB_INDIVIDUAL, getString(R.string.individual)) {
                    @Override
                    public Fragment getFragment() {
                        return new IndividualBookingsFragment();
                    }
                },
                new Tab(TAB_Full, getString(R.string.full)) {
                    @Override
                    public Fragment getFragment() {
                        return new FullBookingsFragment();
                    }
                }
        };

        RTLPagerAdapter mTabsAdapter = new RTLPagerAdapter(getSupportFragmentManager(), mTabs, true);
        vp_content.setAdapter(mTabsAdapter);
        vp_content.setRtlOriented(true);
        tl_container.setupWithViewPager(vp_content);

        vp_content.setCurrentItem(0);

        datePickerDialog = DatePickerDialog.newInstance(this);
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_1);

//        FragmentsAdapter mAdapter = new FragmentsAdapter(getSupportFragmentManager());
//        mAdapter.addFragment(new Fragment(), getString(R.string.individual));
//        mAdapter.addFragment(new (), getString(R.string.full));

//        vp_content.setOffscreenPageLimit(mAdapter.getCount() - 1);
//        vp_content.setAdapter(mAdapter);

//        vp_content.setRtlOriented(true);
//        tl_container.setupWithViewPager(vp_content);
    }


    @OnClick(R.id.header_btn_back)
    public void goBack() {
        onBackPressed();
    }

    @OnClick(R.id.fab_date)
    public void selectDate() {
        datePickerDialog.show(getFragmentManager(), Constants.DATEPICKER_TAG);
    }


    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        month = month + 1;
        String monthStr = "" + month;
        String dayStr = "" + day;

        if (month < 10)
            monthStr = "0" + month;

        if (day < 10)
            dayStr = "0" + day;

        long dateInMilli = DateTimeUtils.getMilliseconds(year + "-" + monthStr + "-" + dayStr + " 00:00:00");

        header_txt_title.setText(DateTimeUtils.changeDateFormat(new Date(dateInMilli), DateTimeUtils.PATTERN_DATE_3));

        EventBus.getDefault().post(new OnEventFilterByDatetime(dateInMilli));
    }


    @Override
    public void onNoInternetConnection() {
        onError(R.string.error_no_connection);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.onResume();
        fab_date.show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        hideKeyboard();
        mPresenter.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }
}

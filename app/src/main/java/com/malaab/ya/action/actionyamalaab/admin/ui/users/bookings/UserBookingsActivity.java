package com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings.past.PastBookingsFragment;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings.upcoming.UpcomingBookingsFragment;
import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;
import com.malaab.ya.action.actionyamalaab.owner.ui.adapter.FragmentsAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserBookingsActivity extends BaseActivity implements UserBookingsMvpView {

    @BindView(R.id.header_txt_title)
    TextView header_txt_title;
    @BindView(R.id.header_btn_back)
    AppCompatImageButton header_btn_back;
    @BindView(R.id.header_btn_notifications)
    AppCompatImageButton header_btn_notifications;

    @BindView(R.id.tl_container)
    public TabLayout tl_container;
    @BindView(R.id.vp_content)
    public ViewPager vp_content;

    @Inject
    UserBookingsMvpPresenter<UserBookingsMvpView> mPresenter;

    private UpcomingBookingsFragment upcomingBookingsFragment;
    private PastBookingsFragment pastBookingsFragment;

    private String userUid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bookings);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);

        if (getIntent() != null && getIntent().hasExtra(Constants.INTENT_KEY_USER_UID)) {
            userUid = getIntent().getStringExtra(Constants.INTENT_KEY_USER_UID);
        }
    }

    @Override
    protected void initUI() {
        header_btn_back.setVisibility(View.VISIBLE);
        header_btn_notifications.setVisibility(View.INVISIBLE); /* Just To fix UI (to center Title) */
        header_txt_title.setText(R.string.title_bookings_manage);

        Bundle bundle = new Bundle();
        bundle.putString(Constants.INTENT_KEY_USER_UID, userUid);

        upcomingBookingsFragment = new UpcomingBookingsFragment();
        upcomingBookingsFragment.setArguments(bundle);

        pastBookingsFragment = new PastBookingsFragment();
        pastBookingsFragment.setArguments(bundle);


        FragmentsAdapter mAdapter = new FragmentsAdapter(getSupportFragmentManager());
        mAdapter.addFragment(upcomingBookingsFragment, getString(R.string.upcoming_bookings));
        mAdapter.addFragment(pastBookingsFragment, getString(R.string.past_bookings));

        vp_content.setOffscreenPageLimit(mAdapter.getCount() - 1);
        vp_content.setAdapter(mAdapter);

        tl_container.setupWithViewPager(vp_content);
    }


    @OnClick(R.id.header_btn_back)
    public void goBack() {
        onBackPressed();
    }


    @Override
    public void onGetCurrentUser(User user) {
    }


    @Override
    public void onNoInternetConnection() {
        onError(R.string.error_no_connection);
    }


    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.onResume();
        mPresenter.getCurrentUserLocal();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mPresenter.onPause();
    }

    @Override
    public void onBackPressed() {
        if (vp_content.getCurrentItem() > 0) {
            vp_content.setCurrentItem(0, true);
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

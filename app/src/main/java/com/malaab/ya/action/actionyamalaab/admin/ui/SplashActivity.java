package com.malaab.ya.action.actionyamalaab.admin.ui;

import android.os.Bundle;

import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.annotations.LoginMode;
import com.malaab.ya.action.actionyamalaab.admin.data.DataManager;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.home.HomeActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.login.LoginActivity;
import com.malaab.ya.action.actionyamalaab.admin.utils.ActivityUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;


public class SplashActivity extends BaseActivity {

    @Inject
    public DataManager mDataManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        if (mDataManager.getCurrentUser() != null &&
                mDataManager.getCurrentUser().isActive &&
                mDataManager.getCurrentUser().loggedInMode != LoginMode.LOGGED_IN_MODE_LOGGED_OUT) {
            ActivityUtils.goTo(SplashActivity.this, HomeActivity.class, true);
        } else {
            ActivityUtils.goTo(SplashActivity.this, LoginActivity.class, true);
        }

        setUnBinder(ButterKnife.bind(this));
    }

    @Override
    protected void initUI() {
    }


    @Override
    public void onNoInternetConnection() {
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

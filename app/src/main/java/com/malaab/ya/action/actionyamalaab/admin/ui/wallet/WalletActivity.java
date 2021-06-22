package com.malaab.ya.action.actionyamalaab.admin.ui.wallet;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.wallet.full.WalletFullBookingsActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.wallet.individual.WalletIndividualBookingsActivity;
import com.malaab.ya.action.actionyamalaab.admin.utils.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WalletActivity extends BaseActivity {

    @BindView(R.id.header_txt_title)
    TextView header_txt_title;
    @BindView(R.id.header_btn_back)
    AppCompatImageButton header_btn_back;
    @BindView(R.id.header_btn_notifications)
    AppCompatImageButton header_btn_notifications;

    @BindView(R.id.btn_full)
    public Button btn_full;
    @BindView(R.id.btn_individual)
    public Button btn_individual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
    }

    @Override
    protected void initUI() {
        header_btn_back.setVisibility(View.VISIBLE);
        header_btn_notifications.setVisibility(View.INVISIBLE); /* Just To fix UI (to center Title) */
        header_txt_title.setText(R.string.title_wallet);
    }


    @OnClick(R.id.header_btn_back)
    public void goBack() {
        onBackPressed();
    }

    @OnClick(R.id.btn_full)
    public void openFullWallet() {
        ActivityUtils.goTo(WalletActivity.this, WalletFullBookingsActivity.class, false);
    }

    @OnClick(R.id.btn_individual)
    public void openIndividualWallet() {
        ActivityUtils.goTo(WalletActivity.this, WalletIndividualBookingsActivity.class, false);
    }


    @Override
    public void onNoInternetConnection() {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

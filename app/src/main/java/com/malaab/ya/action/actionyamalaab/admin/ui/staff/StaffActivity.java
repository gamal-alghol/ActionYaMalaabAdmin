package com.malaab.ya.action.actionyamalaab.admin.ui.staff;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.staff.add.StaffAddActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.staff.list.StaffListActivity;
import com.malaab.ya.action.actionyamalaab.admin.utils.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class StaffActivity extends BaseActivity {

    @BindView(R.id.header_txt_title)
    TextView header_txt_title;
    @BindView(R.id.header_btn_back)
    AppCompatImageButton header_btn_back;
    @BindView(R.id.header_btn_notifications)
    AppCompatImageButton header_btn_notifications;

    @BindView(R.id.btn_add)
    public Button btn_add;
    @BindView(R.id.btn_list)
    public Button btn_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
    }

    @Override
    protected void initUI() {
        header_btn_back.setVisibility(View.VISIBLE);
        header_btn_notifications.setVisibility(View.INVISIBLE); /* Just To fix UI (to center Title) */
        header_txt_title.setText(R.string.title_staff);
    }


    @OnClick(R.id.btn_add)
    public void addStaff() {
        ActivityUtils.goTo(StaffActivity.this, StaffAddActivity.class, false);
    }

    @OnClick(R.id.btn_list)
    public void viewStaff() {
        ActivityUtils.goTo(StaffActivity.this, StaffListActivity.class, false);
    }

    @OnClick(R.id.header_btn_back)
    public void goBack() {
        onBackPressed();
    }


    @Override
    public void onNoInternetConnection() {
        onError(R.string.error_no_connection);
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

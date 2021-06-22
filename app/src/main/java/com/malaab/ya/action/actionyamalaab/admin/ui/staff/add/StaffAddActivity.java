package com.malaab.ya.action.actionyamalaab.admin.ui.staff.add;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class StaffAddActivity extends BaseActivity implements StaffAddMvpView {

    @BindView(R.id.header_txt_title)
    TextView header_txt_title;
    @BindView(R.id.header_btn_back)
    AppCompatImageButton header_btn_back;
    @BindView(R.id.header_btn_notifications)
    AppCompatImageButton header_btn_notifications;

    @BindView(R.id.edt_first_name)
    public EditText edt_first_name;
    @BindView(R.id.edt_last_name)
    public EditText edt_last_name;
    @BindView(R.id.edt_phone)
    public EditText edt_phone;
    @BindView(R.id.edt_email)
    public EditText edt_email;
    @BindView(R.id.edt_password)
    public EditText edt_password;
    @BindView(R.id.edt_confirm_password)
    public EditText edt_confirm_password;

    @BindView(R.id.txt_message)
    public TextView txt_message;

    @BindView(R.id.btn_add)
    public Button btn_add;

    @Inject
    StaffAddMvpPresenter<StaffAddMvpView> mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_add);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
    }

    @Override
    protected void initUI() {
        header_btn_back.setVisibility(View.VISIBLE);
        header_btn_notifications.setVisibility(View.INVISIBLE); /* Just To fix UI (to center Title) */
        header_txt_title.setText(R.string.title_staff_add);

        mValidator.addField(edt_first_name)
                .addField(edt_last_name)
                .addField(edt_phone)
                .addField(edt_email)
                .addField(edt_password)
                .addField(edt_confirm_password);
    }


    @OnClick(R.id.btn_add)
    public void addStaff() {
        hideKeyboard();

        txt_message.setVisibility(View.GONE);

        if (mValidator.isValid()) {
            mPresenter.addStaffToServer(edt_first_name.getText().toString().trim(), edt_last_name.getText().toString().trim(),
                    edt_phone.getText().toString().trim(), edt_email.getText().toString().trim(),
                    edt_password.getText().toString().trim(), edt_confirm_password.getText().toString().trim());
        }
    }

    @OnClick(R.id.header_btn_back)
    public void goBack() {
        onBackPressed();
    }


    @Override
    public void onStaffAddedToServer(User user) {
        mPresenter.generateUserUniqueId(user);
    }

    @Override
    public void onUserUniqueIdGenerated(User user) {
        mPresenter.addStaffToeDatabase(user);
    }

    @Override
    public void onStaffAddedToDatabase(User user) {
        showMessage("تم اضافة " + user.getUserFullName() + " بنجاح");
        onBackPressed();
    }


    @Override
    public void onError(String message) {
        txt_message.setText(message);
        txt_message.setVisibility(View.VISIBLE);
        txt_message.requestFocus();
    }

    @Override
    public void onNoInternetConnection() {
        txt_message.setVisibility(View.GONE);
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

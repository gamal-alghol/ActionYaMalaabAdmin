package com.malaab.ya.action.actionyamalaab.admin.ui.login;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jackandphantom.blurimage.BlurImage;
import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.custom.BSheetLogin;
import com.malaab.ya.action.actionyamalaab.admin.custom.OnBottomSheetListener;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.home.HomeActivity;
import com.malaab.ya.action.actionyamalaab.admin.utils.ActivityUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.KeyboardUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.StringUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity implements LoginMvpView, OnBottomSheetListener {

    @BindView(R.id.img_background)
    public ImageView img_background;

    @BindView(R.id.txt_message)
    public TextView txt_message;

    @BindView(R.id.btn_login)
    public Button btn_login;
    @BindView(R.id.txt_loginByPhoneNumber)
    public TextView txt_loginByPhoneNumber;
    @BindView(R.id.btn_register)
    public Button btn_register;

    private BSheetLogin mBSheetLogin;


    @Inject
    LoginMvpPresenter<LoginMvpView> mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
    }

    @Override
    protected void initUI() {
        Glide.with(this)
                .load(R.drawable.img_background_new)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(img_background);

        mBSheetLogin = new BSheetLogin();
        mBSheetLogin.attachAndInit(this);
        mBSheetLogin.setOnBottomSheetListener(this);
    }


    @OnClick(R.id.btn_login)
    public void loginServer() {
        txt_message.setVisibility(View.GONE);
        mBSheetLogin.show();
    }

    @OnClick(R.id.txt_loginByPhoneNumber)
    public void loginByPhone() {
//        ActivityUtils.goTo(LoginActivity.this, LoginByPhoneActivity.class, false);
    }


//    @OnClick(R.id.txt_forgotPassword)
//    public void forgotPassword() {
//        txt_hint.setVisibility(View.GONE);
//        txt_message.setVisibility(View.GONE);
//
//        mBSheetForgotPassword.show();
//    }

//    @OnClick(R.id.txt_skip)
//    public void skip() {
//        if (extra != null) {
//            finish();
//        } else {
//            ActivityUtils.goTo(LoginActivity.this, HomeActivity.class, true);
//        }
//    }

    @OnClick(R.id.btn_register)
    public void register() {
//        ActivityUtils.goTo(LoginActivity.this, RegisterActivity.class, false);
    }


    @Override
    public void onPreClose() {

    }

    @Override
    public void onSlide(float slideOffset) {
        if (slideOffset == 0) {
            Glide.with(this)
                    .load(R.drawable.img_background_new)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(img_background);

            btn_login.setAlpha(1f);
            txt_loginByPhoneNumber.setAlpha(1f);
            btn_register.setAlpha(1f);

            return;
        }

//        float intensity = slideOffset * 100 / 25;
//        AppLogger.d("intensity = " + intensity);

        btn_login.setAlpha(1 - slideOffset);
        txt_loginByPhoneNumber.setAlpha(1 - slideOffset);
        btn_register.setAlpha(1 - slideOffset);

//        BlurImage.with(getApplicationContext())
//                .load(R.drawable.img_background_new)
//                .intensity(slideOffset * 100 / 25)
//                .Async(false)
//                .into(img_background);
    }

    @Override
    public void onBottomSheetExpanded(int bsheetId) {

    }

    @Override
    public void onBottomSheetCollapsed(@IdRes int bsheetId, Object... args) {
        if (bsheetId == R.id.bSheet_login) {
            String email = (String) args[0];
            String password = (String) args[1];

            mPresenter.doServerLogin(email, password);
        }
    }


    @Override
    public void showLoading() {
        super.showLoading();

        KeyboardUtils.hideSoftInput(this);

        btn_login.setAlpha(0.5f);
        btn_register.setAlpha(0.5f);

        btn_login.setEnabled(false);
        btn_register.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();

        btn_login.setAlpha(1f);
        btn_register.setAlpha(1f);

        btn_login.setEnabled(true);
        btn_register.setEnabled(true);
    }


    @Override
    public void onServerLoginSuccess(String userUId) {
        mPresenter.isUserExistInDB(userUId);
    }

    @Override
    public void onUserExistInDB(User user) {
//        if (extra != null) {
//            Intent returnIntent = new Intent();
//            returnIntent.putExtra(Constants.INTENT_KEY, extra);
//            setResult(Activity.RESULT_OK, returnIntent);
//
//            finish();
//
//        } else {
        ActivityUtils.goTo(LoginActivity.this, HomeActivity.class, true);
//        }

    }

    @Override
    public void onUserNotExistInDB() {

    }


    @Override
    public void onResetPassword(String message) {
//        mDialogConfirmation.withTitle(getString(R.string.reset_password))
//                .withMessage(message)
//                .withPositiveButton("OK")
//                .build()
//                .show();
    }

    @Override
    public void showMessage(String message) {
        if (!StringUtils.isEmpty(message)) {
            txt_message.setText(message);
            YoYo.with(Techniques.FadeInUp)
                    .duration(300)
                    .withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            txt_message.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).playOn(txt_message);

            btn_login.setAlpha(1f);
            btn_register.setAlpha(1f);

            btn_login.setEnabled(true);
            btn_register.setEnabled(true);
        }
    }


    @Override
    public void onNoInternetConnection() {
        onError(R.string.error_no_connection);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        hideKeyboard();
    }

    @Override
    public void onBackPressed() {
        if (!mBSheetLogin.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        mBSheetLogin.onDetach();
        mPresenter.onDetach();
        super.onDestroy();
    }

}

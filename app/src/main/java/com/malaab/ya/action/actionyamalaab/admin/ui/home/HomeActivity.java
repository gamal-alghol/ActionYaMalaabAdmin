package com.malaab.ya.action.actionyamalaab.admin.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;

import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.annotations.PermissionsCodes;
import com.malaab.ya.action.actionyamalaab.admin.annotations.ToolbarOption;
import com.malaab.ya.action.actionyamalaab.admin.custom.CustomToolbar;
import com.malaab.ya.action.actionyamalaab.admin.custom.DialogConfirmation;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.events.OnEventSendFirebaseTokenToServer;
import com.malaab.ya.action.actionyamalaab.admin.events.OnEventToolbarItemClicked;
import com.malaab.ya.action.actionyamalaab.admin.ui.attendancepayment.AttendeesActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.bookings.BookingsActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.login.LoginActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.messages.MessagesActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.notifications.NotificationsActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.playgrounds.PlaygroundsActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.staff.StaffActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.UsersActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.wallet.WalletActivity;
import com.malaab.ya.action.actionyamalaab.admin.utils.ActivityUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.AppUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;
import com.malaab.ya.action.actionyamalaab.admin.utils.DateTimeUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.PermissionsUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomeActivity extends BaseActivity implements HomeMvpView {

    @BindView(R.id.mCustomToolbar)
    public CustomToolbar mCustomToolbar;

    @BindView(R.id.ln_users)
    public LinearLayout ln_users;
    @BindView(R.id.ln_owners)
    public LinearLayout ln_owners;

    @BindView(R.id.ln_addPlayground)
    public LinearLayout ln_addPlayground;
    @BindView(R.id.ln_manage_bookings)
    public LinearLayout ln_manage_bookings;
    @BindView(R.id.ln_wallet)
    public LinearLayout ln_wallet;

    @Inject
    HomeMvpPresenter<HomeMvpView> mPresenter;
    @Inject
    DialogConfirmation mDialogConfirmation;

    private String mUserUid;
    private String newVersion, downloadUrl;

    private boolean isLoaded = false;
    private boolean doubleBackToExit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);

        mPresenter.getCurrentUserInfoLocal();
    }

    @Override
    protected void initUI() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }


    @OnClick(R.id.ln_users)
    public void openUsers() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.INTENT_KEY_IS_OWNER, false);

        ActivityUtils.goTo(HomeActivity.this, UsersActivity.class, false, bundle);
    }
    @OnClick(R.id.ln_owners)
    public void openOwners() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.INTENT_KEY_IS_OWNER, true);

        ActivityUtils.goTo(HomeActivity.this, UsersActivity.class, false, bundle);
    }

    @OnClick(R.id.ln_addPlayground)
    public void openAddPlayground() {
        ActivityUtils.goTo(HomeActivity.this, PlaygroundsActivity.class, false);
    }

    @OnClick(R.id.ln_manage_bookings)
    public void openManageBookings() {
        ActivityUtils.goTo(HomeActivity.this, BookingsActivity.class, false);
    }

    @OnClick(R.id.ln_attendance)
    public void openAttendance() {
        ActivityUtils.goTo(HomeActivity.this, AttendeesActivity.class, false);
    }

    @OnClick(R.id.ln_wallet)
    public void openWallet() {
        ActivityUtils.goTo(HomeActivity.this, WalletActivity.class, false);
    }


    @Override
    public void onUserAsAdmin() {
        mCustomToolbar.onUserAsAdmin();
        ln_wallet.setVisibility(View.VISIBLE);

        mPresenter.isUserAuthenticated();
    }

    @Override
    public void onUserAsStaff() {
        mCustomToolbar.onUserAsStaff();
        ln_wallet.setVisibility(View.GONE);

        mPresenter.isUserAuthenticated();
    }


    @Override
    public void onUserAuthenticationSuccess(String userUid) {
        mUserUid = userUid;

//        mPresenter.updateLastSeen(userUid, DateTimeUtils.getCurrentDatetime());
        mPresenter.refreshUserInfo(mUserUid);
    }

    @Override
    public void onRefreshUserInfo(User user) {
        if (!isLoaded) {
            isLoaded = true;

//            mPresenter.checkForUpdates();

            mPresenter.isDeviceRegisteredForNotifications(user);
        }
    }


    @Override
    public void onUpdateNotificationsCounter(int count) {
        mCustomToolbar.updateNotificationsCounter(count);
    }

    @Override
    public void onUpdateMessagesCounter(int count) {
        mCustomToolbar.updateMessagesCounter(count);
    }


    @Override
    public void onRegisterDeviceForNotification(User user) {
        mPresenter.registerForFirebaseNotifications(user);
    }


    @Override
    public void onUpdateAvailable(String version, String downloadUrl) {
        newVersion = version;
        this.downloadUrl = downloadUrl;

        mDialogConfirmation
                .withTitle(String.format(getString(R.string.dialog_update_title), version))
                .withMessage(getString(R.string.dialog_update_message))
                .withPositiveButton(getString(R.string.yes))
                .withNegativeButton(getString(R.string.no))
                .setOnDialogConfirmationListener(new DialogConfirmation.OnDialogConfirmationListener() {
                    @Override
                    public void onPositiveButtonClick() {
                        PermissionsUtils.requestPermission(HomeActivity.this, PermissionsCodes.WRITE_EXTERNAL_STORAGE, Permission.STORAGE);
                    }

                    @Override
                    public void onNegativeButtonClick() {
                    }
                })
                .build()
                .show();
    }

    @Override
    public void onDownloadNewVersionCompleted(File file) {
        AppUtils.installNewVersion(this, file);
//        AppUtils.update(this, file);
    }

    @Override
    public void onDownloadNewVersionFailed(int messageId) {
        onError(messageId);
    }


    @Override
    public void onUserSignOut() {
        ActivityUtils.goTo(HomeActivity.this, LoginActivity.class, true);
    }


    @Override
    public void onNoInternetConnection() {
        onError(R.string.error_no_connection);
    }


    @PermissionYes(PermissionsCodes.WRITE_EXTERNAL_STORAGE)
    private void gotStoragePermission(@NonNull List<String> grantedPermissions) {
        mPresenter.downloadNewVersion(this, newVersion, downloadUrl);
    }

    @PermissionNo(PermissionsCodes.WRITE_EXTERNAL_STORAGE)
    private void noStoragePermission(@NonNull List<String> deniedPermissions) {
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            PermissionsUtils.showAlwaysDeniedCustomDialog(this);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventToolbarItemClicked(OnEventToolbarItemClicked event) {
        switch (event.getAction()) {
            case ToolbarOption.CHAT:
                ActivityUtils.goTo(HomeActivity.this, NotificationsActivity.class, false);
                break;

            case ToolbarOption.MESSAGES:
                ActivityUtils.goTo(HomeActivity.this, MessagesActivity.class, false);
                break;

            case ToolbarOption.TEAM:
                ActivityUtils.goTo(HomeActivity.this, StaffActivity.class, false);
                break;

            case ToolbarOption.LOGOUT:
                mDialogConfirmation
                        .withTitle(getString(R.string.dialog_title_logout))
                        .withMessage("")
                        .withPositiveButton(getString(R.string.yes))
                        .withNegativeButton(getString(R.string.no))
                        .setOnDialogConfirmationListener(new DialogConfirmation.OnDialogConfirmationListener() {
                            @Override
                            public void onPositiveButtonClick() {
                                mPresenter.signOut();
                            }

                            @Override
                            public void onNegativeButtonClick() {
                            }
                        })
                        .build()
                        .show();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventSendFirebaseTokenToServer(OnEventSendFirebaseTokenToServer event) {
        mPresenter.registerForFirebaseNotifications(null);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        mPresenter.onResume();
        mPresenter.updateCounters();

//        AppLogger.d("FCM = " + FirebaseInstanceId.getInstance().getToken());
    }

    @Override
    protected void onPause() {
        super.onPause();

        mPresenter.onPause();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExit) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExit = true;
        showMessage(getString(R.string.msg_exit));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExit = false;
            }
        }, Constants.EXIT_DELAY);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        mPresenter.onDetach();
        super.onDestroy();
    }
}

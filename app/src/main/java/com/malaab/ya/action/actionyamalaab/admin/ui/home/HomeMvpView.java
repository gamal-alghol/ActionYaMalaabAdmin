package com.malaab.ya.action.actionyamalaab.admin.ui.home;

import android.support.annotation.StringRes;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpView;

import java.io.File;


public interface HomeMvpView extends MvpView {

    void onUserAsAdmin();
    void onUserAsStaff();

    void onUserAuthenticationSuccess(String userUid);
    void onRefreshUserInfo(User user);

    void onUpdateNotificationsCounter(int count);
    void onUpdateMessagesCounter(int count);

    void onRegisterDeviceForNotification(User user);

    void onUpdateAvailable(String version, String downloadUrl);
    void onDownloadNewVersionCompleted(File file);
    void onDownloadNewVersionFailed(@StringRes int messageId);

    void onUserSignOut();
}

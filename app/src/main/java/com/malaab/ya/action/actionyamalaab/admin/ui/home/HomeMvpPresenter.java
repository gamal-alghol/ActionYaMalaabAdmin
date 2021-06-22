package com.malaab.ya.action.actionyamalaab.admin.ui.home;

import android.content.Context;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpPresenter;


@PerActivity
public interface HomeMvpPresenter<V extends HomeMvpView> extends MvpPresenter<V> {

    void isUserAuthenticated();

    void getCurrentUserInfoLocal();

    void refreshUserInfo(String userUid);


    void updateLastSeen(final String userUid, long lastSeen);


    void updateCounters();


    void isDeviceRegisteredForNotifications(User user);

    void registerForFirebaseNotifications(User user);


    void checkForUpdates();

    void downloadNewVersion(Context context, String newVersion, String downloadUrl);


    void signOut();
}

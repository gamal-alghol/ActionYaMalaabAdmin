package com.malaab.ya.action.actionyamalaab.admin.ui.notifications;

import com.malaab.ya.action.actionyamalaab.admin.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.messages.MessagesMvpView;


@PerActivity
public interface NotificationsMvpPresenter<V extends NotificationsMvpView> extends MvpPresenter<V> {

    void getNotifications();
}

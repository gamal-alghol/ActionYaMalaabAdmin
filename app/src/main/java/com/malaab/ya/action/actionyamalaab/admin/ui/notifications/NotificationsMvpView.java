package com.malaab.ya.action.actionyamalaab.admin.ui.notifications;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Notification;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpView;

import java.util.List;


public interface NotificationsMvpView extends MvpView {

    void onGetNotifications(List<Notification> notifications);
}

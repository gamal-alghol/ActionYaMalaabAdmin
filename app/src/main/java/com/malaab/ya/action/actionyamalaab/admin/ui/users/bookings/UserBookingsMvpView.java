package com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpView;


public interface UserBookingsMvpView extends MvpView {

    void onGetCurrentUser(User user);
}

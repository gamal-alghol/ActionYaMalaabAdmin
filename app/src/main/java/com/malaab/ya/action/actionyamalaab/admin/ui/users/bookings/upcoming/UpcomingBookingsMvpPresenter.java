package com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings.upcoming;

import com.malaab.ya.action.actionyamalaab.admin.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpPresenter;


@PerActivity
public interface UpcomingBookingsMvpPresenter<V extends UpcomingBookingsMvpView> extends MvpPresenter<V> {

    void getUpcomingBookings(String userUid);
}

package com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings.past;

import com.malaab.ya.action.actionyamalaab.admin.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings.past.PastBookingsMvpView;


@PerActivity
public interface PastBookingsMvpPresenter<V extends PastBookingsMvpView> extends MvpPresenter<V> {

    void getPastBookings(String userUid);
}

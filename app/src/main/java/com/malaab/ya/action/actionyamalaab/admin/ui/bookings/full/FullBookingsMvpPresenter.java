package com.malaab.ya.action.actionyamalaab.admin.ui.bookings.full;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpPresenter;


@PerActivity
public interface FullBookingsMvpPresenter<V extends FullBookingsMvpView> extends MvpPresenter<V> {

    void getFullBookings(long startDatetime, long endDatetime);


    void confirmBooking(Booking booking);

    void rejectBooking(Booking booking);
}

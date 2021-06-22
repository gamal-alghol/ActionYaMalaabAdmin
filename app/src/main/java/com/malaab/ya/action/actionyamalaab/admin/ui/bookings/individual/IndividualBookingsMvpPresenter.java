package com.malaab.ya.action.actionyamalaab.admin.ui.bookings.individual;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.bookings.full.FullBookingsMvpView;


@PerActivity
public interface IndividualBookingsMvpPresenter<V extends IndividualBookingsMvpView> extends MvpPresenter<V> {

    void getIndividualBookings(long startDatetime, long endDatetime);


    void confirmAgeCategory(Booking booking, int ageRangeStart);

    void rejectAgeCategory(Booking booking, int ageRangeStart);


    void confirmBooking(Booking booking);

    void rejectBooking(Booking booking);

    void cancelBooking(Booking booking);
}

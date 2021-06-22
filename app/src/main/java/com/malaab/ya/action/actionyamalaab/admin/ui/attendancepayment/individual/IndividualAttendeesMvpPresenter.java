package com.malaab.ya.action.actionyamalaab.admin.ui.attendancepayment.individual;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.BookingPlayer;
import com.malaab.ya.action.actionyamalaab.admin.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpPresenter;


@PerActivity
public interface IndividualAttendeesMvpPresenter<V extends IndividualAttendeesMvpView> extends MvpPresenter<V> {

    void getCurrentUser();


    void getIndividualBookings(long startDatetime, long endDatetime);


    void takeAttendanceBooking(Booking booking, BookingPlayer player);

    void takeAbsentBooking(Booking booking, BookingPlayer player);


    void createFine(final Booking booking, String playerId);
}

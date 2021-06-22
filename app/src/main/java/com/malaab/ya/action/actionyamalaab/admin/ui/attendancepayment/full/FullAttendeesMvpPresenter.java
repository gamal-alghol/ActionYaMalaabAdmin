package com.malaab.ya.action.actionyamalaab.admin.ui.attendancepayment.full;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpPresenter;

import java.util.List;


@PerActivity
public interface FullAttendeesMvpPresenter<V extends FullAttendeesMvpView> extends MvpPresenter<V> {

    void getCurrentUser();


    void getFullBookings(long startDatetime, long endDatetime);

    void getFullBookingsFines(List<Booking> bookings);


    void takeAttendanceBooking(Booking booking);

    void takeAbsentBooking(Booking booking);

    void createFine(final Booking booking);
}

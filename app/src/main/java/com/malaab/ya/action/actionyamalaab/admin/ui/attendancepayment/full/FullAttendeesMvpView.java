package com.malaab.ya.action.actionyamalaab.admin.ui.attendancepayment.full;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpView;

import java.util.List;


public interface FullAttendeesMvpView extends MvpView {

    void onGetCurrentUser(User user);


    void onGetFullBookings(List<Booking> bookings);

    void onGetFullBookingsEmpty();

    void onGetFullBookingsFines(Booking booking);


    void onTakeAttendanceBookingSuccess(Booking booking);

    void onTakeAbsentBookingSuccess(Booking booking);


    void onFineCreateSuccess(Booking booking);
}

package com.malaab.ya.action.actionyamalaab.admin.ui.attendancepayment.individual;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.BookingPlayer;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpView;

import java.util.List;


public interface IndividualAttendeesMvpView extends MvpView {

    void onGetCurrentUser(User user);


    void onGetIndividualBookings(List<Booking> bookings);

    void onGetIndividualBookingsEmpty();


    void onTakeAttendanceBookingSuccess(Booking booking, BookingPlayer player);

    void onTakeAbsentBookingSuccess(Booking booking, BookingPlayer player);


    void onFineCreateSuccess(Booking booking);
}

package com.malaab.ya.action.actionyamalaab.admin.ui.bookings.individual;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpView;

import java.util.List;


public interface IndividualBookingsMvpView extends MvpView {

    void onGetIndividualBookings(List<Booking> bookings);

    void onGetIndividualBookingsEmpty();


    void onConfirmBookingSuccess(Booking booking);

    void onRejectBookingSuccess(Booking booking);

    void onCancelBookingSuccess(Booking booking);
}

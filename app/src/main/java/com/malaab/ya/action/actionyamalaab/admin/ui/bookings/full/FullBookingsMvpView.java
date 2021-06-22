package com.malaab.ya.action.actionyamalaab.admin.ui.bookings.full;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpView;

import java.util.List;


public interface FullBookingsMvpView extends MvpView {

    void onGetFullBookings(List<Booking> bookings);

    void onGetFullBookingsEmpty();


    void onConfirmBookingSuccess(Booking booking);

    void onRejectBookingSuccess(Booking booking);
}

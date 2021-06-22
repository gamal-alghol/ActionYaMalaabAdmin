package com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings.past;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpView;

import java.util.List;


public interface PastBookingsMvpView extends MvpView {

    void onGetPastBookings(List<Booking> bookings);
}

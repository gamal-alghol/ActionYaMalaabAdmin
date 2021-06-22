package com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings.upcoming;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpView;

import java.util.List;


public interface UpcomingBookingsMvpView extends MvpView {

    void onGetUpcomingBookings(List<Booking> bookings);
}

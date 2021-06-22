package com.malaab.ya.action.actionyamalaab.admin.ui.users.wallet;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpView;

import java.util.List;


public interface OwnerWalletFullBookingsMvpView extends MvpView {

    void onGetWalletFullBookings(List<Booking> bookings, float total);

    void onGetWalletFullBookingsEmpty();
}

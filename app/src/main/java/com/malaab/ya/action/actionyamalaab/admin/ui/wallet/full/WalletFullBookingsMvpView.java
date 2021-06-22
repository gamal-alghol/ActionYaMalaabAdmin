package com.malaab.ya.action.actionyamalaab.admin.ui.wallet.full;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpView;

import java.util.List;


public interface WalletFullBookingsMvpView extends MvpView {

    void onGetWalletFullBookings(List<Booking> bookings, float total,int totalBookings);

    void onGetWalletFullBookingsEmpty();
}

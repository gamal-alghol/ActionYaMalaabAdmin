package com.malaab.ya.action.actionyamalaab.admin.ui.wallet.individual;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpView;

import java.util.List;


public interface WalletIndividualBookingsMvpView extends MvpView {

    void onGetWalletIndividualBookings(List<Booking> bookings, float total,int totalBookings);

    void onGetWalletIndividualBookingsEmpty();

}

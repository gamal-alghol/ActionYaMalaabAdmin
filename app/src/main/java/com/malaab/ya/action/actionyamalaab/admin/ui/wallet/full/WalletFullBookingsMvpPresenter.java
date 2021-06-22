package com.malaab.ya.action.actionyamalaab.admin.ui.wallet.full;

import com.malaab.ya.action.actionyamalaab.admin.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpPresenter;


@PerActivity
public interface WalletFullBookingsMvpPresenter<V extends WalletFullBookingsMvpView> extends MvpPresenter<V> {

    void getWalletFullBookings(long startDatetime, long endDatetime);

}

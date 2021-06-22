package com.malaab.ya.action.actionyamalaab.admin.ui.wallet.individual;

import com.malaab.ya.action.actionyamalaab.admin.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpPresenter;


@PerActivity
public interface WalletIndividualBookingsMvpPresenter<V extends WalletIndividualBookingsMvpView> extends MvpPresenter<V> {

    void getWalletIndividualBookings(long startDatetime, long endDatetime);

}

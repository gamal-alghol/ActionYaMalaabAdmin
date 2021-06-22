package com.malaab.ya.action.actionyamalaab.admin.ui.users.wallet;

import com.malaab.ya.action.actionyamalaab.admin.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpPresenter;


@PerActivity
public interface OwnerWalletFullBookingsMvpPresenter<V extends OwnerWalletFullBookingsMvpView> extends MvpPresenter<V> {

    void getWalletFullBookings(String ownerUid, long startDatetime, long endDatetime);
}

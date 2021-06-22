package com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings;

import com.malaab.ya.action.actionyamalaab.admin.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpPresenter;


@PerActivity
public interface UserBookingsMvpPresenter<V extends UserBookingsMvpView> extends MvpPresenter<V> {

    void getCurrentUserLocal();

}

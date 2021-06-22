package com.malaab.ya.action.actionyamalaab.admin.ui.playgrounds;

import com.malaab.ya.action.actionyamalaab.admin.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.bookings.BookingsMvpView;


@PerActivity
public interface PlaygroundsMvpPresenter<V extends PlaygroundsMvpView> extends MvpPresenter<V> {

    void getCurrentUserInfoLocal();

    void getPlaygrounds();

}

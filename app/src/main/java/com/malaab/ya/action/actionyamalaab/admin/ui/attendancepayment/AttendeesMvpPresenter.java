package com.malaab.ya.action.actionyamalaab.admin.ui.attendancepayment;

import com.malaab.ya.action.actionyamalaab.admin.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpPresenter;


@PerActivity
public interface AttendeesMvpPresenter<V extends AttendeesMvpView> extends MvpPresenter<V> {

    void getCurrentUser();
}

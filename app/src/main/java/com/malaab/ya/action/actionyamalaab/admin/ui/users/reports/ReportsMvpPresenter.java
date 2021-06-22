package com.malaab.ya.action.actionyamalaab.admin.ui.users.reports;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.UsersMvpView;

import java.util.List;


@PerActivity
public interface ReportsMvpPresenter<V extends ReportsMvpView> extends MvpPresenter<V> {

    void getFullReports(String ownerUid, long date);

    void getIndividualReports(String ownerUid, List<Booking> bookings, long date);

//    void getReports(long date);
}

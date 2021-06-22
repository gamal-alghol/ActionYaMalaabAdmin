package com.malaab.ya.action.actionyamalaab.admin.ui.users.reports;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpView;

import java.util.List;


public interface ReportsMvpView extends MvpView {

    void onGetReportsAsFull(List<Booking> bookings, long date);

    void onGetReportsAsIndividual(List<Booking> bookings);
}

package com.malaab.ya.action.actionyamalaab.admin.ui.attendancepayment;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpView;


public interface AttendeesMvpView extends MvpView {

    void onGetCurrentUser(User user);
}

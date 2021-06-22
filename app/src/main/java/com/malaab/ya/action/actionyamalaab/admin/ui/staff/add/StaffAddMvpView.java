package com.malaab.ya.action.actionyamalaab.admin.ui.staff.add;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpView;


public interface StaffAddMvpView extends MvpView {

    void onStaffAddedToServer(User user);

    void onUserUniqueIdGenerated(User user);

    void onStaffAddedToDatabase(User user);
}

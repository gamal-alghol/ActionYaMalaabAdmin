package com.malaab.ya.action.actionyamalaab.admin.ui.staff.list;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpView;

import java.util.List;


public interface StaffListMvpView extends MvpView {

    void onGetStaffCount(int count);

    void onGetStaff(User staff);


    void onBlockUserSuccess();

    void onBlockUserFailed();
}

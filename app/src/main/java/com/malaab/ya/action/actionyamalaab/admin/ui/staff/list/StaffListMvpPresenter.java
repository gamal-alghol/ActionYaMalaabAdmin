package com.malaab.ya.action.actionyamalaab.admin.ui.staff.list;

import com.malaab.ya.action.actionyamalaab.admin.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpPresenter;


@PerActivity
public interface StaffListMvpPresenter<V extends StaffListMvpView> extends MvpPresenter<V> {

    void getStaffCount();

    void getStaff();

    void activateUser(String userUid, boolean isActive);
}

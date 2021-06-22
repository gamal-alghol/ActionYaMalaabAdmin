package com.malaab.ya.action.actionyamalaab.admin.ui.staff.add;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpPresenter;


@PerActivity
public interface StaffAddMvpPresenter<V extends StaffAddMvpView> extends MvpPresenter<V> {

    void addStaffToServer(String fName, String lName, String phone, String email, String password, String confirmPassword);

    void generateUserUniqueId(User user);

    void addStaffToeDatabase(User user);
}

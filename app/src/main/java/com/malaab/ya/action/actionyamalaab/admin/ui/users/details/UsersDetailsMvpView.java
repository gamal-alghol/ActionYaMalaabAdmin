package com.malaab.ya.action.actionyamalaab.admin.ui.users.details;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpView;


public interface UsersDetailsMvpView extends MvpView {

    void onGetUserDetails(User user);


    void onGetOwnerPlaygroundsCount(int count);


    void onBlockUserSuccess();

    void onBlockUserFailed();
}

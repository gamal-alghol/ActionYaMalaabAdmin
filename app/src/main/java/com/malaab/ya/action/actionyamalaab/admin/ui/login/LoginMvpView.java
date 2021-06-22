package com.malaab.ya.action.actionyamalaab.admin.ui.login;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpView;


public interface LoginMvpView extends MvpView {

    void onServerLoginSuccess(String userUId);

    void onUserExistInDB(User user);
    void onUserNotExistInDB();

    void onResetPassword(String message);
}

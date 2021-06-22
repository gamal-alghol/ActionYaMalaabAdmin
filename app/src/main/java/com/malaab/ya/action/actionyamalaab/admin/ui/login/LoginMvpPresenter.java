package com.malaab.ya.action.actionyamalaab.admin.ui.login;

import com.malaab.ya.action.actionyamalaab.admin.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpPresenter;


@PerActivity
public interface LoginMvpPresenter<V extends LoginMvpView> extends MvpPresenter<V> {

    void doServerLogin(String username, String password);

    void isUserExistInDB(String userUId);

    void forgotPassword(String username, String email);
}

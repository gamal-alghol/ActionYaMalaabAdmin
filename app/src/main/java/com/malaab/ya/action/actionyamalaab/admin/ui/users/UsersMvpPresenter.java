package com.malaab.ya.action.actionyamalaab.admin.ui.users;

import com.malaab.ya.action.actionyamalaab.admin.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpPresenter;


@PerActivity
public interface UsersMvpPresenter<V extends UsersMvpView> extends MvpPresenter<V> {

    void getUsers(boolean isOwner);

    void searchUser(String username);
}

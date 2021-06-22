package com.malaab.ya.action.actionyamalaab.admin.ui.users.details;

import com.malaab.ya.action.actionyamalaab.admin.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpPresenter;


@PerActivity
public interface UsersDetailsMvpPresenter<V extends UsersDetailsMvpView> extends MvpPresenter<V> {

    void getUserDetails(String userUid);

    void getOwnerPlaygroundsCount(String ownerUid);

    void deactivateUser(String userUid, boolean isActive);
}

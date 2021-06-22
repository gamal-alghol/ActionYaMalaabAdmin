package com.malaab.ya.action.actionyamalaab.admin.ui.users.playgrounds;

import com.malaab.ya.action.actionyamalaab.admin.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpPresenter;


@PerActivity
public interface OwnerPlaygroundsMvpPresenter<V extends OwnerPlaygroundsMvpView> extends MvpPresenter<V> {

    void getPlaygrounds(String ownerUid);
}

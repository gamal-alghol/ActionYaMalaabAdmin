package com.malaab.ya.action.actionyamalaab.admin.ui.messages;

import com.malaab.ya.action.actionyamalaab.admin.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpPresenter;


@PerActivity
public interface MessagesMvpPresenter<V extends MessagesMvpView> extends MvpPresenter<V> {

    void getMessages();
}

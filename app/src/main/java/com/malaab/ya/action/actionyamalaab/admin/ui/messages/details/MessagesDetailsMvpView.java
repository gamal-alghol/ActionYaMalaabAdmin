package com.malaab.ya.action.actionyamalaab.admin.ui.messages.details;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpView;


public interface MessagesDetailsMvpView extends MvpView {

    void onGetCurrentUser(User user);


    void onSendNewMessageSuccess();

    void onSendReplySuccess();
}

package com.malaab.ya.action.actionyamalaab.admin.ui.messages;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Message;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpView;

import java.util.List;


public interface MessagesMvpView extends MvpView {

    void onGetMessages(List<Message> messages);
}

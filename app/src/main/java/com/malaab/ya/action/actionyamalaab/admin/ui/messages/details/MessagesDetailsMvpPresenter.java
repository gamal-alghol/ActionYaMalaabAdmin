package com.malaab.ya.action.actionyamalaab.admin.ui.messages.details;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Message;
import com.malaab.ya.action.actionyamalaab.admin.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.MvpPresenter;


@PerActivity
public interface MessagesDetailsMvpPresenter<V extends MessagesDetailsMvpView> extends MvpPresenter<V> {

    void getCurrentUserLocal();

    void sendNewMessage(String toUserUid, String toUserAppId, String toUsername, String toUserEmail, String toUserPhone, String toUserProfileImage,
                        String fromUserUid, String fromUserAppId, String fromUsername, String fromUserEmail, String fromUserPhone, String fromUserProfileImage,
                        String text);

    void sendReply(Message message,
                   String toUserUid, String toUserAppId, String toUsername, String toUserEmail, String toUserPhone, String toUserProfileImage,
                   String fromUserUid, String fromUserAppId, String fromUsername, String fromUserEmail, String fromUserPhone, String fromUserProfileImage,
                   String text);
}

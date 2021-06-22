package com.malaab.ya.action.actionyamalaab.admin.data.model;

public class NotificationsSettings {

    public String firebaseToken;
    public boolean isRegistered;


    public NotificationsSettings(String firebaseToken, boolean isRegistered) {
        this.firebaseToken = firebaseToken;
        this.isRegistered = isRegistered;
    }
}

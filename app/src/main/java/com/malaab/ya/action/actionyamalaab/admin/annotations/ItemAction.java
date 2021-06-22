package com.malaab.ya.action.actionyamalaab.admin.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@IntDef({ItemAction.PICK, ItemAction.DETAILS, ItemAction.SHARE, ItemAction.LOCATION, ItemAction.FAVOURITE_ADD,
        ItemAction.FAVOURITE_REMOVE, ItemAction.RECEIVE, ItemAction.TAKE_ATTENDANCE, ItemAction.PAY_BOOKING,
        ItemAction.CONFIRM, ItemAction.REJECT, ItemAction.VIEW_LIST, ItemAction.TAKE_ABSENT, ItemAction.CANCEL})
@Retention(RetentionPolicy.SOURCE)
public @interface ItemAction {
    int PICK = 1;
    int DETAILS = 2;
    int SHARE = 3;
    int LOCATION = 4;
    int FAVOURITE_ADD = 5;
    int FAVOURITE_REMOVE = 6;
    int RECEIVE = 7;
    int TAKE_ATTENDANCE = 8;
    int PAY_BOOKING = 9;
    int CONFIRM = 10;
    int REJECT = 11;
    int VIEW_LIST = 12;
    int TAKE_ABSENT = 13;
    int CANCEL = 14;
}

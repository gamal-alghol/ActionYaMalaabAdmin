package com.malaab.ya.action.actionyamalaab.admin.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@IntDef({ToolbarOption.CHAT, ToolbarOption.MESSAGES, ToolbarOption.TEAM, ToolbarOption.LOGOUT})
@Retention(RetentionPolicy.SOURCE)
public @interface ToolbarOption {
    int CHAT = 1;
    int MESSAGES = 2;
    int TEAM = 3;
    int LOGOUT = 0;
}

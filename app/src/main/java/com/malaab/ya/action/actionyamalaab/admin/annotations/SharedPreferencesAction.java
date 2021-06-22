package com.malaab.ya.action.actionyamalaab.admin.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@IntDef({SharedPreferencesAction.ADD, SharedPreferencesAction.UPDATE, SharedPreferencesAction.DELETE})
@Retention(RetentionPolicy.SOURCE)
public @interface SharedPreferencesAction {
    int ADD = 1;
    int UPDATE = 2;
    int DELETE = 3;
}

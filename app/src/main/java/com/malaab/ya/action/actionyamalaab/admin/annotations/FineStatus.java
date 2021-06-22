package com.malaab.ya.action.actionyamalaab.admin.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@IntDef({FineStatus.NOT_PAID, FineStatus.PAID})
@Retention(RetentionPolicy.SOURCE)
public @interface FineStatus {
    int NOT_PAID = 0;
    int PAID = 1;
}

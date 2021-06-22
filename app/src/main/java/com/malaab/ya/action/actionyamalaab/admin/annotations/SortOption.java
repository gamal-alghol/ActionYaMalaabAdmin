package com.malaab.ya.action.actionyamalaab.admin.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@IntDef({SortOption.RECENT, SortOption.DEACTIVATED, SortOption.NO_OF_BOOKINGS})
@Retention(RetentionPolicy.SOURCE)
public @interface SortOption {
    int RECENT = 1;
    int DEACTIVATED = 2;
    int NO_OF_BOOKINGS = 3;
}

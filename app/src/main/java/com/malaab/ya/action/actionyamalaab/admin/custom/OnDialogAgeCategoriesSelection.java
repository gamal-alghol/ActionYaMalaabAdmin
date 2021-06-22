package com.malaab.ya.action.actionyamalaab.admin.custom;

import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;

public interface OnDialogAgeCategoriesSelection {

    void onAgeYoungSelected(Booking booking);

    void onAgeMiddleSelected(Booking booking);

    void onAgeOldSelected(Booking booking);
}

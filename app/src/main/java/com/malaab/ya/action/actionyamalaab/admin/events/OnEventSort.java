package com.malaab.ya.action.actionyamalaab.admin.events;

import com.malaab.ya.action.actionyamalaab.admin.annotations.SortOption;

public class OnEventSort {

    @SortOption
    private int mSortOption;


    public OnEventSort(@SortOption int sortOption) {
        mSortOption = sortOption;
    }


    public int getSortOption() {
        return mSortOption;
    }
}

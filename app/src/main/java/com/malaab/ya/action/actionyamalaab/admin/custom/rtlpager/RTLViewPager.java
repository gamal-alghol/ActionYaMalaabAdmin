package com.malaab.ya.action.actionyamalaab.admin.custom.rtlpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;


public class RTLViewPager extends ViewPager {

    private boolean mIsRtlOriented;

    public RTLViewPager(Context context) {
        this(context, null);
    }

    public RTLViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * To use this method call first {@link android.support.v4.view.ViewPager#setAdapter(android.support.v4.view.PagerAdapter)}
     *
     * @param isRtlOriented is right to left oriented
     */
    public void setRtlOriented(boolean isRtlOriented) {
        mIsRtlOriented = isRtlOriented;
        if (mIsRtlOriented && getAdapter() != null) {
            setCurrentItem(getAdapter().getCount() - 1);
        } else {
            setCurrentItem(0);
        }
    }
}
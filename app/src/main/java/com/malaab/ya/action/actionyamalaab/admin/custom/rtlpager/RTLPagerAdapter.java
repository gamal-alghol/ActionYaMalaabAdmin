package com.malaab.ya.action.actionyamalaab.admin.custom.rtlpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;


public class RTLPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private Tab[] mTabs;
    private boolean mIsRtlOrientated;

    public RTLPagerAdapter(FragmentManager fm, Tab[] tabs, boolean isRtlOriented) {
        super(fm);

        mTabs = tabs;
        mIsRtlOrientated = isRtlOriented;
    }

    @Override
    public Fragment getItem(int position) {
        if (mIsRtlOrientated && mTabs != null && mTabs.length > 0) {
            return mTabs[mTabs.length - position - 1].getFragment();
        } else {
            return mTabs[position].getFragment();
        }
    }

    @Override
    public int getCount() {
        return mTabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mIsRtlOrientated && mTabs != null && mTabs.length > 0) {
            return mTabs[mTabs.length - position - 1].getTitle();
        } else {
            return mTabs[position].getTitle();
        }
    }
}
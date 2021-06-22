package com.malaab.ya.action.actionyamalaab.admin.custom.rtlpager;

import android.support.v4.app.Fragment;

public abstract class Tab {

    private int id;
    private String title;


    public Tab(int id, String title) {
        setId(id);
        setTitle(title);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public abstract Fragment getFragment();
}
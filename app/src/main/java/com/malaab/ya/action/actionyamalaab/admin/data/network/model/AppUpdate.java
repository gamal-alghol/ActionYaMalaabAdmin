package com.malaab.ya.action.actionyamalaab.admin.data.network.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class AppUpdate {

    public String version;


    public AppUpdate() {
        // Default constructor required for calls to DataSnapshot.getValue(MetaData.class)
    }


    @Exclude
    @Override
    public String toString() {
        return "AppUpdate{" +
                "version='" + version + '\'' +
                '}';
    }
}

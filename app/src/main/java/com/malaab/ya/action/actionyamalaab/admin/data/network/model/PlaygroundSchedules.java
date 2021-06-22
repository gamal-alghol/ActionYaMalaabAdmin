package com.malaab.ya.action.actionyamalaab.admin.data.network.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class PlaygroundSchedules {

    public List<PlaygroundSchedule> playgroundSchedules;


    public PlaygroundSchedules() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }
}

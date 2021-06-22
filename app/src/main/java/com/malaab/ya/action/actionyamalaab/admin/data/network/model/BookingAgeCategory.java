package com.malaab.ya.action.actionyamalaab.admin.data.network.model;

import com.google.firebase.database.IgnoreExtraProperties;
import com.malaab.ya.action.actionyamalaab.admin.annotations.BookingStatus;

import java.util.List;


@IgnoreExtraProperties
public class BookingAgeCategory {

    public int ageRangeStart;
    public int ageRangeEnd;

    public String name;

    public List<BookingPlayer> players;

    public boolean isConfirmed;

    public @BookingStatus
    int status;


    public BookingAgeCategory() {
        // Default constructor required for calls to DataSnapshot.getValue(MetaData.class)
    }


    @Override
    public String toString() {
        return "BookingAgeCategory{" +
                "name=" + name +
                "ageRangeStart=" + ageRangeStart +
                ", ageRangeEnd=" + ageRangeEnd +
                ", isConfirmed=" + isConfirmed +
                ", players=" + players +
                '}';
    }
}

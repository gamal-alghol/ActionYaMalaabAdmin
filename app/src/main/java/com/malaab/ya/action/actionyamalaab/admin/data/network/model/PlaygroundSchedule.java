package com.malaab.ya.action.actionyamalaab.admin.data.network.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.malaab.ya.action.actionyamalaab.admin.data.model.DurationListItem;
import com.malaab.ya.action.actionyamalaab.admin.data.model.SizeListItem;

import java.util.List;
import java.util.Objects;

@IgnoreExtraProperties
public class PlaygroundSchedule {

    public String day;
    public float price;

    //region if it is for Individuals
    public boolean isIndividuals;
    public String isIndividualsDatetime;

    public int ageRangeStart;
    public int ageRangeEnd;
    //endregion

    public String timeStart;
    public String timeEnd;

    public List<SizeListItem> size;
    public List<DurationListItem> duration;


    public PlaygroundSchedule() {
        // Default constructor required for calls to DataSnapshot.getValue(PlaygroundSchedule.class)
    }


    @Exclude
    @Override
    public String toString() {
        return "PlaygroundSchedule{" +
                "day='" + day + '\'' +
                ", price='" + price + '\'' +
                ", size='" + size + '\'' +
                ", duration='" + duration + '\'' +
                ", isIndividuals='" + isIndividuals + '\'' +
                ", isIndividualsDatetime='" + isIndividualsDatetime + '\'' +
                ", ageRangeStart='" + ageRangeStart + '\'' +
                ", ageRangeEnd='" + ageRangeEnd + '\'' +
                ", timeStart='" + timeStart + '\'' +
                ", timeEnd='" + timeEnd + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaygroundSchedule that = (PlaygroundSchedule) o;
        return Objects.equals(day, that.day);
    }

    @Override
    public int hashCode() {

        return Objects.hash(day);
    }
}

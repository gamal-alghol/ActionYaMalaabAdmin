package com.malaab.ya.action.actionyamalaab.admin.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class BookingPlayground implements Parcelable {

    public String playgroundId;
    public String ownerId;

    public String name;
    public String address_city;
    public String address_direction;
    public String address_region;

    public List<String> images;


    public BookingPlayground() {
        // Default constructor required for calls to DataSnapshot.getValue(MetaData.class)
    }


    protected BookingPlayground(Parcel in) {
        playgroundId = in.readString();
        ownerId = in.readString();
        name = in.readString();
        address_city = in.readString();
        address_direction = in.readString();
        address_region = in.readString();
        images = in.createStringArrayList();
    }

    public static final Creator<BookingPlayground> CREATOR = new Creator<BookingPlayground>() {
        @Override
        public BookingPlayground createFromParcel(Parcel in) {
            return new BookingPlayground(in);
        }

        @Override
        public BookingPlayground[] newArray(int size) {
            return new BookingPlayground[size];
        }
    };

    @Exclude
    @Override
    public String toString() {
        return "BookingPlayground{" +
                "playgroundId='" + playgroundId + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", name='" + name + '\'' +
                ", address_city='" + address_city + '\'' +
                ", address_direction='" + address_direction + '\'' +
                ", address_region='" + address_region + '\'' +
                ", images=" + images +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(playgroundId);
        parcel.writeString(ownerId);
        parcel.writeString(name);
        parcel.writeString(address_city);
        parcel.writeString(address_direction);
        parcel.writeString(address_region);
        parcel.writeStringList(images);
    }
}

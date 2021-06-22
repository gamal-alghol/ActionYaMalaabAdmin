package com.malaab.ya.action.actionyamalaab.admin.data.network.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.malaab.ya.action.actionyamalaab.admin.annotations.BookingStatus;

@IgnoreExtraProperties
public class BookingPlayer {

    public String uId;
    public long appUserId;

    public String name;

    public String email;
    public String mobileNo;

    public String profileImageUrl;

    public float price;

    public boolean isPaid;
    public boolean isAttended;

    public int invitees;

    public @BookingStatus
    int status;

    public boolean hasFine;

    @Exclude
    public float totalFines;      /* To be used in attendance and payment adapters */


    public BookingPlayer() {
        // Default constructor required for calls to DataSnapshot.getValue(MetaData.class)
    }


    @Exclude
    @Override
    public String toString() {
        return "BookingPlayer{" +
                "uId='" + uId + '\'' +
                ", appUserId=" + appUserId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", price=" + price +
                ", isPaid=" + isPaid +
                ", isAttended=" + isAttended +
                ", invitees=" + invitees +
                ", status=" + status +
                ", hasFine=" + hasFine +
                ", totalFines=" + totalFines +
                '}';
    }
}

package com.malaab.ya.action.actionyamalaab.admin.data.network.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;

import com.google.firebase.database.IgnoreExtraProperties;
import com.malaab.ya.action.actionyamalaab.admin.annotations.BookingType;
import com.malaab.ya.action.actionyamalaab.admin.annotations.FineStatus;
import com.malaab.ya.action.actionyamalaab.admin.annotations.FineType;


import java.util.Objects;


@IgnoreExtraProperties
public class Fine implements Parcelable {


    public String userId;

    public String bookingId;

    public @BookingType
    int bookType;

    public String playgroundId;
    public BookingPlayground playground;

    public @FineType
    int fineType;

    public long datetimeCreated;

    public long timeStart;
    public long timeEnd;

    public float amount;

    public @FineStatus
    int fineStatus;


    public Fine() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    protected Fine(Parcel in) {
        userId = in.readString();
        bookingId = in.readString();
        bookType = in.readInt();
        playgroundId = in.readString();
        fineType = in.readInt();
        datetimeCreated = in.readLong();
        timeStart = in.readLong();
        timeEnd = in.readLong();
        amount = in.readFloat();
        fineStatus = in.readInt();

        playground=(BookingPlayground) in.readValue( BookingPlayer.class.getClassLoader());
    }

    public static final Creator<Fine> CREATOR = new Creator<Fine>() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public Fine createFromParcel(Parcel in) {
            return new Fine(in);
        }

        @Override
        public Fine[] newArray(int size) {
            return new Fine[size];
        }
    };

    @Override
    public String toString() {
        return "Fine{" +
                "userId='" + userId + '\'' +
                ", bookingId='" + bookingId + '\'' +
                ", bookType=" + bookType +
                ", playgroundId='" + playgroundId + '\'' +
                ", playground=" + playground +
                ", fineType=" + fineType +
                ", datetimeCreated=" + datetimeCreated +
                ", timeStart=" + timeStart +
                ", timeEnd=" + timeEnd +
                ", amount=" + amount +
                ", fineStatus=" + fineStatus +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fine fine = (Fine) o;
        return Objects.equals(userId, fine.userId) &&
                Objects.equals(bookingId, fine.bookingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, bookingId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(bookingId);
        parcel.writeInt(bookType);
        parcel.writeString(playgroundId);
        parcel.writeInt(fineType);
        parcel.writeLong(datetimeCreated);
        parcel.writeLong(timeStart);
        parcel.writeLong(timeEnd);
        parcel.writeFloat(amount);
        parcel.writeInt(fineStatus);
        parcel.writeValue(playground);
    }
}

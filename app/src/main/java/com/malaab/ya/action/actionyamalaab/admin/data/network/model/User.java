package com.malaab.ya.action.actionyamalaab.admin.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.malaab.ya.action.actionyamalaab.admin.annotations.LoginMode;
import com.malaab.ya.action.actionyamalaab.admin.annotations.UserRole;

import java.util.Objects;


@IgnoreExtraProperties
public class User implements Parcelable {

    public String uId;
    public long appUserId;

    public String email;
    public String password;

    public String fName;
    public String lName;
    public String dob;
    public int age;

    public String mobileNo;

    public long modifyDate;
    public long createdDate;

    public String address1;
    public String address2;
    public String postcode;
    public String address_city;
    public String address_direction;
    public String address_region;


    public String state;
    public String country;
    public double latitude;
    public double longitude;
    public String profileImageUrl;
    public String resStop;

    public String referral_code;
    public String referred_by;

    @UserRole
    public String role;

    @LoginMode
    public int loggedInMode;

    public boolean isActive;

    public String fcmToken;

    public int noOfBookings;

    public boolean isOnline;
    public long lastSeen;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(MetaData.class)
    }

    public User(UserBuilder userBuilder) {
        this.uId = userBuilder.uId;
        this.appUserId = userBuilder.appUserId;

        this.email = userBuilder.email;
        this.password = userBuilder.password;

        this.fName = userBuilder.fName;
        this.lName = userBuilder.lName;
        this.dob = userBuilder.dob;
        this.age = userBuilder.age;

        this.mobileNo = userBuilder.mobileNo;

        this.modifyDate = userBuilder.modifyDate;
        this.createdDate = userBuilder.createdDate;

        this.address1 = userBuilder.address1;
        this.address2 = userBuilder.address2;
        this.postcode = userBuilder.postcode;
        this.address_city = userBuilder.city;
        this.state = userBuilder.state;
        this.country = userBuilder.country;

        this.profileImageUrl = userBuilder.profileImageUrl;

        this.referral_code = userBuilder.referral_code;
        this.referred_by = userBuilder.referred_by;

        this.role = userBuilder.role;

        this.loggedInMode = userBuilder.loggedInMode;

        this.isActive = userBuilder.isActive;

        this.fcmToken = userBuilder.fcmToken;

        this.noOfBookings = userBuilder.noOfBookings;

        this.isOnline = userBuilder.isOnline;
        this.lastSeen = userBuilder.lastSeen;
    }


    @Exclude
    public String getUserFullName() {
        return String.format("%s %s", fName, lName);
    }


    public static class UserBuilder {

        public String uId;
        public long appUserId;

        public String email;
        public String password;

        public String fName;
        public String lName;
        public String dob;
        public int age;
        public String  resStop;
        public String mobileNo;

        public long modifyDate;
        public long createdDate;

        public String address1;
        public String address2;
        public String postcode;
        public String city;
        public String state;
        public String country;

        public String profileImageUrl;

        public String referral_code;
        public String referred_by;

        public String role;

        @LoginMode
        private int loggedInMode;

        public boolean isActive;

        public String fcmToken;

        public int noOfBookings;

        public boolean isOnline;
        public long lastSeen;


        public UserBuilder(String email, String password, @LoginMode int loggedInMode) {
            this.email = email;
            this.password = password;
            this.loggedInMode = loggedInMode;
        }


        public UserBuilder withOptionalUID(String uId) {
            this.uId = uId;
            return this;
        }

        public UserBuilder withOptionalAppUserId(long appUserId) {
            this.appUserId = appUserId;
            return this;
        }


        public UserBuilder withOptionalFirstName(String fName) {
            this.fName = fName;
            return this;
        }

        public UserBuilder withOptionalLastName(String lName) {
            this.lName = lName;
            return this;
        }

        public UserBuilder withOptionalDob(String dob) {
            this.dob = dob;
            return this;
        }

        public UserBuilder withOptionalAge(int age) {
            this.age = age;
            return this;
        }

        public UserBuilder withOptionalMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
            return this;
        }

        public UserBuilder withOptionalModifyDate(long modifyDate) {
            this.modifyDate = modifyDate;
            return this;
        }

        public UserBuilder withOptionalCreatedDate(long createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public UserBuilder withOptionalAddress1(String address1) {
            this.address1 = address1;
            return this;
        }

        public UserBuilder withOptionalAddress2(String address2) {
            this.address2 = address2;
            return this;
        }

        public UserBuilder withOptionalState(String state) {
            this.state = state;
            return this;
        }

        public UserBuilder withOptionalPostcode(String postcode) {
            this.postcode = postcode;
            return this;
        }

        public UserBuilder withOptionalCity(String city) {
            this.city = city;
            return this;
        }

        public UserBuilder withOptionalCountry(String country) {
            this.country = country;
            return this;
        }

        public UserBuilder withOptionalImageUrl(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
            return this;
        }


        public UserBuilder withOptionalReferralCode(String referralCode) {
            this.referral_code = referralCode;
            return this;
        }

        public UserBuilder withOptionalReferredBy(String referredBy) {
            this.referred_by = referredBy;
            return this;
        }


        public UserBuilder withOptionalRole(@UserRole String role) {
            this.role = role;
            return this;
        }

        public UserBuilder withOptionalResStop( String resStop) {
            this.resStop = resStop;
            return this;
        }
        public UserBuilder withOptionalIsActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }


        public UserBuilder withOptionalFCMToken(String fcmToken) {
            this.fcmToken = fcmToken;
            return this;
        }


        public UserBuilder withOptionalNoOfBookings(int noOfBookings) {
            this.noOfBookings = noOfBookings;
            return this;
        }


        public UserBuilder withOptionalIsOnline(boolean isOnline) {
            this.isOnline = isOnline;
            return this;
        }


        public UserBuilder withOptionalLastSeen(int lastSeen) {
            this.lastSeen = lastSeen;
            return this;
        }


        public User build() {
            return new User(this);
        }
    }


    @Exclude
    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", dob='" + dob + '\'' +
                ", age='" + age + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", modifyDate='" + modifyDate + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", postcode='" + postcode + '\'' +
                ", city='" + address_city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", referral_code='" + referral_code + '\'' +
                ", referred_by='" + referred_by + '\'' +
                ", role='" + role + '\'' +
                ", resStop='" + resStop + '\'' +
                ", loggedInMode=" + loggedInMode +
                ", isActive=" + isActive +
                ", fcmToken=" + fcmToken +
                ", isOnline=" + isOnline +
                ", lastSeen=" + lastSeen +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return appUserId == user.appUserId &&
                Objects.equals(uId, user.uId) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uId, appUserId, email);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uId);
        dest.writeLong(this.appUserId);
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.fName);
        dest.writeString(this.lName);
        dest.writeString(this.dob);
        dest.writeInt(this.age);

        dest.writeString(this.mobileNo);
        dest.writeString(this.resStop);

        dest.writeLong(this.modifyDate);
        dest.writeLong(this.createdDate);
        dest.writeString(this.address1);
        dest.writeString(this.address2);
        dest.writeString(this.postcode);
        dest.writeString(this.address_city);
        dest.writeString(this.state);
        dest.writeString(this.country);
        dest.writeString(this.profileImageUrl);
        dest.writeString(this.referral_code);
        dest.writeString(this.referred_by);
        dest.writeString(this.role);
        dest.writeInt(this.loggedInMode);
        dest.writeByte(this.isActive ? (byte) 1 : (byte) 0);
        dest.writeString(this.fcmToken);
        dest.writeInt(this.noOfBookings);
        dest.writeByte(this.isOnline ? (byte) 1 : (byte) 0);
        dest.writeLong(this.lastSeen);
    }

    protected User(Parcel in) {
        this.uId = in.readString();
        this.appUserId = in.readLong();
        this.email = in.readString();
        this.password = in.readString();
        this.fName = in.readString();
        this.lName = in.readString();
        this.dob = in.readString();
        this.resStop = in.readString();

        this.age = in.readInt();
        this.mobileNo = in.readString();
        this.modifyDate = in.readLong();
        this.createdDate = in.readLong();
        this.address1 = in.readString();
        this.address2 = in.readString();
        this.postcode = in.readString();
        this.address_city = in.readString();
        this.state = in.readString();
        this.country = in.readString();
        this.profileImageUrl = in.readString();
        this.referral_code = in.readString();
        this.referred_by = in.readString();
        this.role = in.readString();
        this.loggedInMode = in.readInt();
        this.isActive = in.readByte() != 0;
        this.fcmToken = in.readString();
        this.noOfBookings = in.readInt();
        this.isOnline = in.readByte() != 0;
        this.lastSeen = in.readLong();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
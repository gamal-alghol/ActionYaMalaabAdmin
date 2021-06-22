package com.malaab.ya.action.actionyamalaab.admin.utils;

import java.util.Arrays;
import java.util.List;

public final class Constants {

    public static final boolean isRelease = false;

    //    public static final String DB_NAME = "actionyamalab.db";
    public static final String PREF_NAME = "actionyamalab_admin_pref";
    public static String INTENT_KEY = "intent";
    public static String INTENT_KEY_PLAYGROUND = "playground";
    public static String INTENT_KEY_ISGUEST = "isGuest";
    public static String INTENT_KEY_IS_FIRST_TIME = "isFirstTime";
    public static String INTENT_KEY_MESSAGE = "message";
    public static final String KEY_USER = "User";

    public static final String FIREBASE_DB_META_DATA = "meta-data";
    public static final String FIREBASE_DB_META_DATA_USER_APPUSERID = "appUserId";

    public static final String FIREBASE_DB_UPDATES_FOLDER = "APK";

    public static final String FIREBASE_DB_APP_TABLE = "app";
    public static final String FIREBASE_DB_USERS_TABLE = "users";
    public static final String FIREBASE_DB_USERS_BOOKINGS_TABLE = "users_bookings";
    public static final String FIREBASE_DB_REGIONS_TABLE = "regions";
    public static final String FIREBASE_DB_PLAYGROUNDS_TABLE = "playgrounds";
    public static final String FIREBASE_DB_PLAYGROUNDS_SEARCH_TABLE = "playground_search";
    public static final String FIREBASE_DB_PLAYGROUNDS_SCHEDULES_TABLE = "playgrounds_schedules";
    public static final String FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_TABLE = "playgrounds_schedules_bookings";
    public static final String FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_INDIVIDUALS_TABLE = "playgrounds_schedules_bookings_individuals";
    public static final String FIREBASE_DB_CONTACT_US = "contact_us";
    public static final String FIREBASE_DB_NOTIFICATIONS_TABLE = "notifications";
    public static final String FIREBASE_DB_FINES_TABLE = "fines";

    public static final String PASSWORD_STRENGTH_WEAK = "Weak";
    public static final String PASSWORD_STRENGTH_MEDIUM = "Medium";
    public static final String PASSWORD_STRENGTH_STRONG = "Strong";

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";

    // broadcast receiver intent filters
    public static final String PUSH_COUNTERS = "Counters";
    public static final String PUSH_NOTIFICATION = "NotificationContent";
    public static final String KEY_NOTIFICATIONS_SETTINGS = "NotificationsSettings";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String REMOTE_CONFIG_DYNAMICBANNERS = "isDynamicBannersEnabled";

    public static final String DEFAULT_COUNTRY_CODE = "MY";

    public static String LANGUAGE_ARABIC = "Arabic";
    public static String LANGUAGE_ARABIC_CODE = "ar";

    public static final int EXIT_DELAY = 2000;

    public static final int TIMEOUT = 60;                               /* 60 Seconds */
    public static final int TIMEOUT_SHORT = 5;                          /* 5 Seconds */

    public static String INTENT_KEY_USER = "user";
    public static String INTENT_KEY_IS_OWNER = "isOwner";
    public static String INTENT_KEY_USER_UID = "userUid";
    public static String INTENT_KEY_UID = "uid";

    public static final int AGE_8 = 8;
    public static final int AGE_12 = 12;
    public static final int AGE_13 = 13;
    public static final int AGE_17 = 17;
    public static final int AGE_18 = 18;
    public static final int AGE_50 = 50;

    public static final int SIZE_10 = 10;
    public static final int SIZE_12 = 12;
    public static final int SIZE_14 = 14;
    public static final int SIZE_16 = 16;
    public static final int SIZE_18 = 18;

    public static final int DURATION_60 = 60;
    public static final int DURATION_90 = 90;
    public static final int DURATION_120 = 120;


    // Image supported formats
    public static final List<String> FILE_EXTN = Arrays.asList("jpg", "jpeg", "png");
}

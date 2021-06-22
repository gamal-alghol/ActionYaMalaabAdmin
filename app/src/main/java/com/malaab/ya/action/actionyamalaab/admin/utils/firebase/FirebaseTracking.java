package com.malaab.ya.action.actionyamalaab.admin.utils.firebase;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;

import dagger.internal.Preconditions;


public class FirebaseTracking implements IFirebaseTracking {

    private final String CATEGORY_SCREEN = "screen";
    private final String CATEGORY_CLICK = "click";

    private final FirebaseAnalytics firebaseAnalytics;


    public FirebaseTracking(FirebaseAnalytics firebaseAnalytics) {
        this.firebaseAnalytics = Preconditions.checkNotNull(firebaseAnalytics);
    }


    @Override
    public void LogEventScreen(String eventName) {
        LogEvent(CATEGORY_SCREEN, eventName);
    }

    @Override
    public void LogEventClick(String eventName) {
        LogEvent(CATEGORY_CLICK, eventName);
    }

    @Override
    public void LogEvent(String category, String eventName) {
        Bundle parameters = new Bundle();
        parameters.putString(FirebaseAnalytics.Param.CONTENT_TYPE, eventName);

        synchronized (firebaseAnalytics) {
            firebaseAnalytics.logEvent(category, parameters);
        }
    }

    @Override
    public void LogException(String exception) {
        FirebaseCrash.log(exception);
    }

    @Override
    public void setUserProperty(String experimentName, String experimentVariant) {
        synchronized (firebaseAnalytics) {
            firebaseAnalytics.setUserProperty(experimentName, experimentVariant);
        }
    }
}
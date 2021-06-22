package com.malaab.ya.action.actionyamalaab.admin.utils.analytics;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.malaab.ya.action.actionyamalaab.admin.R;

import java.util.HashMap;
import java.util.Map;


public class AnalyticsTracking implements IAnalyticsTracking {

    private final Context mContext;
    private final String CATEGORY_CLICK = "click";

    private final Map<Target, Tracker> mTrackers = new HashMap<>();

    public enum Target {
        APP,
        // Add more trackers here if you need, and update the code in #get(Target) below
    }


    public AnalyticsTracking(Context context) {
        mContext = context.getApplicationContext();

        get(Target.APP);
    }


    @Override
    public void LogEventScreen(String screenName) {
        Tracker t = get(Target.APP);

        t.setScreenName(screenName);
        t.send(new HitBuilders.ScreenViewBuilder().build());

        GoogleAnalytics.getInstance(mContext).dispatchLocalHits();
    }


    @Override
    public void LogEventClick(String eventName) {
        LogEvent(CATEGORY_CLICK, eventName, eventName);
    }


    @Override
    public void LogEvent(String category, String action, String label) {
        Tracker t = get(Target.APP);
        t.send(
                new HitBuilders.EventBuilder()
                        .setCategory(category)
                        .setAction(action)
                        .setLabel(label).build()
        );
    }


    @Override
    public void LogException(Exception exception) {
        if (exception != null) {
            Tracker t = get(Target.APP);

            t.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(
                            new StandardExceptionParser(mContext, null)
                                    .getDescription(Thread.currentThread().getName(), exception))
                    .setFatal(false)
                    .build()
            );
        }
    }


    @Override
    public void setUserProperty(String experimentName, String experimentVariant) {

    }


    public synchronized Tracker get(Target target) {
        if (!mTrackers.containsKey(target)) {
            Tracker tracker;

            switch (target) {
                case APP:
                    tracker = GoogleAnalytics.getInstance(mContext).newTracker(R.xml.app_tracker);
                    break;
                default:
                    throw new IllegalArgumentException("Unhandled analytics target " + target);
            }

            mTrackers.put(target, tracker);
        }

        return mTrackers.get(target);
    }
}
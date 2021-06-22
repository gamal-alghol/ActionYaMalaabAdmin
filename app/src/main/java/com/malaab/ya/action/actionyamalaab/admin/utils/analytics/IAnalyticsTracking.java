package com.malaab.ya.action.actionyamalaab.admin.utils.analytics;

public interface IAnalyticsTracking {

    void LogEventScreen(String screenName);

    void LogEventClick(String eventName);

    void LogEvent(String category, String action, String label);

    void LogException(Exception exception);

    void setUserProperty(String experimentName, String experimentVariant);
}

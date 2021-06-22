package com.malaab.ya.action.actionyamalaab.admin.di.component;

import android.content.Context;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.malaab.ya.action.actionyamalaab.admin.AppController;
import com.malaab.ya.action.actionyamalaab.admin.data.DataManager;
import com.malaab.ya.action.actionyamalaab.admin.di.ApplicationContext;
import com.malaab.ya.action.actionyamalaab.admin.di.module.ApplicationModule;
import com.malaab.ya.action.actionyamalaab.admin.utils.analytics.IAnalyticsTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.firebase.IFirebaseTracking;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(AppController app);

    @ApplicationContext
    Context context();


    FirebaseRemoteConfig getFirebaseRemoteConfig();

    DataManager getDataManager();


    IFirebaseTracking getFirebaseTracking();        /* To pass an instance to ActivityComponent activities and fragments*/

    IAnalyticsTracking getAnalyticsTracking();        /* To pass an instance to ActivityComponent activities and fragments*/
}
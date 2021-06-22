package com.malaab.ya.action.actionyamalaab.admin.ui.messages;

import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.data.DataManager;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Message;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.admin.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;
import com.malaab.ya.action.actionyamalaab.admin.utils.NetworkUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.analytics.IAnalyticsTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class MessagesPresenter<V extends MessagesMvpView>
        extends BasePresenter<V>
        implements MessagesMvpPresenter<V> {

    @Inject
    public AppCompatActivity mActivity;


    @Inject
    public MessagesPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IAnalyticsTracking iAnalyticsTracking, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iAnalyticsTracking, iFirebaseTracking);

        iAnalyticsTracking.LogEventScreen("Android - Admin - Messages Screen");
        iFirebaseTracking.LogEventScreen("Android - Admin - Messages Screen");
    }


    @Override
    public void getMessages() {
        getMvpView().showLoading();

        getDataManager().resetMessagesCounters();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_CONTACT_US);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!isViewAttached()) {
                    return;
                }

                getMvpView().hideLoading();

                if (!NetworkUtils.isNetworkConnected(mActivity.getApplicationContext())) {
                    getMvpView().onNoInternetConnection();
                    return;
                }

                if (dataSnapshot != null && dataSnapshot.exists()) {
                    Message message;
                    List<Message> list = new ArrayList<>();

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        message = child.getValue(Message.class);
                        if (message != null) {
                            message.uid = child.getKey();
                            list.add(message);
                        }
                    }

                    Collections.reverse(list);

                    getMvpView().onGetMessages(list);

                } else {
                    getMvpView().onError(R.string.error_no_data);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                AppLogger.e(" Error -> " + databaseError.toException().getLocalizedMessage());

                if (!isViewAttached()) {
                    return;
                }

                getMvpView().hideLoading();
            }
        });

    }
}

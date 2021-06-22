package com.malaab.ya.action.actionyamalaab.admin.ui.playgrounds;

import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.annotations.LoginMode;
import com.malaab.ya.action.actionyamalaab.admin.data.DataManager;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.bookings.BookingsMvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.bookings.BookingsMvpView;
import com.malaab.ya.action.actionyamalaab.admin.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;
import com.malaab.ya.action.actionyamalaab.admin.utils.NetworkUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.analytics.IAnalyticsTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class PlaygroundsPresenter<V extends PlaygroundsMvpView>
        extends BasePresenter<V>
        implements PlaygroundsMvpPresenter<V> {

    @Inject
    public AppCompatActivity mActivity;


    @Inject
    public PlaygroundsPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IAnalyticsTracking iAnalyticsTracking, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iAnalyticsTracking, iFirebaseTracking);

        iAnalyticsTracking.LogEventScreen("Android - Admin - Playgrounds Screen");
        iFirebaseTracking.LogEventScreen("Android - Admin - Playgrounds Screen");
    }


    @Override
    public void getCurrentUserInfoLocal() {
        if (!isViewAttached()) {
            return;
        }

        if (getDataManager().getCurrentUser() == null || getDataManager().getCurrentUser().loggedInMode == LoginMode.LOGGED_IN_MODE_LOGGED_OUT) {
            getMvpView().onError(R.string.msg_user_not_signed_in);
        } else {
            getMvpView().onUserLoggedIn(getDataManager().getCurrentUser());
        }
    }


    @Override
    public void getPlaygrounds() {
        getMvpView().showLoading();

        DatabaseReference mDatabasePlaygrounds = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_TABLE);
        mDatabasePlaygrounds.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    Playground playground;
                    List<Playground> playgrounds = new ArrayList<>();

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        playground = child.getValue(Playground.class);
                        if (playground != null) {
                            playground.playgroundId = child.getKey();
                        }

                        playgrounds.add(playground);
                    }

                    getMvpView().onGetPlayground(playgrounds);

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

package com.malaab.ya.action.actionyamalaab.admin.ui.base;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.data.DataManager;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.di.ApplicationContext;
import com.malaab.ya.action.actionyamalaab.admin.exceptions.NoConnectivityException;
import com.malaab.ya.action.actionyamalaab.admin.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;
import com.malaab.ya.action.actionyamalaab.admin.utils.DateTimeUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.StringUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.analytics.IAnalyticsTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.rx.SchedulerProvider;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * onAttach() and onDetach(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private final DataManager mDataManager;
    private final SchedulerProvider mSchedulerProvider;
    private final CompositeDisposable mCompositeDisposable;

    private V mMvpView;

    private IAnalyticsTracking mAnalyticsTracking;
    private IFirebaseTracking mFirebaseTracking;

    @Inject
    @ApplicationContext
    Context mContext;


    @Inject
    public BasePresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IAnalyticsTracking analyticsTracking, IFirebaseTracking firebaseTracking) {
        this.mDataManager = dataManager;
        this.mSchedulerProvider = schedulerProvider;
        this.mCompositeDisposable = compositeDisposable;

        mAnalyticsTracking = analyticsTracking;
        mFirebaseTracking = firebaseTracking;
    }

    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void onDetach() {
        mCompositeDisposable.dispose();
        mMvpView = null;
    }


    @Override
    public void onResume() {
        updateUserOnlineStatus(true);
    }

    @Override
    public void onPause() {
        updateUserOnlineStatus(false);
    }


    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public V getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }


    public DataManager getDataManager() {
        return mDataManager;
    }

    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }


//    public User getCurrentUser() {
//        return mDataManager.getCurrentUser();
//    }

    public User getUserDetails() {
        return mDataManager.getCurrentUser();
    }

    public String getUserUid() {
        return mDataManager.getCurrentUser() != null && !StringUtils.isEmpty(mDataManager.getCurrentUser().uId) ? mDataManager.getCurrentUser().uId : "";
    }

    public String getUsername() {
        return mDataManager.getCurrentUser() != null && !StringUtils.isEmpty(mDataManager.getCurrentUser().email) ? mDataManager.getCurrentUser().email : "";
    }


    @Override
    public void setUserAsLoggedOut() {
        getDataManager().setUserAsLoggedOut();
    }


    @Override
    public IAnalyticsTracking getIAnalyticsTrackingTracking() {
        return mAnalyticsTracking;
    }

    @Override
    public IFirebaseTracking getIFirebaseTracking() {
        return mFirebaseTracking;
    }


    @Override
    public void handleApiError(Throwable e) {
        AppLogger.e(e.getMessage());

        if (e instanceof SocketTimeoutException) {
            AppLogger.e("SocketTimeoutException");
            getMvpView().onError(R.string.error_timeout);

//        } else if (e instanceof IOException) {
//            AppLogger.e("IOException");
//            getMvpView().onError(R.string.error_no_connection);
//            getMvpView().onError(R.string.error);

        } else if (e instanceof NoConnectivityException || e instanceof UnknownHostException) {
            AppLogger.e("NoConnectivityException");
            getMvpView().onNoInternetConnection();
//            getMvpView().onError(R.string.error_no_connection);

        } else {
            AppLogger.e("onUnknownError");
            getMvpView().onError(R.string.error);
        }
    }

    private static class MvpViewNotAttachedException extends RuntimeException {
        MvpViewNotAttachedException() {
            super("Please call Presenter.onAttach(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }


    private void updateUserOnlineStatus(boolean isUserOnline) {
        User user = getUserDetails();

        if (user == null || getUserUid().isEmpty()) {
            return;
        }

        user.isOnline = isUserOnline;
        user.lastSeen = DateTimeUtils.getCurrentDatetime();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_USERS_TABLE);
        mDatabase.child(user.uId)
                .setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AppLogger.i(" onSuccess");
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AppLogger.i(" onComplete");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AppLogger.e(" Error -> " + e.getLocalizedMessage());
                    }
                });
    }
}

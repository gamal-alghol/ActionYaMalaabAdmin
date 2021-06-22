package com.malaab.ya.action.actionyamalaab.admin.ui.users.details;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.annotations.NotificationType;
import com.malaab.ya.action.actionyamalaab.admin.data.DataManager;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.admin.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;
import com.malaab.ya.action.actionyamalaab.admin.utils.FirebaseUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.StringUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.analytics.IAnalyticsTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class UsersDetailsPresenter<V extends UsersDetailsMvpView>
        extends BasePresenter<V>
        implements UsersDetailsMvpPresenter<V> {

    @Inject
    public AppCompatActivity mActivity;


    @Inject
    public UsersDetailsPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IAnalyticsTracking iAnalyticsTracking, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iAnalyticsTracking, iFirebaseTracking);

        iAnalyticsTracking.LogEventScreen("Android - Admin - Users Details Screen");
        iFirebaseTracking.LogEventScreen("Android - Admin - Users Details Screen");
    }


    @Override
    public void getUserDetails(String userUid) {
        if (StringUtils.isEmpty(userUid)) {
            getMvpView().onError(R.string.error_no_data);
            return;
        }

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_USERS_TABLE);
        mDatabase.child(userUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        if (dataSnapshot != null && dataSnapshot.exists()) {
                            User user = dataSnapshot.getValue(User.class);

                            if (user != null) {
                                getMvpView().onGetUserDetails(user);
                            }

                        } else {
                            getMvpView().showMessage(R.string.error_no_data);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        AppLogger.d("onCancelled = " + databaseError.getMessage());

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().showMessage(R.string.error);
                    }
                });
    }


    @Override
    public void getOwnerPlaygroundsCount(String ownerUid) {
        DatabaseReference mDatabasePlaygrounds = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_TABLE);
        mDatabasePlaygrounds.orderByChild("ownerId")
                .equalTo(ownerUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (!isViewAttached()) {
                            return;
                        }

                        if (dataSnapshot != null && dataSnapshot.exists()) {
                            getMvpView().onGetOwnerPlaygroundsCount((int) dataSnapshot.getChildrenCount());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        AppLogger.e(" Error -> " + databaseError.toException());
                    }
                });
    }


    @Override
    public void deactivateUser(final String userUid, final boolean isActive) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_USERS_TABLE);
        mDatabase.child(userUid)
                .child("isActive")
                .setValue(isActive)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AppLogger.i(" onSuccess");

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().onBlockUserSuccess();
                        getMvpView().showMessage(R.string.msg_success);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AppLogger.i(" onComplete");

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        if (isActive) {
                            FirebaseUtils.sendNotificationToUser(NotificationType.USER_ADMIN_APPROVED, userUid,
                                    getDataManager().getCurrentUser().uId, getDataManager().getCurrentUser().getUserFullName(), getDataManager().getCurrentUser().profileImageUrl,
                                    "");
                        }
                        if (!isActive) {
                            FirebaseUtils.sendNotificationToUser(NotificationType.BLOCK, userUid,
                                    getDataManager().getCurrentUser().uId, getDataManager().getCurrentUser().getUserFullName(), getDataManager().getCurrentUser().profileImageUrl,
                                    "");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AppLogger.e(" Error -> " + e.getLocalizedMessage());

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().onBlockUserFailed();
                        getMvpView().onError(e.getLocalizedMessage());
                    }
                });
    }

}

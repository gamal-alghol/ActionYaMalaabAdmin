package com.malaab.ya.action.actionyamalaab.admin.ui.messages.details;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.annotations.NotificationType;
import com.malaab.ya.action.actionyamalaab.admin.data.DataManager;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Message;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.admin.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;
import com.malaab.ya.action.actionyamalaab.admin.utils.DateTimeUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.FirebaseUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.analytics.IAnalyticsTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class MessagesDetailsPresenter<V extends MessagesDetailsMvpView>
        extends BasePresenter<V>
        implements MessagesDetailsMvpPresenter<V> {

    @Inject
    public AppCompatActivity mActivity;


    @Inject
    public MessagesDetailsPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IAnalyticsTracking iAnalyticsTracking, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iAnalyticsTracking, iFirebaseTracking);

        iAnalyticsTracking.LogEventScreen("Android - Admin - Messages Details Screen");
        iFirebaseTracking.LogEventScreen("Android - Admin - Messages Details Screen");
    }


    @Override
    public void getCurrentUserLocal() {
        if (!isViewAttached()) {
            return;
        }

        getMvpView().onGetCurrentUser(getDataManager().getCurrentUser());
    }


    @Override
    public void sendNewMessage(final String toUserUid, String toUserAppId, String toUsername, String toUserEmail, String toUserPhone, String toUserProfileImage,
                               final String fromUserUid, String fromUserAppId, final String fromUsername, String fromUserEmail, String fromUserPhone, final String fromUserProfileImage,
                               final String text) {

        getMvpView().showLoading();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_CONTACT_US);
        String uid = mDatabase.push().getKey();

        Message message = new Message(uid,
                toUserUid, toUserAppId, toUsername, toUserEmail, toUserPhone, toUserProfileImage,
                fromUserUid, fromUserAppId, fromUsername, fromUserEmail, fromUserPhone, fromUserProfileImage,
                text, DateTimeUtils.getCurrentDatetime(),
                true);

        mDatabase.child(uid)
                .setValue(message)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AppLogger.i(" onSuccess");

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        FirebaseUtils.sendNotificationToUser(NotificationType.MESSAGE_CONTACT_US,
                                toUserUid,
                                fromUserUid, fromUsername, fromUserProfileImage,
                                text);

                        getMvpView().onSendNewMessageSuccess();
                        getMvpView().showMessage(R.string.msg_contactus_success);
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
                        getMvpView().onError(e.getLocalizedMessage());
                    }
                });
    }

    @Override
    public void sendReply(Message message,
                          final String toUserUid, String toUserAppId, String toUsername, String toUserEmail, String toUserPhone, String toUserProfileImage,
                          final String fromUserUid, String fromUserAppId, final String fromUsername, String fromUserEmail, String fromUserPhone, final String fromUserProfileImage,
                          final String text) {

        getMvpView().showLoading();

        message.reply = new Message(toUserUid, toUserAppId, toUsername, toUserEmail, toUserPhone, toUserProfileImage,
                fromUserUid, fromUserAppId, fromUsername, fromUserEmail, fromUserPhone, fromUserProfileImage,
                text, DateTimeUtils.getCurrentDatetime(),
                true);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_CONTACT_US);

        mDatabase.child(message.uid)
                .setValue(message)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AppLogger.i(" onSuccess");

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        FirebaseUtils.sendNotificationToUser(NotificationType.MESSAGE_CONTACT_US,
                                toUserUid,
                                fromUserUid, fromUsername, fromUserProfileImage,
                                text);

                        getMvpView().onSendReplySuccess();
                        getMvpView().showMessage(R.string.msg_contactus_success);
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
                        getMvpView().onError(e.getLocalizedMessage());
                    }
                });
    }


//    @Override
//    public void getMessageDetails(String uid) {
//
//        getMvpView().showLoading();
//
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_CONTACT_US);
//        mDatabase.child(uid)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                        if (!isViewAttached()) {
//                            return;
//                        }
//
//                        getMvpView().hideLoading();
//
//                        if (!NetworkUtils.isNetworkConnected(mActivity.getApplicationContext())) {
//                            getMvpView().onNoInternetConnection();
//                            return;
//                        }
//
//                        if (dataSnapshot != null && dataSnapshot.exists()) {
//
//                            Message message = dataSnapshot.getValue(Message.class);
//                            if (message != null) {
//                                getMvpView().onGetMessageDetails(message);
//                            }
//
//                        } else {
//                            getMvpView().onError(R.string.error_no_data);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        AppLogger.e(" Error -> " + databaseError.toException().getLocalizedMessage());
//
//                        if (!isViewAttached()) {
//                            return;
//                        }
//
//                        getMvpView().hideLoading();
//                    }
//                });
//    }
//
//    @Override
//    public void getUserDetails(String uid) {
//
//        getMvpView().showLoading();
//
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_USERS_TABLE);
//        mDatabase.child(uid)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                        if (!isViewAttached()) {
//                            return;
//                        }
//
//                        getMvpView().hideLoading();
//
//                        if (!NetworkUtils.isNetworkConnected(mActivity.getApplicationContext())) {
//                            getMvpView().onNoInternetConnection();
//                            return;
//                        }
//
//                        if (dataSnapshot != null && dataSnapshot.exists()) {
//
//                            User user = dataSnapshot.getValue(User.class);
//                            if (user != null) {
//                                getMvpView().onGetUserDetails(user);
//                            }
//
//                        } else {
//                            getMvpView().onError(R.string.error_no_data);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        AppLogger.e(" Error -> " + databaseError.toException().getLocalizedMessage());
//
//                        if (!isViewAttached()) {
//                            return;
//                        }
//
//                        getMvpView().hideLoading();
//                    }
//                });
//    }

}

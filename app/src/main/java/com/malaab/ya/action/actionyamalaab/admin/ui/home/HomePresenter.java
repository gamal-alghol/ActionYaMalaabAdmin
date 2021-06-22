package com.malaab.ya.action.actionyamalaab.admin.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.esafirm.rxdownloader.RxDownloader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malaab.ya.action.actionyamalaab.admin.BuildConfig;
import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.annotations.UserRole;
import com.malaab.ya.action.actionyamalaab.admin.data.DataManager;
import com.malaab.ya.action.actionyamalaab.admin.data.model.Counter;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.AppUpdate;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.admin.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.admin.utils.AppUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;
import com.malaab.ya.action.actionyamalaab.admin.utils.StringUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.analytics.IAnalyticsTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.rx.SchedulerProvider;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;


public class HomePresenter<V extends HomeMvpView>
        extends BasePresenter<V>
        implements HomeMvpPresenter<V> {

    @Inject
    public AppCompatActivity mActivity;

    //    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;
    private ValueEventListener mValueEventListener;


    @Inject
    public HomePresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IAnalyticsTracking iAnalyticsTracking, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iAnalyticsTracking, iFirebaseTracking);

        iAnalyticsTracking.LogEventScreen("Android - Admin - Home Screen");
        iFirebaseTracking.LogEventScreen("Android - Admin - Home Screen");

//        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void getCurrentUserInfoLocal() {
        if (!isViewAttached()) {
            return;
        }

        if (getUserDetails().role.equals(UserRole.ROLE_ADMIN)) {
            getMvpView().onUserAsAdmin();
        } else {
            getMvpView().onUserAsStaff();
        }
    }


    @Override
    public void isUserAuthenticated() {
        getMvpView().onUserAuthenticationSuccess(getUserDetails().uId);

//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    // User is signed in
//                    AppLogger.d("onAuthStateChanged:signed_in:" + user.getUid() + " | " + user.getEmail() + " | " + user.getPhoneNumber());
//                    getMvpView().onUserAuthenticationSuccess(user.getUid());
//                } else {
//                    // User is signed out
//                    if (getDataManager().getCurrentUser() != null && getDataManager().getCurrentUser().loggedInMode == LoginMode.LOGGED_IN_MODE_LOGGED_OUT) {
//                        getMvpView().onUserAuthenticationFailed(0);
//                    } else {
//                        AppLogger.d("onAuthStateChanged:signed_out");
//                        getMvpView().onUserAuthenticationFailed(R.string.msg_user_not_signed_in);
//                    }
//                }
//            }
//        };
//
//        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    public void refreshUserInfo(final String userUid) {
        if (StringUtils.isEmpty(userUid)) {
            getMvpView().onRefreshUserInfo(null);
            return;
        }

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_USERS_TABLE).child(userUid);

        /* To load the list once only*/
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!isViewAttached()) {
                    return;
                }

                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        getDataManager().updateCurrentUserInfo(userUid, user.appUserId,
                                user.email, user.password, user.fName, user.lName, user.dob, user.age, user.mobileNo,
                                user.modifyDate, user.createdDate,
                                user.address1, user.address2, user.postcode, user.address_city, user.state, user.country,
                                user.profileImageUrl,
                                user.referral_code, user.referred_by,
                                user.role,
                                user.fcmToken,
                                user.loggedInMode, user.isActive);

                        getMvpView().onRefreshUserInfo(user);
                        return;
                    }
                }

                getMvpView().onRefreshUserInfo(null);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                AppLogger.e(" Error -> " + error.toException());

                if (!isViewAttached()) {
                    return;
                }

                getMvpView().onRefreshUserInfo(null);
            }
        };

        mDatabase.addListenerForSingleValueEvent(mValueEventListener);
    }


    @Override
    public void updateLastSeen(final String userUid, long lastSeen) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_USERS_TABLE);
        mDatabase.child(userUid)
                .child("lastSeen")
                .setValue(lastSeen)
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


    @Override
    public void updateCounters() {
        Counter counter = getDataManager().getCounters();
        if (counter != null) {
            getMvpView().onUpdateNotificationsCounter(counter.notificationsCounter);
            getMvpView().onUpdateMessagesCounter(counter.messagesCounter);
        }
    }


    @Override
    public void isDeviceRegisteredForNotifications(User user) {
        if (getDataManager().getNotificationsSettings() == null
                || !getDataManager().getNotificationsSettings().isRegistered)

            getMvpView().onRegisterDeviceForNotification(user);
    }

    @Override
    public void registerForFirebaseNotifications(User user) {
        if (user == null) {
            user = getDataManager().getCurrentUser();
        }

        if (user == null) {
            return;
        }

        final String token = getDataManager().getNotificationsSettings() == null ? "" : getDataManager().getNotificationsSettings().firebaseToken;
        if (StringUtils.isEmpty(token)) {
            return;
        }

        user.fcmToken = token;

        final User finalUser = user;

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_USERS_TABLE);
        mDatabase.child(finalUser.uId)
                .setValue(finalUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AppLogger.i(" onSuccess");

                        if (!isViewAttached()) {
                            return;
                        }

                        getDataManager().updateCurrentUserInfo(finalUser.uId, finalUser.appUserId,
                                finalUser.email, finalUser.password, finalUser.fName, finalUser.lName, finalUser.dob, finalUser.age, finalUser.mobileNo,
                                finalUser.modifyDate, finalUser.createdDate,
                                finalUser.address1, finalUser.address2, finalUser.postcode, finalUser.address_city, finalUser.state, finalUser.country,
                                finalUser.profileImageUrl,
                                finalUser.referral_code, finalUser.referred_by,
                                finalUser.role,
                                finalUser.fcmToken,
                                finalUser.loggedInMode, finalUser.isActive);

                        getDataManager().updateNotificationsSettings(token, true);

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

                        if (!isViewAttached()) {
                            return;
                        }

                        getDataManager().updateNotificationsSettings(token, false);
                    }
                });
    }

    @Override
    public void checkForUpdates() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_APP_TABLE);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!isViewAttached()) {
                    return;
                }

                if (dataSnapshot.exists()) {
                    AppUpdate appUpdate = dataSnapshot.getValue(AppUpdate.class);
                    if (appUpdate != null) {
                        if (AppUtils.compareVersionNames(BuildConfig.VERSION_NAME, appUpdate.version) == -1) {
                            String url = "gs://actionyamalaab.appspot.com/APK/Admin_v" + appUpdate.version + ".apk";
                            getMvpView().onUpdateAvailable(appUpdate.version, url);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                AppLogger.e(" Error -> " + databaseError.toException());
            }
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void downloadNewVersion(final Context context, final String newVersion, final String downloadUrl) {
        if (StringUtils.isEmpty(downloadUrl)) {
            return;
        }

//        final String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        final String fileName = "Admin_v" + newVersion + ".apk";

//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference();
//        StorageReference islandRef = storageRef.child(Constants.FIREBASE_DB_UPDATES_FOLDER + "/" + fileName);
//
//        final File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
//
//        islandRef.getFile(file)
//                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//
//                        AppLogger.i("Local file created -> " + file.toString());
//
//                        getMvpView().onDownloadNewVersionCompleted(file);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        AppLogger.e(exception.getMessage());
//                    }
//                });


//        File rootPath = new File(Environment.getExternalStorageDirectory(), "file_name");
//        if (!rootPath.exists()) {
//            rootPath.mkdirs();
//        }
//
//        final File localFile = new File(rootPath, "imageName.txt");
//
//        islandRef.getFile(localFile)
//                .addOnSuccessListener(
//                        new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//
//                                AppLogger.e("Local tem file created  created " + localFile.toString());
//
//                                //  updateDb(timestamp,localFile.toString(),position);
////                                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
//
//                            }
//                        })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        AppLogger.e(exception.getMessage());
//                    }
//                });
//
//

        RxDownloader rxDownloader = new RxDownloader(context);
        rxDownloader.download(
                "https://firebasestorage.googleapis.com/v0/b/actionyamalaab.appspot.com/o/APK%2FAdmin_v1.1.3.apk?alt=media&token=dac2ca95-9321-4b8d-b645-43e6b7368f95",
                fileName,
                "application/vnd.android.package-archive",
                true)
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String filePath) throws Exception {

//                        if (!isViewAttached()) {
//                            return;
//                        }

                        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                        AppUtils.installNewVersion(mActivity, file);
//                        getMvpView().onDownloadNewVersionCompleted(file);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        AppLogger.e(throwable.getMessage());
                        getMvpView().onDownloadNewVersionFailed(R.string.error_download_app);
                    }
                });
    }


    @Override
    public void signOut() {
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }

        getDataManager().setUserAsLoggedOut();
        getMvpView().onUserAuthenticationFailed(0);

//        mAuth.signOut();

//        getMvpView().onUserSignOut();
    }


    @Override
    public void onDetach() {
        super.onDetach();

//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
    }
}

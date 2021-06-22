package com.malaab.ya.action.actionyamalaab.admin.ui.staff.add;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.annotations.LoginMode;
import com.malaab.ya.action.actionyamalaab.admin.annotations.UserRole;
import com.malaab.ya.action.actionyamalaab.admin.data.DataManager;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.admin.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;
import com.malaab.ya.action.actionyamalaab.admin.utils.DateTimeUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.StringUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.analytics.IAnalyticsTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.rx.SchedulerProvider;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class StaffAddPresenter<V extends StaffAddMvpView>
        extends BasePresenter<V>
        implements StaffAddMvpPresenter<V> {

    @Inject
    public AppCompatActivity mActivity;


    @Inject
    public StaffAddPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IAnalyticsTracking iAnalyticsTracking, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iAnalyticsTracking, iFirebaseTracking);

        iAnalyticsTracking.LogEventScreen("Android - Admin - Staff Add Screen");
        iFirebaseTracking.LogEventScreen("Android - Admin - Staff Add Screen");
    }


    @Override
    public void addStaffToServer(final String fName, final String lName, final String phone, final String email, final String password, String confirmPassword) {
        if (!StringUtils.isWordsMatched(password, confirmPassword)) {
            getMvpView().onError("الرقم السري غير متطايق");
            return;
        }

        getMvpView().showLoading();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!isViewAttached()) {
                            return;
                        }

                        if (task.isSuccessful()) {
                            AppLogger.i("Authentication Success.");

                            // creating user object
                            User user = new User(new User.UserBuilder(email, password, LoginMode.LOGGED_IN_MODE_SERVER)
                                    .withOptionalUID(task.getResult().getUser().getUid())
                                    .withOptionalRole(UserRole.ROLE_ADMIN_STAFF)
                                    .withOptionalIsActive(true)
                                    .withOptionalFirstName(fName)
                                    .withOptionalLastName(lName)
                                    .withOptionalMobileNo(phone)
                                    .withOptionalCreatedDate(DateTimeUtils.getCurrentDatetime())
                            );

                            getMvpView().onStaffAddedToServer(user);

                        } else {
                            AppLogger.e("Authentication Failed." + task.getException());

                            getMvpView().hideLoading();
                            getMvpView().onError("Authentication Failed \n" + task.getException());
                        }
                    }
                });
    }

    @Override
    public void generateUserUniqueId(final User user) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_META_DATA).child(Constants.FIREBASE_DB_META_DATA_USER_APPUSERID);
        mDatabase.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer currentValue = mutableData.getValue(Integer.class);
                if (currentValue == null) {
                    mutableData.setValue(1);
                } else {
                    mutableData.setValue(currentValue + 1);
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                // Analyse databaseError for any error during increment
//                AppLogger.d("onComplete - getMessage()" + databaseError.getMessage());

                if (!isViewAttached()) {
                    return;
                }

                if (dataSnapshot.exists()) {
                    Long appUserId = dataSnapshot.getValue(Long.class);
                    if (appUserId != null) {
                        user.appUserId = appUserId;
                        getMvpView().onUserUniqueIdGenerated(user);
                        return;
                    }
                }

                getMvpView().hideLoading();
                getMvpView().onError(R.string.error);
            }
        });
    }

    @Override
    public void addStaffToeDatabase(final User user) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_USERS_TABLE);
        mDatabase.child(user.uId)
                .setValue(user)  // pushing user to 'users' node using the userId
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        getMvpView().hideLoading();

                        if (task.isSuccessful()) {
                            AppLogger.i("Adding User To Database -> Success.");

                            getMvpView().onStaffAddedToDatabase(user);

                        } else {
                            AppLogger.i("Adding User To Database -> Failed.");
                            getMvpView().onError(Objects.requireNonNull(task.getException()).getMessage());
                        }
                    }
                });

    }
}

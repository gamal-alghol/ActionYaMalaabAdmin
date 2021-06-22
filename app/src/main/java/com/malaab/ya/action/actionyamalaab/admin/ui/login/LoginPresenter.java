package com.malaab.ya.action.actionyamalaab.admin.ui.login;

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
import com.google.firebase.database.ValueEventListener;
import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.annotations.LoginMode;
import com.malaab.ya.action.actionyamalaab.admin.annotations.UserRole;
import com.malaab.ya.action.actionyamalaab.admin.data.DataManager;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.admin.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;
import com.malaab.ya.action.actionyamalaab.admin.utils.analytics.IAnalyticsTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.rx.SchedulerProvider;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class LoginPresenter<V extends LoginMvpView>
        extends BasePresenter<V>
        implements LoginMvpPresenter<V> {

    @Inject
    public AppCompatActivity mActivity;

    private FirebaseAuth mAuth;


    @Inject
    public LoginPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IAnalyticsTracking iAnalyticsTracking, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iAnalyticsTracking, iFirebaseTracking);

        iAnalyticsTracking.LogEventScreen("Android - Admin - Login Screen");
        iFirebaseTracking.LogEventScreen("Android - Admin - Login Screen");

        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void doServerLogin(final String email, final String password) {
        getMvpView().showLoading();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!isViewAttached()) {
                            return;
                        }

                        if (task.isSuccessful()) {
                            Logger.i("Authentication Success.");
                            getMvpView().onServerLoginSuccess(task.getResult().getUser().getUid());

                        } else {
                            getMvpView().hideLoading();

                            if (task.getException() != null) {
                                Logger.e("Authentication Failed => " + task.getException().getMessage());
                                getMvpView().showMessage("Authentication Failed \n" + task.getException().getMessage());
                            }
                        }
                    }
                });
    }


    @Override
    public void isUserExistInDB(String userUId) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_USERS_TABLE);
        mDatabase.child(userUId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        if (dataSnapshot.exists()) {
                            User user = dataSnapshot.getValue(User.class);
                            if (user != null) {

                                if ((user.role.equals(UserRole.ROLE_ADMIN)
                                        || user.role.equals(UserRole.ROLE_ADMIN_STAFF))
                                        && user.isActive) {

                                    user.uId = dataSnapshot.getKey();
                                    getDataManager().setCurrentUser(user);
                                    getMvpView().onUserExistInDB(user);

                                } else {
                                    getMvpView().showMessage(R.string.error_user_owner);
                                }
                            }

                        } else {
                            getMvpView().showMessage(R.string.error_user_not_exist);
                            getMvpView().onUserNotExistInDB();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        AppLogger.d("isUserExistInDB onCancelled = " + databaseError.getMessage());

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().showMessage(R.string.error);
                    }
                });
    }


    @Override
    public void forgotPassword(String username, String email) {
//        getMvpView().showLoading();
//
//        getCompositeDisposable().add(getDataManager()
//                .forgotPassword(username, email, getUserLanguage())
//                .subscribeOn(getSchedulerProvider().io())
//                .observeOn(getSchedulerProvider().ui())
//                .subscribe(new Consumer<ForgotPasswordResponse>() {
//                    @Override
//                    public void accept(ForgotPasswordResponse response) throws Exception {
//
//                        if (!isViewAttached()) {
//                            return;
//                        }
//
//                        getMvpView().hideLoading();
//
//                        if (response != null && response.result != null) {
//                            if (response.result.status == 0) {
//                                getMvpView().onResetPassword(context.getString(R.string.error_forgot_password));
//
//                            } else {
//                                getMvpView().onResetPassword(context.getString(R.string.msg_reset_password_success));
//                            }
//                        } else {
//                            getMvpView().onError(R.string.error);
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        AppLogger.e(throwable.getMessage());
//
//                        if (!isViewAttached()) {
//                            return;
//                        }
//
//                        getMvpView().hideLoading();
//
//                        handleApiError(throwable);
//                    }
//                }));
    }

}

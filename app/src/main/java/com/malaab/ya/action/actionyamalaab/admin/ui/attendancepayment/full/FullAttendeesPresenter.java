package com.malaab.ya.action.actionyamalaab.admin.ui.attendancepayment.full;

import android.support.annotation.NonNull;
import android.util.Log;

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
import com.malaab.ya.action.actionyamalaab.admin.annotations.BookingStatus;
import com.malaab.ya.action.actionyamalaab.admin.annotations.BookingType;
import com.malaab.ya.action.actionyamalaab.admin.annotations.FineStatus;
import com.malaab.ya.action.actionyamalaab.admin.annotations.FineType;
import com.malaab.ya.action.actionyamalaab.admin.annotations.NotificationType;
import com.malaab.ya.action.actionyamalaab.admin.data.DataManager;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Fine;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.admin.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;
import com.malaab.ya.action.actionyamalaab.admin.utils.DateTimeUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.FirebaseUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.ListUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.analytics.IAnalyticsTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class FullAttendeesPresenter<V extends FullAttendeesMvpView>
        extends BasePresenter<V>
        implements FullAttendeesMvpPresenter<V> {

    private DatabaseReference mDatabase;


    @Inject
    public FullAttendeesPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IAnalyticsTracking iAnalyticsTracking, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iAnalyticsTracking, iFirebaseTracking);

        iAnalyticsTracking.LogEventScreen("Android - Admin - Full Attendees Screen");
        iFirebaseTracking.LogEventScreen("Android - Admin - Full Attendees Screen");

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_TABLE);
    }


    @Override
    public void getCurrentUser() {
        if (!isViewAttached()) {
            return;
        }

        getMvpView().onGetCurrentUser(getDataManager().getCurrentUser());
    }


    @Override
    public void getFullBookings(long startDatetime, long endDatetime) {
        getMvpView().showLoading();
           Log.d("ttt","startDatetime "+startDatetime);
       Log.d("ttt","endDatetime "+endDatetime);

        mDatabase.orderByChild("timeStart")
                .startAt(startDatetime)
                .endAt(endDatetime)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        if (dataSnapshot != null && dataSnapshot.exists()) {

                            Booking booking;
                            List<Booking> fullBookings = new ArrayList<>();
//                            List<Booking> individualBookings = new ArrayList<>();

                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                booking = child.getValue(Booking.class);

                                if (booking != null && booking.status == BookingStatus.OWNER_RECEIVED) {
                                    Log.d("ttt","booking"+booking.timeStart);
                                    Log.d("ttt","booking,timeEnd"+booking.timeEnd);

                                    if (booking.isIndividuals) {
//                                        individualBookings.add(booking);
                                    } else {
//                                        if (DateTimeUtils.isDateAfterCurrentDate(booking.timeStart)) {
                                            fullBookings.add(booking);
//                                        }
                                    }
                                }
                            }

                            ListUtils.sortBookingsListDesc(fullBookings);

                            getMvpView().onGetFullBookings(fullBookings);

                        } else {
                            getMvpView().onGetFullBookingsEmpty();
                            getMvpView().onError(R.string.error_no_data);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        AppLogger.e(" Error -> " + databaseError.toException());

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                    }
                });
    }

    @Override
    public void getFullBookingsFines(final List<Booking> bookings) {
        for (final Booking booking : bookings) {

            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_FINES_TABLE).child(booking.userId);
            mDatabase
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {

                                Fine fine;

                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    fine = child.getValue(Fine.class);
                                    if (fine != null) {
                                        booking.totalFines += fine.amount;
                                    }
                                }

                                getMvpView().onGetFullBookingsFines(booking);
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


    @Override
    public void takeAttendanceBooking(final Booking booking) {
        getMvpView().showLoading();

        booking.hasFine = false;
        booking.isAttended = true;
        booking.isPaid = true;

        mDatabase.child(booking.bookingUId)
                .setValue(booking)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().onTakeAttendanceBookingSuccess(booking);
                        getMvpView().showMessage(R.string.msg_success);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AppLogger.w(" onComplete");

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
    public void takeAbsentBooking(final Booking booking) {
        getMvpView().showLoading();

        booking.hasFine = true;
        booking.isAttended = false;
        booking.isPaid = false;

        mDatabase.child(booking.bookingUId)
                .setValue(booking)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().onTakeAbsentBookingSuccess(booking);
                        getMvpView().showMessage(R.string.msg_success);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AppLogger.w(" onComplete");

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
    public void createFine(final Booking booking) {
        Fine fine = new Fine();
        fine.userId = booking.userId;
        fine.bookingId = booking.bookingUId;
        fine.playgroundId = booking.playgroundId;
        fine.playground = booking.playground;
        fine.fineType = FineType.ATTENDANCE;
        fine.datetimeCreated = DateTimeUtils.getCurrentDatetime();
        fine.timeStart = booking.timeStart;
        fine.timeEnd = booking.timeEnd;
        fine.fineStatus = FineStatus.NOT_PAID;
        fine.bookType = BookingType.FULL;
        fine.amount = 50;

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_FINES_TABLE).child(booking.userId).push();

        mDatabase
                .setValue(fine)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AppLogger.w(" onSuccess");

                        getMvpView().onFineCreateSuccess(booking);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AppLogger.w(" onComplete");

                        FirebaseUtils.sendNotificationToUser(NotificationType.FINE_ISSUED,
                                booking.user.uId,
                                getDataManager().getCurrentUser().uId, getDataManager().getCurrentUser().getUserFullName(), getDataManager().getCurrentUser().profileImageUrl);

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

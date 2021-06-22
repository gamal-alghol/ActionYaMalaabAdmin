package com.malaab.ya.action.actionyamalaab.admin.ui.attendancepayment.individual;

import android.support.annotation.NonNull;

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
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.BookingAgeCategory;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.BookingPlayer;
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
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class IndividualAttendeesPresenter<V extends IndividualAttendeesMvpView>
        extends BasePresenter<V>
        implements IndividualAttendeesMvpPresenter<V> {

    private DatabaseReference mDatabase;


    @Inject
    public IndividualAttendeesPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IAnalyticsTracking iAnalyticsTracking, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iAnalyticsTracking, iFirebaseTracking);

        iAnalyticsTracking.LogEventScreen("Android - Admin - Individual Attendees Screen");
        iFirebaseTracking.LogEventScreen("Android - Admin - Individual Attendees Screen");

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_INDIVIDUALS_TABLE);
    }


    @Override
    public void getCurrentUser() {
        if (!isViewAttached()) {
            return;
        }

        getMvpView().onGetCurrentUser(getDataManager().getCurrentUser());
    }


    @Override
    public void getIndividualBookings(long startDatetime, long endDatetime) {
        getMvpView().showLoading();

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

                        if (dataSnapshot.exists()) {
                            Booking booking;
//                            List<Booking> fullBookings = new ArrayList<>();
                            List<Booking> individualBookings = new ArrayList<>();

                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                booking = child.getValue(Booking.class);

                                if (booking != null && booking.status == BookingStatus.ADMIN_APPROVED) {

                                    if (booking.isIndividuals) {
//                                        if (DateTimeUtils.isDateAfterCurrentDate(booking.timeStart)) {
                                            individualBookings.add(booking);
//                                        }
                                    } else {
//                                        fullBookings.add(booking);
                                    }
                                }
                            }

                            ListUtils.sortBookingsListDesc(individualBookings);

                            getMvpView().onGetIndividualBookings(individualBookings);

                        }
                        else {
                            getMvpView().onGetIndividualBookingsEmpty();
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


//    @Override
//    public void takeAttendanceBooking(final Booking booking, String userUid) {
//        getMvpView().showLoading();
//
//        booking.isAttended = true;
//        booking.isPaid = true;
//
//        mDatabase.child(booking.bookingUId)
//                .setValue(booking)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        if (!isViewAttached()) {
//                            return;
//                        }
//
//                        getMvpView().hideLoading();
//                        getMvpView().onTakeAttendanceBookingSuccess(booking);
//                        getMvpView().showMessage(R.string.msg_success);
//                    }
//                })
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        AppLogger.w(" onComplete");
//
//                        if (!isViewAttached()) {
//                            return;
//                        }
//
//                        getMvpView().hideLoading();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        AppLogger.e(" Error -> " + e.getLocalizedMessage());
//
//                        if (!isViewAttached()) {
//                            return;
//                        }
//
//                        getMvpView().hideLoading();
//                        getMvpView().onError(e.getLocalizedMessage());
//                    }
//                });
//    }

    @Override
    public void takeAttendanceBooking(final Booking booking, final BookingPlayer player) {
        getMvpView().showLoading();

        if (!ListUtils.isEmpty(booking.ageCategories)) {
            for (BookingAgeCategory category : booking.ageCategories) {
                if (category.isConfirmed) {
                    for (BookingPlayer p : category.players) {
                        if (p.uId.equals(player.uId)) {
                            p.hasFine = false;
                            p.isAttended = true;
                            p.isPaid = true;
                            booking.isAttended = true;
                            booking.isPaid = true;
                            break;
                        }
                    }
                }
            }
        }

        mDatabase.child(booking.bookingUId)
                .setValue(booking)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        getMvpView().onTakeAttendanceBookingSuccess(booking, player);

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
        FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_INDIVIDUALS_TABLE)
                .child(booking.bookingUId)
                .setValue(booking)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        getMvpView().onTakeAttendanceBookingSuccess(booking, player);

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
    public void takeAbsentBooking(final Booking booking, final BookingPlayer player) {
        getMvpView().showLoading();

        if (!ListUtils.isEmpty(booking.ageCategories)) {
            for (BookingAgeCategory category : booking.ageCategories) {
                if (category.isConfirmed) {
                    for (BookingPlayer p : category.players) {
                        if (p.uId.equals(player.uId)) {
                            p.hasFine = true;
                            p.isAttended = false;
                            p.isPaid = false;

                            booking.isAttended = true;
                            booking.isPaid = true;

                            break;
                        }
                    }
                }
            }
        }

        mDatabase.child(booking.bookingUId)
                .setValue(booking)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        getMvpView().onTakeAbsentBookingSuccess(booking, player);

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
    public void createFine(final Booking booking, final String playerId) {
        Fine fine = new Fine();
        fine.userId = playerId;
        fine.bookingId = booking.bookingUId;
        fine.playgroundId = booking.playgroundId;
        fine.playground = booking.playground;
        fine.fineType = FineType.ATTENDANCE;
        fine.datetimeCreated = DateTimeUtils.getCurrentDatetime();
        fine.timeStart = booking.timeStart;
        fine.timeEnd = booking.timeEnd;
        fine.fineStatus = FineStatus.NOT_PAID;
        fine.bookType = BookingType.INDIVIDUAL;
        fine.amount = booking.priceIndividual ;

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_FINES_TABLE).child(playerId).push();

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
                                playerId,
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

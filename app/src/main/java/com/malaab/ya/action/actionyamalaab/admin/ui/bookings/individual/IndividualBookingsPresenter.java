package com.malaab.ya.action.actionyamalaab.admin.ui.bookings.individual;

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
import com.malaab.ya.action.actionyamalaab.admin.annotations.NotificationType;
import com.malaab.ya.action.actionyamalaab.admin.data.DataManager;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.BookingAgeCategory;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.BookingPlayer;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.admin.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;
import com.malaab.ya.action.actionyamalaab.admin.utils.FirebaseUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.ListUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.analytics.IAnalyticsTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class IndividualBookingsPresenter<V extends IndividualBookingsMvpView>
        extends BasePresenter<V>
        implements IndividualBookingsMvpPresenter<V> {

    private DatabaseReference mDatabase;


    @Inject
    public IndividualBookingsPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IAnalyticsTracking iAnalyticsTracking, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iAnalyticsTracking, iFirebaseTracking);

        iAnalyticsTracking.LogEventScreen("Android - Admin - Individual Bookings Screen");
        iFirebaseTracking.LogEventScreen("Android - Admin - Individual Bookings Screen");
    }


    @Override
    public void getIndividualBookings(long startDatetime, long endDatetime) {
        getMvpView().showLoading();

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_INDIVIDUALS_TABLE);
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
                            List<Booking> individualBookings = new ArrayList<>();
//                            List<Booking> individualBookings = new ArrayList<>();

                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                booking = child.getValue(Booking.class);

                                if (booking != null && booking.isActive) {

                                    booking.bookingUId = child.getKey();

                                    if (booking.isIndividuals) {

                                        individualBookings.add(booking);

//                                        if (!ListUtils.isEmpty(booking.ageCategories)) {
//
//                                            for (BookingAgeCategory category : booking.ageCategories) {
//
//                                                if (!ListUtils.isEmpty(category.players)) {
//
//                                                    int players = category.players.size();
//
//                                                    for (BookingPlayer player : category.players) {
//                                                        players += player.invitees;
//                                                    }
//
//                                                    if(booking.size == players){
//                                                        individualBookings.add(booking);
//                                                        break;
//                                                    }
//                                                }
//                                            }
//                                        }

//                                        for (BookingAgeCategory category : booking.ageCategories) {
//                                            if (category.isConfirmed) {
//                                                individualBookings.add(booking);
//                                            }
//                                        }

                                    } else {
//                                        fullBookings.add(booking);
                                    }
                                }
                            }

//                            Collections.reverse(individualBookings);

                            ListUtils.sortBookingsListDesc(individualBookings);

                            getMvpView().onGetIndividualBookings(individualBookings);
//                            getMvpView().onGetIndividualBookings(ListUtils.sortBookingsListDesc(individualBookings));

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


    @Override
    public void confirmAgeCategory(final Booking booking, int ageRangeStart) {
        getMvpView().showLoading();

        booking.status = BookingStatus.ADMIN_APPROVED;
        booking.ownerId_status = booking.ownerId + "_" + BookingStatus.ADMIN_APPROVED;

        mDatabase.child(booking.bookingUId)
                .setValue(booking)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().onConfirmBookingSuccess(booking);
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

//                        FirebaseUtils.sendNotificationToOwner(NotificationType.BOOKING_ADMIN_APPROVED, booking.ownerId,
//                                getDataManager().getCurrentUser().uId, getDataManager().getCurrentUser().getUserFullName(), getDataManager().getCurrentUser().profileImageUrl);

                        for (BookingAgeCategory category : booking.ageCategories) {
                            if (!ListUtils.isEmpty(category.players)) {

                                if (category.isConfirmed) {
                                    for (BookingPlayer player : category.players) {
                                        FirebaseUtils.sendNotificationToUser(NotificationType.BOOKING_INDIVIDUAL_COMPLETED,
                                                getDataManager().getCurrentUser().uId, getDataManager().getCurrentUser().getUserFullName(), getDataManager().getCurrentUser().profileImageUrl,
                                                player.uId, player.name, player.profileImageUrl);

                                        updateUserBooking(booking, player.uId, BookingStatus.ADMIN_APPROVED);
                                    }
                                } else {
                                    for (BookingPlayer player : category.players) {
                                        FirebaseUtils.sendNotificationToUser(NotificationType.BOOKING_INDIVIDUAL_NOT_COMPLETED,
                                                getDataManager().getCurrentUser().uId, getDataManager().getCurrentUser().getUserFullName(), getDataManager().getCurrentUser().profileImageUrl,
                                                player.uId, player.name, player.profileImageUrl);

                                        updateUserBooking(booking, player.uId, BookingStatus.ADMIN_REJECTED);
                                    }
                                }
                            }
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
                        getMvpView().onError(e.getLocalizedMessage());
                    }
                });
    }

    @Override
    public void rejectAgeCategory(final Booking booking, final int ageRangeStart) {
        getMvpView().showLoading();

        mDatabase.child(booking.bookingUId)
                .setValue(booking)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().onRejectBookingSuccess(booking);
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

//                        FirebaseUtils.sendNotificationToUser(NotificationType.BOOKING_ADMIN_REJECTED,
//                                getDataManager().getCurrentUser().uId, getDataManager().getCurrentUser().getUserFullName(), getDataManager().getCurrentUser().profileImageUrl,
//                                booking.user.uId, booking.user.name, booking.user.profileImageUrl);

                        for (BookingAgeCategory category : booking.ageCategories) {

                            if (!ListUtils.isEmpty(category.players)) {

                                if (category.ageRangeStart == ageRangeStart) {
                                    for (BookingPlayer player : category.players) {
                                        FirebaseUtils.sendNotificationToUser(NotificationType.BOOKING_INDIVIDUAL_NOT_COMPLETED,
                                                getDataManager().getCurrentUser().uId, getDataManager().getCurrentUser().getUserFullName(), getDataManager().getCurrentUser().profileImageUrl,
                                                player.uId, player.name, player.profileImageUrl);

                                        updateUserBooking(booking, player.uId, BookingStatus.ADMIN_REJECTED);
                                    }
                                }
                            }
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
                        getMvpView().onError(e.getLocalizedMessage());
                    }
                });
    }


    @Override
    public void confirmBooking(final Booking booking) {
        getMvpView().showLoading();

        booking.status = BookingStatus.ADMIN_APPROVED;
        booking.ownerId_status = booking.ownerId + "_" + BookingStatus.ADMIN_APPROVED;

        if (!ListUtils.isEmpty(booking.ageCategories)) {

            for (BookingAgeCategory category : booking.ageCategories) {

                if (!ListUtils.isEmpty(category.players)) {

                    int players = category.players.size();

                    for (BookingPlayer player : category.players) {
                        players += player.invitees;
                    }

                    if (booking.size == players) {
                        category.isConfirmed = true;
                        break;
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
                        getMvpView().onConfirmBookingSuccess(booking);
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

                        FirebaseUtils.sendNotificationToOwner(NotificationType.BOOKING_ADMIN_APPROVED, booking.ownerId,
                                getDataManager().getCurrentUser().uId, getDataManager().getCurrentUser().getUserFullName(), getDataManager().getCurrentUser().profileImageUrl);

//                        FirebaseUtils.sendNotificationToUser(NotificationType.BOOKING_INDIVIDUAL_COMPLETED,
//                                getDataManager().getCurrentUser().uId, getDataManager().getCurrentUser().getUserFullName(), getDataManager().getCurrentUser().profileImageUrl,
//                                booking.user.uId, booking.user.name, booking.user.profileImageUrl);

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
    public void rejectBooking(final Booking booking) {
        getMvpView().showLoading();

        booking.status = BookingStatus.ADMIN_REJECTED;
        booking.ownerId_status = booking.ownerId + "_" + BookingStatus.ADMIN_REJECTED;

        mDatabase.child(booking.bookingUId)
                .setValue(booking)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().onRejectBookingSuccess(booking);
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

//                        FirebaseUtils.sendNotificationToUser(NotificationType.BOOKING_ADMIN_REJECTED,
//                                getDataManager().getCurrentUser().uId, getDataManager().getCurrentUser().getUserFullName(), getDataManager().getCurrentUser().profileImageUrl,
//                                booking.user.uId, booking.user.name, booking.user.profileImageUrl);

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
    public void cancelBooking(final Booking booking) {
        getMvpView().showLoading();

        booking.isActive = false;

        mDatabase.child(booking.bookingUId)
                .setValue(booking)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().onCancelBookingSuccess(booking);
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

                        for (BookingAgeCategory category : booking.ageCategories) {
                            if (!ListUtils.isEmpty(category.players)) {
                                for (BookingPlayer player : category.players) {

                                    FirebaseUtils.sendNotificationToUser(NotificationType.BOOKING_ADMIN_REJECTED,
                                            getDataManager().getCurrentUser().uId, getDataManager().getCurrentUser().getUserFullName(), getDataManager().getCurrentUser().profileImageUrl,
                                            player.uId, player.name, player.profileImageUrl);

                                    updateUserBooking(booking, player.uId, BookingStatus.ADMIN_REJECTED);
                                }
                            }
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
                        getMvpView().onError(e.getLocalizedMessage());
                    }
                });
    }


    private void updateUserBooking(Booking booking, String userUId, @BookingStatus int bookingStatus) {
        booking.status = bookingStatus;

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_USERS_BOOKINGS_TABLE).child(userUId);
        mDatabase
                .child(booking.bookingUId)
                .setValue(booking)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AppLogger.w(" onSuccess");
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AppLogger.w(" onComplete");
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

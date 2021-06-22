package com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings.upcoming;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malaab.ya.action.actionyamalaab.admin.data.DataManager;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.admin.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;
import com.malaab.ya.action.actionyamalaab.admin.utils.DateTimeUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.ListUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.analytics.IAnalyticsTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class UpcomingBookingsPresenter<V extends UpcomingBookingsMvpView>
        extends BasePresenter<V>
        implements UpcomingBookingsMvpPresenter<V> {

    private DatabaseReference mDatabase;
    //    private ValueEventListener mValueEventListener;
    private int count = 0;


    @Inject
    public UpcomingBookingsPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IAnalyticsTracking iAnalyticsTracking, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iAnalyticsTracking, iFirebaseTracking);

        iAnalyticsTracking.LogEventScreen("Android - Admin - Upcoming Bookings Screen");
        iFirebaseTracking.LogEventScreen("Android - Admin - Upcoming Bookings Screen");
    }


    @Override
    public void getUpcomingBookings(final String userUid) {
        getMvpView().showLoading();

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_TABLE);
        mDatabase.orderByChild("userId")
                .equalTo(userUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Booking booking;
                        List<Booking> upcomingBookings = new ArrayList<>();

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            booking = child.getValue(Booking.class);

                            if (booking != null) {
                                if (DateTimeUtils.isDateAfterCurrentDate(booking.timeStart)) {
                                    upcomingBookings.add(booking);
                                }
                            }
                        }

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

//                        Collections.reverse(pastBookings);

                        ListUtils.sortBookingsListDesc(upcomingBookings);

                        getMvpView().onGetUpcomingBookings(upcomingBookings);
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


//        mDatabase.child(Constants.FIREBASE_DB_USERS_BOOKINGS_TABLE)
//                .child(userUid)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                        if (dataSnapshot.exists()) {
//
//                            final Booking booking;
//                            final List<Booking> upcomingBookings = new ArrayList<>();
//                            List<Booking> pastBookings = new ArrayList<>();
//
//                            for (DataSnapshot child : dataSnapshot.getChildren()) {
//                                String key = child.getKey();
//
//                                mDatabase.child(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_TABLE)
//                                        .child(key)
//                                        .addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                                if (dataSnapshot.exists()) {
//
//                                                    Booking booking = dataSnapshot.getValue(Booking.class);
//                                                    if (booking != null) {
//                                                        upcomingBookings.add(booking);
//                                                    }
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onCancelled(DatabaseError databaseError) {
//
//                                            }
//                                        });
//
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });


//        /* To load the list once only*/
//        mValueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//
//
//                Booking booking;
//                List<Booking> upcomingBookings = new ArrayList<>();
//                List<Booking> pastBookings = new ArrayList<>();
//
//                for (DataSnapshot child : dataSnapshot.getChildren()) {
//                    booking = child.getValue(Booking.class);
//
//                    if (booking != null) {
//                        if (DateTimeUtils.isDateAfterCurrentDate(DateTimeUtils.changeDateFormat(booking.datetimeCreated, DateTimeUtils.PATTERN_DATE_1, DateTimeUtils.PATTERN_DATETIME_DEFAULT))) {
//                            upcomingBookings.add(booking);
//                        } else {
//                            pastBookings.add(booking);
//                        }
//                    }
//                }
//
//                if (!isViewAttached()) {
//                    return;
//                }
//
//                getMvpView().hideLoading();
//
//                getMvpView().onGetUpcomingBookings(upcomingBookings);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                AppLogger.e(" Error -> " + error.toException());
//
//                if (!isViewAttached()) {
//                    return;
//                }
//
//                getMvpView().hideLoading();
//            }
//        };
//
//        mDatabase.addListenerForSingleValueEvent(mValueEventListener);
    }
}

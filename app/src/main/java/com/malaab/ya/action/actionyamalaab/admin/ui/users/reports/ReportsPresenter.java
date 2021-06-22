package com.malaab.ya.action.actionyamalaab.admin.ui.users.reports;

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


public class ReportsPresenter<V extends ReportsMvpView>
        extends BasePresenter<V>
        implements ReportsMvpPresenter<V> {


    @Inject
    public ReportsPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IAnalyticsTracking iAnalyticsTracking, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iAnalyticsTracking, iFirebaseTracking);

        iAnalyticsTracking.LogEventScreen("Android - Admin - Owner Reports Screen");
        iFirebaseTracking.LogEventScreen("Android - Admin - Owner Reports Screen");
    }


    @Override
    public void getFullReports(String ownerUid, final long date) {
        getMvpView().showLoading();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_TABLE);

        mDatabase.orderByChild("ownerId")
                .equalTo(ownerUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Booking booking;
                        List<Booking> bookings = new ArrayList<>();

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            booking = child.getValue(Booking.class);

                            if (booking != null) {
                                bookings.add(booking);
                            }
                        }

                        if (!isViewAttached()) {
                            return;
                        }

                        if (date > 0) {
                            for (Booking booking1 : bookings) {
                                if (!DateTimeUtils.isDatesEqual(booking1.timeStart, date)) {
                                    bookings.remove(booking1);
                                }
                            }
                        }

//                        Collections.reverse(bookings);

                        getMvpView().hideLoading();

                        getMvpView().onGetReportsAsFull(bookings, date);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        AppLogger.e(" Error -> " + databaseError.toException());

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        getMvpView().onGetReportsAsFull(new ArrayList<Booking>(), date);
                    }
                });
    }

    @Override
    public void getIndividualReports(String ownerUid, final List<Booking> bookings, final long date) {
        getMvpView().showLoading();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_INDIVIDUALS_TABLE);

        mDatabase.orderByChild("ownerId")
                .equalTo(ownerUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        Booking booking;

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            booking = child.getValue(Booking.class);

                            if (booking != null) {
                                bookings.add(booking);
                            }
                        }

                        if (date > 0) {
                            for (Booking booking1 : bookings) {
                                if (!DateTimeUtils.isDatesEqual(booking1.timeStart, date)) {
                                    bookings.remove(booking1);
                                }
                            }
                        }

//                        Collections.reverse(bookings);
                        ListUtils.sortBookingsListDesc(bookings);

                        getMvpView().onGetReportsAsIndividual(bookings);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        AppLogger.e(" Error -> " + databaseError.toException());

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        getMvpView().onGetReportsAsIndividual(bookings);
                    }
                });
    }

//    @Override
//    public void getReports(long date) {
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_TABLE);
//
//        mDatabase.orderByChild("timeStart").startAt(date).endAt(date + "\uf8ff")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                        Booking booking;
//                        List<Booking> pastBookings = new ArrayList<>();
//
//                        for (DataSnapshot child : dataSnapshot.getChildren()) {
//                            booking = child.getValue(Booking.class);
//
//                            if (booking != null) {
//                                pastBookings.add(booking);
//                            }
//                        }
//
//                        if (!isViewAttached()) {
//                            return;
//                        }
//
//                        getMvpView().hideLoading();
//
//                        Collections.reverse(pastBookings);
//
//                        getMvpView().onGetReports(pastBookings);
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        AppLogger.e(" Error -> " + databaseError.toException());
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

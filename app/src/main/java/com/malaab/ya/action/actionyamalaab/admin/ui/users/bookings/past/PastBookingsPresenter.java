package com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings.past;

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


public class PastBookingsPresenter<V extends PastBookingsMvpView>
        extends BasePresenter<V>
        implements PastBookingsMvpPresenter<V> {

    private DatabaseReference mDatabase;
    private ValueEventListener mValueEventListener;


    @Inject
    public PastBookingsPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IAnalyticsTracking iAnalyticsTracking, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iAnalyticsTracking, iFirebaseTracking);

        iAnalyticsTracking.LogEventScreen("Android - Admin - Past Bookings Screen");
        iFirebaseTracking.LogEventScreen("Android - Admin - Past Bookings Screen");

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_TABLE);
    }


    @Override
    public void getPastBookings(String userUid) {
        getMvpView().showLoading();

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_TABLE);
        mDatabase.orderByChild("userId")
                .equalTo(userUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Booking booking;
                        List<Booking> pastBookings = new ArrayList<>();

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            booking = child.getValue(Booking.class);

                            if (booking != null) {
                                if (!DateTimeUtils.isDateAfterCurrentDate(booking.timeStart)) {
                                    booking.isPast = true;
                                    pastBookings.add(booking);
                                }
                            }
                        }

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

//                        Collections.reverse(pastBookings);

                        ListUtils.sortBookingsListDesc(pastBookings);

                        getMvpView().onGetPastBookings(pastBookings);
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
}

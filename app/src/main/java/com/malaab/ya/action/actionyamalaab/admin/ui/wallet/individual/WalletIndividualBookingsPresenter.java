package com.malaab.ya.action.actionyamalaab.admin.ui.wallet.individual;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.data.DataManager;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.admin.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;
import com.malaab.ya.action.actionyamalaab.admin.utils.ListUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.analytics.IAnalyticsTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class WalletIndividualBookingsPresenter<V extends WalletIndividualBookingsMvpView>
        extends BasePresenter<V>
        implements WalletIndividualBookingsMvpPresenter<V> {

    private DatabaseReference mDatabase;


    @Inject
    public WalletIndividualBookingsPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IAnalyticsTracking iAnalyticsTracking, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iAnalyticsTracking, iFirebaseTracking);

        iAnalyticsTracking.LogEventScreen("Android - Admin - Wallet Individual Bookings Screen");
        iFirebaseTracking.LogEventScreen("Android - Admin - Wallet Individual Bookings Screen");

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_INDIVIDUALS_TABLE);
    }


    @Override
    public void getWalletIndividualBookings(long startDatetime, long endDatetime) {
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
                            List<Booking> bookings = new ArrayList<>();

                            float total = 0;
                            int totalBooking = 0;

                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                booking = child.getValue(Booking.class);
                                Log.d("ttt",booking.bookingUId);
                                if (booking != null) {
                                    if (booking.isPaid) {
                                        total += booking.price;
                                        totalBooking+=1;
                                        bookings.add(booking);
                                    }
                                }
                            }

                            ListUtils.sortBookingsListDesc(bookings);

                            getMvpView().onGetWalletIndividualBookings(bookings, total,totalBooking);

                        } else {
                            getMvpView().onGetWalletIndividualBookingsEmpty();
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
}

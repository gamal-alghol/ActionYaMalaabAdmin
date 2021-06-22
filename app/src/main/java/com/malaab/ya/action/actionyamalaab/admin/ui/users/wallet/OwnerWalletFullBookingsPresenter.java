package com.malaab.ya.action.actionyamalaab.admin.ui.users.wallet;

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
import com.malaab.ya.action.actionyamalaab.admin.utils.StringUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.analytics.IAnalyticsTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.admin.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class OwnerWalletFullBookingsPresenter<V extends OwnerWalletFullBookingsMvpView>
        extends BasePresenter<V>
        implements OwnerWalletFullBookingsMvpPresenter<V> {

    private DatabaseReference mDatabase;


    @Inject
    public OwnerWalletFullBookingsPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IAnalyticsTracking iAnalyticsTracking, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iAnalyticsTracking, iFirebaseTracking);

        iAnalyticsTracking.LogEventScreen("Android - Admin - Owner Wallet Full Bookings Screen");
        iFirebaseTracking.LogEventScreen("Android - Admin - Owner Wallet Full Bookings Screen");

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_TABLE);
    }


    @Override
    public void getWalletFullBookings(final String ownerUid, long startDatetime, long endDatetime) {
        if (StringUtils.isEmpty(ownerUid)) {
            getMvpView().onError(R.string.error_user_not_exist);
            return;
        }

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

                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                booking = child.getValue(Booking.class);
                                if (booking != null && booking.ownerId.equals(ownerUid)) {
                                    if (booking.isPaid) {
                                        total += booking.price;
                                        bookings.add(booking);
                                    }
                                }
                            }

                            if (ListUtils.isEmpty(bookings)) {
                                getMvpView().onGetWalletFullBookingsEmpty();
                                getMvpView().onError(R.string.error_no_data);
                                return;
                            }

                            ListUtils.sortBookingsListDesc(bookings);

                            getMvpView().onGetWalletFullBookings(bookings, total);

                        } else {
                            getMvpView().onGetWalletFullBookingsEmpty();
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

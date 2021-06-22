package com.malaab.ya.action.actionyamalaab.admin.ui.bookings.individual;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.annotations.ItemAction;
import com.malaab.ya.action.actionyamalaab.admin.custom.DialogAgeCategoriesSelection;
import com.malaab.ya.action.actionyamalaab.admin.custom.DialogConfirmation;
import com.malaab.ya.action.actionyamalaab.admin.custom.DialogList;
import com.malaab.ya.action.actionyamalaab.admin.custom.OnDialogAgeCategoriesSelection;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.BookingPlayer;
import com.malaab.ya.action.actionyamalaab.admin.di.component.ActivityComponent;
import com.malaab.ya.action.actionyamalaab.admin.events.OnEventFilterByDatetime;
import com.malaab.ya.action.actionyamalaab.admin.events.OnEventItemClicked;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseFragment;
import com.malaab.ya.action.actionyamalaab.admin.ui.bookings.BookingsActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.details.UsersDetailsActivity;
import com.malaab.ya.action.actionyamalaab.admin.utils.ActivityUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;
import com.malaab.ya.action.actionyamalaab.admin.utils.DateTimeUtils;
import com.yayandroid.theactivitymanager.TheActivityManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IndividualBookingsFragment extends BaseFragment implements IndividualBookingsMvpView {

    @BindView(R.id.pBar_loading)
    public ProgressBar pBar_loading;

    @BindView(R.id.rv_items)
    public RecyclerView rv_items;

    @Inject
    IndividualBookingsMvpPresenter<IndividualBookingsMvpView> mPresenter;

    @Inject
    DialogConfirmation mDialogConfirmation;
    @Inject
    DialogList mDialogList;
    @Inject
    DialogAgeCategoriesSelection mDialogAgeCategoriesSelection;

    private IndividualBookingsAdapter adapter;

    private long startDatetime, endDatetime;

    private boolean mUserVisibleHint = true;
    private boolean isLoaded = false;


    public IndividualBookingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);

        mUserVisibleHint = visible;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_bookings, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, rootView));
            mPresenter.onAttach(this);
        }

        return rootView;
    }


    @Override
    protected void initUI() {
        rv_items.setHasFixedSize(true);
        rv_items.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        mDialogList.build();
        mDialogAgeCategoriesSelection.build();

        /* To enable smooth scroll on RecyclerView */
        ViewCompat.setNestedScrollingEnabled(rv_items, false);
    }


    @Override
    public void showLoading() {
        pBar_loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pBar_loading.setVisibility(View.GONE);
    }


    @Override
    public void onGetIndividualBookings(List<Booking> bookings) {
        adapter = new IndividualBookingsAdapter(bookings);
        rv_items.setAdapter(adapter);

        rv_items.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGetIndividualBookingsEmpty() {
        rv_items.setAdapter(null);
        rv_items.setVisibility(View.GONE);
    }

    @Override
    public void onConfirmBookingSuccess(Booking booking) {
        adapter.updateItem(booking, true);
    }

    @Override
    public void onRejectBookingSuccess(Booking booking) {
        adapter.updateItem(booking, true);
    }

    @Override
    public void onCancelBookingSuccess(Booking booking) {
        adapter.removeItem(booking, true);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventItemClicked(final OnEventItemClicked event) {
        if (TheActivityManager.getInstance().getCurrentActivity() instanceof BookingsActivity && mUserVisibleHint) {

            if (event.getItem() instanceof Booking) {
                final Booking booking = (Booking) event.getItem();

                if (event.getAction() == ItemAction.CONFIRM) {
                    mDialogAgeCategoriesSelection.addBookingAgeCategories(booking, booking.size);
                    mDialogAgeCategoriesSelection.showBookingAgeCategories(true);
                    mDialogAgeCategoriesSelection.setOnDialogAgeCategoriesListener(new OnDialogAgeCategoriesSelection() {
                        @Override
                        public void onAgeYoungSelected(Booking b) {
                            mPresenter.confirmAgeCategory(b, 8);
                        }

                        @Override
                        public void onAgeMiddleSelected(Booking b) {
                            mPresenter.confirmAgeCategory(b, 13);
                        }

                        @Override
                        public void onAgeOldSelected(Booking b) {
                            mPresenter.confirmAgeCategory(b, 18);
                        }
                    });

                } else if (event.getAction() == ItemAction.REJECT) {
                    mDialogAgeCategoriesSelection.addBookingAgeCategories(booking, booking.size);
                    mDialogAgeCategoriesSelection.showBookingAgeCategories(false);
                    mDialogAgeCategoriesSelection.setOnDialogAgeCategoriesListener(new OnDialogAgeCategoriesSelection() {
                        @Override
                        public void onAgeYoungSelected(Booking b) {
                            mPresenter.rejectAgeCategory(b, 8);
                        }

                        @Override
                        public void onAgeMiddleSelected(Booking b) {
                            mPresenter.rejectAgeCategory(b, 13);
                        }

                        @Override
                        public void onAgeOldSelected(Booking b) {
                            mPresenter.rejectAgeCategory(b, 18);
                        }
                    });

                } else if (event.getAction() == ItemAction.VIEW_LIST) {
                    mDialogList.addBookingAgeCategories(booking, booking.size);
                    mDialogList.showBookingAgeCategories();

                } else if (event.getAction() == ItemAction.CANCEL) {
                    mDialogConfirmation
                            .withTitle(getString(R.string.dialog_title_cancel))
                            .withMessage("")
                            .withPositiveButton(getString(R.string.yes))
                            .withNegativeButton(getString(R.string.no))
                            .setOnDialogConfirmationListener(new DialogConfirmation.OnDialogConfirmationListener() {
                                @Override
                                public void onPositiveButtonClick() {
                                    mPresenter.cancelBooking(booking);
                                }

                                @Override
                                public void onNegativeButtonClick() {
                                }
                            })
                            .build()
                            .show();
                }

            } else if (event.getItem() instanceof BookingPlayer) {
                mDialogList.dismiss();

                Bundle bundle = new Bundle();
                bundle.putString(Constants.INTENT_KEY_USER_UID, ((BookingPlayer) event.getItem()).uId);

                ActivityUtils.goTo(getActivity(), UsersDetailsActivity.class, false, bundle);

            } else if (event.getItem() instanceof String) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.INTENT_KEY_USER_UID, ((String) event.getItem()));

                ActivityUtils.goTo(getActivity(), UsersDetailsActivity.class, false, bundle);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventFilterByDatetime(OnEventFilterByDatetime event) {
        if (mUserVisibleHint) {
            startDatetime = event.getDatetime();
            endDatetime = DateTimeUtils.addDaysToDate(startDatetime, 1).getTime();

            mPresenter.getIndividualBookings(startDatetime, endDatetime);
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isLoaded) {
            isLoaded = true;

            startDatetime = DateTimeUtils.getCurrentDatetime();
            endDatetime = DateTimeUtils.addDaysToDate(startDatetime, 7).getTime();

            mPresenter.getIndividualBookings(startDatetime, endDatetime);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        mDialogList.dismiss();
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        mPresenter.onDetach();
        super.onDetach();
    }
}
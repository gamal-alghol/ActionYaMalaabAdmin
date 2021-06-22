package com.malaab.ya.action.actionyamalaab.admin.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.malaab.ya.action.actionyamalaab.admin.custom.DialogAgeCategoriesSelection;
import com.malaab.ya.action.actionyamalaab.admin.custom.DialogConfirmation;
import com.malaab.ya.action.actionyamalaab.admin.custom.DialogList;
import com.malaab.ya.action.actionyamalaab.admin.custom.DialogSort;
import com.malaab.ya.action.actionyamalaab.admin.di.ActivityContext;
import com.malaab.ya.action.actionyamalaab.admin.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.attendancepayment.AttendeesMvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.attendancepayment.AttendeesMvpView;
import com.malaab.ya.action.actionyamalaab.admin.ui.attendancepayment.AttendeesPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.attendancepayment.full.FullAttendeesMvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.attendancepayment.full.FullAttendeesMvpView;
import com.malaab.ya.action.actionyamalaab.admin.ui.attendancepayment.full.FullAttendeesPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.attendancepayment.individual.IndividualAttendeesMvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.attendancepayment.individual.IndividualAttendeesMvpView;
import com.malaab.ya.action.actionyamalaab.admin.ui.attendancepayment.individual.IndividualAttendeesPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.bookings.BookingsMvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.bookings.BookingsMvpView;
import com.malaab.ya.action.actionyamalaab.admin.ui.bookings.BookingsPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.bookings.full.FullBookingsMvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.bookings.full.FullBookingsMvpView;
import com.malaab.ya.action.actionyamalaab.admin.ui.bookings.full.FullBookingsPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.bookings.individual.IndividualBookingsMvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.bookings.individual.IndividualBookingsMvpView;
import com.malaab.ya.action.actionyamalaab.admin.ui.bookings.individual.IndividualBookingsPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.messages.MessagesMvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.messages.MessagesMvpView;
import com.malaab.ya.action.actionyamalaab.admin.ui.messages.MessagesPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.messages.details.MessagesDetailsMvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.messages.details.MessagesDetailsMvpView;
import com.malaab.ya.action.actionyamalaab.admin.ui.messages.details.MessagesDetailsPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.notifications.NotificationsMvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.notifications.NotificationsMvpView;
import com.malaab.ya.action.actionyamalaab.admin.ui.notifications.NotificationsPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.staff.add.StaffAddMvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.staff.add.StaffAddMvpView;
import com.malaab.ya.action.actionyamalaab.admin.ui.staff.add.StaffAddPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.staff.list.StaffListMvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.staff.list.StaffListMvpView;
import com.malaab.ya.action.actionyamalaab.admin.ui.staff.list.StaffListPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings.past.PastBookingsMvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings.past.PastBookingsMvpView;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings.past.PastBookingsPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings.upcoming.UpcomingBookingsMvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings.upcoming.UpcomingBookingsMvpView;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings.upcoming.UpcomingBookingsPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.home.HomeMvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.home.HomeMvpView;
import com.malaab.ya.action.actionyamalaab.admin.ui.home.HomePresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.login.LoginMvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.login.LoginMvpView;
import com.malaab.ya.action.actionyamalaab.admin.ui.login.LoginPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.playgrounds.PlaygroundsMvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.playgrounds.PlaygroundsMvpView;
import com.malaab.ya.action.actionyamalaab.admin.ui.playgrounds.PlaygroundsPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.UsersMvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.UsersMvpView;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.UsersPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings.UserBookingsMvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings.UserBookingsMvpView;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings.UserBookingsPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.details.UsersDetailsMvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.details.UsersDetailsMvpView;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.details.UsersDetailsPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.playgrounds.OwnerPlaygroundsMvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.playgrounds.OwnerPlaygroundsMvpView;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.playgrounds.OwnerPlaygroundsPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.reports.ReportsMvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.reports.ReportsMvpView;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.reports.ReportsPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.wallet.OwnerWalletFullBookingsMvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.wallet.OwnerWalletFullBookingsMvpView;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.wallet.OwnerWalletFullBookingsPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.wallet.full.WalletFullBookingsMvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.wallet.full.WalletFullBookingsMvpView;
import com.malaab.ya.action.actionyamalaab.admin.ui.wallet.full.WalletFullBookingsPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.wallet.individual.WalletIndividualBookingsMvpPresenter;
import com.malaab.ya.action.actionyamalaab.admin.ui.wallet.individual.WalletIndividualBookingsMvpView;
import com.malaab.ya.action.actionyamalaab.admin.ui.wallet.individual.WalletIndividualBookingsPresenter;
import com.malaab.ya.action.actionyamalaab.admin.utils.Validator;
import com.malaab.ya.action.actionyamalaab.admin.utils.rx.AppSchedulerProvider;
import com.malaab.ya.action.actionyamalaab.admin.utils.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;


@Module
public class ActivityModule {

    private AppCompatActivity mActivity;


    public ActivityModule(AppCompatActivity activity) {
        this.mActivity = activity;
    }


    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }


    @Provides
    LinearLayoutManager provideLinearLayoutManager(AppCompatActivity activity) {
        return new LinearLayoutManager(activity);
    }

    @Provides
    @PerActivity
    Validator provideValidator(AppCompatActivity activity) {
        return new Validator(activity);
    }


    @Provides
    @PerActivity
    DialogConfirmation provideDialogConfirmation(AppCompatActivity activity) {
        return new DialogConfirmation().with(activity);
    }


    @Provides
    @PerActivity
    DialogList provideDialogList(AppCompatActivity activity) {
        return new DialogList().with(activity);
    }

    @Provides
    @PerActivity
    DialogAgeCategoriesSelection provideDialogAgeCategoriesSelection(AppCompatActivity activity) {
        return new DialogAgeCategoriesSelection().with(activity);
    }

    @Provides
    @PerActivity
    DialogSort provideDialogSort(AppCompatActivity activity) {
        return new DialogSort().with(activity);
    }


    @Provides
    @PerActivity
    LoginMvpPresenter<LoginMvpView> provideLoginPresenter(LoginPresenter<LoginMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    HomeMvpPresenter<HomeMvpView> provideHomePresenter(HomePresenter<HomeMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    MessagesMvpPresenter<MessagesMvpView> provideMessagesPresenter(MessagesPresenter<MessagesMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    MessagesDetailsMvpPresenter<MessagesDetailsMvpView> provideMessagesDetailsPresenter(MessagesDetailsPresenter<MessagesDetailsMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    NotificationsMvpPresenter<NotificationsMvpView> provideNotificationsPresenter(NotificationsPresenter<NotificationsMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    StaffListMvpPresenter<StaffListMvpView> provideStaffListPresenter(StaffListPresenter<StaffListMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    StaffAddMvpPresenter<StaffAddMvpView> provideStaffAddPresenter(StaffAddPresenter<StaffAddMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    UsersMvpPresenter<UsersMvpView> provideUsersPresenter(UsersPresenter<UsersMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    UsersDetailsMvpPresenter<UsersDetailsMvpView> provideUsersDetailsPresenter(UsersDetailsPresenter<UsersDetailsMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    OwnerPlaygroundsMvpPresenter<OwnerPlaygroundsMvpView> provideOwnerPlaygroundsPresenter(OwnerPlaygroundsPresenter<OwnerPlaygroundsMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    ReportsMvpPresenter<ReportsMvpView> provideOwnerReportsPresenter(ReportsPresenter<ReportsMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    OwnerWalletFullBookingsMvpPresenter<OwnerWalletFullBookingsMvpView> provideOwnerWalletFullPresenter(OwnerWalletFullBookingsPresenter<OwnerWalletFullBookingsMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    UserBookingsMvpPresenter<UserBookingsMvpView> provideUserBookingsPresenter(UserBookingsPresenter<UserBookingsMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    UpcomingBookingsMvpPresenter<UpcomingBookingsMvpView> provideUpcomingBookingsPresenter(UpcomingBookingsPresenter<UpcomingBookingsMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    PastBookingsMvpPresenter<PastBookingsMvpView> providePastBookingsPresenter(PastBookingsPresenter<PastBookingsMvpView> presenter) {
        return presenter;
    }


    BookingsMvpPresenter<BookingsMvpView> provideBookingsPresenter(BookingsPresenter<BookingsMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    FullBookingsMvpPresenter<FullBookingsMvpView> provideFullBookingsPresenter(FullBookingsPresenter<FullBookingsMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    IndividualBookingsMvpPresenter<IndividualBookingsMvpView> provideIndividualBookingsPresenter(IndividualBookingsPresenter<IndividualBookingsMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    PlaygroundsMvpPresenter<PlaygroundsMvpView> providePlaygroundsPresenter(PlaygroundsPresenter<PlaygroundsMvpView> presenter) {
        return presenter;
    }


    AttendeesMvpPresenter<AttendeesMvpView> provideAttendeesPresenter(AttendeesPresenter<AttendeesMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    FullAttendeesMvpPresenter<FullAttendeesMvpView> provideFullAttendeesPresenter(FullAttendeesPresenter<FullAttendeesMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    IndividualAttendeesMvpPresenter<IndividualAttendeesMvpView> provideAttendeesBookingsPresenter(IndividualAttendeesPresenter<IndividualAttendeesMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    WalletFullBookingsMvpPresenter<WalletFullBookingsMvpView> provideWalletFullBookingsPresenter(WalletFullBookingsPresenter<WalletFullBookingsMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    WalletIndividualBookingsMvpPresenter<WalletIndividualBookingsMvpView> provideWalletIndividualBookingsPresenter(WalletIndividualBookingsPresenter<WalletIndividualBookingsMvpView> presenter) {
        return presenter;
    }
}

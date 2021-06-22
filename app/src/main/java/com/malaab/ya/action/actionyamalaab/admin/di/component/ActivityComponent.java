package com.malaab.ya.action.actionyamalaab.admin.di.component;

import com.malaab.ya.action.actionyamalaab.admin.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.admin.di.module.ActivityModule;
import com.malaab.ya.action.actionyamalaab.admin.ui.SplashActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.attendancepayment.AttendeesActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.attendancepayment.full.FullAttendeesFragment;
import com.malaab.ya.action.actionyamalaab.admin.ui.attendancepayment.individual.IndividualAttendeesFragment;
import com.malaab.ya.action.actionyamalaab.admin.ui.bookings.BookingsActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.bookings.full.FullBookingsFragment;
import com.malaab.ya.action.actionyamalaab.admin.ui.bookings.individual.IndividualBookingsFragment;
import com.malaab.ya.action.actionyamalaab.admin.ui.home.HomeActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.login.LoginActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.messages.MessagesActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.messages.details.MessagesDetailsActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.notifications.NotificationsActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.playgrounds.PlaygroundsActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.staff.StaffActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.staff.add.StaffAddActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.staff.list.StaffListActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.UsersActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings.UserBookingsActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings.past.PastBookingsFragment;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings.upcoming.UpcomingBookingsFragment;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.details.UsersDetailsActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.playgrounds.OwnerPlaygroundsActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.reports.ReportsActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.wallet.OwnerWalletActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.wallet.WalletActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.wallet.full.WalletFullBookingsActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.wallet.individual.WalletIndividualBookingsActivity;

import dagger.Component;


@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(SplashActivity activity);

    void inject(LoginActivity activity);


    void inject(HomeActivity activity);


    void inject(MessagesActivity activity);

    void inject(MessagesDetailsActivity activity);

    void inject(NotificationsActivity activity);


    void inject(StaffActivity activity);

    void inject(StaffAddActivity activity);

    void inject(StaffListActivity activity);


    void inject(UsersActivity activity);

    void inject(UsersDetailsActivity activity);


    void inject(UserBookingsActivity activity);

    void inject(UpcomingBookingsFragment fragment);

    void inject(PastBookingsFragment fragment);

    void inject(ReportsActivity activity);

    void inject(OwnerPlaygroundsActivity activity);

    void inject(OwnerWalletActivity activity);


    void inject(BookingsActivity activity);

    void inject(FullBookingsFragment fragment);

    void inject(IndividualBookingsFragment fragment);


    void inject(PlaygroundsActivity activity);


    void inject(AttendeesActivity activity);

    void inject(FullAttendeesFragment fragment);

    void inject(IndividualAttendeesFragment fragment);


    void inject(WalletActivity activity);

    void inject(WalletFullBookingsActivity activity);

    void inject(WalletIndividualBookingsActivity activity);
}

package com.malaab.ya.action.actionyamalaab.admin.custom;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.annotations.BookingStatus;
import com.malaab.ya.action.actionyamalaab.admin.data.model.DurationListItem;
import com.malaab.ya.action.actionyamalaab.admin.data.model.SizeListItem;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.BookingAgeCategory;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.BookingPlayer;
import com.malaab.ya.action.actionyamalaab.admin.ui.adapter.BookingPlayersAdapter;
import com.malaab.ya.action.actionyamalaab.admin.ui.adapter.CitiesAdapter;
import com.malaab.ya.action.actionyamalaab.admin.ui.adapter.DirectionItemsAdapter;
import com.malaab.ya.action.actionyamalaab.admin.ui.adapter.DurationItemsAdapter;
import com.malaab.ya.action.actionyamalaab.admin.ui.adapter.GenericItemsAdapter;
import com.malaab.ya.action.actionyamalaab.admin.ui.adapter.IndividualPlayersAttendeesAdapter;
import com.malaab.ya.action.actionyamalaab.admin.ui.adapter.RegionsAdapter;
import com.malaab.ya.action.actionyamalaab.admin.ui.adapter.SizeItemsAdapter;
import com.malaab.ya.action.actionyamalaab.admin.utils.ListUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DialogList {

    @BindView(R.id.dialog_btn_close)
    ImageButton btn_close;
    @BindView(R.id.dialog_txt_title)
    TextView txt_title;

    @BindView(R.id.ln_ageCategories)
    LinearLayout ln_ageCategories;

    @BindView(R.id.ln_young)
    LinearLayout ln_young;
    @BindView(R.id.txt_young)
    TextView txt_young;

    @BindView(R.id.ln_middle)
    LinearLayout ln_middle;
    @BindView(R.id.txt_middle)
    TextView txt_middle;

    @BindView(R.id.ln_old)
    LinearLayout ln_old;
    @BindView(R.id.txt_old)
    TextView txt_old;

    @BindView(R.id.txt_sep1)
    TextView txt_sep1;
    @BindView(R.id.txt_sep2)
    TextView txt_sep2;

    @BindView(R.id.dialog_rv_list)
    RecyclerView rv_list;

    private Activity mActivity;
    private Dialog mDialog;

    private GenericItemsAdapter mGenericItemsAdapter;
    private RegionsAdapter mRegionsAdapter;
    private CitiesAdapter mCitiesAdapter;
    private DirectionItemsAdapter mDirectionAdapter;
    private SizeItemsAdapter mSizeAdapter;
    private DurationItemsAdapter mDurationAdapter;

    private BookingPlayersAdapter mBookingPlayersAdapter;
    private IndividualPlayersAttendeesAdapter mIndividualPlayersAttendeesAdapter;

    private List<BookingPlayer> youngList, middleList, oldList;
    private boolean hasYoung = false, hasMiddle = false, hasOld = false;
    private int youngPlayers = 0, middlePlayers = 0, oldPlayers = 0;
    private boolean isYoungClicked = false, isMiddleClicked = false, isOldClicked = false;


    public DialogList with(Activity activity) {
        if (mDialog == null) {
            mActivity = activity;
            mDialog = new Dialog(mActivity);
        }

        return this;
    }

    public DialogList build() {
        if (mDialog.getWindow() != null) {
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            mDialog.setContentView(R.layout.custom_dialog_list);

            mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mDialog.setCanceledOnTouchOutside(true);

            ButterKnife.bind(this, mDialog);
        }

        rv_list.setHasFixedSize(true);
        rv_list.setLayoutManager(new LinearLayoutManager(mActivity));
        rv_list.setNestedScrollingEnabled(false);

//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        rv_list.setLayoutParams(lp);

        return this;
    }


    public void onConfigurationChanged() {
        if (mDialog != null && mDialog.isShowing()) {
            if (mDialog.getWindow() != null)
                mDialog.getWindow().setLayout((int) (ScreenUtils.getScreenWidth(mActivity) * 0.9f), (int) (ScreenUtils.getScreenHeight(mActivity) * 0.9f));
        }
    }


    @OnClick(R.id.dialog_btn_close)
    void back() {
        dismiss();
    }


    public DialogList withTitle(String title) {
        txt_title.setText(title);
        return this;
    }


    public void addSizes(List<SizeListItem> items) {
        mSizeAdapter = new SizeItemsAdapter(items);
    }

    public void showSizes() {
        txt_title.setText("اختر حجم الملعب");
        rv_list.setAdapter(mSizeAdapter);
        show();
    }

    public void setSizeSelected(SizeListItem item) {
        mSizeAdapter.updateItem(item, false);
    }

    public List<SizeListItem> getSizes() {
        return mSizeAdapter.getItems();
    }


    public void addDurations(List<DurationListItem> items) {
        mDurationAdapter = new DurationItemsAdapter(items);
    }

    public void showDurations() {
        txt_title.setText("اختر المدة");
        rv_list.setAdapter(mDurationAdapter);
        show();
    }

    public void setDurationSelected(DurationListItem item) {
        mDurationAdapter.updateItem(item, false);
    }

    public List<DurationListItem> getDurations() {
        return mDurationAdapter.getItems();
    }


    public void addBookingAgeCategories(final Booking booking, int bookingSize) {
        ln_middle.setBackgroundColor(0);
        ln_young.setBackgroundColor(0);
        ln_old.setBackgroundColor(0);

        ln_ageCategories.setVisibility(View.GONE);
        ln_young.setVisibility(View.GONE);
        ln_middle.setVisibility(View.GONE);
        ln_old.setVisibility(View.GONE);

        rv_list.setAdapter(null);

        youngPlayers = 0;
        middlePlayers = 0;
        oldPlayers = 0;

        youngList = new ArrayList<>();
        middleList = new ArrayList<>();
        oldList = new ArrayList<>();

        if (!ListUtils.isEmpty(booking.ageCategories)) {
            for (BookingAgeCategory category : booking.ageCategories) {
                if (category.ageRangeStart == 8) {
                    hasYoung = true;
                    if (!ListUtils.isEmpty(category.players)) {
                        youngList = new ArrayList<>(category.players);
                        youngPlayers = category.players.size();
                        for (BookingPlayer player : category.players) {
                            if (player.status != BookingStatus.USER_CANCELLED) {
                                youngPlayers += player.invitees;
                            }
                        }
                    }
                } else if (category.ageRangeStart == 13) {
                    hasMiddle = true;
                    if (!ListUtils.isEmpty(category.players)) {
                        middleList = new ArrayList<>(category.players);
                        middlePlayers = category.players.size();
                        for (BookingPlayer player : category.players) {
                            if (player.status != BookingStatus.USER_CANCELLED) {
                                middlePlayers += player.invitees;
                            }
                        }
                    }
                } else if (category.ageRangeStart == 18) {
                    hasOld = true;
                    if (!ListUtils.isEmpty(category.players)) {
                        oldList = new ArrayList<>(category.players);
                        oldPlayers = category.players.size();
                        for (BookingPlayer player : category.players) {
                            if (player.status != BookingStatus.USER_CANCELLED) {
                                oldPlayers += player.invitees;
                            }
                        }
                    }
                }
            }

            if (hasYoung) {
                ln_young.setVisibility(View.VISIBLE);
                txt_young.setText(String.format(Locale.ENGLISH, "%d/%d", youngPlayers, bookingSize));

                txt_sep1.setVisibility(View.VISIBLE);

                ln_young.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isYoungClicked = true;
                        isMiddleClicked = false;
                        isOldClicked = false;

                        ln_young.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.gray_transparent));
                        ln_middle.setBackgroundColor(0);
                        ln_old.setBackgroundColor(0);

                        rv_list.setAdapter(null);

                        mBookingPlayersAdapter = new BookingPlayersAdapter(youngList);
                        rv_list.setAdapter(mBookingPlayersAdapter);
                    }
                });
            }

            if (hasMiddle) {
                isMiddleClicked = true;
                isYoungClicked = false;
                isOldClicked = false;

                ln_middle.setVisibility(View.VISIBLE);
                txt_middle.setText(String.format(Locale.ENGLISH, "%d/%d", middlePlayers, bookingSize));

                txt_sep2.setVisibility(View.VISIBLE);

                ln_middle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ln_middle.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.gray_transparent));
                        ln_young.setBackgroundColor(0);
                        ln_old.setBackgroundColor(0);

//                        rv_list.setAdapter(null);

                        mBookingPlayersAdapter = new BookingPlayersAdapter(middleList);
                        rv_list.setAdapter(mBookingPlayersAdapter);
                    }
                });
            }

            if (hasOld) {
                isOldClicked = true;
                isYoungClicked = false;
                isMiddleClicked = false;

                ln_old.setVisibility(View.VISIBLE);
                txt_old.setText(String.format(Locale.ENGLISH, "%d/%d", oldPlayers, bookingSize));

                ln_old.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ln_old.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.gray_transparent));
                        ln_young.setBackgroundColor(0);
                        ln_middle.setBackgroundColor(0);

                        rv_list.setAdapter(null);

                        mBookingPlayersAdapter = new BookingPlayersAdapter(oldList);
                        rv_list.setAdapter(mBookingPlayersAdapter);
                    }
                });
            }

            ln_ageCategories.setVisibility(View.VISIBLE);
        }
    }

    public void showBookingAgeCategories() {
        txt_title.setText("قائمة اللاعبين");
//        rv_list.setAdapter(mBookingPlayersAdapter);
        show();
    }

//    private void confirmBooking(Booking booking, int ageRangeStart) {
//        if (!ListUtils.isEmpty(booking.ageCategories)) {
//            for (BookingAgeCategory category : booking.ageCategories) {
//                category.isConfirmed = category.ageRangeStart == ageRangeStart;
//            }
//        }
//    }
//
//    private void rejectBooking(Booking booking, int ageRangeStart) {
//        if (!ListUtils.isEmpty(booking.ageCategories)) {
//            for (BookingAgeCategory category : booking.ageCategories) {
//                if (category.ageRangeStart == ageRangeStart) {
//                    category.isConfirmed = false;
//                    break;
//                }
//            }
//        }
//    }


    public void addBookingPlayers(List<BookingPlayer> items) {
        mBookingPlayersAdapter = new BookingPlayersAdapter(items);
    }

    public void showBookingPlayers() {
        txt_title.setText("قائمة اللاعبين");
        rv_list.setAdapter(mBookingPlayersAdapter);
        show();
    }


    public void addAttendeesBookingPlayers(List<BookingPlayer> items) {
        mIndividualPlayersAttendeesAdapter = new IndividualPlayersAttendeesAdapter(items);
    }

    public void updateAttendeesBookingPlayer(BookingPlayer player) {
        mIndividualPlayersAttendeesAdapter.updateItem(player, false);
    }

    public void showAttendeesBookingPlayers() {
        txt_title.setText("قائمة اللاعبين");
        rv_list.setAdapter(mIndividualPlayersAttendeesAdapter);
        show();
    }


    private boolean isDialogShown() {
        return (mDialog != null && mDialog.isShowing());
    }


    private void show() {
        if (mDialog != null && !mDialog.isShowing()) {
            if (mDialog.getWindow() != null) {
//                mDialog.getWindow().setLayout((int) (ScreenUtils.getScreenWidth(mActivity) * 0.9f), (int) (ScreenUtils.getScreenHeight(mActivity) * 0.6f));
                mDialog.getWindow().setLayout((int) (ScreenUtils.getScreenWidth(mActivity) * 0.9f), LinearLayout.LayoutParams.WRAP_CONTENT);
            }

            mDialog.show();
        }
    }

    public void dismiss() {
        if (isDialogShown()) {
            mDialog.dismiss();
        }
    }


    public void onDestroy() {
        if (mDialog != null) {
            dismiss();

            mGenericItemsAdapter = null;

            mDialog = null;
            mActivity = null;
        }
    }
}

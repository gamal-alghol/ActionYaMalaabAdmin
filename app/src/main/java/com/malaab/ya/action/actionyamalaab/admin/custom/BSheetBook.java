package com.malaab.ya.action.actionyamalaab.admin.custom;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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
import com.malaab.ya.action.actionyamalaab.admin.annotations.ItemAction;
import com.malaab.ya.action.actionyamalaab.admin.annotations.NotificationType;
import com.malaab.ya.action.actionyamalaab.admin.annotations.PaymentMethod;
import com.malaab.ya.action.actionyamalaab.admin.data.model.CalendarDay;
import com.malaab.ya.action.actionyamalaab.admin.data.model.CalendarTime;
import com.malaab.ya.action.actionyamalaab.admin.data.model.DurationListItem;
import com.malaab.ya.action.actionyamalaab.admin.data.model.SizeListItem;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.BookingAgeCategory;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.BookingPlayer;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.BookingPlayground;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.PlaygroundSchedule;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.PlaygroundSearch;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.events.OnEventItemClicked;
import com.malaab.ya.action.actionyamalaab.admin.ui.adapter.DaysAdapter;
import com.malaab.ya.action.actionyamalaab.admin.ui.adapter.TimesAdapter;
import com.malaab.ya.action.actionyamalaab.admin.ui.home.HomeActivity;
import com.malaab.ya.action.actionyamalaab.admin.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;
import com.malaab.ya.action.actionyamalaab.admin.utils.DateTimeUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.FirebaseUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.ListUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.NotificationUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.StringUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.ViewUtils;
import com.rd.PageIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BSheetBook extends BottomSheetBehavior {

    @BindView(R.id.bSheet_book)
    RelativeLayout bSheet_book;

    @BindView(R.id.pBar_loading)
    ProgressBar pBar_loading;
    @BindView(R.id.pBar_times)
    ProgressBar pBar_times;

    @BindView(R.id.nsc_content)
    NestedScrollView nsc_content;

    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_price)
    TextView txt_price;
    @BindView(R.id.txt_location)
    TextView txt_location;

    @BindView(R.id.btn_favourite)
    AppCompatImageButton btn_favourite;

    @BindView(R.id.vp_images)
    ViewPager vp_images;
    @BindView(R.id.mPageIndicatorView)
    PageIndicatorView mPageIndicatorView;

    @BindView(R.id.btn_amenitiesShower)
    AppCompatImageView btn_amenitiesShower;
    @BindView(R.id.txt_amenitiesShower)
    TextView txt_amenitiesShower;

    @BindView(R.id.btn_amenitiesWC)
    AppCompatImageView btn_amenitiesWC;
    @BindView(R.id.txt_amenitiesWC)
    TextView txt_amenitiesWC;

    @BindView(R.id.btn_amenitiesGrass)
    AppCompatImageView btn_amenitiesGrass;
    @BindView(R.id.txt_amenitiesGrass)
    TextView txt_amenitiesGrass;

    @BindView(R.id.btn_amenitiesWater)
    AppCompatImageView btn_amenitiesWater;
    @BindView(R.id.txt_amenitiesWater)
    TextView txt_amenitiesWater;

    @BindView(R.id.btn_amenitiesBall)
    AppCompatImageView btn_amenitiesBall;
    @BindView(R.id.txt_amenitiesBall)
    TextView txt_amenitiesBall;

    @BindView(R.id.chk_young)
    CheckBox chk_young;
    @BindView(R.id.chk_middle)
    CheckBox chk_middle;
    @BindView(R.id.chk_old)
    CheckBox chk_old;

    @BindView(R.id.txt_duration)
    TextView txt_duration;
    @BindView(R.id.txt_size)
    TextView txt_size;

    @BindView(R.id.edt_price)
    EditText edt_price;


//    @BindView(R.id.ln_peopleNumber)
//    LinearLayout ln_peopleNumber;

    //    @BindView(R.id.txt_datetime)
//    TextView txt_datetime;
    @BindView(R.id.rv_days)
    RecyclerView rv_days;
    @BindView(R.id.rv_times)
    RecyclerView rv_times;

    @BindView(R.id.btn_continue)
    Button btn_continue;


    @BindView(R.id.nsc_summary)
    NestedScrollView nsc_summary;
    @BindView(R.id.summary_txt_date)
    TextView summary_txt_date;
    @BindView(R.id.summary_txt_timeStart)
    TextView summary_txt_timeStart;
    @BindView(R.id.summary_txt_timeEnd)
    TextView summary_txt_timeEnd;
    @BindView(R.id.summary_txt_type)
    TextView summary_txt_type;
    @BindView(R.id.summary_txt_ageRange)
    TextView summary_txt_ageRange;
    @BindView(R.id.summary_txt_playersNumber)
    TextView summary_txt_playersNumber;
    @BindView(R.id.summary_txt_price)
    TextView summary_txt_price;
    @BindView(R.id.summary_txt_paymentMethod)
    TextView summary_txt_paymentMethod;
    @BindView(R.id.summary_btn_bookNow)
    Button summary_btn_bookNow;


    @BindView(R.id.ln_result)
    LinearLayout ln_result;
    @BindView(R.id.img_status)
    AppCompatImageView img_status;
    @BindView(R.id.txt_status)
    TextView txt_status;


    private DaysAdapter daysAdapter;
    private TimesAdapter timesAdapter;

    private Activity mActivity;
    private BottomSheetBehavior mBottomSheetBehavior;
    private OnBottomSheetListener mBottomSheetListener;

    private DialogList mDialogList;

    private DatabaseReference mDatabasePlaygrounds;
    private DatabaseReference mDatabaseBookings;
    private ValueEventListener mValueEventListener;

    private User mUser;
    private Playground mPlayground;
    private PlaygroundSchedule playgroundSchedule;
    private Booking booking;
    private CalendarDay mCalendarDay;
    private CalendarTime mCalendarTime;

    private boolean isCollapsed = true;

    private List<PlaygroundSchedule> playgroundSchedules;
    private SizeListItem sizeListItem;
    private DurationListItem durationListItem;


    public void attachAndInit(final Activity activity) {
        if (activity != null) {
            mActivity = activity;

            View bottomSheet = mActivity.findViewById(R.id.bSheet_book);
            ButterKnife.bind(this, bottomSheet);

            nsc_content.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (scrollY == 0 && scrollY < oldScrollY) {
                        AppLogger.w("TOP SCROLL && Scroll UP");
                        if (mBottomSheetBehavior instanceof LockableBottomSheetBehavior) {
                            ((LockableBottomSheetBehavior) mBottomSheetBehavior).setLocked(false);
                        }
                    } else {
                        if (mBottomSheetBehavior instanceof LockableBottomSheetBehavior) {
                            ((LockableBottomSheetBehavior) mBottomSheetBehavior).setLocked(true);
                        }
                    }

//                    if (scrollY > oldScrollY) {
//                        AppLogger.w("Scroll DOWN");
//                        isLocked = true;
//                    }
//
//                    if (scrollY < oldScrollY) {
//                        AppLogger.w("Scroll UP");
//                        isLocked = true;
//                    }
//
//                    if (scrollY == 0) {
//                        AppLogger.w("TOP SCROLL");
//                        isLocked = false;
//                    }
//
//                    if (scrollY == ( v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() )) {
//                        AppLogger.w("BOTTOM SCROLL");
//                        isLocked = false;
//                    }
//
//                    if (mBottomSheetBehavior instanceof LockableBottomSheetBehavior) {
//                        ((LockableBottomSheetBehavior) mBottomSheetBehavior).setLocked(isLocked);
//                    }
                }
            });


            vp_images.setPageTransformer(true, new CubeInRotationTransformation());

            mDialogList = new DialogList().with(mActivity);
            mDialogList.build();

            rv_days.setHasFixedSize(true);
            rv_days.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));

            rv_times.setHasFixedSize(true);
            rv_times.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));

            if (!EventBus.getDefault().isRegistered(this))
                EventBus.getDefault().register(this);

            mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            mBottomSheetBehavior.setPeekHeight(0);

            mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (mBottomSheetListener == null)
                        return;

                    if (newState == STATE_EXPANDED) {
//                        if (mBottomSheetBehavior instanceof LockableBottomSheetBehavior) {
//                            ((LockableBottomSheetBehavior) mBottomSheetBehavior).setLocked(true);
//                        }

                        mBottomSheetListener.onBottomSheetExpanded(R.id.bSheet_book);

                        if (isCollapsed) {
                            isCollapsed = false;
                            getPlaygroundSchedules();
                        }

                    } else if (newState == STATE_COLLAPSED) {
                        isCollapsed = true;
                        mPlayground = null;

                        ln_result.setVisibility(View.GONE);
                        nsc_summary.setVisibility(View.GONE);

                        nsc_content.setVisibility(View.GONE);
                        btn_continue.setVisibility(View.GONE);
//                        pBar_loading.setVisibility(View.VISIBLE);

//                        mBottomSheetListener.onBottomSheetCollapsed(edt_email.getText().toString(), edt_password.getText().toString());
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    if (mBottomSheetListener == null)
                        return;

                    mBottomSheetListener.onSlide(slideOffset);
                }
            });
        }
    }

    public void setOnBottomSheetListener(OnBottomSheetListener listener) {
        mBottomSheetListener = listener;
    }


    private void getPlaygroundSchedules() {
        if (mPlayground == null) {
            return;
        }

        pBar_loading.setVisibility(View.VISIBLE);

        mDatabasePlaygrounds = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_TABLE).child(mPlayground.playgroundId);

        /* To load the list once only*/
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (isCollapsed) {
                    return;
                }

                PlaygroundSchedule playgroundSchedule;
                playgroundSchedules = new ArrayList<>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    playgroundSchedule = child.getValue(PlaygroundSchedule.class);
                    playgroundSchedules.add(playgroundSchedule);
                }

                initPlaygroundDetails();

                populateDays();

                pBar_loading.setVisibility(View.GONE);
                nsc_content.setVisibility(View.VISIBLE);
                btn_continue.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                AppLogger.e(" Error -> " + error.toException());

                if (isCollapsed) {
                    return;
                }
            }
        };

        mDatabasePlaygrounds.addListenerForSingleValueEvent(mValueEventListener);
    }


    @OnClick(R.id.btn_location)
    public void showPlaygroundLocation() {
        EventBus.getDefault().post(new OnEventItemClicked<>(mPlayground, ItemAction.LOCATION, 0));
    }

//    @OnClick(R.id.btn_favourite)
//    public void setFavourite() {
//        mPlayground.isFavourite = !mPlayground.isFavourite;
//        if (mPlayground.isFavourite) {
//            EventBus.getDefault().post(new OnEventItemClicked<>(mPlayground, ItemAction.FAVOURITE_ADD, 0));
//        } else {
//            EventBus.getDefault().post(new OnEventItemClicked<>(mPlayground, ItemAction.FAVOURITE_REMOVE, 0));
//        }
//    }


    @OnClick(R.id.ln_duration)
    public void selectDuration() {
        mDialogList.showDurations();
    }

    @OnClick(R.id.ln_peopleNumber)
    public void selectPeopleNumber() {
    }

    @OnClick(R.id.ln_size)
    public void selectSize() {
        mDialogList.showSizes();
    }

    @OnClick(R.id.btn_continue)
    public void continueBooking() {
        if (mCalendarDay == null) {
            showMessage("Please select a day first!");
            return;
        }

        if (mCalendarTime == null) {
            showMessage("Please select time first!");
            return;
        }

        booking = new Booking();
//        booking.day = mCalendarDay.dayName;
//        booking.datetimeCreated = mCalendarDay.dayName + ", " + mCalendarDay.day + " " + mCalendarDay.monthName + " " + mCalendarDay.year;
//        booking.datetimeCreated = mCalendarDay.day + " " + mCalendarDay.monthName + " " + mCalendarDay.year;
        booking.datetimeCreated = DateTimeUtils.getCurrentDatetime();

        booking.timeStart = mCalendarTime.timeStart;
        booking.timeEnd = mCalendarTime.timeEnd;

        booking.duration = durationListItem.value;
        booking.size = sizeListItem.value;

        booking.price = playgroundSchedule.price;

        if (StringUtils.isEmpty(edt_price.getText().toString())) {
            booking.priceIndividual = playgroundSchedule.price;
        } else {
            booking.priceIndividual = Float.valueOf(edt_price.getText().toString());
        }
        booking.paymentMethod = PaymentMethod.CASH;

        booking.isIndividuals = true;

        booking.ageCategories = new ArrayList<>();

        if (chk_young.isChecked()) {
            BookingAgeCategory young = new BookingAgeCategory();
            young.ageRangeStart = 8;
            young.ageRangeEnd = 12;
            young.name = "8 - 12";
            young.players = new ArrayList<>();

            booking.ageCategories.add(young);
        }

        if (chk_middle.isChecked()) {
            BookingAgeCategory middle = new BookingAgeCategory();
            middle.ageRangeStart = 13;
            middle.ageRangeEnd = 17;
            middle.name = "13 - 17";
            middle.players = new ArrayList<>();

            booking.ageCategories.add(middle);
        }

        if (chk_old.isChecked()) {
            BookingAgeCategory adult = new BookingAgeCategory();
            adult.ageRangeStart = 18;
            adult.ageRangeEnd = 50;
            adult.name = "+18";
            adult.players = new ArrayList<>();

            booking.ageCategories.add(adult);
        }

        summary_txt_date.setText(DateTimeUtils.changeDateFormat(new Date(booking.timeStart), DateTimeUtils.PATTERN_DATE_3));
        summary_txt_timeStart.setText(String.valueOf(DateTimeUtils.getTime12Hour(booking.timeStart)));
        summary_txt_timeEnd.setText(String.valueOf(DateTimeUtils.getTime12Hour(booking.timeEnd)));

        summary_txt_type.setText("حجز أفراد");
        summary_txt_playersNumber.setText(String.valueOf(sizeListItem.value));

        StringBuilder ageRange = new StringBuilder();
        for (BookingAgeCategory category : booking.ageCategories) {
            if (category.ageRangeStart == 18) {
                ageRange.append("+18").append("\n");
            } else {
                ageRange.append("من ").append(category.ageRangeStart).append(" الى ").append(category.ageRangeEnd).append("\n");
            }
        }
        ageRange.deleteCharAt(ageRange.lastIndexOf("\n"));

        summary_txt_ageRange.setText(ageRange);

        summary_txt_ageRange.setVisibility(View.VISIBLE);
        summary_txt_playersNumber.setVisibility(View.VISIBLE);

        summary_txt_price.setText(String.format(Locale.ENGLISH, mActivity.getString(R.string.price), booking.priceIndividual));
        summary_txt_paymentMethod.setText("نقدي");

        YoYo.with(Techniques.RotateInDownLeft)
                .duration(300)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        nsc_summary.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        nsc_content.setVisibility(View.GONE);
                        btn_continue.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).playOn(nsc_summary);
    }

    @OnClick(R.id.summary_btn_bookNow)
    public void bookNow() {
        summary_btn_bookNow.setAlpha(.3f);
        summary_btn_bookNow.setEnabled(false);

        makeBooking();
    }

    @OnClick(R.id.result_btn_back)
    public void back() {
        hide();
//        ActivityUtils.goTo(mActivity, HomeActivity.class, true);
    }


    public void initPlaygroundDetails() {
        txt_name.setText(mPlayground.name);
txt_location.setText(mPlayground.address_city+" - "+mPlayground.address_region+" - "+mPlayground.address_direction);
        if (!ListUtils.isEmpty(mPlayground.images)) {
            PlaygroundImagesAdapter playgroundImagesAdapter = new PlaygroundImagesAdapter(mActivity, mPlayground.images);
            vp_images.setAdapter(playgroundImagesAdapter);

            mPageIndicatorView.setViewPager(vp_images);
        }

        if (mPlayground.amenity != null) {
            if (mPlayground.amenity.hasShower) {
                btn_amenitiesShower.setVisibility(View.VISIBLE);
                txt_amenitiesShower.setVisibility(View.VISIBLE);
            }

            if (mPlayground.amenity.hasWC) {
                btn_amenitiesWC.setVisibility(View.VISIBLE);
                txt_amenitiesWC.setVisibility(View.VISIBLE);
            }

            if (mPlayground.amenity.hasGrass) {
                btn_amenitiesGrass.setVisibility(View.VISIBLE);
                txt_amenitiesGrass.setVisibility(View.VISIBLE);
            }

            if (mPlayground.amenity.hasWater) {
                btn_amenitiesWater.setVisibility(View.VISIBLE);
                txt_amenitiesWater.setVisibility(View.VISIBLE);
            }

            if (mPlayground.amenity.hasBall) {
                btn_amenitiesBall.setVisibility(View.VISIBLE);
                txt_amenitiesBall.setVisibility(View.VISIBLE);
            }
        }

//        Calendar startDate = Calendar.getInstance();
////        String monthName = startDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
////        txt_month.setText(monthName);
//
//        Calendar endDate = Calendar.getInstance();
//        endDate.add(Calendar.DAY_OF_MONTH, 14);
//
//        daysAdapter = new DaysAdapter(getDates(startDate.getTime(), endDate.getTime()));
//        rv_days.setAdapter(daysAdapter);
//        daysAdapter.setIsSelectedByUSer(0, true);
//
//
//        Calendar startTime = Calendar.getInstance();
//        Calendar endTime = Calendar.getInstance();
//        endTime.add(Calendar.HOUR, 10);
//
//        timesAdapter = new TimesAdapter(getTimes(startTime.getTime(), endTime.getTime(), 30));
//        rv_times.setAdapter(timesAdapter);
//
//        timesAdapter.setIsSelectedByUSer(0, true);

    }


    private void populateDays() {
        Calendar startDate = Calendar.getInstance();
//        startDate.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, 0, 0, 0);

        Calendar endDate = Calendar.getInstance();
//        endDate.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, 0, 0, 0);
        endDate.add(Calendar.DAY_OF_MONTH, 14);

        daysAdapter = new DaysAdapter(getDates(startDate.getTime(), endDate.getTime()));
        rv_days.setAdapter(daysAdapter);
        daysAdapter.setIsSelectedByUSer(0, true);
        mCalendarDay = daysAdapter.getItem(0);

        String dayFullName = startDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);
        getTimesOfDay(dayFullName);
    }

    private void getTimesOfDay(String dayFullName) {
        rv_times.setAdapter(null);
        rv_times.setVisibility(View.GONE);
        pBar_times.setVisibility(View.VISIBLE);

        boolean isTimesFound = false;

        for (PlaygroundSchedule schedule : playgroundSchedules) {

            if (dayFullName.toLowerCase().equals(schedule.day.toLowerCase())) {
                playgroundSchedule = schedule;

                txt_price.setText(String.format(Locale.ENGLISH, mActivity.getString(R.string.price), schedule.price));
                if (!ListUtils.isEmpty(schedule.size)) {
                    mDialogList.addSizes(schedule.size);
                    sizeListItem = schedule.size.get(0);
                    txt_size.setText(String.format(Locale.ENGLISH, "%s", sizeListItem.name));
                }

                if (!ListUtils.isEmpty(schedule.duration)) {
                    mDialogList.addDurations(schedule.duration);
                    durationListItem = schedule.duration.get(0);
                    txt_duration.setText(String.format(Locale.ENGLISH, "%s", durationListItem.name));

                    populateTimes(schedule.timeStart, schedule.timeEnd, schedule.duration.get(0).value);
                }

                isTimesFound = true;
                break;
            }
        }

        if (!isTimesFound) {
            mDialogList.addDurations(new ArrayList<DurationListItem>());
            mDialogList.addSizes(new ArrayList<SizeListItem>());

            txt_duration.setText("");
            txt_size.setText("");

            pBar_times.setVisibility(View.GONE);

            mCalendarTime = null;

            btn_continue.setAlpha(.3f);
            btn_continue.setClickable(false);
        }
    }

    private void populateTimes(String timeStart, String timeEnd, int duration) {
        String[] startTime = timeStart.split(":");
        int startHour = Integer.parseInt(startTime[0]);
        int startMin = Integer.parseInt(startTime[1]);

        Calendar startTimeCal = Calendar.getInstance();
        startTimeCal.setTime(mCalendarDay.date.getTime());
        startTimeCal.set(Calendar.HOUR_OF_DAY, startHour);
        startTimeCal.set(Calendar.MINUTE, startMin);

        String[] endTime = timeEnd.split(":");
        int endHour = Integer.parseInt(endTime[0]);
        int endMin = Integer.parseInt(endTime[1]);

        Calendar endTimeCal = Calendar.getInstance();
        endTimeCal.setTime(mCalendarDay.date.getTime());
        endTimeCal.set(Calendar.HOUR_OF_DAY, endHour);
        endTimeCal.set(Calendar.MINUTE, endMin);

        if (endTimeCal.before(startTimeCal)) {
            endTimeCal.add(Calendar.DAY_OF_MONTH, 1);
        }

        timesAdapter = new TimesAdapter(getTimes(startTimeCal.getTime(), endTimeCal.getTime(), duration));
        rv_times.setAdapter(timesAdapter);

        if (timesAdapter.getItemCount() == 0) {
            pBar_times.setVisibility(View.GONE);
        } else {
            timesAdapter.setIsSelectedByUSer(0, true);
            mCalendarTime = timesAdapter.getItem(0);

            btn_continue.setAlpha(1);
            btn_continue.setClickable(true);

//        getBookings(mPlayground.playgroundId, mCalendarDay.dayName + " " + mCalendarDay.day + " " + mCalendarDay.monthName + " " + mCalendarDay.year);
            getBookings(mPlayground.playgroundId, DateTimeUtils.getDatetime(mCalendarDay.date.getTimeInMillis(), DateTimeUtils.PATTERN_DATE, Locale.ENGLISH));
        }
    }


    public void setDaySelected(CalendarDay calendarDay, int pos) {
        mCalendarDay = calendarDay;
        daysAdapter.setIsSelectedByUSer(pos, true);

        String dayFullName = mCalendarDay.date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);
        getTimesOfDay(dayFullName);

        ViewUtils.scrollToBottom(nsc_content);
    }

    public void setTimeSelected(CalendarTime calendarTime, int pos) {
        mCalendarTime = calendarTime;
        timesAdapter.setIsSelectedByUSer(pos, true);

        if (calendarTime.isAvailable) {
            btn_continue.setAlpha(1);
            btn_continue.setClickable(true);

        } else {
            btn_continue.setAlpha(.3f);
            btn_continue.setClickable(false);
        }
    }


    private static List<CalendarDay> getDates(Date fromDate, Date toDate) {

        Calendar startDate = Calendar.getInstance();
        startDate.setTime(fromDate);

        Calendar endDate = Calendar.getInstance();
        endDate.setTime(toDate);

        int startDayNo;
        String dayName, monthName;
        int year;

        CalendarDay calendarDay;
        List<CalendarDay> days = new ArrayList<>();

        while (!startDate.after(endDate)) {
            dayName = startDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
            monthName = startDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
            startDayNo = startDate.get(Calendar.DAY_OF_MONTH);
            year = startDate.get(Calendar.YEAR);

            Calendar tempDate = Calendar.getInstance();
            tempDate.setTime(startDate.getTime());

//            calendarDay = new CalendarDay(monthName, startDayNo, dayName, year, false);
            calendarDay = new CalendarDay(tempDate, false);
            days.add(calendarDay);

            startDate.add(Calendar.DATE, 1);
        }

        return days;
    }

    private static List<CalendarTime> getTimes(Date fromDate, Date toDate, int duration) {

        Calendar startDate = Calendar.getInstance();
        startDate.setTime(fromDate);

        Calendar endDate = Calendar.getInstance();
        endDate.setTime(toDate);

        String timeStart, timeEnd;

        CalendarTime calendarTime;
        List<CalendarTime> times = new ArrayList<>();

        while (startDate.before(endDate)) {

            /* To show upcoming hours only */
            if (startDate.getTime().after(Calendar.getInstance().getTime())) {

                timeStart = DateTimeUtils.changeDateFormat(startDate.getTime(), DateTimeUtils.PATTERN_TIME);

                Calendar tempTime = Calendar.getInstance();
                tempTime.setTime(startDate.getTime());

                Calendar endDateTemp = Calendar.getInstance();
                endDateTemp.setTime(tempTime.getTime());
                endDateTemp.add(Calendar.MINUTE, duration);

                timeEnd = DateTimeUtils.changeDateFormat(endDateTemp.getTime(), DateTimeUtils.PATTERN_TIME);

                calendarTime = new CalendarTime(tempTime.getTimeInMillis(), endDateTemp.getTimeInMillis(), duration, R.drawable.icon_football_field_new, false, true);
                times.add(calendarTime);
            }

            startDate.add(Calendar.MINUTE, duration);
        }

        return times;
    }


    private void getBookings(final String playgroundId, final String date) {
        mDatabaseBookings = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_TABLE);

        String criteria = playgroundId + "_" + date;
        mDatabaseBookings.orderByChild("playgroundId_date").equalTo(criteria)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            Booking booking;
                            List<Booking> bookingList = new ArrayList<>();

                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                booking = child.getValue(Booking.class);
                                bookingList.add(booking);
                            }

                            if (bookingList.size() > 0) {
                                List<CalendarTime> calendarTimes;

                                if (timesAdapter != null && timesAdapter.getItemCount() > 0) {
                                    calendarTimes = timesAdapter.getItems();

                                    for (Booking book : bookingList) {
                                        CalendarTime calendarTime = new CalendarTime(book.timeStart, book.timeEnd, book.duration, R.drawable.icon_football_field_new, false, false);

                                        for (CalendarTime time : calendarTimes) {
                                            if (DateTimeUtils.getHour(time.timeStart) == DateTimeUtils.getHour(calendarTime.timeStart)) {
                                                int pos = calendarTimes.indexOf(time);
                                                time = calendarTimes.get(pos);
                                                time.isAvailable = false;

                                                timesAdapter.setItem(time, pos);
                                            }
                                        }

//                                        if (calendarTimes.contains(calendarTime)) {
//                                            int pos = calendarTimes.indexOf(calendarTime);
//                                            calendarTime = calendarTimes.get(pos);
//                                            calendarTime.isAvailable = false;
//
//                                            timesAdapter.updateItem(calendarTime, pos, true);
//                                        }
                                    }

                                    if (timesAdapter.getSelectedItem().isAvailable) {
                                        btn_continue.setAlpha(1);
                                        btn_continue.setClickable(true);
                                    } else {
                                        btn_continue.setAlpha(.3f);
                                        btn_continue.setClickable(false);
                                    }
                                }
                            }
                        }

                        getIndividualsBookings(playgroundId, date);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        pBar_times.setVisibility(View.GONE);
                        AppLogger.e("GetBookings() Error -> " + databaseError.toException());
                    }
                });
    }

    private void getIndividualsBookings(String playgroundId, String date) {
        mDatabaseBookings = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_INDIVIDUALS_TABLE);

        String criteria = playgroundId + "_" + date;
        mDatabaseBookings.orderByChild("playgroundId_date").equalTo(criteria)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            Booking booking;
                            List<Booking> bookingList = new ArrayList<>();

                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                booking = child.getValue(Booking.class);
                                if (booking != null && booking.isActive) {
                                    bookingList.add(booking);
                                }
                            }

                            if (bookingList.size() > 0) {
                                List<CalendarTime> calendarTimes;

                                if (timesAdapter != null && timesAdapter.getItemCount() > 0) {
                                    calendarTimes = timesAdapter.getItems();

                                    for (Booking book : bookingList) {
                                        CalendarTime calendarTime = new CalendarTime(book.timeStart, book.timeEnd, book.duration, R.drawable.icon_football_field_new, false, false);

                                        for (CalendarTime time : calendarTimes) {
                                            if (DateTimeUtils.getHour(time.timeStart) == DateTimeUtils.getHour(calendarTime.timeStart)) {
                                                int pos = calendarTimes.indexOf(time);
                                                time = calendarTimes.get(pos);
                                                time.isAvailable = false;

                                                timesAdapter.setItem(time, pos);
                                            }
                                        }

//                                        if (calendarTimes.contains(calendarTime)) {
//                                            int pos = calendarTimes.indexOf(calendarTime);
//                                            calendarTime = calendarTimes.get(pos);
//                                            calendarTime.isAvailable = false;
//
//                                            timesAdapter.updateItem(calendarTime, pos, true);
//                                        }
                                    }

                                    if (timesAdapter.getSelectedItem().isAvailable) {
                                        btn_continue.setAlpha(mUser == null ? .3f : 1);
                                        btn_continue.setClickable(mUser != null);
                                    } else {
                                        btn_continue.setAlpha(.3f);
                                        btn_continue.setClickable(false);
                                    }
                                }
                            }
                        }

                        pBar_times.setVisibility(View.GONE);

                        rv_times.setVisibility(View.VISIBLE);
                        ViewUtils.scrollToBottom(nsc_content);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        pBar_times.setVisibility(View.GONE);
                        AppLogger.e("GetBookings() Error -> " + databaseError.toException());
                    }
                });
    }


//    private void makeFullBooking() {
//        Booking booking = new Booking();
//
//        booking.datetimeCreated = DateTimeUtils.getCurrentDatetime();
//        booking.timeStart = mCalendarTime.timeStart;
//        booking.timeEnd = mCalendarTime.timeEnd;
//        booking.duration = durationListItem.value;
//        booking.size = sizeListItem.value;
//        booking.price = playgroundSchedule.price;
//        booking.paymentMethod = PaymentMethod.CASH;
//
//        booking.isIndividuals = false;
//
//        booking.playground = new BookingPlayground();
//        booking.playground.playgroundId = mPlayground.playgroundId;
//        booking.playground.ownerId = mPlayground.ownerId;
//        booking.playground.name = mPlayground.name;
//        booking.playground.address_city = mPlayground.address_city;
//        booking.playground.address_direction = mPlayground.address_direction;
//        booking.playground.address_region = mPlayground.address_region;
//        booking.playground.images = mPlayground.images;
//
//        booking.playground_region = mPlayground.address_region;
//
//        booking.ownerId = mPlayground.ownerId;
//        booking.playgroundId = mPlayground.playgroundId;
//        booking.userId = mUser.uId;
//
//        booking.user = new BookingPlayer();
//        booking.user.uId = mUser.uId;
//        booking.user.appUserId = mUser.appUserId;
//        booking.user.name = mUser.getUserFullName();
//        booking.user.email = mUser.email;
//        booking.user.mobileNo = mUser.mobileNo;
//        booking.user.profileImageUrl = mUser.profileImageUrl;
//
//        booking.isActive = true;
//        booking.status = BookingStatus.PENDING;
//
//        booking.ownerId_status = mPlayground.ownerId + "_" + BookingStatus.PENDING;
//        booking.playgroundId_date = mPlayground.playgroundId + "_" + DateTimeUtils.getDatetime(mCalendarDay.date.getTimeInMillis(), DateTimeUtils.PATTERN_DATE, Locale.ENGLISH);
//        booking.playgroundId_datetime_timeStart = mPlayground.playgroundId + "_" + booking.timeStart;
//
//        mDatabaseBookings = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_TABLE);
//        mDatabaseBookings.orderByChild("playgroundId_datetime_timeStart").equalTo(booking.playgroundId_datetime_timeStart)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            showMessage("Another booking has been made on same time!");
//
//                            summary_btn_bookNow.setAlpha(1f);
//                            summary_btn_bookNow.setEnabled(true);
//
//                        } else {
//                            booking.bookingUId = mDatabaseBookings.child(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_TABLE).push().getKey();
//
//                            mDatabaseBookings.child(booking.bookingUId)
//                                    .setValue(booking)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            AppLogger.w(" onSuccess");
//
//                                            FirebaseUtils.sendNotificationToOwner(NotificationType.BOOKING_FULL_NEW, "", mUser.uId, mUser.getUserFullName(), mUser.profileImageUrl);
//                                        }
//                                    })
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            AppLogger.w(" onComplete");
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            AppLogger.e(" Error -> " + e.getLocalizedMessage());
//                                        }
//                                    });
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        AppLogger.e("MakeBooking() Error -> " + databaseError.toException());
//
//                        summary_btn_bookNow.setAlpha(1f);
//                        summary_btn_bookNow.setEnabled(true);
//                    }
//                });
//
//    }

    private void makeBooking() {
        mDatabaseBookings = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_INDIVIDUALS_TABLE);

        booking.playground = new BookingPlayground();
        booking.playground.playgroundId = mPlayground.playgroundId;
        booking.playground.ownerId = mPlayground.ownerId;
        booking.playground.name = mPlayground.name;
        booking.playground.address_city = mPlayground.address_city;
        booking.playground.address_direction = mPlayground.address_direction;
        booking.playground.address_region = mPlayground.address_region;
        booking.playground.images = mPlayground.images;

        booking.playground_region = mPlayground.address_region;
        booking.playground_city = mPlayground.address_city;

        booking.ownerId = mPlayground.ownerId;
        booking.playgroundId = mPlayground.playgroundId;
        booking.userId = mUser.uId;

        booking.user = new BookingPlayer();
        booking.user.uId = mUser.uId;
        booking.user.appUserId = mUser.appUserId;
        booking.user.name = mUser.getUserFullName();
        booking.user.email = mUser.email;
        booking.user.mobileNo = mUser.mobileNo;
        booking.user.profileImageUrl = mUser.profileImageUrl;

        booking.isActive = true;
        booking.status = BookingStatus.PENDING;

        booking.ownerId_status = mPlayground.ownerId + "_" + BookingStatus.PENDING;
        booking.playgroundId_date = mPlayground.playgroundId + "_" + DateTimeUtils.getDatetime(mCalendarDay.date.getTimeInMillis(), DateTimeUtils.PATTERN_DATE, Locale.ENGLISH);
        booking.playgroundId_datetime_timeStart = mPlayground.playgroundId + "_" + booking.timeStart;

//            booking.playersNumber = playgroundSchedule.playersNumber;

        mDatabaseBookings.orderByChild("playgroundId_datetime_timeStart").equalTo(booking.playgroundId_datetime_timeStart)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            showMessage("لقد تم اتمام حجز فردي في نفس الوقت مسبقا!");

                            summary_btn_bookNow.setAlpha(1f);
                            summary_btn_bookNow.setEnabled(true);

                        } else {
                            booking.bookingUId = mDatabaseBookings.child(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_INDIVIDUALS_TABLE).push().getKey();

                            mDatabaseBookings.child(booking.bookingUId)
                                    .setValue(booking)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            AppLogger.w(" onSuccess");

//                                            getBookings(mPlayground.playgroundId, mCalendarDay.dayName + " " + mCalendarDay.day + " " + mCalendarDay.monthName + " " + mCalendarDay.year);
//                                            getBookings(mPlayground.playgroundId, mCalendarDay.day + " " + mCalendarDay.monthName + " " + mCalendarDay.year);
                                            getBookings(mPlayground.playgroundId, DateTimeUtils.getDatetime(mCalendarDay.date.getTimeInMillis(), DateTimeUtils.PATTERN_DATE, Locale.ENGLISH));

                                            img_status.setImageResource(R.drawable.icon_success);
                                            txt_status.setText("تم إضافة الحجز بنجاح!");

                                            addPlaygroundSearch(booking);

                                            Intent intent = new Intent(mActivity, HomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.putExtra(Constants.INTENT_KEY, Constants.PUSH_NOTIFICATION);

                                            NotificationUtils notificationUtils = new NotificationUtils(mActivity);
                                            notificationUtils.showNotificationMessage(NotificationType.BOOKING_INDIVIDUAL_NEW, mActivity.getString(R.string.app_name), "تم إضافة الحجز بنجاح!", DateTimeUtils.getStandardCurrentDatetime(), intent);

//                                            FirebaseUtils.sendNotificationToOwner(NotificationType.BOOKING_INDIVIDUAL_NEW, booking.ownerId,
//                                                    mUser.uId, mUser.getUserFullName(), mUser.profileImageUrl);

                                            YoYo.with(Techniques.RotateInDownRight)
                                                    .duration(300)
                                                    .withListener(new Animator.AnimatorListener() {
                                                        @Override
                                                        public void onAnimationStart(Animator animation) {
                                                            ln_result.setVisibility(View.VISIBLE);
                                                        }

                                                        @Override
                                                        public void onAnimationEnd(Animator animation) {
                                                            nsc_summary.setVisibility(View.GONE);
                                                        }

                                                        @Override
                                                        public void onAnimationCancel(Animator animation) {

                                                        }

                                                        @Override
                                                        public void onAnimationRepeat(Animator animation) {

                                                        }
                                                    }).playOn(ln_result);
                                        }
                                    })
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            AppLogger.w(" onComplete");

                                            summary_btn_bookNow.setAlpha(1f);
                                            summary_btn_bookNow.setEnabled(true);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            AppLogger.e(" Error -> " + e.getLocalizedMessage());

                                            img_status.setImageResource(R.drawable.icon_failed);
                                            txt_status.setText("Sorry, an error has occurred\n please try again!");

                                            YoYo.with(Techniques.RotateInDownRight)
                                                    .duration(300)
                                                    .withListener(new Animator.AnimatorListener() {
                                                        @Override
                                                        public void onAnimationStart(Animator animation) {
                                                            ln_result.setVisibility(View.VISIBLE);
                                                        }

                                                        @Override
                                                        public void onAnimationEnd(Animator animation) {
                                                            nsc_summary.setVisibility(View.GONE);
                                                        }

                                                        @Override
                                                        public void onAnimationCancel(Animator animation) {

                                                        }

                                                        @Override
                                                        public void onAnimationRepeat(Animator animation) {

                                                        }
                                                    }).playOn(ln_result);
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        AppLogger.e("MakeBooking() Error -> " + databaseError.toException());

                        summary_btn_bookNow.setAlpha(1f);
                        summary_btn_bookNow.setEnabled(true);
                    }
                });
    }

    public void addPlaygroundSearch(Booking booking) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SEARCH_TABLE);

        final PlaygroundSearch playgroundSearch = new PlaygroundSearch();
        playgroundSearch.playgroundId = booking.playgroundId;
        playgroundSearch.name = booking.playground.name;
        playgroundSearch.address_region = booking.playground.address_region;
        playgroundSearch.address_city = booking.playground.address_city;
        playgroundSearch.address_direction = booking.playground.address_direction;
//        playgroundSearch.latitude = booking.playground.latitude;
//        playgroundSearch.longitude = booking.playground.longitude;

        if (!ListUtils.isEmpty(booking.playground.images)) {
            playgroundSearch.images = new ArrayList<>(booking.playground.images);
        }

        playgroundSearch.isIndividuals = true;
        playgroundSearch.bookingId = booking.bookingUId;

        playgroundSearch.hasAgeYoung = chk_young.isChecked();
        playgroundSearch.hasAgeMiddle = chk_middle.isChecked();
        playgroundSearch.hasAgeOld = chk_old.isChecked();

        playgroundSearch.timeStart = booking.timeStart;
        playgroundSearch.timeEnd = booking.timeEnd;

        playgroundSearch.price = booking.priceIndividual;
        playgroundSearch.size = booking.size;

        playgroundSearch.isActive = true;

        final String key = mDatabase.child(Constants.FIREBASE_DB_PLAYGROUNDS_SEARCH_TABLE).push().getKey();
        mDatabase.child(key)
                .setValue(playgroundSearch)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AppLogger.w("addPlaygroundSearch -> onSuccess");
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AppLogger.w("addPlaygroundSearch -> onComplete");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AppLogger.e("addPlaygroundSearch -> Error -> " + e.getLocalizedMessage());
                    }
                });
    }


//    public void setFavourite(boolean isFavourite) {
//        mPlayground.isFavourite = isFavourite;
//
//        if (isFavourite) {
//            btn_favourite.setImageResource(R.drawable.icon_star_filled);
//        } else {
//            btn_favourite.setImageResource(R.drawable.icon_star_empty);
//        }
//    }


    private boolean isShown() {
        return mBottomSheetBehavior != null && mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED;
    }

    public void show(Playground playground, User user) {
        mPlayground = playground;
        mUser = user;


        if (!isShown()) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    private void hide() {
//        if (isShown()) {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        }
    }


    private void hideSummary() {
        YoYo.with(Techniques.RotateOutUpLeft)
                .duration(300)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        nsc_content.setVisibility(View.VISIBLE);
                        btn_continue.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        nsc_summary.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).playOn(nsc_summary);
    }

    private void hideResult() {
        YoYo.with(Techniques.RotateOutUpRight)
                .duration(300)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        nsc_content.setVisibility(View.VISIBLE);
                        btn_continue.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ln_result.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).playOn(ln_result);
    }


    public void showMessage(String message) {
        if (!StringUtils.isEmpty(message)) {
            Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventItemClicked(OnEventItemClicked event) {
        if (event.getItem() instanceof SizeListItem) {
            sizeListItem = (SizeListItem) event.getItem();
            if (event.getAction() == ItemAction.PICK) {
                txt_size.setText(String.format(Locale.ENGLISH, "%s", sizeListItem.name));
            }

        } else if (event.getItem() instanceof DurationListItem) {
            durationListItem = (DurationListItem) event.getItem();
            if (event.getAction() == ItemAction.PICK) {
                txt_duration.setText(String.format(Locale.ENGLISH, "%s", durationListItem.name));
            }
        }

        mDialogList.dismiss();
    }


    public boolean onBackPressed() {
        if (ln_result.getVisibility() == View.VISIBLE) {
            hideResult();
            return true;
        }

        if (nsc_summary.getVisibility() == View.VISIBLE) {
            hideSummary();
            return true;
        }

        if (isShown()) {
            hide();
            return true;
        }

        return false;
    }

    public void onDetach() {
        EventBus.getDefault().unregister(this);
        mBottomSheetListener = null;
        mActivity = null;
    }


    class PlaygroundImagesAdapter extends PagerAdapter {

        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private List<String> mImagesUrls;


        PlaygroundImagesAdapter(Context context, List<String> urls) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mImagesUrls = urls;
        }


        public void clear() {
            mImagesUrls.clear();
            notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return mImagesUrls.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            View itemView = mLayoutInflater.inflate(R.layout.playground_image, container, false);

            final ImageView img_View = itemView.findViewById(R.id.img_image);

            Glide.with(mContext).load(mImagesUrls.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .error(R.drawable.img_logo_white)
                    .into(img_View);

            img_View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    ProductImageBanner imageBanner = getItem(position);
//                    imageBanner.imageView = img_banner;
//
//                    EventBus.getDefault().post(new OnEventItemClicked<>(imageBanner, ItemAction.DETAILS, position));
                }
            });

            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    public class CubeInRotationTransformation implements ViewPager.PageTransformer {
        @Override
        public void transformPage(View page, float position) {

            page.setCameraDistance(20000);


            if (position < -1) {     // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.setAlpha(0);

            } else if (position <= 0) {    // [-1,0]
                page.setAlpha(1);
                page.setPivotX(page.getWidth());
                page.setRotationY(90 * Math.abs(position));

            } else if (position <= 1) {    // (0,1]
                page.setAlpha(1);
                page.setPivotX(0);
                page.setRotationY(-90 * Math.abs(position));

            } else {    // (1,+Infinity]
                // This page is way off-screen to the right.
                page.setAlpha(0);

            }
        }
    }

}

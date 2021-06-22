//package com.malaab.ya.action.actionyamalaab.admin.custom;
//
//import android.app.Activity;
//import android.support.annotation.NonNull;
//import android.support.design.widget.BottomSheetBehavior;
//import android.support.v4.widget.NestedScrollView;
//import android.support.v7.widget.AppCompatImageButton;
//import android.support.v7.widget.AppCompatImageView;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.InputType;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.malaab.ya.action.actionyamalaab.admin.R;
//import com.malaab.ya.action.actionyamalaab.admin.annotations.ItemAction;
//import com.malaab.ya.action.actionyamalaab.admin.data.model.DurationListItem;
//import com.malaab.ya.action.actionyamalaab.admin.data.model.SizeListItem;
//import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Playground;
//import com.malaab.ya.action.actionyamalaab.admin.data.network.model.PlaygroundSchedule;
//import com.malaab.ya.action.actionyamalaab.admin.data.network.model.PlaygroundSchedules;
//import com.malaab.ya.action.actionyamalaab.admin.events.OnEventItemClicked;
//import com.malaab.ya.action.actionyamalaab.admin.ui.adapter.DaysAdapter;
//import com.malaab.ya.action.actionyamalaab.admin.ui.adapter.TimesAdapter;
//import com.malaab.ya.action.actionyamalaab.admin.utils.AppLogger;
//import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;
//import com.malaab.ya.action.actionyamalaab.admin.utils.StringUtils;
//import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//
//public class BSheetPlaygroundSchedule extends BottomSheetBehavior {
//
//    @BindView(R.id.header_txt_title)
//    TextView header_txt_title;
//    @BindView(R.id.header_btn_back)
//    AppCompatImageButton header_btn_back;
//    @BindView(R.id.header_btn_notifications)
//    AppCompatImageButton header_btn_notifications;
//
//    @BindView(R.id.pBar_loading)
//    ProgressBar pBar_loading;
//    @BindView(R.id.pBar_times)
//    ProgressBar pBar_times;
//
//    @BindView(R.id.nsc_content)
//    NestedScrollView nsc_content;
//
//    @BindView(R.id.txt_size)
//    TextView txt_size;
//    @BindView(R.id.txt_duration)
//    TextView txt_duration;
//
//    @BindView(R.id.rv_days)
//    RecyclerView rv_days;
//    @BindView(R.id.rv_times)
//    RecyclerView rv_times;
//
//    @BindView(R.id.edt_price)
//    EditText edt_price;
//
//    @BindView(R.id.btn_saveAdd)
//    Button btn_saveAdd;
//    @BindView(R.id.txt_daysBalance)
//    TextView txt_daysBalance;
//    @BindView(R.id.btn_save)
//    Button btn_save;
//
//
//    @BindView(R.id.nsc_summary)
//    NestedScrollView nsc_summary;
//    @BindView(R.id.summary_txt_date)
//    TextView summary_txt_date;
//    @BindView(R.id.summary_txt_timeStart)
//    TextView summary_txt_timeStart;
//    @BindView(R.id.summary_txt_timeEnd)
//    TextView summary_txt_timeEnd;
//    @BindView(R.id.summary_txt_type)
//    TextView summary_txt_type;
//    @BindView(R.id.summary_txt_ageRange)
//    TextView summary_txt_ageRange;
//    @BindView(R.id.summary_txt_playersNumber)
//    TextView summary_txt_playersNumber;
//    @BindView(R.id.summary_txt_price)
//    TextView summary_txt_price;
//    @BindView(R.id.summary_txt_paymentMethod)
//    TextView summary_txt_paymentMethod;
//    @BindView(R.id.summary_btn_bookNow)
//    Button summary_btn_bookNow;
//
//
//    @BindView(R.id.ln_result)
//    LinearLayout ln_result;
//    @BindView(R.id.img_status)
//    AppCompatImageView img_status;
//    @BindView(R.id.txt_status)
//    TextView txt_status;
//
//
//    private DaysAdapter daysAdapter;
//    private TimesAdapter timesAdapter;
//
//
//    private Activity mActivity;
//    private BottomSheetBehavior mBottomSheetBehavior;
//    private OnBottomSheetListener mBottomSheetListener;
//
//    private DialogList mDialogList;
//
//    private Playground mPlayground;
//    private PlaygroundSchedules mPlaygroundSchedules;
//    private List<PlaygroundSchedule> playgroundSchedules;
//
//    private String timeStart, timeEnd;
//    private int size, duration;
//    private int daysSelected = 0;
//
//    private boolean isSaturday, isSunday, isMonday, isTuesday, isWednesday, isThursday, isFriday;
//    private boolean isFrom;
//
//    private boolean isAddingMore;
//
//    private boolean isActionTaken;
//    private boolean isCollapsed = true;
//
//    private DatabaseReference mDatabasePlaygrounds;
//    private DatabaseReference mDatabaseBookings;
//    private ValueEventListener mValueEventListener;
//
//
//    public void attachAndInit(final Activity activity) {
//        if (activity != null) {
//            mActivity = activity;
//
//            View bottomSheet = mActivity.findViewById(R.id.bSheet_playground_schedule);
//            ButterKnife.bind(this, bottomSheet);
//
//            nsc_content.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//                @Override
//                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
//                    if (scrollY == 0 && scrollY < oldScrollY) {
//                        AppLogger.w("TOP SCROLL && Scroll UP");
//                        if (mBottomSheetBehavior instanceof LockableBottomSheetBehavior) {
//                            ((LockableBottomSheetBehavior) mBottomSheetBehavior).setLocked(false);
//                        }
//                    } else {
//                        if (mBottomSheetBehavior instanceof LockableBottomSheetBehavior) {
//                            ((LockableBottomSheetBehavior) mBottomSheetBehavior).setLocked(true);
//                        }
//                    }
//
////                    if (scrollY > oldScrollY) {
////                        AppLogger.w("Scroll DOWN");
////                        isLocked = true;
////                    }
////
////                    if (scrollY < oldScrollY) {
////                        AppLogger.w("Scroll UP");
////                        isLocked = true;
////                    }
////
////                    if (scrollY == 0) {
////                        AppLogger.w("TOP SCROLL");
////                        isLocked = false;
////                    }
////
////                    if (scrollY == ( v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() )) {
////                        AppLogger.w("BOTTOM SCROLL");
////                        isLocked = false;
////                    }
////
////                    if (mBottomSheetBehavior instanceof LockableBottomSheetBehavior) {
////                        ((LockableBottomSheetBehavior) mBottomSheetBehavior).setLocked(isLocked);
////                    }
//                }
//            });
//
//
//            header_btn_back.setVisibility(View.VISIBLE);
//            header_txt_title.setText("إضافة جدول الحجوزات");
//            header_btn_notifications.setVisibility(View.INVISIBLE);
//
////            mPlaygroundSchedules = new PlaygroundSchedules();
////            mPlaygroundSchedules.playgroundSchedules = new ArrayList<>();
//
//            mDialogList = new DialogList().with(mActivity);
//            mDialogList.build();
//
//            rv_days.setHasFixedSize(true);
//            rv_days.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
//
//            rv_times.setHasFixedSize(true);
//            rv_times.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
//
//            if (!EventBus.getDefault().isRegistered(this))
//                EventBus.getDefault().register(this);
//
//            mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
//            mBottomSheetBehavior.setPeekHeight(0);
//
//            mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetCallback() {
//                @Override
//                public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                    if (mBottomSheetListener == null)
//                        return;
//
//                    if (newState == STATE_EXPANDED) {
//                        if (mBottomSheetBehavior instanceof LockableBottomSheetBehavior) {
//                            ((LockableBottomSheetBehavior) mBottomSheetBehavior).setLocked(true);
//                        }
//
//                        mBottomSheetListener.onBottomSheetExpanded(R.id.bSheet_playground_schedule);
//
//                        if (isCollapsed) {
//                            isCollapsed = false;
//                            getPlaygroundSchedules();
//                        }
//
//                    } else if (newState == STATE_COLLAPSED) {
//                        isCollapsed = true;
//                        mPlayground = null;
//
//                        nsc_content.setVisibility(View.GONE);
////                        btn_continue.setVisibility(View.GONE);
//                        nsc_summary.setVisibility(View.GONE);
//                        pBar_loading.setVisibility(View.VISIBLE);
//
//                        if (isActionTaken) {
//                            isActionTaken = false;
//                            mBottomSheetListener.onBottomSheetCollapsed(R.id.bSheet_playground_schedule, mPlaygroundSchedules, isAddingMore);
//                        }
//                    }
//                }
//
//                @Override
//                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                    if (mBottomSheetListener == null)
//                        return;
//
//                    mBottomSheetListener.onSlide(slideOffset);
//                }
//            });
//        }
//    }
//
//    public void setOnBottomSheetListener(OnBottomSheetListener listener) {
//        mBottomSheetListener = listener;
//    }
//
//
//    private void getPlaygroundSchedules() {
//        if (mPlayground == null) {
//            return;
//        }
//
//        pBar_loading.setVisibility(View.VISIBLE);
//
//        mDatabasePlaygrounds = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_TABLE).child(mPlayground.playgroundId);
//
//        /* To load the list once only*/
//        mValueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (isCollapsed) {
//                    return;
//                }
//
//                PlaygroundSchedule playgroundSchedule;
//                playgroundSchedules = new ArrayList<>();
//
//                for (DataSnapshot child : dataSnapshot.getChildren()) {
//                    playgroundSchedule = child.getValue(PlaygroundSchedule.class);
//                    playgroundSchedules.add(playgroundSchedule);
//                }
//
//                initPlaygroundDetails();
//
//                populateDays();
//
//                pBar_loading.setVisibility(View.GONE);
//                nsc_content.setVisibility(View.VISIBLE);
//                btn_continue.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                AppLogger.e(" Error -> " + error.toException());
//
//                if (isCollapsed) {
//                    return;
//                }
//            }
//        };
//
//        mDatabasePlaygrounds.addListenerForSingleValueEvent(mValueEventListener);
//    }
//
//
//    @OnClick(R.id.header_btn_back)
//    void close() {
//        hide();
//    }
//
//
//    @OnClick(R.id.ln_size)
//    void selectSize() {
//        initPlaygroundSizes();
//    }
//
//    @OnClick(R.id.ln_duration)
//    void selectDuration() {
//        initDurations();
//    }
//
//
//
//
//    @OnClick(R.id.btn_saveAdd)
//    void saveAdd() {
//        if (size == 0) {
//            showMessage(mActivity.getString(R.string.error_no_size_selected));
//            return;
//        }
//
//        if (duration == 0) {
//            showMessage(mActivity.getString(R.string.error_no_duration_selected));
//            return;
//        }
//
//        if (StringUtils.isEmpty(timeStart) || StringUtils.isEmpty(timeEnd)) {
//            showMessage(mActivity.getString(R.string.error_no_operation_hour_selected));
//            return;
//        }
//
//        if (StringUtils.isEmpty(edt_price.getText().toString())) {
//            showMessage(mActivity.getString(R.string.error_no_price_selected));
//            return;
//        }
//
//        if (!isSaturday && !isSunday && !isMonday && !isTuesday && !isWednesday && !isThursday && !isFriday) {
//            showMessage(mActivity.getString(R.string.error_no_day_selected));
//            return;
//        }
//
//        makeSchedule(isFriday, "Friday");
//        makeSchedule(isThursday, "Thursday");
//        makeSchedule(isWednesday, "Wednesday");
//        makeSchedule(isTuesday, "Tuesday");
//        makeSchedule(isMonday, "Monday");
//        makeSchedule(isSunday, "Sunday");
//        makeSchedule(isSaturday, "Saturday");
//
//        isAddingMore = true;
//        isActionTaken = true;
//        hide();
//    }
//
//    @OnClick(R.id.btn_save)
//    void save() {
//        isAddingMore = false;
//        isActionTaken = true;
//    }
//
//    private void makeSchedule(boolean isSelected, String dayName) {
//        if (!isSelected) {
//            return;
//        }
//
//        PlaygroundSchedule playgroundSchedule = new PlaygroundSchedule();
//        playgroundSchedule.day = dayName;
//        playgroundSchedule.size = size;
//        playgroundSchedule.duration = duration;
//        playgroundSchedule.timeStart = timeStart;
//        playgroundSchedule.timeEnd = timeEnd;
//        playgroundSchedule.price = Float.valueOf(edt_price.getText().toString());
//
//        if (mPlaygroundSchedules.playgroundSchedules.contains(playgroundSchedule)) {
//            mPlaygroundSchedules.playgroundSchedules.remove(playgroundSchedule);
//        }
//        mPlaygroundSchedules.playgroundSchedules.add(0, playgroundSchedule);
//    }
//
//
//
//
//
//    private void initPlaygroundSizes() {
//        List<SizeListItem> list = new ArrayList<>();
//        list.add(new SizeListItem("5 x 5", String.valueOf(Constants.SIZE_10)));
//        list.add(new SizeListItem("6 x 6", String.valueOf(Constants.SIZE_12)));
//        list.add(new SizeListItem("7 x 7", String.valueOf(Constants.SIZE_14)));
//        list.add(new SizeListItem("8 x 8", String.valueOf(Constants.SIZE_16)));
//        list.add(new SizeListItem("9 x 9", String.valueOf(Constants.SIZE_18)));
//        mDialogList.addSizes(list);
//        mDialogList.showSizes();
//    }
//
//    private void initDurations() {
//        List<DurationListItem> list = new ArrayList<>();
//        list.add(new DurationListItem("60 min", String.valueOf(Constants.DURATION_60)));
//        list.add(new DurationListItem("90 min", String.valueOf(Constants.DURATION_90)));
//        list.add(new DurationListItem("120 min", String.valueOf(Constants.DURATION_120)));
//        mDialogList.addDurations(list);
//        mDialogList.showDurations();
//    }
//
//
//    private void selectDay(TextView view, boolean isSelected) {
//        if (isSelected) {
//            daysSelected++;
//            view.setBackgroundResource(R.drawable.layout_rounded_border_green);
//        } else {
//            daysSelected--;
//            view.setBackgroundResource(R.drawable.layout_rounded_border_gray);
//        }
//
//        getDaysBalance();
//    }
//
//    private void getDaysBalance() {
//        txt_daysBalance.setText("باقي " + (7 - daysSelected) + " أيام");
//    }
//
//
//    private void showMessage(String message) {
//        if (!StringUtils.isEmpty(message)) {
//            Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
//        }
//    }
//
//
//    public void reset() {
//        mPlaygroundSchedules = new PlaygroundSchedules();
//        mPlaygroundSchedules.playgroundSchedules = new ArrayList<>();
//
//        timeStart = "";
//        timeEnd = "";
//
//        size = 0;
//        duration = 0;
//
//        daysSelected = 0;
//
//        isSaturday = false;
//        isSunday = false;
//        isMonday = false;
//        isTuesday = false;
//        isWednesday = false;
//        isThursday = false;
//        isFriday = false;
//
//        isFrom = false;
//
//        isAddingMore = false;
//
//        txt_size.setText(mActivity.getString(R.string.title_playground_size));
//        txt_duration.setText(mActivity.getString(R.string.duration));
//
//        edt_price.setText("");
//    }
//
//
//    private boolean isShown() {
//        return mBottomSheetBehavior != null && mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED;
//    }
//
//    public void show() {
//        if (!isShown()) {
//            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//        }
//    }
//
//    private void hide() {
////        if (isShown()) {
//        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
////        }
//    }
//
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void OnEventItemClicked(OnEventItemClicked event) {
//        if (event.getAction() == ItemAction.PICK) {
//            if (event.getItem() instanceof SizeListItem) {
//                size = Integer.valueOf(((SizeListItem) event.getItem()).value);
//                txt_size.setText(((SizeListItem) event.getItem()).name);
//
//            } else if (event.getItem() instanceof DurationListItem) {
//                duration = Integer.valueOf(((DurationListItem) event.getItem()).value);
//                txt_duration.setText(((DurationListItem) event.getItem()).name);
//            }
//
//            mDialogList.dismiss();
//        }
//    }
//
//
//    public boolean onBackPressed() {
//        if (isShown()) {
//            hide();
//            return true;
//        }
//
//        return false;
//    }
//
//    public void onDetach() {
//        EventBus.getDefault().unregister(this);
//        mBottomSheetListener = null;
//        mActivity = null;
//    }
//
//}

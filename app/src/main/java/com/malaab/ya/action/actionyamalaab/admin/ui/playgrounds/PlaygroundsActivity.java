package com.malaab.ya.action.actionyamalaab.admin.ui.playgrounds;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.annotations.ItemAction;
import com.malaab.ya.action.actionyamalaab.admin.custom.BSheetBook;
import com.malaab.ya.action.actionyamalaab.admin.custom.OnBottomSheetListener;
import com.malaab.ya.action.actionyamalaab.admin.data.model.CalendarDay;
import com.malaab.ya.action.actionyamalaab.admin.data.model.CalendarTime;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.PlaygroundSchedules;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.events.OnEventItemClicked;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseActivity;
import com.yayandroid.theactivitymanager.TheActivityManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PlaygroundsActivity extends BaseActivity implements PlaygroundsMvpView, OnBottomSheetListener {

    @BindView(R.id.header_txt_title)
    TextView header_txt_title;
    @BindView(R.id.header_btn_back)
    AppCompatImageButton header_btn_back;
    @BindView(R.id.header_btn_notifications)
    AppCompatImageButton header_btn_notifications;

    @BindView(R.id.pBar_loading)
    public ProgressBar pBar_loading;

    @BindView(R.id.rv_items)
    public RecyclerView rv_items;

    @Inject
    PlaygroundsPresenter<PlaygroundsMvpView> mPresenter;

    private BSheetBook mBSheetBook;

    private User mUser;
    private Playground mPlayground;
    private PlaygroundSchedules schedules;

    private boolean isLoaded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playgrounds);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
    }

    @Override
    protected void initUI() {
        header_btn_back.setVisibility(View.VISIBLE);
        header_btn_notifications.setVisibility(View.INVISIBLE); /* Just To fix UI (to center Title) */
        header_txt_title.setText(R.string.title_playground_add);

        rv_items.setHasFixedSize(true);
        rv_items.setLayoutManager(new LinearLayoutManager(this));

        mBSheetBook = new BSheetBook();
        mBSheetBook.attachAndInit(this);
        mBSheetBook.setOnBottomSheetListener(this);

        /* To enable smooth scroll on RecyclerView */
        ViewCompat.setNestedScrollingEnabled(rv_items, false);
    }


    @OnClick(R.id.header_btn_back)
    public void goBack() {
        onBackPressed();
    }


    @Override
    public void onPreClose() {

    }

    @Override
    public void onSlide(float slideOffset) {

    }

    @Override
    public void onBottomSheetExpanded(int bsheetId) {

    }

    @Override
    public void onBottomSheetCollapsed(int bsheetId, Object... args) {
//        if (bsheetId == R.id.bSheet_playground_schedule) {
//            if (args != null) {
//                schedules = (PlaygroundSchedules) args[0];
//                isAddingMore = (Boolean) args[1];
//
//                if (StringUtils.isEmpty(mPlayground.playgroundId)) {
//                    if (mValidator.isValid()) {
//                        isSave = true;
//                        mPresenter.addPlayground(mPlayground.playgroundId, edt_playgroundName.getText().toString().trim(), region, city, direction, uploadListAdapter.getItems(),
//                                mBSheetMap.getSelectedLocation(),
//                                chk_amenitiesShower.isChecked(), chk_amenitiesWC.isChecked(), chk_amenitiesGrass.isChecked(), chk_amenitiesWater.isChecked(), chk_amenitiesBall.isChecked()
//                                , true);
//                    }
//                } else {
//                    mPresenter.addPlaygroundSchedules(mPlayground.playgroundId, schedules.playgroundSchedules);
//                }
//            }
//        }
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
    public void onUserLoggedIn(User user) {
        mUser = user;

        mPresenter.getPlaygrounds();
    }

    @Override
    public void onGetPlayground(List<Playground> playgrounds) {
        PlaygroundsAdapter adapter = new PlaygroundsAdapter(playgrounds);
        adapter.registerListener(new PlaygroundsAdapter.OnPlaygroundClicked() {
            @Override
            public void setOnPlaygroundClicked(Playground playground, int position) {
                mPlayground = playground;
                mBSheetBook.show(mPlayground, mUser);
            }
        });

        rv_items.setAdapter(adapter);

        rv_items.setVisibility(View.VISIBLE);
    }


    @Override
    public void onNoInternetConnection() {
        onError(R.string.error_no_connection);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventItemClicked(OnEventItemClicked event) {
        if (TheActivityManager.getInstance().getCurrentActivity() instanceof PlaygroundsActivity) {

            Object item = event.getItem();
            int action = event.getAction();

            if (item instanceof CalendarDay && action == ItemAction.PICK) {
                mBSheetBook.setDaySelected(((CalendarDay) item), event.getPosition());

            } else if (item instanceof CalendarTime && action == ItemAction.PICK) {
                mBSheetBook.setTimeSelected((CalendarTime) event.getItem(), event.getPosition());
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        mPresenter.onResume();

        if (!isLoaded) {
            isLoaded = true;
            mPresenter.getCurrentUserInfoLocal();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        hideKeyboard();

        mPresenter.onPause();
    }

    @Override
    public void onBackPressed() {
        if (!mBSheetBook.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        mPresenter.onDetach();
        super.onDestroy();
    }
}

package com.malaab.ya.action.actionyamalaab.admin.ui.users.reports;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.custom.SimpleDividerItemDecoration;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;
import com.malaab.ya.action.actionyamalaab.admin.utils.DateTimeUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ReportsActivity extends BaseActivity implements ReportsMvpView, DatePickerDialog.OnDateSetListener {

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

    @BindView(R.id.fab_date)
    public FloatingActionButton fab_date;

    @Inject
    ReportsMvpPresenter<ReportsMvpView> mPresenter;

    private DatePickerDialog datePickerDialog;

    private ReportsAdapter adapter;

    private String ownerUid;
    private boolean isLoaded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);

        if (getIntent() != null && getIntent().hasExtra(Constants.INTENT_KEY_USER_UID)) {
            ownerUid = getIntent().getStringExtra(Constants.INTENT_KEY_USER_UID);
        }
    }

    @Override
    protected void initUI() {
        header_btn_back.setVisibility(View.VISIBLE);
        header_btn_notifications.setVisibility(View.INVISIBLE); /* Just To fix UI (to center Title) */
        header_txt_title.setText(R.string.title_reports);

        rv_items.setHasFixedSize(true);
        rv_items.setLayoutManager(new LinearLayoutManager(this));
        rv_items.addItemDecoration(new SimpleDividerItemDecoration(this));

        datePickerDialog = DatePickerDialog.newInstance(this);
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_1);

        adapter = new ReportsAdapter(new ArrayList<Booking>());
        rv_items.setAdapter(adapter);
    }


    @OnClick(R.id.header_btn_back)
    public void goBack() {
        onBackPressed();
    }

    @OnClick(R.id.fab_date)
    public void selectDate() {
        datePickerDialog.show(getFragmentManager(), Constants.DATEPICKER_TAG);
    }


    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        month = month + 1;
        String monthStr = "" + month;
        String dayStr = "" + day;

        if (month < 10)
            monthStr = "0" + month;

        if (day < 10)
            dayStr = "0" + day;

        long dateInMilli = DateTimeUtils.getMilliseconds(year + "-" + monthStr + "-" + dayStr + " 00:00:00");

        header_txt_title.setText(DateTimeUtils.changeDateFormat(new Date(dateInMilli), DateTimeUtils.PATTERN_DATE_3));

        adapter.clear();
        rv_items.setVisibility(View.GONE);

        mPresenter.getFullReports(ownerUid, dateInMilli);
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
    public void onGetReportsAsFull(List<Booking> bookings, long date) {
        adapter.addItems(bookings);

        mPresenter.getIndividualReports(ownerUid, bookings, date);
    }

    @Override
    public void onGetReportsAsIndividual(List<Booking> bookings) {
        adapter.setItems(bookings);

        rv_items.setVisibility(View.VISIBLE);
    }


    @Override
    public void onNoInternetConnection() {
        onError(R.string.error_no_connection);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.onResume();

        if (!isLoaded) {
            isLoaded = true;
            mPresenter.getFullReports(ownerUid, 0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        mPresenter.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }
}

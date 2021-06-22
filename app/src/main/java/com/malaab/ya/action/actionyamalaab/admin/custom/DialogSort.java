package com.malaab.ya.action.actionyamalaab.admin.custom;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.annotations.SortOption;
import com.malaab.ya.action.actionyamalaab.admin.events.OnEventSort;
import com.malaab.ya.action.actionyamalaab.admin.utils.ScreenUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DialogSort {

    @BindView(R.id.dialog_rd_recent)
    RadioButton dialog_rd_recent;
    @BindView(R.id.dialog_rd_deactivated)
    RadioButton dialog_rd_deactivated;
    @BindView(R.id.dialog_rd_bookings)
    RadioButton dialog_rd_bookings;

    private Activity mActivity;
    private Dialog mDialog;


    public DialogSort with(Activity activity) {
        if (mDialog == null) {
            mActivity = activity;
            mDialog = new Dialog(mActivity);
        }

        return this;
    }

    public DialogSort build() {
        if (mDialog.getWindow() != null) {
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            mDialog.setContentView(R.layout.custom_dialog_sort);

            mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mDialog.setCanceledOnTouchOutside(true);

            ButterKnife.bind(this, mDialog);
        }

        return this;
    }


    public void onConfigurationChanged() {
        if (mDialog != null && mDialog.isShowing()) {
            if (mDialog.getWindow() != null)
                mDialog.getWindow().setLayout((int) (ScreenUtils.getScreenWidth(mActivity) * 0.9f), (int) (ScreenUtils.getScreenHeight(mActivity) * 0.9f));
        }
    }


    @OnClick(R.id.dialog_rd_recent)
    void sortByRecent() {
        dialog_rd_deactivated.setChecked(false);
        dialog_rd_bookings.setChecked(false);
    }

    @OnClick(R.id.dialog_rd_deactivated)
    void sortByDeactivated() {
        dialog_rd_recent.setChecked(false);
        dialog_rd_bookings.setChecked(false);
    }

    @OnClick(R.id.dialog_rd_bookings)
    void sortByBookings() {
        dialog_rd_recent.setChecked(false);
        dialog_rd_deactivated.setChecked(false);
    }

    @OnClick(R.id.dialog_btn_search)
    void sort() {
        EventBus.getDefault().post(new OnEventSort(getSeSortCriteria()));
        dismiss();
    }

    @OnClick(R.id.dialog_btn_close)
    void back() {
        dismiss();
    }


    @SortOption
    public int getSeSortCriteria() {
        if (dialog_rd_recent.isChecked()) {
            return SortOption.RECENT;
        } else if (dialog_rd_deactivated.isChecked()) {
            return SortOption.DEACTIVATED;
        } else if (dialog_rd_bookings.isChecked()) {
            return SortOption.NO_OF_BOOKINGS;
        }

        return SortOption.RECENT;
    }


    private boolean isDialogShown() {
        return (mDialog != null && mDialog.isShowing());
    }

    public void show() {
        if (mDialog != null && !mDialog.isShowing()) {
            if (mDialog.getWindow() != null) {
//                mDialog.getWindow().setLayout((int) (ScreenUtils.getScreenWidth(mActivity) * 0.9f), (int) (ScreenUtils.getScreenHeight(mActivity) * 0.6f));
                mDialog.getWindow().setLayout((int) (ScreenUtils.getScreenWidth(mActivity) * 0.9f), RelativeLayout.LayoutParams.WRAP_CONTENT);
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

            mDialog = null;
            mActivity = null;
        }
    }
}

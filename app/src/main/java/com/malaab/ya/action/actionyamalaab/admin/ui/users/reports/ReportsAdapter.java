package com.malaab.ya.action.actionyamalaab.admin.ui.users.reports;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseAdapter;
import com.malaab.ya.action.actionyamalaab.admin.utils.DateTimeUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.NumberUtils;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ReportsAdapter extends BaseAdapter<Booking> {

    public OnReportClicked mListener;


    public ReportsAdapter(List<Booking> list) {
        super(list);
    }

    public void registerListener(OnReportClicked listener) {
        mListener = listener;
    }


    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_reports_item, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position, Booking item) {
        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).bind(item, position);
        }
    }


    class UserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ln_container)
        LinearLayout ln_container;

        @BindView(R.id.txt_date)
        TextView txt_date;
        @BindView(R.id.txt_time)
        TextView txt_time;
        @BindView(R.id.txt_username)
        TextView txt_username;
        @BindView(R.id.txt_type)
        TextView txt_type;
        @BindView(R.id.txt_price)
        TextView txt_price;

        private Context mContext;


        UserViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            mContext = itemView.getContext();
        }

        void bind(final Booking item, final int position) {
//            if ((position % 2) == 1) {
//                ln_container.setBackgroundColor(ContextCompat.getColor(mContext, R.color.semi_gray));
//            }

            txt_date.setText(DateTimeUtils.getDatetime(item.timeStart, DateTimeUtils.PATTERN_DATE, Locale.getDefault()));
            txt_time.setText(DateTimeUtils.getDatetime(item.timeStart, DateTimeUtils.PATTERN_TIME, Locale.getDefault()));
            txt_username.setText(String.valueOf(item.user.name));

            if (item.isIndividuals) {
                txt_type.setText("حجز فردي");
                txt_price.setText(String.format(mContext.getString(R.string.price), item.price / item.size));
            } else {
                txt_type.setText("حجز كامل");
                txt_price.setText(String.format(mContext.getString(R.string.price), item.price));
            }
        }
    }


    public interface OnReportClicked {
        void setOnReportClicked(Booking booking, int position);
    }
}
package com.malaab.ya.action.actionyamalaab.admin.ui.users.wallet;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseAdapter;
import com.malaab.ya.action.actionyamalaab.admin.utils.DateTimeUtils;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OwnerWalletFullBookingsAdapter extends BaseAdapter<Booking> {


    public OwnerWalletFullBookingsAdapter(List<Booking> list) {
        super(list);
    }


    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_wallet_full_bookings_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position, Booking item) {
        ((ItemViewHolder) holder).bind(item, position);
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cv_container)
        CardView cv_container;

        @BindView(R.id.txt_admin_playground)
        TextView txt_playground;
        @BindView(R.id.txt_datetime)
        TextView txt_datetime;
        @BindView(R.id.txt_price)
        TextView txt_price;
        @BindView(R.id.txt_location)
        TextView txt_location;


        ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void bind(final Booking item, final int position) {
            txt_playground.setText(item.playground.name);
            txt_datetime.setText(DateTimeUtils.getDatetime(item.timeStart, DateTimeUtils.PATTERN_DATE_3, Locale.getDefault()));
            txt_price.setText(String.format(Locale.ENGLISH, "%.0f ر.س", item.price));
            txt_location.setText(item.playground.address_city);

            cv_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    EventBus.getDefault().post(new OnEventItemClicked<>(item, ItemAction.REJECT, position));
                }
            });
        }
    }
}
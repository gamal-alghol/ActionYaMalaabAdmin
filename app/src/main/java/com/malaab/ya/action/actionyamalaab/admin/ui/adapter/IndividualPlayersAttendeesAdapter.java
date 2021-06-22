package com.malaab.ya.action.actionyamalaab.admin.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.annotations.ItemAction;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.BookingPlayer;
import com.malaab.ya.action.actionyamalaab.admin.events.OnEventItemClicked;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IndividualPlayersAttendeesAdapter extends BaseAdapter<BookingPlayer> {


    public IndividualPlayersAttendeesAdapter(List<BookingPlayer> list) {
        super(list);
    }


    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_individual_players_attendees_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position, BookingPlayer item) {
        ((ItemViewHolder) holder).bind(item, position);
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_captainName)
        TextView txt_captainName;
        @BindView(R.id.txt_players)
        TextView txt_players;
        @BindView(R.id.txt_price)
        TextView txt_price;
        @BindView(R.id.txt_outstanding)
        TextView txt_outstanding;
        @BindView(R.id.txt_total)
        TextView txt_total;

        @BindView(R.id.btn_attendance)
        Button btn_attendance;
        @BindView(R.id.btn_absent)
        Button btn_absent;

        ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void bind(final BookingPlayer item, final int position) {
            txt_captainName.setText(item.name);
            txt_players.setText(String.valueOf(item.invitees));

            txt_price.setText(String.format(Locale.ENGLISH, "%.0f ر.س", item.price));
            txt_outstanding.setText(String.format(Locale.ENGLISH, "%.0f ر.س", 0.0));
            txt_total.setText(String.format(Locale.ENGLISH, "%.0f ر.س", item.price));

            if (item.hasFine || item.isAttended) {
                btn_attendance.setVisibility(View.GONE);
                btn_absent.setVisibility(View.GONE);
            }

            btn_attendance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new OnEventItemClicked<>(item, ItemAction.TAKE_ATTENDANCE, position));
                }
            });

            btn_absent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new OnEventItemClicked<>(item, ItemAction.TAKE_ABSENT, position));
                }
            });
        }
    }
}
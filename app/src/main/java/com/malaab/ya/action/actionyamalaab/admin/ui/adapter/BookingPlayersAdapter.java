package com.malaab.ya.action.actionyamalaab.admin.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.annotations.ItemAction;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.BookingPlayer;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.events.OnEventItemClicked;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class BookingPlayersAdapter extends BaseAdapter<BookingPlayer> {

    public OnUserClicked mListener;


    public BookingPlayersAdapter(List<BookingPlayer> list) {
        super(list);
    }

    public void registerListener(OnUserClicked listener) {
        mListener = listener;
    }


    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_booking_players_item, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position, BookingPlayer item) {
        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).bind(item, position);
        }
    }


    class UserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cv_container)
        CardView cv_container;

        @BindView(R.id.img_profile)
        CircleImageView img_profile;
        @BindView(R.id.txt_name)
        TextView txt_name;
        @BindView(R.id.txt_description)
        TextView txt_description;
        @BindView(R.id.txt_players)
        TextView txt_players;

        private Context mContext;


        UserViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            mContext = itemView.getContext();
        }

        void bind(final BookingPlayer item, final int position) {
            Glide.with(mContext).load(item.profileImageUrl)
                    .placeholder(R.drawable.img_profile_player_default)
                    .error(R.drawable.img_profile_player_default)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(img_profile);

            txt_name.setText(item.name);
            txt_description.setText(String.valueOf(item.appUserId));
            txt_players.setText("عدد المرافقين: " + String.valueOf(item.invitees));

            cv_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new OnEventItemClicked<>(item, ItemAction.DETAILS, position));
                }
            });
        }
    }


    public interface OnUserClicked {
        void setOnUserClicked(User user, int position);
    }
}
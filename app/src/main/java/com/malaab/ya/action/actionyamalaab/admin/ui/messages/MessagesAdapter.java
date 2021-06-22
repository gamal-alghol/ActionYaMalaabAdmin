package com.malaab.ya.action.actionyamalaab.admin.ui.messages;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Message;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseAdapter;
import com.malaab.ya.action.actionyamalaab.admin.utils.DateTimeUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.StringUtils;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class MessagesAdapter extends BaseAdapter<Message> {

    public OnMessageClicked mListener;


    public MessagesAdapter(List<Message> list) {
        super(list);
    }

    public void setOnItemClickListener(OnMessageClicked listener) {
        mListener = listener;
    }


    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_messages_item, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position, Message item) {
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
        @BindView(R.id.txt_datetime)
        TextView txt_datetime;

        private Context mContext;


        UserViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            mContext = itemView.getContext();
        }

        void bind(final Message item, final int position) {
            if (item.isFromAdmin) {
                Glide.with(mContext)
                        .load(item.toUserProfileImage)
                        .placeholder(R.drawable.img_profile_player_default)
                        .error(R.drawable.img_profile_player_default)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(img_profile);

                txt_name.setText(item.toUsername);

            } else {
                Glide.with(mContext)
                        .load(item.fromUserProfileImage)
                        .placeholder(R.drawable.img_profile_player_default)
                        .error(R.drawable.img_profile_player_default)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(img_profile);

                txt_name.setText(item.fromUsername);
            }

            txt_description.setText(item.message);
            txt_datetime.setText(DateTimeUtils.getDatetime(item.datetimeCreated, DateTimeUtils.PATTERN_DATE_3, Locale.getDefault()));

            cv_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    AppController.getInstance().setQrCode(item.qr_code);
//                    EventBus.getDefault().post(new OnEventItemClicked<>(item, ItemAction.DETAILS, position));

                    if (mListener != null) {
                        mListener.setOnMessageClicked(item, position);
                    }
                }
            });
        }
    }


    public interface OnMessageClicked {
        void setOnMessageClicked(Message message, int position);
    }
}
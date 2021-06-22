package com.malaab.ya.action.actionyamalaab.admin.ui.notifications;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.annotations.NotificationType;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Notification;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class NotificationsAdapter extends BaseAdapter<Notification> {

    public OnNotificationClicked mListener;


    public NotificationsAdapter(List<Notification> list) {
        super(list);
    }

    public void setOnItemClickListener(OnNotificationClicked listener) {
        mListener = listener;
    }


    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_messages_item, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position, Notification item) {
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

        void bind(final Notification item, final int position) {
            Glide.with(mContext)
                    .load(item.fromUserProfileImage)
                    .placeholder(R.drawable.img_profile_default)
                    .error(R.drawable.img_profile_default)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(img_profile);

            generateTitleAndMessage(item);

            txt_name.setText(item.title);
            txt_description.setText(item.message);
            txt_datetime.setVisibility(View.GONE);
//            txt_datetime.setText(DateTimeUtils.getDatetime(item.datetimeCreated, DateTimeUtils.PATTERN_DATE_3));

            cv_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    AppController.getInstance().setQrCode(item.qr_code);
//                    EventBus.getDefault().post(new OnEventItemClicked<>(item, ItemAction.DETAILS, position));

                    if (mListener != null) {
                        mListener.setOnNotificationClicked(item, position);
                    }
                }
            });
        }

        private void generateTitleAndMessage(Notification notification) {
            if (notification.type.equals(NotificationType.CAPTAIN_NEW)) {
                notification.title = getStringExtra(R.string.notification_new_captain_title);
                notification.message = String.format(getStringExtra(R.string.notification_new_captain_message), notification.fromUsername);

            } else if (notification.type.equals(NotificationType.OWNER_NEW)) {
                notification.title = getStringExtra(R.string.notification_new_owner_title);
                notification.message = String.format(getStringExtra(R.string.notification_new_owner_message), notification.fromUsername);

            } else if (notification.type.equals(NotificationType.BOOKING_FULL_NEW)) {
                notification.title = getStringExtra(R.string.notification_new_booking_title);
                notification.message = String.format(getStringExtra(R.string.notification_new_booking_message), notification.fromUsername);

            } else if (notification.type.equals(NotificationType.BOOKING_INDIVIDUAL_NEW)) {
                notification.title = getStringExtra(R.string.notification_new_individual_title);
                notification.message = String.format(getStringExtra(R.string.notification_new_booking_message), notification.fromUsername);

            } else if (notification.type.equals(NotificationType.BOOKING_USER_CANCELLED)) {
                notification.title = getStringExtra(R.string.notification_full_booking_cancelled_title);
                notification.message = String.format(getStringExtra(R.string.notification_full_booking_cancelled_message), notification.fromUsername);

            } else if (notification.type.equals(NotificationType.BOOKING_OWNER_RECEIVED)) {
                notification.title = getStringExtra(R.string.notification_booking_received_title);
                notification.message = String.format(getStringExtra(R.string.notification_booking_received_description), notification.fromUsername);

            } else if (notification.type.equals(NotificationType.OWNER_PLAYGROUND_NEW)) {
                notification.title = getStringExtra(R.string.notification_new_playground_title);
                notification.message = String.format(getStringExtra(R.string.notification_new_playground_description), notification.fromUsername);

            } else if (notification.type.equals(NotificationType.OWNER_PLAYGROUND_NEW)) {
                notification.title = getStringExtra(R.string.notification_new_playground_title);
                notification.message = String.format(getStringExtra(R.string.notification_new_playground_description), notification.fromUsername);

            } else if (notification.type.equals(NotificationType.MESSAGE_CONTACT_US)) {
                notification.title = getStringExtra(R.string.notification_new_message_title);
                notification.message = String.format(getStringExtra(R.string.notification_new_message_description), notification.fromUsername);
            }
        }

        private String getStringExtra(@StringRes int resId) {
            return mContext.getString(resId);
        }
    }


    public interface OnNotificationClicked {
        void setOnNotificationClicked(Notification notification, int position);
    }
}
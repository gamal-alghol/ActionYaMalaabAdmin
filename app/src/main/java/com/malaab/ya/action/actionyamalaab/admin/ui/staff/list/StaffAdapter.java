package com.malaab.ya.action.actionyamalaab.admin.ui.staff.list;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseAdapter;
import com.malaab.ya.action.actionyamalaab.admin.utils.DateTimeUtils;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StaffAdapter extends BaseAdapter<User> {

    public OnStaffClicked mListener;


    public StaffAdapter(List<User> list) {
        super(list);
    }

    public void setOnItemClickListener(OnStaffClicked listener) {
        mListener = listener;
    }


    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_staff_item, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position, User item) {
        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).bind(item, position);
        }
    }


    class UserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cv_container)
        CardView cv_container;

        @BindView(R.id.txt_name)
        TextView txt_name;
        @BindView(R.id.txt_email)
        TextView txt_email;

        @BindView(R.id.txt_phone)
        TextView txt_phone;
        @BindView(R.id.txt_password)
        TextView txt_password;

        @BindView(R.id.lbl_lastSeen)
        TextView lbl_lastSeen;
        @BindView(R.id.txt_lastSeen)
        TextView txt_lastSeen;

        @BindView(R.id.sw_block)
        public Switch sw_block;


        UserViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void bind(final User item, final int position) {

            txt_name.setText(item.getUserFullName());
            txt_email.setText(item.email);
            txt_phone.setText(item.mobileNo);
            txt_password.setText(item.password);

            if (item.isOnline) {
                lbl_lastSeen.setText("الحالة");
                txt_lastSeen.setText("اون لاين");

                txt_lastSeen.setTextColor(ContextCompat.getColor(cv_container.getContext(), R.color.green));

            } else {
                lbl_lastSeen.setText("اخر ظهور");
                txt_lastSeen.setText(DateTimeUtils.getDatetime(item.lastSeen, DateTimeUtils.PATTERN_DATE_3, Locale.getDefault())
                        + "\n"
                        + DateTimeUtils.getDatetime(item.lastSeen, DateTimeUtils.PATTERN_TIME, Locale.getDefault()));

                txt_lastSeen.setTextColor(Color.parseColor("#000000"));
                txt_lastSeen.setAlpha(0.54f);
            }

            sw_block.setChecked(!item.isActive);
            sw_block.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.updateUserActive(item, !sw_block.isChecked());
                    }
                }
            });
        }
    }


    public interface OnStaffClicked {
        void updateUserActive(User user, boolean isActivate);
    }
}
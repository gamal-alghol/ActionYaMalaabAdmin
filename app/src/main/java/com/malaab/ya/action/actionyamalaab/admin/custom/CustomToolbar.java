package com.malaab.ya.action.actionyamalaab.admin.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.annotations.ToolbarOption;
import com.malaab.ya.action.actionyamalaab.admin.events.OnEventToolbarItemClicked;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CustomToolbar extends Toolbar {

    @BindView(R.id.txt_title)
    public TextView txt_title;

    @BindView(R.id.btn_chat)
    public AppCompatImageButton btn_chat;
    @BindView(R.id.txt_notificationCount)
    public CircularTextView txt_notificationCount;

    @BindView(R.id.btn_mail)
    public AppCompatImageButton btn_mail;
    @BindView(R.id.txt_messagesCount)
    public CircularTextView txt_messagesCount;

    @BindView(R.id.btn_staff)
    public AppCompatImageButton btn_staff;

    @BindView(R.id.btn_logout)
    public AppCompatImageButton btn_logout;


    public CustomToolbar(Context context) {
        super(context);
    }

    public CustomToolbar(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            View view = inflater.inflate(R.layout.custom_toolbar, this, true);
            ButterKnife.bind(this, view);

            init();
        }
    }


    private void init() {
    }


    @OnClick(R.id.btn_chat)
    public void openMenu() {
        EventBus.getDefault().post(new OnEventToolbarItemClicked(ToolbarOption.CHAT));
    }

    @OnClick(R.id.btn_mail)
    public void openMap() {
        EventBus.getDefault().post(new OnEventToolbarItemClicked(ToolbarOption.MESSAGES));
    }

    @OnClick(R.id.btn_staff)
    public void openStaff() {
        EventBus.getDefault().post(new OnEventToolbarItemClicked(ToolbarOption.TEAM));
    }

    @OnClick(R.id.btn_logout)
    public void logout() {
        EventBus.getDefault().post(new OnEventToolbarItemClicked(ToolbarOption.LOGOUT));
    }


    public void onUserAsStaff() {
        btn_staff.setVisibility(GONE);
    }

    public void onUserAsAdmin() {
        btn_staff.setVisibility(VISIBLE);
    }


    public void updateNotificationsCounter(int count) {
        if (count > 0) {
            txt_notificationCount.setText(String.valueOf(count));
            txt_notificationCount.setVisibility(VISIBLE);
        } else {
            txt_notificationCount.setText("");
            txt_notificationCount.setVisibility(GONE);
        }
    }

    public void updateMessagesCounter(int count) {
        if (count > 0) {
            txt_messagesCount.setText(String.valueOf(count));
            txt_messagesCount.setVisibility(VISIBLE);
        } else {
            txt_messagesCount.setText("");
            txt_messagesCount.setVisibility(GONE);
        }
    }
}


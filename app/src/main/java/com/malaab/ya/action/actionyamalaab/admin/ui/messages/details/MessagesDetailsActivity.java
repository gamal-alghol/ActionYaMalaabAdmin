package com.malaab.ya.action.actionyamalaab.admin.ui.messages.details;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Message;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.events.OnEventRefresh;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;
import com.malaab.ya.action.actionyamalaab.admin.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class MessagesDetailsActivity extends BaseActivity implements MessagesDetailsMvpView {

    @BindView(R.id.header_txt_title)
    TextView header_txt_title;
    @BindView(R.id.header_btn_back)
    AppCompatImageButton header_btn_back;
    @BindView(R.id.header_btn_notifications)
    AppCompatImageButton header_btn_notifications;

    @BindView(R.id.img_profile)
    public CircleImageView img_profile;

    @BindView(R.id.txt_username)
    public TextView txt_username;
    @BindView(R.id.txt_userid)
    public TextView txt_userid;

    @BindView(R.id.edt_email)
    public EditText edt_email;

    @BindView(R.id.lbl_body)
    public TextView lbl_body;
    @BindView(R.id.txt_body)
    public TextView txt_body;

    @BindView(R.id.lbl_message)
    public TextView lbl_message;
    @BindView(R.id.edt_message)
    public EditText edt_message;

    @BindView(R.id.btn_send)
    public AppCompatImageButton btn_send;

    @Inject
    MessagesDetailsMvpPresenter<MessagesDetailsMvpView> mPresenter;

    private User mUser, mAdmin;
    private Message mMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_details);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);

        if (getIntent() != null) {
            if (getIntent().hasExtra(Constants.INTENT_KEY_USER)) {
                mUser = getIntent().getParcelableExtra(Constants.INTENT_KEY_USER);

            } else if (getIntent().hasExtra(Constants.INTENT_KEY_MESSAGE)) {
                mMessage = getIntent().getParcelableExtra(Constants.INTENT_KEY_MESSAGE);
            }
        }
    }

    @Override
    protected void initUI() {
        header_btn_back.setVisibility(View.VISIBLE);
        header_btn_notifications.setVisibility(View.INVISIBLE); /* Just To fix UI (to center Title) */
        header_txt_title.setText(R.string.title_messages);

        mValidator.addField(edt_email)
                .addField(edt_message);

        if (mUser != null) {
            Glide.with(this)
                    .load(mUser.profileImageUrl)
                    .error(R.drawable.img_profile_default)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(img_profile);

            txt_username.setText(mUser.getUserFullName());
            txt_userid.setText(String.valueOf(mUser.appUserId));
            edt_email.setText(mUser.email);
        }

        if (mMessage != null) {
            Glide.with(this)
                    .load(mMessage.fromUserProfileImage)
                    .error(R.drawable.img_profile_default)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(img_profile);

            txt_username.setText(mMessage.fromUsername);
            txt_userid.setText(String.valueOf(mMessage.fromUserAppId));

            if (mMessage.isFromAdmin) {
                edt_email.setText(mMessage.toUserEmail);

                lbl_message.setVisibility(View.GONE);
                edt_message.setVisibility(View.GONE);

                btn_send.setVisibility(View.GONE);

            } else {
                edt_email.setText(mMessage.fromUserEmail);

                if (mMessage.reply != null) {
                    edt_message.setEnabled(false);
                    btn_send.setVisibility(View.GONE);

                    if (!StringUtils.isEmpty(mMessage.reply.message)) {
                        edt_message.setText(mMessage.reply.message);
                    }
                }
            }

            lbl_body.setVisibility(View.VISIBLE);
            txt_body.setVisibility(View.VISIBLE);

            txt_body.setText(mMessage.message);
        }
    }

    @OnClick(R.id.header_btn_back)
    public void goBack() {
        onBackPressed();
    }

    @OnClick(R.id.btn_send)
    public void send() {
        if (mValidator.isValid()) {

            if (mMessage != null) {
                mPresenter.sendReply(mMessage,
                        mMessage.fromUserUid, mMessage.fromUserAppId, mMessage.fromUsername, mMessage.fromUserEmail, mMessage.fromUserPhone, mMessage.fromUserProfileImage,
                        mAdmin.uId, String.valueOf(mAdmin.appUserId), mAdmin.getUserFullName(), mAdmin.email, mAdmin.mobileNo, mAdmin.profileImageUrl,
                        edt_message.getText().toString());

                return;
            }

            mPresenter.sendNewMessage(mUser.uId, String.valueOf(mUser.appUserId), mUser.getUserFullName(), mUser.email, mUser.mobileNo, mUser.profileImageUrl,
                    mAdmin.uId, String.valueOf(mAdmin.appUserId), mAdmin.getUserFullName(), mAdmin.email, mAdmin.mobileNo, mAdmin.profileImageUrl,
                    edt_message.getText().toString());
        }
    }


    @Override
    public void onGetCurrentUser(User user) {
        mAdmin = user;
    }

    @Override
    public void onSendNewMessageSuccess() {
        edt_message.setEnabled(false);
        btn_send.setVisibility(View.GONE);

        EventBus.getDefault().post(new OnEventRefresh());
    }

    @Override
    public void onSendReplySuccess() {
        edt_message.setEnabled(false);
        btn_send.setVisibility(View.GONE);

        EventBus.getDefault().post(new OnEventRefresh());
    }


    @Override
    public void onNoInternetConnection() {
        onError(R.string.error_no_connection);
    }


    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.onResume();
        mPresenter.getCurrentUserLocal();
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
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}

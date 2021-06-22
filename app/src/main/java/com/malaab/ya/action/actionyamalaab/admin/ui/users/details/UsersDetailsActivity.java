package com.malaab.ya.action.actionyamalaab.admin.ui.users.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.annotations.UserRole;
import com.malaab.ya.action.actionyamalaab.admin.custom.CircularTextView;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Fine;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.events.OnEventRefresh;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.messages.details.MessagesDetailsActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings.UserBookingsActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.playgrounds.OwnerPlaygroundsActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.reports.ReportsActivity;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.wallet.OwnerWalletActivity;
import com.malaab.ya.action.actionyamalaab.admin.utils.ActivityUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class UsersDetailsActivity extends BaseActivity implements UsersDetailsMvpView {

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

    @BindView(R.id.ln_address)
    public LinearLayout ln_address;
    @BindView(R.id.txt_address)
    public TextView txt_address;

    @BindView(R.id.ln_phone)
    public LinearLayout ln_phone;
    @BindView(R.id.txt_phone)
    public TextView txt_phone;

    @BindView(R.id.ln_email)
    public LinearLayout ln_email;
    @BindView(R.id.txt_email)
    public TextView txt_email;

    @BindView(R.id.txt_block)
    public TextView txt_block;

    @BindView(R.id.ln_reward)
    public LinearLayout ln_reward;
    @BindView(R.id.ln_bookings)
    public LinearLayout ln_bookings;

    @BindView(R.id.txt_sep1)
    public TextView txt_sep1;

    @BindView(R.id.edt_res_stop)
    public EditText edt_res_stop;
    @BindView(R.id.txt_sep2)
    public TextView txt_sep2;
    @BindView(R.id.txt_sep3)
    public TextView txt_sep3;
    @BindView(R.id.txt_sep4)
    public TextView txt_sep4;
    @BindView(R.id.txt_sep5)
    public TextView txt_sep5;

    @BindView(R.id.txt_playgroundsCount)
    public CircularTextView txt_playgroundsCount;

    @BindView(R.id.ln_message)
    public LinearLayout ln_message;

    @BindView(R.id.ln_playgrounds)
    public LinearLayout ln_playgrounds;
    @BindView(R.id.ln_profits)
    public LinearLayout ln_profits;
    @BindView(R.id.ln_reports)
    public LinearLayout ln_reports;

    @BindView(R.id.sw_block)
    public Switch sw_block;

    @BindView(R.id.txt_fines)
    public TextView txt_fines;

    @BindView(R.id.txt_fines_show)
    public TextView txt_fines_show;

    @Inject
    UsersDetailsMvpPresenter<UsersDetailsMvpView> mPresenter;

    private User mUser;
    private String userUid;
    private boolean isOwner = false;
    private boolean isLoaded = false;
private    Bundle b;

    private   ArrayList<Fine> fineList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_details);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);

        if (getIntent() != null && getIntent().hasExtra(Constants.INTENT_KEY_USER_UID)) {
            userUid = getIntent().getStringExtra(Constants.INTENT_KEY_USER_UID);
            isOwner = getIntent().getBooleanExtra(Constants.INTENT_KEY_IS_OWNER, false);
        }
         fineList = new ArrayList<>();
        b = new Bundle();


        DatabaseReference mDatabase = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_DB_FINES_TABLE).child(userUid);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Fine fine;
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        fine = child.getValue(Fine.class);
                        Log.d("ttt",fine.toString());
                        if (fine != null) {
                            fineList.add(fine);
                        }
                        txt_fines.setText(getString(R.string.has_fine));

                    }
                }
                else {
                    txt_fines_show.setVisibility(View.GONE);
                    txt_fines.setText(getString(R.string.dont_has_fine));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                txt_fines_show.setVisibility(View.GONE);
                txt_fines.setText(getString(R.string.dont_has_fine));

            }
        });

        txt_fines_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.putSerializable("list", fineList);
                startActivity(new Intent(getApplicationContext(),FinesActivity.class).putExtras(b));
            }
        });



    }

    @Override
    protected void initUI() {
        header_btn_back.setVisibility(View.VISIBLE);
        header_btn_notifications.setVisibility(View.INVISIBLE); /* Just To fix UI (to center Title) */

        if (isOwner) {
            header_txt_title.setText(R.string.title_owners_details);
        } else {
            header_txt_title.setText(R.string.title_players_details);
        }
    }


    @OnClick(R.id.header_btn_back)
    public void goBack() {
        onBackPressed();
    }

    @OnClick(R.id.sw_block)
    public void switchStatus() {

            mPresenter.deactivateUser(userUid, sw_block.isChecked());
            if(sw_block.isChecked()==false) {
                setResStop(edt_res_stop.getText().toString());

            }else{
          setResStop("");
            }
    }

    private void setResStop(String resStop) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_USERS_TABLE);
        mDatabase.child(userUid)
                .child("resStop")
                .setValue(resStop).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("ttt","onComplete");
            }
        });
    }

    @OnClick(R.id.ln_message)
    public void sendMessage() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.INTENT_KEY_USER, mUser);

        ActivityUtils.goTo(UsersDetailsActivity.this, MessagesDetailsActivity.class, false, bundle);
    }


    @OnClick(R.id.ln_reward)
    public void openRewards() {
    }

    @OnClick(R.id.ln_bookings)
    public void openBookings() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.INTENT_KEY_USER_UID, userUid);

        ActivityUtils.goTo(UsersDetailsActivity.this, UserBookingsActivity.class, false, bundle);
    }

    @OnClick(R.id.ln_playgrounds)
    public void opePlaygrounds() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.INTENT_KEY_USER_UID, userUid);

        ActivityUtils.goTo(UsersDetailsActivity.this, OwnerPlaygroundsActivity.class, false, bundle);
    }

    @OnClick(R.id.ln_profits)
    public void openProfits() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.INTENT_KEY_USER_UID, userUid);

        ActivityUtils.goTo(UsersDetailsActivity.this, OwnerWalletActivity.class, false, bundle);
    }

    @OnClick(R.id.ln_reports)
    public void openReports() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.INTENT_KEY_USER_UID, userUid);

        ActivityUtils.goTo(UsersDetailsActivity.this, ReportsActivity.class, false, bundle);
    }


    @Override
    public void onGetUserDetails(User user) {
        if (user == null) {
            onError(R.string.error_no_data);
            return;
        }

        mUser = user;

        Glide.with(this).load(user.profileImageUrl)
                .placeholder(R.drawable.img_profile_player_default)
                .error(R.drawable.img_profile_player_default)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(img_profile);

        txt_username.setText(user.getUserFullName());
        txt_userid.setText(String.valueOf(user.appUserId));
        txt_address.setText(user.address_city);

        txt_phone.setText(user.mobileNo);
        txt_email.setText(user.email);
if(user.isActive==false){
    sw_block.setChecked(user.isActive);
edt_res_stop.setText(user.resStop);
}else{
    sw_block.setChecked(user.isActive);

}

        if (user.role.equals(UserRole.ROLE_CAPTAIN)) {
            txt_block.setText("توقيف حساب المستخدم");

            ln_playgrounds.setVisibility(View.GONE);
            ln_profits.setVisibility(View.GONE);
            ln_reports.setVisibility(View.GONE);
            txt_sep3.setVisibility(View.GONE);
            txt_sep4.setVisibility(View.GONE);
            txt_sep5.setVisibility(View.GONE);

            ln_reward.setVisibility(View.VISIBLE);
            ln_bookings.setVisibility(View.VISIBLE);
            txt_sep1.setVisibility(View.VISIBLE);
            txt_sep2.setVisibility(View.VISIBLE);

        } else if (user.role.equals(UserRole.ROLE_OWNER)) {
            txt_block.setText("توقيف حساب صاحب العمل");

            ln_reward.setVisibility(View.GONE);
            ln_bookings.setVisibility(View.GONE);
            txt_sep1.setVisibility(View.GONE);
            txt_sep2.setVisibility(View.GONE);

            ln_playgrounds.setVisibility(View.VISIBLE);
            ln_profits.setVisibility(View.VISIBLE);
            ln_reports.setVisibility(View.VISIBLE);
            txt_sep3.setVisibility(View.VISIBLE);
            txt_sep4.setVisibility(View.VISIBLE);
            txt_sep5.setVisibility(View.VISIBLE);

            mPresenter.getOwnerPlaygroundsCount(user.uId);
        }
    }


    @Override
    public void onGetOwnerPlaygroundsCount(int count) {
        txt_playgroundsCount.setText(String.valueOf(count));
        txt_playgroundsCount.setVisibility(View.VISIBLE);
    }


    @Override
    public void onBlockUserSuccess() {
        EventBus.getDefault().post(new OnEventRefresh());
    }

    @Override
    public void onBlockUserFailed() {

    }


    @Override
    public void onNoInternetConnection() {
        onError(R.string.error_no_connection);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.onResume();

        if (!isLoaded) {
            isLoaded = true;
            mPresenter.getUserDetails(userUid);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        hideKeyboard();

        mPresenter.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }
}

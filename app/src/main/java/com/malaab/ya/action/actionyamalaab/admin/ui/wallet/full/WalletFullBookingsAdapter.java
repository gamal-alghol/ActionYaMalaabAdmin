package com.malaab.ya.action.actionyamalaab.admin.ui.wallet.full;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.annotations.FineStatus;
import com.malaab.ya.action.actionyamalaab.admin.annotations.ItemAction;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.BookingPlayer;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.events.OnEventItemClicked;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseAdapter;
import com.malaab.ya.action.actionyamalaab.admin.ui.users.details.UsersDetailsActivity;
import com.malaab.ya.action.actionyamalaab.admin.utils.ActivityUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;
import com.malaab.ya.action.actionyamalaab.admin.utils.DateTimeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WalletFullBookingsAdapter extends BaseAdapter<Booking> {

Activity activity;
    public WalletFullBookingsAdapter(List<Booking> list, Activity activity) {
        super(list);
        this.activity=activity;
    }


    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_wallet_full_bookings_item, parent, false);
        return new ItemViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position, Booking item) {
        ((ItemViewHolder) holder).bind(item, position);
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cv_container)
        CardView cv_container;
        @BindView(R.id.txt_captainName)
        TextView txt_captainName;
        @BindView(R.id.txt_viewPlayers)
        TextView txt_viewPlayers;
        @BindView(R.id.txt_name_playground)
        TextView txt_playground;
        @BindView(R.id.txt_datetime)
        TextView txt_datetime;
        @BindView(R.id.txt_price)
        TextView txt_price;
        @BindView(R.id.txt_location)

        TextView txt_location;

        @BindView(R.id.txt_date)
        TextView txt_date;

        @BindView(R.id.txt_guard_info)
        TextView txt_guard_info;
        @BindView(R.id.txt_owner_info)
        TextView txt_owner_info;
        @BindView(R.id.txt_status)
        TextView txt_status;
        @BindView(R.id.btn_confirm)
        Button btn_confirm;
        @BindView(R.id.txt_admin_playground)
        TextView txt_admin;

        @BindView(R.id.txt_sep)
        TextView txt_sep;
        @BindView(R.id.txt_sep10)
        TextView txt_sep10;
        @BindView(R.id.txt_sep1)
        TextView txt_sep1;
        ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        void bind(final Booking item, final int position) {
            txt_captainName.setText(item.user.name);

            txt_playground.setText(item.playground.name);
            txt_datetime.setText(DateTimeUtils.getDatetime(item.timeStart, DateTimeUtils.PATTERN_TIME, Locale.getDefault()) + " - " + DateTimeUtils.getDatetime(item.timeEnd, DateTimeUtils.PATTERN_TIME, Locale.getDefault()));
            txt_date.setText(DateTimeUtils.getDatetime(item.timeStart, DateTimeUtils.PATTERN_DATE_3, Locale.getDefault()));

            txt_price.setText(String.format(Locale.ENGLISH, "%.0f ر.س", item.price));
            txt_location.setText(item.playground.address_city);
            if(item.isReceipt==true){
                btn_confirm.setVisibility(View.GONE);
            }else {
                txt_sep.setBackgroundColor(activity.getColor(R.color.red));
                txt_sep1.setBackgroundColor(activity.getColor(R.color.red));
                txt_sep10.setBackgroundColor(activity.getColor(R.color.red));
            }
            getNameAdmin(item.ownerId);
            getPlaygroundInfo(item.playgroundId);
            if(item.isAttended){getFineStatus(1,"تم الحضور والدفع"); }
            else{getFineStatus(2,"لم يتم الحضور");}
            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.isReceipt=true;
                        FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_TABLE)
                            .child(item.bookingUId).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            btn_confirm.setVisibility(View.GONE);
                            txt_sep.setBackgroundColor(activity.getColor(R.color.green));
                            txt_sep1.setBackgroundColor(activity.getColor(R.color.green));
                            txt_sep10.setBackgroundColor(activity.getColor(R.color.green));
                            Toast.makeText(activity.getApplicationContext(),R.string.msg_success,Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            });
            txt_viewPlayers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.INTENT_KEY_USER_UID, item.user.uId);
                    ActivityUtils.goTo(activity, UsersDetailsActivity.class, false, bundle);
                   // EventBus.getDefault().post(new OnEventItemClicked<>(item.user, ItemAction.DETAILS, position));
                }
            });
        }

        private void getPlaygroundInfo(String playgroundId) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_TABLE);
            mDatabase.orderByChild("playgroundId").equalTo(playgroundId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.exists()) {
                        Playground playground;
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            playground = child.getValue(Playground.class);
                            if (playground != null) {
                                if (playground.nameOwner!=null) {
                                    txt_owner_info.setText(playground.nameOwner + " - رقم الاتصال: " + playground.mobileOwner);
                                    txt_guard_info.setText(playground.nameguard + " - رقم الاتصال: " + playground.mobileguard);
                                }
                            }
                        }
                    } else {


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        @RequiresApi(api = Build.VERSION_CODES.M)
        private void getFineStatus(int status, String txt) {
            switch (status) {
                case 2:
                    txt_status.setTextColor(activity.getColor(R.color.red));
                    txt_status.setText(txt);

                    break;
                case 1:
                    txt_status.setTextColor(activity.getColor(R.color.green));
                    txt_status.setText(txt);
break;
                default:
                    txt_status.setTextColor(activity.getColor(R.color.red));
                    txt_status.setText(txt);
            }
        }
        private void getNameAdmin(String ownerId) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_USERS_TABLE);
            mDatabase.orderByChild("uId").equalTo(ownerId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.exists()) {
                        User user;
                        for (DataSnapshot child : dataSnapshot.getChildren()) {

                            user = child.getValue(User.class);

                            if (user != null) {

                                txt_admin.setText(user.fName + " " + user.lName);
                            }
                        }
                    } else {


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("ttt",databaseError.getMessage());
                }
            });

        }


    }
}
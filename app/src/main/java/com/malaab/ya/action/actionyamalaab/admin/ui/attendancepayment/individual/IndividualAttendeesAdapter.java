package com.malaab.ya.action.actionyamalaab.admin.ui.attendancepayment.individual;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.annotations.ItemAction;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.BookingAgeCategory;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.BookingPlayer;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.events.OnEventItemClicked;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseAdapter;
import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;
import com.malaab.ya.action.actionyamalaab.admin.utils.DateTimeUtils;
import com.malaab.ya.action.actionyamalaab.admin.utils.ListUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IndividualAttendeesAdapter extends BaseAdapter<Booking> {


    public IndividualAttendeesAdapter(List<Booking> list) {
        super(list);
    }


    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_individual_attendees_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position, Booking item) {
        ((ItemViewHolder) holder).bind(item, position);
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_players)
        TextView txt_players;
        @BindView(R.id.txt_day)
        TextView txt_day;
        @BindView(R.id.txt_time)
        TextView txt_time;
        //        @BindView(R.id.txt_type)
//        TextView txt_type;
        @BindView(R.id.txt_size)
        TextView txt_size;
     /*   @BindView(R.id.txt_age)
        TextView txt_age;*/
        //        @BindView(R.id.txt_duration)
//        TextView txt_duration;
        @BindView(R.id.txt_price)
        TextView txt_price;
        @BindView(R.id.txt_outstanding)
        TextView txt_outstanding;
        @BindView(R.id.txt_total)
        TextView txt_total;

        @BindView(R.id.txt_name_playground)
        TextView txt_name_playground;

        @BindView(R.id.txt_admin)
        TextView txt_admin;
        @BindView(R.id.txt_guard_info)
        TextView txt_guard_info;
        @BindView(R.id.txt_owner_info)
        TextView txt_owner_info;

        @BindView(R.id.btn_players)
        Button btn_players;

        int players = 0;


        ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void bind(final Booking item, final int position) {

            if (!ListUtils.isEmpty(item.ageCategories)) {
                for (BookingAgeCategory category : item.ageCategories) {
                    if (category.isConfirmed) {
                        players = category.players.size();
                        for (BookingPlayer player : category.players) {
                            players += player.invitees;
                        }
                        txt_players.setText(String.valueOf(players) + " لاعب ");
                        //txt_age.setText(String.valueOf(category.name));
                        break;
                    }
                }
            }


            txt_day.setText(DateTimeUtils.changeDateFormat(new Date(item.timeStart), DateTimeUtils.PATTERN_DATE_3));
            txt_time.setText(String.format("%s - %s", DateTimeUtils.getTime12Hour(item.timeStart), DateTimeUtils.getTime12Hour(item.timeEnd)));

            txt_size.setText(getSize(item.size));
            getNameAdmin(item.ownerId);
            getPlaygroundInfo(item.playgroundId);

//            txt_duration.setText(getDuration(item.duration));

//            txt_price.setText(String.format(Locale.ENGLISH, "%.0f ر.س", item.price));
//            txt_outstanding.setText(String.format(Locale.ENGLISH, "%.0f ر.س", 0.0));
//            txt_total.setText(String.format(Locale.ENGLISH, "%.0f ر.س", item.price));

//            btn_pay.setEnabled(!item.isPaid);
//            btn_pay.setAlpha(item.isPaid ? 0.3f : 1f);

            if (players > 0) {
                btn_players.setEnabled(true);
                btn_players.setAlpha(1f);
            } else {
                btn_players.setEnabled(false);
                btn_players.setAlpha(0.5f);
            }

            btn_players.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new OnEventItemClicked<>(item, ItemAction.VIEW_LIST, position));
                }
            });

//            btn_pay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    EventBus.getDefault().post(new OnEventItemClicked<>(item, ItemAction.PAY_BOOKING, position));
//                }
//            });
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
                                txt_owner_info.setText(playground.nameOwner+ " - رقم الاتصال: " + playground.mobileOwner );
                                txt_guard_info.setText(playground.nameguard  + " - رقم الاتصال: " +playground.mobileguard);
                                txt_name_playground.setText(playground.name);
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

        private void getNameAdmin(String ownerId) {
            Log.d("ttt",ownerId);
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


    private String getSize(int size) {
        int x = size / 2;
        return String.valueOf(x + " x " + x);
    }

    private String getDuration(int duration) {
        if (duration == 60) {
            return "ساعة واحدة";

        } else if (duration == 90) {
            return "ساعة ونصف";

        } else if (duration == 120) {
            return "ساعتين";
        }

        return "";
    }
}
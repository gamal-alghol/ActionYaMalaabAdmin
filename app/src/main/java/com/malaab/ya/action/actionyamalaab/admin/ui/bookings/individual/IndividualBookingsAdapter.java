package com.malaab.ya.action.actionyamalaab.admin.ui.bookings.individual;

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
import com.malaab.ya.action.actionyamalaab.admin.annotations.BookingStatus;
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

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IndividualBookingsAdapter extends BaseAdapter<Booking> {


    public IndividualBookingsAdapter(List<Booking> list) {
        super(list);
    }


    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_individual_bookings_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position, Booking item) {
        ((ItemViewHolder) holder).bind(item, position);
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_players)
        TextView txt_players;
        @BindView(R.id.txt_viewPlayers)
        TextView txt_viewPlayers;
        @BindView(R.id.txt_day)
        TextView txt_day;
        @BindView(R.id.txt_time)
        TextView txt_time;
        @BindView(R.id.txt_admin_playground)
        TextView txt_admin_playground;
        @BindView(R.id.txt_viewOwner)
        TextView txt_viewOwner;
        @BindView(R.id.txt_type)
        TextView txt_type;
        @BindView(R.id.txt_age)
        TextView txt_age;
        @BindView(R.id.txt_size)
        TextView txt_size;


        //        @BindView(R.id.txt_duration)
//        TextView txt_duration;
        @BindView(R.id.txt_price)
        TextView txt_price;


        @BindView(R.id.txt_price_player)
        TextView txt_price_player;
        @BindView(R.id.btn_confirm)
        Button btn_confirm;
        @BindView(R.id.btn_reject)
        Button btn_reject;
        @BindView(R.id.btn_cancel)
        Button btn_cancel;
        @BindView(R.id.txt_guard_info)
        TextView txt_guard_info;
        @BindView(R.id.txt_owner_info)
        TextView txt_owner_info;
        @BindView(R.id.txt_name_playground)
        TextView txt_name_playground;

        ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void bind(final Booking item, final int position) {
            if (!ListUtils.isEmpty(item.ageCategories)) {
                for (BookingAgeCategory category : item.ageCategories) {
                    if (!ListUtils.isEmpty(category.players)) {
                        int players = category.players.size();
                        for (BookingPlayer player : category.players) {
                            if (player.status != BookingStatus.USER_CANCELLED) {
                                players += player.invitees;
                            }
                        }

                        if (item.size == players) {
                            txt_players.setText(String.valueOf(players));
                            txt_age.setText(String.valueOf(category.name));
                            break;
                        }
                    }
                }
            }

            txt_day.setText(DateTimeUtils.getDatetime(item.timeStart, DateTimeUtils.PATTERN_DATE_3, Locale.getDefault()));
            txt_time.setText(String.format("%s - %s", DateTimeUtils.getTime12Hour(item.timeStart), DateTimeUtils.getTime12Hour(item.timeEnd)));

            txt_type.setText(item.isIndividuals ? "تمارين" : "ملاعب");
            txt_size.setText(getSize(item.size));
//            txt_duration.setText(getDuration(item.duration));
            txt_price.setText(String.format(Locale.ENGLISH, "%.0f ر.س", item.priceIndividual));
            txt_price_player.setText(String.format(Locale.ENGLISH, "%.0f ر.س", item.priceIndividual/item.size));
            getNameAdmin(item.ownerId);
            getPlaygroundInfo(item.playgroundId);
            if (item.status == BookingStatus.PENDING) {
                btn_reject.setVisibility(View.VISIBLE);
                btn_confirm.setVisibility(View.VISIBLE);
            } else {
                btn_reject.setVisibility(View.GONE);
                btn_confirm.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.GONE);
            }

//            else if (item.status == BookingStatus.ADMIN_APPROVED) {
//                btn_confirm.setVisibility(View.GONE);
//                btn_reject.setVisibility(View.VISIBLE);
//
//            } else if (item.status == BookingStatus.ADMIN_REJECTED) {
//                btn_reject.setVisibility(View.GONE);
//                btn_confirm.setVisibility(View.VISIBLE);
//
//            } else if (item.status == BookingStatus.USER_CANCELLED || item.status == BookingStatus.OWNER_RECEIVED) {
//                btn_reject.setVisibility(View.GONE);
//                btn_confirm.setVisibility(View.GONE);
//            }

            txt_viewPlayers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new OnEventItemClicked<>(item, ItemAction.VIEW_LIST, position));
                }
            });

            txt_viewOwner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new OnEventItemClicked<>(item.ownerId, ItemAction.DETAILS, position));
                }
            });

            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new OnEventItemClicked<>(item, ItemAction.CONFIRM, position));
                }
            });

            btn_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new OnEventItemClicked<>(item, ItemAction.REJECT, position));
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new OnEventItemClicked<>(item, ItemAction.CANCEL, position));
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
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_USERS_TABLE);
            mDatabase.orderByChild("uId").equalTo(ownerId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.exists()) {
                        User user;
                        for (DataSnapshot child : dataSnapshot.getChildren()) {

                            user = child.getValue(User.class);

                            if (user != null) {

                                txt_admin_playground.setText(user.fName + " " + user.lName);
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
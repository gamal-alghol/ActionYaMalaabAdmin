package com.malaab.ya.action.actionyamalaab.admin.ui.users.bookings;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.annotations.BookingStatus;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseAdapter;
import com.malaab.ya.action.actionyamalaab.admin.utils.DateTimeUtils;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class UserBookingsAdapter extends BaseAdapter<Booking> {


    public UserBookingsAdapter(List<Booking> list) {
        super(list);
    }


    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user_bookings_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position, Booking item) {
        ((ItemViewHolder) holder).bind(item);
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_day)
        TextView txt_day;
        @BindView(R.id.txt_date)
        TextView txt_date;
        @BindView(R.id.txt_status)
        TextView txt_status;
        @BindView(R.id.txt_price)
        TextView txt_price;

        @BindView(R.id.txt_type)
        TextView txt_type;
        @BindView(R.id.txt_attended)
        TextView txt_attended;
        @BindView(R.id.txt_address)
        TextView txt_address;
        @BindView(R.id.txt_name_playground)
        TextView txt_name_playground;
        @BindView(R.id.txt_time)
        TextView txt_time;

        @BindView(R.id.img_status)
        AppCompatImageView img_status;

        private String datetime;


        ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void bind(final Booking item) {
//            datetime = DateTimeUtils.changeDateFormat(item.datetimeCreated, DateTimeUtils.PATTERN_DATE_1, DateTimeUtils.PATTERN_DATETIME_DEFAULT);

            txt_day.setText(String.valueOf(DateTimeUtils.getDay(item.timeStart)));
            txt_date.setText(String.format("%s, %s", DateTimeUtils.getDayName(item.timeStart), DateTimeUtils.getMonthName(item.timeStart)));
            txt_status.setText(getStatus(item.status));
            txt_price.setText(String.format(Locale.ENGLISH, "%.0f ر.س", item.price));
txt_name_playground.setText("اسم الملعب"+": "+ item.playground.name);

            txt_type.setText(String.format("%s: %s", "نوع الحجز", item.isIndividuals ? "حجز أفراد" : "حجز كامل"));
            if (item.playground != null) {
                txt_address.setText(String.format("%s: %s", "العنوان", item.playground.address_region + " - " + item.playground.address_city + " - " + item.playground.address_direction));
            }
            txt_time.setText(String.format("%s: %s - %s", "الوقت", DateTimeUtils.getTime12Hour(item.timeStart), DateTimeUtils.getTime12Hour(item.timeEnd)));

            img_status.setImageResource(getStatusImage(item.status));

            if (item.isPast) {
                img_status.setVisibility(View.GONE);
                txt_status.setVisibility(View.GONE);
                txt_attended.setVisibility(View.VISIBLE);
                if(item.isAttended==true){
                    txt_attended.setText("الحضور: حضر الحجز");
                }else {
                    txt_attended.setText("الحضور: لم يحضر الحجز");

                }


            } else {
                img_status.setVisibility(View.VISIBLE);
                txt_status.setVisibility(View.VISIBLE);
                txt_attended.setVisibility(View.GONE);

            }
        }

        private String getStatus(@BookingStatus int status) {
            switch (status) {
                case BookingStatus.PENDING:
                    return "قيد التأكيد";

                case BookingStatus.OWNER_RECEIVED:
                    return "مؤكد";

                case BookingStatus.ADMIN_REJECTED:
                    return "مرفوض";

                case BookingStatus.USER_CANCELLED:
                    return "ملغى";

                default:
                    return "قيد التأكيد";
            }
        }

        private int getStatusImage(@BookingStatus int status) {
            switch (status) {
                case BookingStatus.PENDING:
                    return R.drawable.icon_status_pending;

                case BookingStatus.OWNER_RECEIVED:
                    return R.drawable.icon_status_approved;

                case BookingStatus.ADMIN_REJECTED:
                    return R.drawable.icon_status_rejected;

                case BookingStatus.USER_CANCELLED:
                    return R.drawable.icon_status_cancelled;

                default:
                    return R.drawable.icon_status_pending;
            }
        }
    }
}
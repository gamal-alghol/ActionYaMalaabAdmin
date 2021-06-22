package com.malaab.ya.action.actionyamalaab.admin.ui.users.details;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.annotations.BookingType;
import com.malaab.ya.action.actionyamalaab.admin.annotations.FineStatus;
import com.malaab.ya.action.actionyamalaab.admin.annotations.FineType;
import com.malaab.ya.action.actionyamalaab.admin.custom.DialogConfirmation;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Fine;
import com.malaab.ya.action.actionyamalaab.admin.utils.Constants;
import com.malaab.ya.action.actionyamalaab.admin.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;


public class FinesAdabter extends RecyclerView.Adapter<FinesAdabter.ViewHolder>{
    private Context context;
    private ArrayList<Fine> arrayList;
    @Inject
    DialogConfirmation mDialogConfirmation;

    public FinesAdabter(ArrayList<Fine> arrayList, Context context) {
        this.context = context;
        this.arrayList = arrayList;
    }

    class  ViewHolder extends RecyclerView.ViewHolder{

        TextView txt_bookingType,
                txt_playground,
                txt_fineType,
                txt_date,
                txt_time,
                txt_amount,
                txt_status;
        Button btn_confirm;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_bookingType=itemView.findViewById(R.id.txt_bookingType);
            txt_playground=itemView.findViewById(R.id.txt_admin_playground);
            txt_fineType=itemView.findViewById(R.id.txt_fineType);
            txt_date=itemView.findViewById(R.id.txt_date);
            txt_time=itemView.findViewById(R.id.txt_time);
            txt_amount=itemView.findViewById(R.id.txt_amount);
            txt_status=itemView.findViewById(R.id.txt_status);
            btn_confirm=itemView.findViewById(R.id.btn_confirm);
            context = itemView.getContext();
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim);
            itemView.startAnimation(animation);
        }

        public void bind(final Fine fine,final int num) {
   //         bookingPlayground=fine.playground;
            txt_bookingType.setText(String.format(context.getString(R.string.booking_type_no_format), getBookingType(context, fine.bookType)));
            txt_playground.setText(String.format(fine.playground.name, ""));
            txt_fineType.setText(getFineType(context, fine.fineType));
            txt_date.setText(DateTimeUtils.getDatetime(fine.timeStart, DateTimeUtils.PATTERN_DATE, Locale.getDefault()));
            txt_time.setText(String.format("%s - %s", DateTimeUtils.getTime12Hour(fine.timeStart), DateTimeUtils.getTime12Hour(fine.timeEnd)));
            txt_amount.setText(String.format(context.getString(R.string.fine_amount), fine.amount));
            txt_status.setText(getFineStatus(context, fine.fineStatus));
            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

           /*         mDialogConfirmation
                            .withTitle("هل أنتا متأكد من دفع الغرامة؟")
                            .withMessage("سيتم ازالة الغرامة عن الكابتن  بشكل نهائي.")
                            .withPositiveButton(context.getString(R.string.yes))
                            .withNegativeButton(context.getString(R.string.no))
                            .setOnDialogConfirmationListener(new DialogConfirmation.OnDialogConfirmationListener() {
                                @Override
                                public void onPositiveButtonClick() {
*/
                                    DeletFine(fine.bookingId,fine.userId,num);
                        /*        }


                                @Override
                                public void onNegativeButtonClick() {
                                }
                            })
                            .build()
                            .show();*/
                }
            });

        }

        private void DeletFine(final String bookingUId, final String userId ,final int num) {

          FirebaseDatabase.getInstance()
                    .getReference(Constants.FIREBASE_DB_FINES_TABLE)
                    .child(userId)
                   .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                Fine fine=dataSnapshot1.getValue(Fine.class);
                                if(fine.bookingId.equals(bookingUId)){
                                    FirebaseDatabase.getInstance()
                                            .getReference(Constants.FIREBASE_DB_FINES_TABLE)
                                            .child(userId).child(dataSnapshot1.getKey()).removeValue();
                                    arrayList.remove(num);
                                    notifyDataSetChanged();
                                    break;
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



        }

        private String getBookingType(Context context, @BookingType int type) {
            switch (type) {
                case BookingType.FULL:
                    return context.getString(R.string.full);

                case BookingType.INDIVIDUAL:
                    return context.getString(R.string.individual);

                default:
                    return context.getString(R.string.full);
            }
        }

        private String getFineType(Context context, @FineType int type) {
            switch (type) {
                case FineType.PAYMENT:
                    return context.getString(R.string.fine_payment);

                case FineType.ATTENDANCE:
                    return context.getString(R.string.fine_attendance);

                default:
                    return context.getString(R.string.fine_payment);
            }
        }

        private String getFineStatus(Context context, @FineStatus int status) {
            switch (status) {
                case FineStatus.NOT_PAID:
                    txt_status.setTextColor(ContextCompat.getColor(context, R.color.red));
                    return context.getString(R.string.fine_status_not_paid);

                case FineStatus.PAID:
                    txt_status.setTextColor(ContextCompat.getColor(context, R.color.green));
                    return context.getString(R.string.fine_status_paid);

                default:
                    return context.getString(R.string.fine_status_not_paid);
            }
        }
    }
    @Override
    public FinesAdabter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_fines_item,parent,false));

    }



    @Override
    public void onBindViewHolder(@NonNull FinesAdabter.ViewHolder holder, int position) {
        Fine fine = arrayList.get(position);


        holder.bind(fine,position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


}

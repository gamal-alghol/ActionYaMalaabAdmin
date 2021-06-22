package com.malaab.ya.action.actionyamalaab.admin.ui.users;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class UsersAdapter extends BaseAdapter<User> {

    public OnUserClicked mListener;
    public boolean isOwner ;


    public UsersAdapter(List<User> list, boolean isOwner) {
        super(list);

        this.isOwner = isOwner;
    }

    public void registerListener(OnUserClicked listener) {
        mListener = listener;
    }


    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_users_item, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position, User item) {
        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).bind(item, position);
        }
    }


    class UserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cl_container)
        LinearLayout cl_container;

        @BindView(R.id.img_profile)
        CircleImageView img_profile;
        @BindView(R.id.txt_name)
        TextView txt_name;
        @BindView(R.id.txt_description)
        TextView txt_description;
        @BindView(R.id.txt_age)
        TextView txt_age;
        @BindView(R.id.txt_city)
        TextView txt_city;
        private Context mContext;


        UserViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            mContext = itemView.getContext();
        }

        void bind(final User item, final int position) {
            Glide.with(mContext).load(item.profileImageUrl)
                    .placeholder(R.drawable.img_profile_player_default)
                    .error(R.drawable.img_profile_player_default)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(img_profile);
            Log.d("ttt",item.toString());
            txt_name.setText(item.getUserFullName());
            txt_age.setText("العمر:"+item.age);
            txt_city.setText(item.address_city);
            txt_description.setText(String.valueOf(item.appUserId));

            cl_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    AppController.getInstance().setQrCode(item.qr_code);
//                    EventBus.getDefault().post(new OnEventItemClicked<>(item, ItemAction.DETAILS, position));

                    if (mListener != null) {
                        mListener.setOnUserClicked(item, position);
                    }
                }
            });
        }
    }


    public interface OnUserClicked {
        void setOnUserClicked(User user, int position);
    }
}
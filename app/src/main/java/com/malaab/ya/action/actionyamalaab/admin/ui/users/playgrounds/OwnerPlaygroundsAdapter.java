package com.malaab.ya.action.actionyamalaab.admin.ui.users.playgrounds;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.admin.ui.base.BaseAdapter;
import com.malaab.ya.action.actionyamalaab.admin.utils.ListUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OwnerPlaygroundsAdapter extends BaseAdapter<Playground> {

    public OnPlaygroundClicked mListener;


    public OwnerPlaygroundsAdapter(List<Playground> list) {
        super(list);
    }

    public void registerListener(OnPlaygroundClicked listener) {
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_playground_item, parent, false);
        return new PlaygroundViewHolder(v);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position, Playground item) {
        if (holder instanceof PlaygroundViewHolder) {
            ((PlaygroundViewHolder) holder).bind(item, position);
        }
    }


    class PlaygroundViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cv_container)
        CardView cv_container;

        @BindView(R.id.img_image)
        ImageView img_image;

        @BindView(R.id.txt_name)
        TextView txt_name;
        @BindView(R.id.txt_address)
        TextView txt_address;

        private Context mContext;
        private String imageUrl;
private    TextView name_owner,name_guard,phone_owner,phone_guard;

        PlaygroundViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            mContext = itemView.getContext();
        }

        void bind(final Playground item, final int position) {
            if (ListUtils.isEmpty(item.images)) {
                imageUrl = "";
            } else {
                imageUrl = item.images.get(0);
            }

            Glide.with(mContext).load(imageUrl)
                    .placeholder(R.drawable.img_logo_white)
                    .error(R.drawable.img_logo_white)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(img_image);

            txt_name.setText(item.name);
            txt_address.setText(item.address_region + "-" + item.address_city + "-" + item.address_direction);
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            final AlertDialog dialog = builder.create();
            final  View dialogLayout = LayoutInflater.from(mContext).inflate(R.layout.info_playground, null,false);
             name_owner=dialogLayout.findViewById(R.id.name_owner);
              name_guard=dialogLayout.findViewById(R.id.name_guard);
             phone_owner=dialogLayout.findViewById(R.id.phone_owner);
             phone_guard=dialogLayout.findViewById(R.id.phone_guard);


            cv_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    AppController.getInstance().setQrCode(item.qr_code);
//
//                    EventBus.getDefault().post(new OnEventItemClicked<>(item, ItemAction.DETAILS, position));

                    dialog.setView(dialogLayout);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    if (!dialog.isShowing()){
                        name_owner.setText(item.nameOwner);
                        name_guard.setText(item.nameguard);
                        phone_guard.setText(item.mobileguard);
                        phone_owner.setText(item.mobileOwner);
                        dialog.show();

                    }
                    if (mListener != null) {
                        mListener.setOnPlaygroundClicked(item, position);
                    }
                }
            });
        }
    }


    public interface OnPlaygroundClicked {
        void setOnPlaygroundClicked(Playground playground, int position);
    }
}
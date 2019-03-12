package com.uiu.helper.KidsHelper.mvp.ui.dashboard.regd_peoples;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.uiu.helper.KidsHelper.mvp.model.ContactEntity;
import com.uiu.helper.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RegdContactListAdapter extends RecyclerView.Adapter<RegdContactListAdapter.ViewHolder>{


    private List<ContactEntity> items;
    private Context context;
    private OnItemClickListener clickListener;

    public RegdContactListAdapter(Context context, List<ContactEntity> items, OnItemClickListener clickListener) {
        this.items = items;
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public RegdContactListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_list_item_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RegdContactListAdapter.ViewHolder holder, int position) {
        holder.tvContactTitle.setText(items.get(position).getName());
        holder.view.setOnClickListener(v -> clickListener.onItemClick(holder.getAdapterPosition()));
        holder.profileImage.setImageURI(items.get(position).getPhotoUri());

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public ContactEntity getItem(int position) {
        return items.get(position);
    }

    public void updateItems(List<ContactEntity> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView profileImage;
        TextView tvContactTitle;
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            profileImage = view.findViewById(R.id.profile_image);
            tvContactTitle = view.findViewById(R.id.contact_name);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
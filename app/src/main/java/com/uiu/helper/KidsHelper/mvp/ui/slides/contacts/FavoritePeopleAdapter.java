package com.uiu.helper.KidsHelper.mvp.ui.slides.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.uiu.helper.KidsHelper.mvp.model.ContactEntity;
import com.uiu.helper.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FavoritePeopleAdapter extends RecyclerView.Adapter<FavoritePeopleAdapter.ViewHolder> {

    private Context mContext;
    private FavoritePeopleAdapter.Callback mCallback;
    private List<ContactEntity> mSlideItems;
    private LayoutInflater inflater;
    private boolean inEditMode=false;

    public FavoritePeopleAdapter(Context context, FavoritePeopleAdapter.Callback callback) {
        this.mCallback = callback;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.mSlideItems = new ArrayList<>();
    }


    public void setSlideItems(List<ContactEntity> slideItems) {
        this.mSlideItems.clear();
        this.mSlideItems.addAll(slideItems);
        notifyDataSetChanged();

    }

    public void setEditMode(boolean isEditable){
        inEditMode = isEditable;
        notifyDataSetChanged();
    }

    public boolean inEditMode(){
        return inEditMode;
    }

    @NonNull
    @Override
    public FavoritePeopleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoritePeopleAdapter.ViewHolder((FavoritePeopleItemView) inflater.inflate(R.layout.fav_people_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritePeopleAdapter.ViewHolder holder, int position) {
        ContactEntity item = mSlideItems.get(position);
        ((FavoritePeopleItemView) holder.itemView).setSlideItem(item,inEditMode, mCallback);

    }


    @Override
    public int getItemCount() {
        return mSlideItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(FavoritePeopleItemView itemView) {
            super(itemView);
        }
    }

    public interface Callback {
        void onSlideItemClick(ContactEntity slideItem);
    }
}
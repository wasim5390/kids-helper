package com.uiu.helper.KidsHelper.mvp.ui.slides.reminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.uiu.helper.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReminderAdapterList extends RecyclerView.Adapter<ReminderAdapterList.ViewHolder> {

    private Context mContext;
    private ReminderAdapterList.Callback mCallback;
    private List<ReminderEntity> mSlideItems;
    private LayoutInflater inflater;
    private boolean inEditMode=false;

    public ReminderAdapterList(Context context, List<ReminderEntity> favItems, ReminderAdapterList.Callback callback) {
        this.mCallback = callback;
        this.mContext = context;
        this.inflater = LayoutInflater.from(mContext);
        this.mSlideItems = favItems;
    }


    public void setSlideItems(List<ReminderEntity> slideItems) {
        if (slideItems != null) {
            this.mSlideItems.clear();
            this.mSlideItems.addAll(slideItems);
            notifyDataSetChanged();
        }

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
    public ReminderAdapterList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ReminderItemView view=(ReminderItemView) inflater.inflate(R.layout.reminder_item_view, parent, false);
        return new ReminderAdapterList.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderAdapterList.ViewHolder holder, int position) {

        ReminderEntity item = mSlideItems.get(position);
        ((ReminderItemView) holder.itemView).setSlideItem(item,inEditMode,mCallback);
    }




    @Override
    public int getItemCount() {
        return mSlideItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(ReminderItemView itemView) {
            super(itemView);
        }
    }

    public interface Callback {
        void onSlideItemClick(ReminderEntity slideItem);
    }

}


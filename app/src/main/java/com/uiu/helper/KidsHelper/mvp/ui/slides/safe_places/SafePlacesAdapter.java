package com.uiu.helper.KidsHelper.mvp.ui.slides.safe_places;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.uiu.helper.KidsHelper.mvp.ui.tracker.DirectionsItem;
import com.uiu.helper.R;

import java.util.ArrayList;
import java.util.List;

public class SafePlacesAdapter extends RecyclerView.Adapter<SafePlacesAdapter.ViewHolder> {

    private Context mContext;
    private SafePlacesAdapter.Callback mCallback;
    private List<DirectionsItem> mSlideItems;
    private LayoutInflater inflater;
    private boolean inEditMode=false;

    public SafePlacesAdapter(Context context, SafePlacesAdapter.Callback callback) {
        this.mCallback = callback;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.mSlideItems = new ArrayList<>();
    }


    public void setSlideItems(List<DirectionsItem> slideItems) {
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
    public SafePlacesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SafePlacesAdapter.ViewHolder((SafePlacesItemView) inflater.inflate(R.layout.direction_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SafePlacesAdapter.ViewHolder holder, int position) {
        DirectionsItem item = mSlideItems.get(position);
        ((SafePlacesItemView) holder.itemView).setSlideItem(item,inEditMode, mCallback);

    }


    @Override
    public int getItemCount() {
        return mSlideItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(SafePlacesItemView itemView) {
            super(itemView);
        }
    }

    public interface Callback {
        void onSlideItemClick(DirectionsItem slideItem);
    }
}
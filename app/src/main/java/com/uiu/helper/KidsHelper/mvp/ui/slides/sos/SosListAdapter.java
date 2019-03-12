package com.uiu.helper.KidsHelper.mvp.ui.slides.sos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.uiu.helper.KidsHelper.mvp.model.ContactEntity;
import com.uiu.helper.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SosListAdapter extends RecyclerView.Adapter<SosListAdapter.ViewHolder> {

    private Context mContext;
    private SosListAdapter.Callback mCallback;
    private List<ContactEntity> mSlideItems;
    private LayoutInflater inflater;
    private boolean inEditMode=false;

    public SosListAdapter(Context context, SosListAdapter.Callback callback) {
        this.mCallback = callback;
        this.mContext = context;
        this.inflater = LayoutInflater.from(mContext);
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
    public SosListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SosListAdapter.ViewHolder((SosItemView) inflater.inflate(R.layout.app_sos_slide_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SosListAdapter.ViewHolder holder, int position) {

        ContactEntity item = mSlideItems.get(position);
        ((SosItemView) holder.itemView).setSlideItem(item, inEditMode,mCallback);

    }




    @Override
    public int getItemCount() {
        return mSlideItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(SosItemView itemView) {
            super(itemView);
        }
    }

    public interface Callback {
        void onSlideItemClick(ContactEntity slideItem);
    }
}

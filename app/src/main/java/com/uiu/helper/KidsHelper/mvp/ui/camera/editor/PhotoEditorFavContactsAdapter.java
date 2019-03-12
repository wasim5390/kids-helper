package com.uiu.helper.KidsHelper.mvp.ui.camera.editor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.uiu.helper.KidsHelper.mvp.model.ContactEntity;
import com.uiu.helper.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PhotoEditorFavContactsAdapter extends RecyclerView.Adapter<PhotoEditorFavContactsAdapter.ViewHolder> {

    private Context mContext;
    private PhotoEditorFavContactsAdapter.Callback mCallback;
    private List<ContactEntity> mSlideItems;
    private LayoutInflater inflater;

    public PhotoEditorFavContactsAdapter(Context context, PhotoEditorFavContactsAdapter.Callback callback) {
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

    public List<String> getSelectedContacts(){
        List<String> contactEntities = new ArrayList<>();
        for(ContactEntity entity: mSlideItems){
            if(entity.isSelected)
                contactEntities.add(entity.getId());
        }
        return contactEntities;
    }

    @NonNull
    @Override
    public PhotoEditorFavContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoEditorFavContactsAdapter.ViewHolder((PhotoEditorContactItemView) inflater.inflate(R.layout.photo_editor_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoEditorFavContactsAdapter.ViewHolder holder, int position) {
        ContactEntity item = mSlideItems.get(position);
        ((PhotoEditorContactItemView) holder.itemView).setSlideItem(item, mCallback);

    }


    @Override
    public int getItemCount() {
        return mSlideItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(PhotoEditorContactItemView itemView) {
            super(itemView);
        }
    }

    public interface Callback {
        void onSlideItemClick(ContactEntity slideItem);
    }
}
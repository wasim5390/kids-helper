package com.uiu.helper.KidsHelper.mvp.ui.share;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.uiu.helper.KidsHelper.mvp.model.ContactEntity;
import com.uiu.helper.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShareToAdapterList extends RecyclerView.Adapter<ShareToAdapterList.ViewHolder> {

    private Context mContext;
    private ShareToAdapterList.Callback mCallback;
    private List<ContactEntity> mSlideItems;
    private LayoutInflater inflater;

    public ShareToAdapterList(Context context, ShareToAdapterList.Callback callback) {
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

    public List<ContactEntity> getSelectedContact(){
        List<ContactEntity> selectedContact = new ArrayList<>();
        for(ContactEntity entity: mSlideItems){
            if(entity.isSelectedForSharing())
                selectedContact.add(entity);
        }
        return selectedContact;
    }

    @NonNull
    @Override
    public ShareToAdapterList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShareToAdapterList.ViewHolder((ShareToUserItemView) inflater.inflate(R.layout.message_list_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShareToAdapterList.ViewHolder holder, int position) {
        ContactEntity item = mSlideItems.get(position);
        ((ShareToUserItemView) holder.itemView).setSlideItem(item, mCallback);

    }


    @Override
    public int getItemCount() {
        return mSlideItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(ShareToUserItemView itemView) {
            super(itemView);
        }
    }

    public interface Callback {
        void onSlideItemClick(ContactEntity slideItem, boolean isSelected);
    }
}
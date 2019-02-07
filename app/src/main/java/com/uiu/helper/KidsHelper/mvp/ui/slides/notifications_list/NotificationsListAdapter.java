package com.uiu.helper.KidsHelper.mvp.ui.slides.notifications_list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.uiu.helper.KidsHelper.mvp.model.LinksEntity;
import com.uiu.helper.KidsHelper.mvp.model.NotificationsItem;
import com.uiu.helper.R;

import java.util.List;


public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.ViewHolder> {

    private Context mContext;
    private NotificationsListAdapter.Callback mCallback;
    private List<NotificationsItem> mSlideItems;
    private LayoutInflater inflater;



    public NotificationsListAdapter(Context context, List<NotificationsItem> favItems, NotificationsListAdapter.Callback callback) {
        this.mCallback = callback;
        this.mContext = context;
        this.inflater = LayoutInflater.from(mContext);
        this.mSlideItems = favItems;
    }


    public void setSlideItems(List<NotificationsItem> slideItems) {
        if (slideItems != null) {
            this.mSlideItems.clear();
            this.mSlideItems.addAll(slideItems);
            notifyDataSetChanged();
        }

    }

    public void addItems(List<NotificationsItem> slideItems) {
        if (slideItems != null) {
            this.mSlideItems.addAll(slideItems);
            notifyDataSetChanged();
        }

    }

    public void removeItem(NotificationsItem item){
       if(!this.mSlideItems.isEmpty()){
           mSlideItems.remove(item);
           notifyDataSetChanged();
       }
    }

    @NonNull
    @Override
    public NotificationsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        NotificationsListItemView view=(NotificationsListItemView) inflater.inflate(R.layout.notification_list_item_view, parent, false);
        return new NotificationsListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsListAdapter.ViewHolder holder, int position) {

        NotificationsItem item = mSlideItems.get(position);
        ((NotificationsListItemView) holder.itemView).setSlideItem(item,mCallback);
    }




    @Override
    public int getItemCount() {
        return mSlideItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(NotificationsListItemView itemView) {
            super(itemView);
        }
    }

    public interface Callback {
        void onSlideItemClick(NotificationsItem notificationsItem,int status);
        void onDeleteSlideItem(NotificationsItem notificationsItem,int status);
    }

}
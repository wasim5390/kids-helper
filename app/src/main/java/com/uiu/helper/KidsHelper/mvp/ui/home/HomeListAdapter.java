package com.uiu.helper.KidsHelper.mvp.ui.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.uiu.helper.KidsHelper.entities.UserEntity;
import com.uiu.helper.KidsHelper.mvp.model.Invitation;
import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.R;

import java.util.List;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {

    private Context mContext;
    private HomeListAdapter.Callback mCallback;
    private List<Invitation> mInviteList;
    private LayoutInflater inflater;

    public HomeListAdapter(Context context, List<Invitation> invitations, HomeListAdapter.Callback callback) {
        this.mCallback = callback;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.mInviteList = invitations;
    }


    public void setItems(List<Invitation> slideItems) {
        if(slideItems!=null) {
            this.mInviteList.clear();
            this.mInviteList.addAll(slideItems);
            notifyDataSetChanged();
        }

    }


    @NonNull
    @Override
    public HomeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeListAdapter.ViewHolder((HomeListItemView) inflater.inflate(R.layout.home_list_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeListAdapter.ViewHolder holder, int position) {
        Invitation item = mInviteList.get(position);
        ((HomeListItemView) holder.itemView).setSlideItem(item,mCallback);

    }


    @Override
    public int getItemCount() {
        return mInviteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(HomeListItemView itemView) {
            super(itemView);
        }
    }

    public interface Callback {
        void onItemClick(Invitation slideItem);
        void onItemLongClick(Invitation slideItem);
    }
}

package com.uiu.helper.KidsHelper.mvp.ui.home.secondary_helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.uiu.helper.KidsHelper.mvp.model.User;

import com.uiu.helper.R;

import java.util.List;

public class SecondaryHelperListAdapter extends RecyclerView.Adapter<SecondaryHelperListAdapter.ViewHolder> {

    private Context mContext;
    private SecondaryHelperListAdapter.Callback mCallback;
    private List<User> mUsersList;
    private LayoutInflater inflater;

    public SecondaryHelperListAdapter(Context context, List<User> users, SecondaryHelperListAdapter.Callback callback) {
        this.mCallback = callback;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.mUsersList = users;
    }


    public void setItems(List<User> slideItems) {
        if(slideItems!=null) {
            this.mUsersList.clear();
            this.mUsersList.addAll(slideItems);
            notifyDataSetChanged();
        }

    }


    @NonNull
    @Override
    public SecondaryHelperListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SecondaryHelperListAdapter.ViewHolder((SecondaryHelperItemView) inflater.inflate(R.layout.secondary_helper_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SecondaryHelperListAdapter.ViewHolder holder, int position) {
        User item = mUsersList.get(position);
        ((SecondaryHelperItemView) holder.itemView).setHelper(item,mCallback);

    }


    @Override
    public int getItemCount() {
        return mUsersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(SecondaryHelperItemView itemView) {
            super(itemView);
        }
    }

    public interface Callback {
        void onItemClick(User slideItem);
    }
}

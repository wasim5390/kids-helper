package com.uiu.helper.KidsHelper.mvp.ui.slides.links;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.uiu.helper.KidsHelper.mvp.model.LinksEntity;
import com.uiu.helper.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class FavoriteLinksAdapter extends RecyclerView.Adapter<FavoriteLinksAdapter.ViewHolder> {

    private Context mContext;
    private FavoriteLinksAdapter.Callback mCallback;
    private List<LinksEntity> mSlideItems;
    private LayoutInflater inflater;
    private boolean inEditMode=false;

    public FavoriteLinksAdapter(Context context, List<LinksEntity> favItems, FavoriteLinksAdapter.Callback callback) {
        this.mCallback = callback;
        this.mContext = context;
        this.inflater = LayoutInflater.from(mContext);
        this.mSlideItems = favItems;
    }


    public void setSlideItems(List<LinksEntity> slideItems) {
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
    public FavoriteLinksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        FavoriteLinksItemView view=(FavoriteLinksItemView) inflater.inflate(R.layout.link_slide_item_view, parent, false);
        return new FavoriteLinksAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteLinksAdapter.ViewHolder holder, int position) {

        LinksEntity item = mSlideItems.get(position);
        ((FavoriteLinksItemView) holder.itemView).setSlideItem(item,inEditMode,mCallback);
    }




    @Override
    public int getItemCount() {
        return mSlideItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(FavoriteLinksItemView itemView) {
            super(itemView);
        }
    }

    public interface Callback {
        void onSlideItemClick(LinksEntity slideItem);
        void onSlideItemRemove(LinksEntity slideItem);
    }

}
package com.uiu.helper.KidsHelper.mvp.ui.c_me.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.model.File;
import com.uiu.helper.R;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CmeeHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Constant {

    private Context mContext;
    private CmeeHistoryAdapter.Callback mCallback;
    private List<File> mSlideItems;
    private LayoutInflater inflater;

    public CmeeHistoryAdapter(Context context, CmeeHistoryAdapter.Callback callback) {
        this.mCallback = callback;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.mSlideItems = new ArrayList<>();
    }


    public void setSlideItems(List<File> slideItems) {
        this.mSlideItems.clear();
        this.mSlideItems.addAll(slideItems);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewAudio = (CmeeHistoryAudioItemView) inflater.inflate(R.layout.chat_audio_item_view, parent, false);
        View viewVideoImage = (CmeeHistoryVideoItemView) inflater.inflate(R.layout.chat_video_item_view, parent, false);
        switch (viewType) {
            case MEDIA_AUDIO:
                return new ViewHolderAudio(viewAudio);
            case MEDIA_VIDEO:
            case MEDIA_IMAGE:
                return new ViewHolderVideo(viewVideoImage);

        }
        return new EmptyViewHolder(new View(mContext));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        File item = mSlideItems.get(position);
        switch (holder.getItemViewType()){
            case MEDIA_AUDIO:
                ((CmeeHistoryAudioItemView) holder.itemView).setSlideItem(item, mCallback);
                break;
            case MEDIA_VIDEO:
            case MEDIA_IMAGE:
                ((CmeeHistoryVideoItemView) holder.itemView).setSlideItem(item, mCallback);
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous

        return mSlideItems.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mSlideItems.size();
    }


    public class ViewHolderAudio extends RecyclerView.ViewHolder {

        public ViewHolderAudio(View itemView) {
            super(itemView);
        }
    }

    public class ViewHolderVideo extends RecyclerView.ViewHolder {

        public ViewHolderVideo(View itemView) {
            super(itemView);
        }
    }
    public class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface Callback {
        void onAudioItemClick(File mediaFile);
        void onVideoItemClick(File mediaFile);
    }
}

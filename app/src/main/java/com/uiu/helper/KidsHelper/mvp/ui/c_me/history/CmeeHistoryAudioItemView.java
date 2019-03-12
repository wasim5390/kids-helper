package com.uiu.helper.KidsHelper.mvp.ui.c_me.history;

import android.content.Context;

import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.model.File;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.R;
import com.uiu.helper.util.Utils;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CmeeHistoryAudioItemView extends ConstraintLayout implements Constant {

    @BindView(R.id.ivReceiver)
    SimpleDraweeView receiverImage;

    @BindView(R.id.ivSender)
    SimpleDraweeView senderImage;

    @BindView(R.id.tv_receiver_name)
    TextView tvSenderName;

    @BindView(R.id.tv_receiver_date)
    TextView tvDateTime;



    private CmeeHistoryAdapter.Callback callback;
    private File item;

    public CmeeHistoryAudioItemView(Context context) {
        super(context);
    }

    public CmeeHistoryAudioItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CmeeHistoryAudioItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);

    }

    public void setSlideItem(File item, CmeeHistoryAdapter.Callback callback) {
        this.callback = callback;
        this.item = item;
        if (item != null) {
            boolean amISender = item.getSender().getId().equals(PreferenceUtil.getInstance(getContext()).getAccount().getId());
            populateData(item);
            if(amISender) {
                senderImage.setVisibility(VISIBLE);
                receiverImage.setVisibility(GONE);

                Glide.with(this).asBitmap()
                        .load(item.getSender().getImageLink())
                        .override(Utils.dpToPx(50)).circleCrop()
                        .placeholder(R.drawable.wiser_avatar)
                        .into(senderImage);
            }
            else{
                receiverImage.setVisibility(VISIBLE);
                senderImage.setVisibility(GONE);
                Glide.with(this).asBitmap()
                        .load(item.getSender().getImageLink())
                        .override(Utils.dpToPx(50)).circleCrop()
                        .placeholder(R.drawable.wiser_avatar)
                        .into(receiverImage);
            }

        }
    }

    private void populateData(File item){

            tvSenderName.setText(item.getSender().getName());
            tvDateTime.setText(Utils.formatDate(String.valueOf(item.getCreatedAt())));
    }



    @OnClick(R.id.receiver_view)
    public void onSlideItemClick() {

        if(item.getType()==MEDIA_AUDIO)
            callback.onAudioItemClick(item);
        if(item.getType()==MEDIA_VIDEO)
            callback.onVideoItemClick(item);

    }
}

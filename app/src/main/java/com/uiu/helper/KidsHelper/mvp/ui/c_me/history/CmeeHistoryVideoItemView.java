package com.uiu.helper.KidsHelper.mvp.ui.c_me.history;

import android.content.Context;

import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.facebook.drawee.view.SimpleDraweeView;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.model.File;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.R;
import com.uiu.helper.util.Utils;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CmeeHistoryVideoItemView extends ConstraintLayout implements Constant {

    @BindView(R.id.ivReceiver)
    SimpleDraweeView receiverImage;

    @BindView(R.id.ivSender)
    SimpleDraweeView senderImage;

    @BindView(R.id.iv_image)
    ImageView ivThumbnail;

    @BindView(R.id.ivVideoIcon)
    ImageView ivVideoPlayBtn;

    @BindView(R.id.tv_receiver_name)
    TextView tvSenderName;

    @BindView(R.id.tv_receiver_date)
    TextView tvDateTime;

    @BindView(R.id.iv_video_btn_overlay)
    FrameLayout iv_video_btn_overlay;

    private CmeeHistoryAdapter.Callback callback;
    private File item;

    public CmeeHistoryVideoItemView(Context context) {
        super(context);
    }

    public CmeeHistoryVideoItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CmeeHistoryVideoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
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
                receiverImage.setVisibility(GONE);
                senderImage.setVisibility(VISIBLE);
                Glide.with(this).load(item.getSender().getImageLink())
                        .override(Utils.dpToPx(50)).circleCrop().placeholder(R.drawable.wiser_avatar).into(senderImage);

            }
            else {
                receiverImage.setVisibility(VISIBLE);
                senderImage.setVisibility(GONE);
                Glide.with(this).load(item.getSender().getImageLink())
                        .override(Utils.dpToPx(50)).circleCrop().placeholder(R.drawable.wiser_avatar).into(receiverImage);
            }

        }
    }

    private void populateData(File item){

        tvSenderName.setText(item.getSender().getName());
        tvDateTime.setText(Utils.formatDate(String.valueOf(item.getCreatedAt())));
        int radius = Utils.dpToPx(10);
        if(item.getType()==MEDIA_IMAGE) {

            Glide.with(this)
                    .load(item.getFileUrl())
                    .transform(new CenterCrop(), new RoundedCorners(radius))
                    .placeholder(R.drawable.placeholder_sqr)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(ivThumbnail);
            ivVideoPlayBtn.setVisibility(GONE);
            iv_video_btn_overlay.setAlpha(0.2f);
        }else if(item.getType()==MEDIA_VIDEO){
            String thumbnail = item.getThumbnail().contains("missing.png")?item.getFileUrl():item.getThumbnail();

            Glide.with(this).asBitmap().load(thumbnail)
                    .transform(new CenterCrop(),new RoundedCorners(radius))
                   .placeholder(R.drawable.placeholder_sqr)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(ivThumbnail);
            ivVideoPlayBtn.setVisibility(VISIBLE);
            iv_video_btn_overlay.setAlpha(0.4f);
        }

    }



    @OnClick(R.id.receiver_view)
    public void onSlideItemClick() {

        if(item.getType()==MEDIA_AUDIO)
            callback.onAudioItemClick(item);
        if(item.getType()==MEDIA_VIDEO)
            callback.onVideoItemClick(item);

    }
}

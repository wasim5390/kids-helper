package com.uiu.helper.KidsHelper.mvp.ui.slides.notifications_list;

import android.content.Context;

import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.model.LinksEntity;
import com.uiu.helper.KidsHelper.mvp.model.NotificationsItem;
import com.uiu.helper.KidsHelper.mvp.model.NotificationsListResponse;
import com.uiu.helper.R;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationsListItemView extends ConstraintLayout implements Constant {



    @BindView(R.id.tvNotification)
    TextView tvNotification;

    @BindView(R.id.tvNotificationTitle)
    TextView tvNotificationTitle;

    @BindView(R.id.ivImage)
    SimpleDraweeView image;

    @BindView(R.id.btnApprove)
    Button btnApprove;

    @BindView(R.id.btnDecline)
    Button btnDecline;

    int status=-1;

    Animation animScale;

    private NotificationsListAdapter.Callback callback;
    private NotificationsItem slideItem;

    public NotificationsListItemView(Context context) {
        super(context);
    }

    public NotificationsListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotificationsListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);
        animScale = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale);

    }

    public void setSlideItem(NotificationsItem item, NotificationsListAdapter.Callback callback) {
        this.callback = callback;
        this.slideItem = item;

        if (item != null) {
            status= slideItem.getRequest_status();
            btnApprove.setVisibility(status==Constant.REQ_REQUESTED?VISIBLE:GONE);
            btnDecline.setText(status==Constant.REQ_REQUESTED?getContext().getString(R.string.decline):getContext().getString(R.string.delete));
            tvNotificationTitle.setText(item.getTitle());
            tvNotification.setText(item.getMessage());
            image.setImageResource(R.drawable.placeholder_sqr);
            if(item.getImage()!=null && !item.getImage().isEmpty())
                Picasso.with(getContext())
                        .load(item.getImage()).placeholder(R.drawable.placeholder_sqr)
                        .into(image);
        }

    }

    @OnClick(R.id.btnApprove)
    public void onApproveClick() {
        callback.onSlideItemClick(slideItem,Constant.ACCEPTED);

    }

    @OnClick(R.id.btnDecline)
    public void onDeclineClick() {
        if(status==Constant.REQ_REQUESTED)
            callback.onSlideItemClick(slideItem,Constant.REJECTED);
        else if(status != 0)
            callback.onDeleteSlideItem(slideItem,0);

    }

}


package com.uiu.helper.KidsHelper.mvp.ui.slides.reminder;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.R;
import com.uiu.helper.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class ReminderItemView extends ConstraintLayout implements Constant {

    @BindView(R.id.rem_item)
    ImageView slideItemImage;

    @BindView(R.id.rem_lable)
    TextView itemLable;
    @BindView(R.id.rem_desc)
    TextView itemDesc;

    @BindView(R.id.rem_dateTime)
    TextView itemdateTimr;

    @BindView(R.id.element_close_btn)
    ImageView removeBtn;
    private boolean isInEditMode;
    Animation animScale;

    private ReminderAdapterList.Callback callback;
    private ReminderEntity slideItem;

    public ReminderItemView(Context context) {
        super(context);
    }

    public ReminderItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReminderItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);
        animScale = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale);

    }

    public void setSlideItem(ReminderEntity item,boolean isInEditMode, ReminderAdapterList.Callback callback) {
        this.callback = callback;
        this.slideItem = item;
        this.isInEditMode = isInEditMode;
        if(slideItem!=null)
        {
            if(slideItem.getId()!=null) {
                itemdateTimr.setVisibility(GONE);
                itemDesc.setVisibility(VISIBLE);
                slideItemImage.setImageResource(R.drawable.alarm_icon);
                itemLable.setText(slideItem.getTitle());
                itemDesc.setText(slideItem.getNote());
                itemdateTimr.setText(Utils.formatDate(slideItem.getTime()));
                removeBtn.setVisibility(isInEditMode?VISIBLE:GONE);
            }else
            {
                itemLable.setText("Add New");
                itemdateTimr.setVisibility(GONE);
                itemDesc.setVisibility(GONE);
                removeBtn.setVisibility(GONE);
                slideItemImage.setImageResource(R.drawable.ic_add_icon);

            }

        }


    }

    @OnClick(R.id.element_close_btn)
    public void onRemoveItemClick(){
        callback.onSlideItemClick(slideItem);
    }


    @OnClick(R.id.container)
    public void onSlideItemClick() {
        if(slideItem.getId()!=null && !isInEditMode)
            return;
        callback.onSlideItemClick(slideItem);
    }


}
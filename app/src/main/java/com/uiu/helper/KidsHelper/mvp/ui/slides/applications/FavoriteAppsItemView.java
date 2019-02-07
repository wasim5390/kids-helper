package com.uiu.helper.KidsHelper.mvp.ui.slides.applications;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.model.AppsEntity;
import com.uiu.helper.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FavoriteAppsItemView extends ConstraintLayout implements Constant {

    @BindView(R.id.iv_iitem)
    SimpleDraweeView slideItemImage;

    @BindView(R.id.tv_llable)
    TextView itemLable;

    @BindView(R.id.element_close_btn)
    ImageView removeBtn;

    @BindView(R.id.ivEggTimer)
    ImageView ivTimer;


    Animation animScale;

    private boolean isInEditMode;

    private FavoriteAppsAdapter.Callback callback;
    private AppsEntity slideItem;

    public FavoriteAppsItemView(Context context) {
        super(context);
    }

    public FavoriteAppsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FavoriteAppsItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this,this);
        animScale = AnimationUtils.loadAnimation(getContext(),R.anim.anim_scale);

    }

    public void setSlideItem(AppsEntity item,boolean isInEditMode, FavoriteAppsAdapter.Callback callback){
        this.callback = callback;
        this.slideItem = item;
        this.isInEditMode = isInEditMode;

        if(item!=null)
        {
            if(item.getName()!=null) {
                slideItemImage.setEnabled(true);
                // ivTimer.setVisibility(slideItem.hasAccess()?GONE:VISIBLE);
                Picasso.with(getContext()).load(slideItem.getAppIcon()).into(slideItemImage);
                itemLable.setText(item.getName());
                removeBtn.setVisibility(isInEditMode?VISIBLE:GONE);
            }
            else
            {
                removeBtn.setVisibility(GONE);
                itemLable.setText("Add New");
                slideItemImage.setImageResource(R.drawable.ic_add_icon);
                // ivTimer.setVisibility(GONE);
            }
        }

    }

    @OnClick(R.id.element_close_btn)
    public void onRemoveItemClick(){
        callback.onSlideItemClick(slideItem);

    }

    @OnClick(R.id.iv_iitem)
    public void onSlideItemClick(){
        if(slideItem.getId()!=null && !isInEditMode)
            return;
        slideItemImage.startAnimation(animScale);
        animScale.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                callback.onSlideItemClick(slideItem);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}

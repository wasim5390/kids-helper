package com.uiu.helper.KidsHelper.mvp.ui.slides.sos;

import android.content.Context;

import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.model.ContactEntity;
import com.uiu.helper.R;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SosItemView extends ConstraintLayout implements Constant {

    @BindView(R.id.iv_item)
    SimpleDraweeView slideItemImage;

    @BindView(R.id.tv_lable)
    TextView itemLable;

    @BindView(R.id.ivEggTimer)
    ImageView ivTimer;

    @BindView(R.id.element_close_btn)
    ImageView removeBtn;
    private boolean isInEditMode;
    Animation animScale;

    private SosListAdapter.Callback callback;
    private ContactEntity slideItem;

    public SosItemView(Context context) {
        super(context);
    }

    public SosItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SosItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this,this);
        animScale = AnimationUtils.loadAnimation(getContext(),R.anim.anim_scale);

    }

    public void setSlideItem(ContactEntity item,boolean isInEditMode, SosListAdapter.Callback callback){
        this.callback = callback;
        this.slideItem = item;
        this.isInEditMode = isInEditMode;
        if(item !=null) {

            if(item.getId()!=null) {

                slideItemImage.setEnabled(true);
               // ivTimer.setVisibility(slideItem.hasAccess()?GONE:VISIBLE);
                itemLable.setText(item.getName());
                slideItemImage.setImageURI(slideItem.getPhotoUri());
                removeBtn.setVisibility(isInEditMode?VISIBLE:GONE);

            }
            else
            {
              //  ivTimer.setVisibility(GONE);
                removeBtn.setVisibility(GONE);
                itemLable.setText("Add New");
                slideItemImage.setImageResource(R.drawable.ic_add_icon);
            }

        }
    }
    @OnClick(R.id.element_close_btn)
    public void onRemoveItemClick(){
        callback.onSlideItemClick(slideItem);
    }

    @OnClick(R.id.itemContainer)
    public void onSlideItemClick(){
        if(slideItem.getId()!=null && !isInEditMode)
            return;
        slideItemImage.startAnimation(animScale);
        animScale.setAnimationListener(new Animation.AnimationListener() {
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
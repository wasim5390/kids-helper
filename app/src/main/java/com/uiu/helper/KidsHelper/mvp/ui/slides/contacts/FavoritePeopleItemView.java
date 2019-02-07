package com.uiu.helper.KidsHelper.mvp.ui.slides.contacts;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.model.ContactEntity;
import com.uiu.helper.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FavoritePeopleItemView extends ConstraintLayout implements Constant {

    @BindView(R.id.iv_item)
    SimpleDraweeView slideItemImage;

    @BindView(R.id.tv_lable)
    TextView itemLable;
    @BindView(R.id.ivEggTimer)
    ImageView ivTimer;
    @BindView(R.id.element_close_btn)
    ImageView removeBtn;
    Animation animScale;

    private boolean isInEditMode;

    private FavoritePeopleAdapter.Callback callback;
    private ContactEntity slideItem;

    public FavoritePeopleItemView(Context context) {
        super(context);
    }

    public FavoritePeopleItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FavoritePeopleItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this,this);
        animScale = AnimationUtils.loadAnimation(getContext(),R.anim.anim_scale);

    }

    public void setSlideItem(ContactEntity item,boolean isInEditMode, FavoritePeopleAdapter.Callback callback){
        this.callback = callback;
        this.slideItem = item;
        this.isInEditMode = isInEditMode;

        if(item !=null) {
            if(item.getId()!=null) {
                slideItemImage.setEnabled(true);
                itemLable.setText(item.getName());
                slideItemImage.setImageURI(slideItem.getProfilePic());
                removeBtn.setVisibility(isInEditMode?VISIBLE:GONE);
            }
            else
            {
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

    @OnClick(R.id.iv_item)
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

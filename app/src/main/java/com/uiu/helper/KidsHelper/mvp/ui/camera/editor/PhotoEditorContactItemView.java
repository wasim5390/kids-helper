package com.uiu.helper.KidsHelper.mvp.ui.camera.editor;

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

public class PhotoEditorContactItemView extends ConstraintLayout implements Constant {

    @BindView(R.id.iv_item)
    SimpleDraweeView slideItemImage;

    @BindView(R.id.tv_lable)
    TextView itemLable;
    @BindView(R.id.iv_selected)
    ImageView ivSelected;

    Animation animScale;

    private PhotoEditorFavContactsAdapter.Callback callback;
    private ContactEntity slideItem;

    public PhotoEditorContactItemView(Context context) {
        super(context);
    }

    public PhotoEditorContactItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoEditorContactItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this,this);
        animScale = AnimationUtils.loadAnimation(getContext(),R.anim.anim_scale);

    }

    public void setSlideItem(ContactEntity item, PhotoEditorFavContactsAdapter.Callback callback){
        this.callback = callback;
        this.slideItem = item;
        if(item !=null) {
            if(item.getName()!=null) {
                ivSelected.setVisibility(slideItem.isSelected ? VISIBLE : GONE );
                itemLable.setText(item.getName());
                slideItemImage.setImageURI(slideItem.getProfilePic());

            }

        }
    }

    @OnClick(R.id.iv_item)
    public void onSlideItemClick(){
        slideItemImage.startAnimation(animScale);
        animScale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                slideItem.isSelected= !slideItem.isSelected;
                callback.onSlideItemClick(slideItem);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}

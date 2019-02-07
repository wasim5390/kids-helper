package com.uiu.helper.KidsHelper.mvp.ui.slides.links;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.model.LinksEntity;
import com.uiu.helper.R;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FavoriteLinksItemView extends ConstraintLayout implements Constant {


    @BindView(R.id.iv_iitem)
    ImageView slideItemImage;

    @BindView(R.id.tv_llable)
    TextView itemLable;

    @BindView(R.id.richLinkCard)
    ConstraintLayout richConstraintCard;

    @BindView(R.id.rich_link_image)
    ImageView richLinkImage;

    @BindView(R.id.rich_link_title)
    TextView richLinkTitle;

    @BindView(R.id.rich_link_desp)
    TextView richLinkDesc;

    @BindView(R.id.rich_link_url)
    TextView richLinkUrl;

    @BindView(R.id.element_close_btn)
    ImageView removeBtn;

    Animation animScale;

    private FavoriteLinksAdapter.Callback callback;
    private LinksEntity slideItem;

    private boolean isInEditMode;

    public FavoriteLinksItemView(Context context) {
        super(context);
    }

    public FavoriteLinksItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FavoriteLinksItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);
        animScale = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale);

    }

    public void setSlideItem(LinksEntity item,boolean isInEditMode, FavoriteLinksAdapter.Callback callback) {
        this.callback = callback;
        this.slideItem = item;
        this.isInEditMode = isInEditMode;
        if (item != null) {

            if (item.getId()!=null) {
                removeBtn.setVisibility(isInEditMode?VISIBLE:GONE);
                itemLable.setVisibility(GONE);
                slideItemImage.setVisibility(GONE);
                richConstraintCard.setVisibility(VISIBLE);

                if( slideItem.getIcon_url().isEmpty()) {
                    Picasso.with(getContext())
                            .load(R.drawable.placeholder_sqr)
                            .into(richLinkImage);
                } else {

                    Picasso.with(getContext())
                            .load(item.getIcon_url()).placeholder(R.drawable.placeholder_sqr)
                            .into(richLinkImage);
                }

                if(slideItem.getTitle().isEmpty() || slideItem.getTitle().equals("")) {
                    richLinkTitle.setVisibility(GONE);
                } else {
                    richLinkTitle.setVisibility(VISIBLE);
                    richLinkTitle.setText(slideItem.getTitle());
                }
                if(slideItem.getLink().isEmpty() || slideItem.getLink().equals("")) {
                    richLinkUrl.setText(slideItem.getLink());
                } else {
                    richLinkUrl.setVisibility(VISIBLE);
                    richLinkUrl.setText(slideItem.getLink());
                }
                if(slideItem.getDesc().isEmpty() || slideItem.getDesc().equals("")) {
                    richLinkDesc.setVisibility(GONE);
                } else {
                    richLinkDesc.setVisibility(VISIBLE);
                    richLinkDesc.setText(item.getDesc());
                }


                Log.e("link", slideItem.getLink());
            } else {
                removeBtn.setVisibility(GONE);
                richConstraintCard.setVisibility(GONE);
                itemLable.setText("Add New");
                slideItemImage.setVisibility(VISIBLE);
                slideItemImage.setImageResource(R.drawable.ic_add_icon);
                slideItemImage.setEnabled(true);
            }
        }

    }

    @OnClick(R.id.iv_iitem)
    public void onSlideItemClick() {
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

    @OnClick(R.id.element_close_btn)
    public void onRemoveItemClick(){
        callback.onSlideItemRemove(slideItem);
    }

    @OnClick(R.id.richLinkCard)
    public void onLinkClick(){
        if(slideItem.getId()!=null && !isInEditMode)
            return;
        callback.onSlideItemClick(slideItem);
    }


}


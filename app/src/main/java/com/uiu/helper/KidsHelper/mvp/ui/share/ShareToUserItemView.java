package com.uiu.helper.KidsHelper.mvp.ui.share;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
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


public class ShareToUserItemView extends ConstraintLayout implements Constant {

    @BindView(R.id.profile_img)
    SimpleDraweeView slideItemImage;

    @BindView(R.id.name)
    TextView itemName;

    @BindView(R.id.phn_no)
    TextView tvPhn;

    @BindView(R.id.select_contact)
    CheckBox checkBox;

    @BindView(R.id.message_item_layout)
    ConstraintLayout layout;


    private ShareToAdapterList.Callback callback;
    private ContactEntity slideItem;

    public ShareToUserItemView(Context context) {
        super(context);
    }

    public ShareToUserItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShareToUserItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);

    }

    public void setSlideItem(ContactEntity item, ShareToAdapterList.Callback callback) {
        this.callback = callback;
        this.slideItem = item;
        if (item != null) {
            String name = item.getName()!=null?item.getName():"";
            String email =item.getEmail()!=null?item.getEmail():"";
            itemName.setText(name.trim().isEmpty()?email:name);
            slideItemImage.setImageURI(slideItem.getPhotoUri());
        }
    }


    @OnClick(R.id.message_item_layout)
    public void onSlideItemClick() {

        checkBox.setVisibility(VISIBLE);
        new Handler().postDelayed(() -> callback.onSlideItemClick(slideItem, false),100);

    }
}

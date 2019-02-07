package com.uiu.helper.KidsHelper.mvp.ui.home;

import android.content.Context;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.model.Invitation;
import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.R;
import com.uiu.helper.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class HomeListItemView extends ConstraintLayout implements Constant {


    @BindView(R.id.invite_screen_profile_photo)
    SimpleDraweeView ivProfileImage;

    @BindView(R.id.tvKidName)
    TextView email;

    @BindView(R.id.tvHelperType)
    TextView tvHelperType;

    @BindView(R.id.tvKidStatus)
    TextView status;

    Invitation mItem;
    HomeListAdapter.Callback callback;

    public HomeListItemView(Context context) {
        super(context);
    }

    public HomeListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);

    }

    public void setSlideItem(Invitation item, HomeListAdapter.Callback callback) {
        this.callback = callback;
        this.mItem = item;
        if (mItem != null) {
            User sender = mItem.getInvitee();
            email.setText(mItem.getInvitee().getEmail());
            status.setText(getStatus(mItem.getStatus()).toLowerCase());
            tvHelperType.setText(mItem.isPrimary()?"(Primary)":"(Secondary)");
            if (sender.getImageLink()==null || sender.getImageLink().isEmpty()) {
                ivProfileImage.setImageResource(R.drawable.avatar_male2);
            } else {
                Utils.setImageURI(Uri.parse(sender.getImageLink()), ivProfileImage);
            }
        }

    }

    private String getStatus(int statusCode){
        String status="";
        switch (statusCode){
            case INVITE.CONNECTED:
                status= "CONNECTED! tap to begin";
                break;
            case INVITE.INVITED:
                status= "INVITED";
                break;
            case INVITE.REJECTED:
                status= "REJECTED";
                break;
        }
        return status;
    }

    @OnClick(R.id.view)
    public void onKidClick()
    {
        callback.onItemClick(mItem);

    }

    @OnLongClick(R.id.view)
    public boolean onKidLongClick(){
        callback.onItemLongClick(mItem);
        return true;
    }
}

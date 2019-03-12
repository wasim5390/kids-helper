package com.uiu.helper.KidsHelper.mvp.ui.home.secondary_helper;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.R;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SecondaryHelperItemView extends ConstraintLayout {

    @BindView(R.id.tvEmail)
    TextView tvEmail;

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.tvPhone)
    TextView tvPhone;

    @BindView(R.id.secondary_helper_profile_photo)
    SimpleDraweeView profilePhoto;

   private SecondaryHelperListAdapter.Callback mCallback;
    private User helper;
    public SecondaryHelperItemView(Context context) {
        super(context);
    }

    public SecondaryHelperItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SecondaryHelperItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this,this);
    }

    public void setHelper(User user, SecondaryHelperListAdapter.Callback callback){
        this.mCallback = callback;
        this.helper = user;
        tvEmail.setText(user.getEmail());
        tvName.setText(user.getName());
        tvPhone.setText(user.getPhoneNumber());
        profilePhoto.setImageURI(user.getImageLink());

    }


    @OnClick(R.id.container)
    public void onHelperSelected(){
        mCallback.onItemClick(this.helper);
    }
}

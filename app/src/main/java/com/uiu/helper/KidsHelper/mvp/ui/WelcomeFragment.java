package com.uiu.helper.KidsHelper.mvp.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.uiu.helper.KidsHelper.mvp.BaseFragment;
import com.uiu.helper.KidsHelper.mvp.events.GoogleLoginEvent;
import com.uiu.helper.KidsHelper.mvp.events.MainUIActionEvent;
import com.uiu.helper.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeFragment extends BaseFragment {

    public static WelcomeFragment newInstance()
    {
        Bundle args=new Bundle();
        WelcomeFragment instance=new WelcomeFragment();
        instance.setArguments(args);
        return instance;
    }

    @Override
    public int getID() {
        return R.layout.welcome_fragment;
    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(getActivity());

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       EventBus.getDefault().post(new MainUIActionEvent(View.GONE));
    }

    @OnClick(R.id.btnSignIn)
    public void onGoogleLoginClick(){
        EventBus.getDefault().post(new GoogleLoginEvent());
    }
}

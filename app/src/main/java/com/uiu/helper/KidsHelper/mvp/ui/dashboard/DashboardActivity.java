package com.uiu.helper.KidsHelper.mvp.ui.dashboard;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.google.gson.Gson;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.Icon;
import com.uiu.helper.KidsHelper.entities.UserEntity;
import com.uiu.helper.KidsHelper.mvp.BaseActivity;
import com.uiu.helper.KidsHelper.mvp.BaseFragment;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.Injection;
import com.uiu.helper.KidsHelper.mvp.events.NotificationReceiveEvent;
import com.uiu.helper.KidsHelper.mvp.events.ShareEvent;
import com.uiu.helper.KidsHelper.mvp.events.SlideEvent;
import com.uiu.helper.KidsHelper.mvp.model.Setting;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;
import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.KidsHelper.mvp.ui.settings.SettingActivity;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.R;
import com.uiu.helper.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.services.settings.SettingsData;


public class DashboardActivity extends BaseActivity implements BaseFragment.INavigation{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    DashboardFragment dashboardFragment;
    DashboardPresenter dashboardPresenter;
    User userEntity;

    @Override
    public int getID() {
        return R.layout.activity_dashboard;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        userEntity = (User) getIntent().getSerializableExtra(SELECTED_CONTACT);
        toolbar.findViewById(R.id.header_btn_right).setVisibility(View.VISIBLE);
        setToolBar(toolbar,userEntity.getName(),true);
        loadDashboardFragment();
    }

    private void loadDashboardFragment() {
        dashboardFragment = dashboardFragment != null ? dashboardFragment : DashboardFragment.newInstance();
        dashboardPresenter = dashboardPresenter != null ? dashboardPresenter : new DashboardPresenter(dashboardFragment, userEntity, PreferenceUtil.getInstance(this), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, dashboardFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
        if(receiveEvent.getNotificationForSlideType()== Constant.SLIDE_CREATE_INDEX) {
            JSONObject jsonObject = receiveEvent.getNotificationResponse();
            SlideItem slide = new Gson().fromJson(jsonObject.toString(), SlideItem.class);

            SlideEvent event = new SlideEvent(slide,true);
            EventBus.getDefault().postSticky(event);
        }
        if(receiveEvent.getNotificationForSlideType() == Constant.REQ_BATTERY_ALERT) {
            String message = getString(R.string.recharge_kid_battery_alert);
            Utils.showAlert(this, message, getResources().getColor(R.color.PrimaryColor));
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ShareEvent shareEvent) {
        if(!shareEvent.isShared())
        {
            showMediaNotification(shareEvent);
            return;
        }


    }


    public void showNotification(String title,String message,int status){
        new FancyAlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setBackgroundColor(Color.parseColor(status== ACCEPTED?"#378718":"#C82506"))  //Don't pass R.color.colorvalue
                .setPositiveBtnBackground(Color.parseColor("#2572D9"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("OK")
                .setNegativeBtnText("Cancel")
                .setAnimation(Animation.POP)
                .isCancellable(true)
                .setIcon(status==ACCEPTED?R.drawable.ic_done:R.drawable.ic_close, Icon.Visible)
                .OnNegativeClicked(() -> { })
                .build();
    }

    @OnClick(R.id.header_btn_left)
    public void onBackClick(){

        onBackPressed();
    }

    @OnClick(R.id.header_btn_right)
    public void onSettingClick(){
        loadKidSetting();
    }

    private void loadKidSetting() {
        Intent intent = new Intent();
        intent.setClass(this, SettingActivity.class);
        intent.putExtra(SELECTED_CONTACT,userEntity);
        startActivityForResult(intent,0);
    }
    @Override
    public void moveToSlide(int slideType) {

        int slideIndexToMove =dashboardFragment.pagerAdapter.getSlideIndex(slideType);
        dashboardFragment.fragmentPager.setCurrentItem(slideIndexToMove,true);
    }
}

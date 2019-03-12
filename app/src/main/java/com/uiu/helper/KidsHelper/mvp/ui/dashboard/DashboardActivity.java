package com.uiu.helper.KidsHelper.mvp.ui.dashboard;


import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.Icon;
import com.squareup.picasso.Picasso;
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
import com.uiu.helper.KidsHelper.mvp.ui.c_me.CmeeSelectorActivity;
import com.uiu.helper.KidsHelper.mvp.ui.c_me.VideoPlayBackView;
import com.uiu.helper.KidsHelper.mvp.ui.c_me.VideoPlaybackActivity;
import com.uiu.helper.KidsHelper.mvp.ui.c_me.history.CmeeHistoryActivity;
import com.uiu.helper.KidsHelper.mvp.ui.settings.SettingActivity;
import com.uiu.helper.KidsHelper.mvp.ui.slides.safe_places.ChooseSafePlaceFragment;
import com.uiu.helper.KidsHelper.mvp.ui.slides.safe_places.SafePlacesFragment;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.R;
import com.uiu.helper.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
    @BindView(R.id.tick)
    ImageView tick;
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
        toolbar.findViewById(R.id.header_btn_chat).setVisibility(View.VISIBLE);
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
    protected void onStart() {
        super.onStart();
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(onDownloadComplete);
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

    @OnClick(R.id.header_btn_chat)
    public void onChatHistoryClick(){
        Intent intent = new Intent(this, CmeeHistoryActivity.class);

        intent.putExtra("KidId",userEntity.getId());
        startActivityForResult(intent,0x101);
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

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ShareEvent shareEvent) {
        if(!shareEvent.isShared())
        {
            showMediaNotification(shareEvent);
            return;
        }
        tick.setVisibility(View.INVISIBLE);
        android.view.animation.Animation expandIn = AnimationUtils.loadAnimation(this, R.anim.pop_in_view);
        expandIn.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
            @Override
            public void onAnimationStart(android.view.animation.Animation animation) {

                tick.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                tick.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {

            }
        });
        tick.startAnimation(expandIn);

    }


    BroadcastReceiver onDownloadComplete=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            Bundle extras = intent.getExtras();
            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterById(extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
            Cursor c = manager.query(q);

            if (c.moveToFirst()) {
                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    // process download
                    final String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    final String mimeStr = c.getString(c.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));

                    String type = getMimeType(Uri.parse(uriString).getPath());

                    Log.i("Download URI: ",uriString);
                    if(type!=null){
                      /*  Log.i("Download MIME: ",type);
                        if(type.toLowerCase().startsWith("audio")){
                            showAudioNotification(uriString);
                        }

                        if(type.toLowerCase().startsWith("video")){
                            intent = new Intent(getApplicationContext(), VideoPlaybackActivity.class);
                            intent.putExtra("uri",uriString);
                            startActivity(intent);
                        }*/
                    }

                }
            }


        }
    };

    public void showMediaNotification( ShareEvent shareEvent){
        int type = shareEvent.getMediaType();

        int notificationHeader = R.drawable.notification_header_bg;
        int btnColor = R.color.notification_accepted;

        TextView tvMessage;
        SimpleDraweeView ivSender;
        ImageView image,ivMediaType;
        Button okBtn,btnReply;


        Dialog dialog = new Dialog(DashboardActivity.this,R.style.Theme_Dialog);
        dialog.setContentView(R.layout.notification_view_media);
        dialog.setCancelable(false);

        ivSender =  dialog.findViewById(R.id.ivSender);
        tvMessage =  dialog.findViewById(R.id.tvNotificationTitle);
        image = dialog.findViewById(R.id.image);
        ivMediaType = dialog.findViewById(R.id.ivMediaType);
        okBtn =  dialog.findViewById(R.id.btnOk);
        btnReply = dialog.findViewById(R.id.btnReply);

        btnColor = R.color.notification_rejected;
        okBtn.setText("Close");
        dialog.findViewById(R.id.header).setBackgroundResource(notificationHeader);
        LinearLayout container = dialog.findViewById(R.id.container);
        image.setVisibility(type==MEDIA_IMAGE?View.VISIBLE:View.GONE);
        container.setVisibility(type==MEDIA_VIDEO?View.VISIBLE:View.GONE);
        ivMediaType.setVisibility(type==MEDIA_IMAGE?View.GONE:View.VISIBLE);
        btnReply.setVisibility(View.VISIBLE);
        if(type==Constant.MEDIA_IMAGE) {

            Picasso.with(this).load(shareEvent.getFileUrl()).placeholder(R.drawable.placeholder_sqr).into(image);
        }
        else if(type == Constant.MEDIA_AUDIO) {
            container = dialog.findViewById(R.id.container_audio);
            container.setVisibility(View.VISIBLE);
            container.setBackgroundColor(ContextCompat.getColor(this,R.color.Gray));
            ivMediaType.setImageResource(R.drawable.ic_audio_small_black);

            showAudioNotification(shareEvent.getFileUrl(),dialog,container);

        }else if(type == Constant.MEDIA_VIDEO){
            ivMediaType.setImageResource(R.drawable.ic_video_camera);
            container.setBackgroundColor(ContextCompat.getColor(this,R.color.Black));
            LayoutInflater inflater = getLayoutInflater();
            VideoPlayBackView view = (VideoPlayBackView) inflater.inflate(R.layout.video_playback_view,null);
            view.findViewById(R.id.back).setVisibility(View.GONE);
            view.setDataSource(shareEvent.getFileUrl(),dialog);
            container.addView(view);
        }

        okBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),btnColor));
        tvMessage.setText(shareEvent.getTitle());
        ivSender.setImageURI(shareEvent.getSender().getSenderImage());

        dialog.show();


        btnReply.setOnClickListener(view -> {
            Intent intent = new Intent(this, CmeeSelectorActivity.class);
            String senderId = shareEvent.getSender().getId();
            intent.putExtra("SenderId",senderId);
            startActivityForResult(intent,0x001);
            dialog.dismiss();
        });


        okBtn.setBackgroundColor(ContextCompat.getColor(this,R.color.notification_rejected));
        okBtn.setOnClickListener(v -> {
            // if(type==MEDIA_AUDIO | type==MEDIA_VIDEO | type==MEDIA_IMAGE)
            //     UploadFileService.downloadMedia(getApplicationContext(), type, shareEvent.getFileUrl(), shareEvent.getCreatedAt());

            if(okBtn.getText().equals("Close")){
                dialog.dismiss();
                return;
            }
        });

    }
}

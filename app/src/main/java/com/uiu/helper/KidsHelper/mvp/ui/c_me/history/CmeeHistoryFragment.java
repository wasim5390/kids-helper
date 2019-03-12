package com.uiu.helper.KidsHelper.mvp.ui.c_me.history;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.uiu.helper.KidsHelper.mvp.BaseFragment;
import com.uiu.helper.KidsHelper.mvp.events.ShareEvent;
import com.uiu.helper.KidsHelper.mvp.model.CmeeChatModel;
import com.uiu.helper.KidsHelper.mvp.model.File;
import com.uiu.helper.KidsHelper.mvp.ui.c_me.AudioPlaybackActivity;
import com.uiu.helper.KidsHelper.mvp.ui.c_me.CmeeAudioActivity;
import com.uiu.helper.KidsHelper.mvp.ui.c_me.VideoPlaybackActivity;
import com.uiu.helper.KidsHelper.mvp.ui.camera.CustomCameraActivity;
import com.uiu.helper.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CmeeHistoryFragment extends BaseFragment implements CmeeHistoryContract.View, CmeeHistoryAdapter.Callback {

    private static final int REQ_CAMERA = 0x10;
    private static final int REQ_AUDIO = 0x01;
    @BindView(R.id.rv_chat_history)
    public RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ContentLoadingProgressBar progressBar;
    public CmeeHistoryContract.Presenter presenter;
    public CmeeHistoryAdapter adapter;

    public static CmeeHistoryFragment newInstance()
    {
        Bundle args=new Bundle();
        CmeeHistoryFragment instance=new CmeeHistoryFragment();
        instance.setArguments(args);
        return instance;
    }

    @Override
    public int getID() {
        return R.layout.fragment_chat_history;
    }

    @Override
    public void initUI(View view) {
        EventBus.getDefault().register(this);
        ButterKnife.bind(getActivity());
        setAdapter();
        presenter.start();
    }

    private void setAdapter() {
        adapter = new CmeeHistoryAdapter(getContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setPresenter(CmeeHistoryContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void showNoInternet() {

    }
    @Override
    public void showProgress() {
        progressBar.show();
    }

    @Override
    public void hideProgress() {
        progressBar.hide();
    }


    @Override
    public void onHistoryLoaded(CmeeChatModel data) {
        adapter.setSlideItems(data.getFiles());
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(mBaseActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAudioItemClick(File mediaFile) {
        Intent intent = new Intent(getActivity(), AudioPlaybackActivity.class);
        intent.putExtra("uri",mediaFile.getFileUrl());
        startActivity(intent);
    }

    @Override
    public void onVideoItemClick(File mediaFile) {
        Intent intent = new Intent(getActivity(), VideoPlaybackActivity.class);
        intent.putExtra("uri",mediaFile.getFileUrl());
        startActivity(intent);
    }

    @OnClick({R.id.btnAudio,R.id.btnVideo,R.id.btnCamera})
    public void onRecordMediaClick(View view){
        switch (view.getId()){
            case R.id.btnAudio:
                gotoAudio();
                break;
            case R.id.btnVideo:
                gotoCamera(true);
                break;
            case R.id.btnCamera:
                gotoCamera(false);
                break;
        }
    }

    private void gotoCamera(boolean cameraType) {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(getActivity(), CustomCameraActivity.class);
            intent.putExtra(key_camera_type,cameraType);
            startActivityForResult(intent, REQ_CAMERA);
        }, 1);

    }


    private void gotoAudio() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(getActivity(), CmeeAudioActivity.class);
            startActivityForResult(intent, REQ_AUDIO);
        }, 1);

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ShareEvent shareEvent) {
        if (shareEvent.isShared()) {
            presenter.fetchCmeeHistory();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

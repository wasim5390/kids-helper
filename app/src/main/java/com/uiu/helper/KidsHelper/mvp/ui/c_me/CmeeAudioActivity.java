package com.uiu.helper.KidsHelper.mvp.ui.c_me;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.uiu.helper.KidsHelper.mvp.BaseActivity;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.ui.Chronometer;
import com.uiu.helper.KidsHelper.mvp.ui.media_notification.UploadFileService;
import com.uiu.helper.KidsHelper.mvp.ui.share.ShareActivity;
import com.uiu.helper.KidsHelper.mvp.util.PermissionUtil;
import com.uiu.helper.R;
import com.uiu.helper.util.Utils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class CmeeAudioActivity extends BaseActivity {

    public static final String TAG = CmeeAudioActivity.class.getSimpleName();

    @BindView(R.id.toggleAudio)
    ToggleButton btnAudio;

    @BindView(R.id.chronometer)
    Chronometer timer;

    private String filePath;
    private String senderId;
    private MediaRecorder recorder;

    @Override
    public int getID() {
        return R.layout.activity_cmee_audio;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        if(getIntent().hasExtra("SenderId"))
            senderId = getIntent().getStringExtra("SenderId");
        timer.setBase(SystemClock.elapsedRealtime());

    }

    @OnCheckedChanged(R.id.toggleAudio)
    public void onToggleClick(CompoundButton button, boolean checked){
        if(checked) {
            filePath=null;
            recordAudio();

        }
        else {
            timer.stop();
            stopRecording(recorder);

        }
    }




    private void recordAudio() {
        PermissionUtil.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                new PermissionUtil.PermissionCallback() {
                    @Override
                    public void onPermissionsGranted(String permission) {

                    }

                    @Override
                    public void onPermissionsGranted() {
                        startRecording();
                    }

                    @Override
                    public void onPermissionDenied() {
                        Toast.makeText(CmeeAudioActivity.this, "Permissions required to record reminder", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void startRecording() {

        filePath= Utils.createAudioFile(this).getAbsolutePath();
        if(recorder == null)
            recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(filePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setMaxDuration(10*12000);

        try
        {
            timer.setBase(SystemClock.elapsedRealtime());
            timer.start();
            recorder.prepare();
            recorder.start();

            recorder.setOnInfoListener((mr, what, extra) -> {
                if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                    new Handler().postDelayed(() -> onToggleClick(btnAudio,false),50);
                }
            });
        }
        catch (IllegalStateException e)
        {
            timer.stop();
            e.printStackTrace();
        } catch (IOException e)
        {
            timer.stop();
            e.printStackTrace();
        }
    }

    public void stopRecording(MediaRecorder recorder1) {

        if(null != recorder)
        {
            try {
                recorder.stop();
            }catch (Exception e){

            }finally {
                timer.stop();
                recorder.reset();
                recorder.release();
                recorder =null;
                if(senderId!=null){

                    UploadFileService.uploadMedia(getApplicationContext(),filePath,MEDIA_AUDIO,senderId);
                    setResult(Activity.RESULT_OK);
                    finish();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(this, ShareActivity.class);
                intent.putExtra(Constant.RECORDED_FILE_TYPE,MEDIA_AUDIO);
                intent.putExtra(RECORDED_FILE_PATH,filePath);
                startActivityForResult(intent,0);

            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            setResult(RESULT_OK);
            finish();
        }
    }
}

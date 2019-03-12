package com.uiu.helper.KidsHelper.mvp.ui.c_me;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;


import com.uiu.helper.KidsHelper.mvp.BaseActivity;

import com.uiu.helper.R;
import com.uiu.helper.util.Utils;

import java.io.IOException;

import androidx.core.widget.ContentLoadingProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class AudioPlaybackActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener{

    @BindView(R.id.progressBar)
    ContentLoadingProgressBar progressBar;
    @BindView(R.id.media_seekbar)
    SeekBar seekBar;

    @BindView(R.id.run_time)
    TextView elapsedTime;

    @BindView(R.id.total_time)
    TextView endTime;

    @BindView(R.id.playIV)
    CheckBox togglePlay;

    MediaPlayer mediaPlayer ;
    AudioManager audioManager ;
     int originalVolume ;
    boolean inLoop = false;

    private Handler mHandler = new Handler();
    @Override
    public int getID() {
        return R.layout.activity_audio_playback;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mediaPlayer = new MediaPlayer();

        seekBar.setOnSeekBarChangeListener(this);
        mediaPlayer.setOnCompletionListener(mp -> {
            togglePlay.setChecked(false);
            elapsedTime.setText(Utils.milliSecondsToTimer(0));
            seekBar.setProgress(0);

        });

        mediaPlayer.setOnPreparedListener(mp -> {
            endTime.setText(Utils.milliSecondsToTimer(mp.getDuration()));
            elapsedTime.setText(Utils.milliSecondsToTimer(mp.getCurrentPosition()));
            progressBar.hide();
            togglePlay.setChecked(true);
        });

        if(!getIntent().hasExtra("uri"))
            finish();
        progressBar.show();
        Uri uri = Uri.parse(getIntent().getStringExtra("uri"));
        setDataSource(uri,false);
    }

    @Override
    protected void onDestroy() {
        stopMediaPlayer();
        super.onDestroy();
    }

    public void stopMediaPlayer(){
        if(mediaPlayer!=null)
        {
            mHandler.removeCallbacks(updateTimeTask);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
            updateTimeTask = null;
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }

    public void setDataSource(Uri uri, boolean isLoop){
        try {
            this.inLoop = isLoop;
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, uri);
            mediaPlayer.prepareAsync();

            togglePlay.setChecked(true);
            mediaPlayer.setLooping(inLoop);



            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                    0);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnCheckedChanged(R.id.playIV)
    public void onPlayToggle(CompoundButton button, boolean checked){
        if(checked)
            playAudio();
        else
            pauseAudio();

    }

    @OnClick(R.id.back)
    public void onBack(){
        finish();
    }


    private void playAudio() {

        mediaPlayer.start();
        // Updating progress bar
        updateProgressBar();
    }

    private void pauseAudio(){
        mediaPlayer.pause();
        mHandler.removeCallbacks(updateTimeTask);

    }


    private void updateProgressBar() {
        mHandler.postDelayed(updateTimeTask, 100);
    }

    private Runnable updateTimeTask = new Runnable() {
        public void run() {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            seekBar.setMax(mediaPlayer.getDuration());
            elapsedTime.setText(Utils.milliSecondsToTimer(mediaPlayer.getCurrentPosition()));
            mHandler.postDelayed(this, 100);
        }
    };


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekbar) {
        mHandler.removeCallbacks(updateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekbar) {
        mHandler.removeCallbacks(updateTimeTask);
        mediaPlayer.seekTo(seekbar.getProgress());
        updateProgressBar();
    }
}

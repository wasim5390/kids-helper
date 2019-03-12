package com.uiu.helper.KidsHelper.mvp.ui.c_me;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;


import com.uiu.helper.R;
import com.uiu.helper.util.Utils;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ContentLoadingProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class VideoPlayBackView extends ConstraintLayout implements SeekBar.OnSeekBarChangeListener,CustomVideoView.PlayPauseListener {

    @BindView(R.id.seekBar2)
    SeekBar seekBar;

    @BindView(R.id.elapsedTime)
    TextView elapsedTime;

    @BindView(R.id.endTime)
    TextView endTime;

    @BindView(R.id.play_toggle_left)
    CheckBox togglePlay;

    @BindView(R.id.videoView)
    VideoView videoView;

    @BindView(R.id.progressBar)
    ContentLoadingProgressBar progressBar;

    private Handler mHandler = new Handler();

    public VideoPlayBackView(Context context) {
        super(context);
    }

    public VideoPlayBackView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoPlayBackView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this,this);
        findViewById(R.id.play_toggle).setVisibility(GONE);
        seekBar.setOnSeekBarChangeListener(this);
       // videoView.setPlayPauseListener(this);
        videoView.setOnPreparedListener(mp -> {
            togglePlay.setChecked(true);
            endTime.setText(Utils.milliSecondsToTimer(mp.getDuration()));
            elapsedTime.setText(Utils.milliSecondsToTimer(mp.getCurrentPosition()));
            progressBar.hide();
        });

        seekBar.setOnSeekBarChangeListener(this);
        videoView.setOnCompletionListener(mp -> {
            togglePlay.setChecked(false);
            elapsedTime.setText(Utils.milliSecondsToTimer(0));
            seekBar.setProgress(0);
        });
        togglePlay.setChecked(false);


    }

    public void setDataSource(String uri, Dialog dialog){
        if(uri==null) {
            dialog.dismiss();
            return;
        }
        progressBar.show();
       // Uri uri = Uri.parse(getIntent().getStringExtra("uri"));
        videoView.setVideoURI(Uri.parse(uri));

        dialog.setOnDismissListener(dialogInterface -> {
            mHandler.removeCallbacks(updateTimeTask);
            videoView.stopPlayback();
        });

    }


    @OnCheckedChanged(R.id.play_toggle_left)
    public void onPlayToggle(CompoundButton button, boolean checked){
        if(checked)
            playVideo();
        else
            pauseVideo();

    }

    private void playVideo() {
        videoView.start();
        // Updating progress bar
        updateProgressBar();


    }

    private void pauseVideo(){
        videoView.pause();
        mHandler.removeCallbacks(updateTimeTask);

    }

    private void updateProgressBar() {
        mHandler.postDelayed(updateTimeTask, 100);
    }

    private Runnable updateTimeTask = new Runnable() {
        public void run() {
            seekBar.setProgress(videoView.getCurrentPosition());
            seekBar.setMax(videoView.getDuration());
            elapsedTime.setText(Utils.milliSecondsToTimer(videoView.getCurrentPosition()));
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
        videoView.seekTo(seekbar.getProgress());
        updateProgressBar();

    }

    @Override
    public void onPlay() {
        updateProgressBar();
    }

    @Override
    public void onPause() {

        mHandler.removeCallbacks(updateTimeTask);

    }

}

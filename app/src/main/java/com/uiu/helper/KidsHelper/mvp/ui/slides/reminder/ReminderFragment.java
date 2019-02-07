package com.uiu.helper.KidsHelper.mvp.ui.slides.reminder;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.uiu.helper.KidsHelper.mvp.BaseFragment;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.events.NotificationReceiveEvent;
import com.uiu.helper.KidsHelper.mvp.events.SlideCreateEvent;
import com.uiu.helper.KidsHelper.mvp.events.SlideEvent;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;
import com.uiu.helper.KidsHelper.mvp.util.PermissionUtil;
import com.uiu.helper.R;

import com.uiu.helper.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ReminderFragment extends BaseFragment implements ReminderContract.View, ReminderAdapterList.Callback {


    public ReminderContract.Presenter presenter;
    public ReminderAdapterList adapter;

    @BindView(R.id.rvReminder)
    public RecyclerView recyclerView;
    @BindView(R.id.reminderText)
    public TextView tvReminder;

    private static final String PARAM_TITLE = "title";
    private static final String PARAM_DATE = "date";
    private static final String PARAM_TIME = "time";
    private static final String PARAM_REPEAT = "repeat";
    private static final String PARAM_NOTE = "note";
    private String TAG = "ReminderSlideFragment";
    String dateString, timeString;
    private RelativeLayout rlCalendar, rlTime;
    private ImageView ivMic;
    private Button btnCancel,btnSave;
    private boolean isRecording;
    public Dialog dialog;
    private String saveMode = "Creat";
    String filePath;
    public EditText etTitle;
    public TextView tvTitle,tvTapToRecord;
    public TextView tvDate;
    public TextView tvTime;

    @BindView(R.id.btnRemove)
    TextView tvRemove;
    private boolean isEditorEnabled=false;

    Calendar myCalendar = Calendar.getInstance();
    MediaRecorder recorder;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION_CODE = 1;


    public static ReminderFragment newInstance() {
        Bundle args = new Bundle();
        ReminderFragment instance = new ReminderFragment();
        instance.setArguments(args);
        return instance;

    }

    @Override
    public int getID() {
        return R.layout.fragment_reminder;
    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(getActivity());
        EventBus.getDefault().register(this);
        setAdapter();
        if(presenter!=null)
        presenter.start();

    }


    private void setAdapter() {
        adapter = new ReminderAdapterList(getContext(), new ArrayList<>(), this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onLoadedReminderList(List<ReminderEntity> list) {
        if (!list.isEmpty())
        {
            tvReminder.setVisibility(View.GONE);
        }
        else {
            tvReminder.setVisibility(View.VISIBLE);
        }
        adapter.setSlideItems(list);
    }

    @Override
    public void showMessage(String msg) {
        if(getActivity()!=null)
        Utils.showAlert(getActivity(),msg, R.color.contacts_bg);
    }

    @Override
    public void slideSerial(int serial,int count) {
        serial++;
        String pageNum = serial+"/"+count;
        ((TextView)getView().findViewById(R.id.tvReminderTitle)).setText(getString(R.string.home_title_reminder)+" ("+pageNum+")");
    }

    @Override
    public void onSlideCreated(SlideItem slide) {
        EventBus.getDefault().post(new SlideEvent(slide,true));
    }

    @Override
    public void onSlideRemoved(SlideItem slide) {
        EventBus.getDefault().post(new SlideEvent(slide,false));
    }

    @Override
    public void onNewSlideCreated(SlideItem newSlide) {

    }

    @Override
    public void itemAddedOnNewSlide(SlideItem newSlide) {
        EventBus.getDefault().postSticky(new SlideCreateEvent(newSlide));

    }

    @Override
    public void setPresenter(ReminderContract.Presenter presenter) {

        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Override
    public void onSlideItemClick(ReminderEntity slideItem) {

        new Handler().postDelayed(() -> {
            if (slideItem.getId() == null) {
                showAlarmDialog();
            }else {
            presenter.removeReminderFromSlide(slideItem);
            }

        }, 1);
    }

    public void showAlarmDialog() {
        long milli =0;
        isRecording=false;
        filePath=null;
        recorder = null;
        dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        dialog.setContentView(R.layout.helper_dialog_create_reminder);
        rlCalendar = (RelativeLayout) dialog.findViewById(R.id.rlDate);
        rlTime = (RelativeLayout) dialog.findViewById(R.id.rlTime);
        tvTitle = (TextView) dialog.findViewById(R.id.textView);
        tvDate = (TextView) dialog.findViewById(R.id.tvDate);
        ivMic = (ImageView) dialog.findViewById(R.id.ivMic);
        tvTime = (TextView) dialog.findViewById(R.id.tvTime);
        etTitle = (EditText) dialog.findViewById(R.id.etTitle);
        btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnSave = (Button) dialog.findViewById(R.id.btnSave);
        tvTapToRecord = (TextView) dialog.findViewById(R.id.tvTapToRecord);
        recorder = new MediaRecorder();
        long mDate = System.currentTimeMillis();
        SimpleDateFormat sdfd = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdft= new SimpleDateFormat("hh:mm aa");

        if (milli == 0) {
            dateString = sdfd.format(mDate);
            timeString = sdft.format(mDate);
            myCalendar.setTimeInMillis(mDate);
        }else {
            dateString = sdfd.format(milli);
            timeString = sdft.format(milli);
            myCalendar.setTimeInMillis(milli);
        }
        tvDate.setText(dateString);

        tvTime.setText(timeString);
        etTitle.setText("");
        btnSave.setVisibility(filePath==null?View.GONE:View.VISIBLE);
        tvTapToRecord.setText(!isRecording?getString(R.string.tap_to_record_notes):getString(R.string.tap_to_stop_recording));
        setLisenter();
        dialog.show();

    }

    public void startRecording() {
        isRecording = true;
        ivMic.setImageResource(R.drawable.xicon);
        tvTapToRecord.setText(!isRecording?getString(R.string.tap_to_record_notes):getString(R.string.tap_to_stop_recording));
        filePath= Utils.createAudioFile();
        if(recorder == null)
            recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(filePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setMaxDuration(10*6000);

        try
        {

            recorder.prepare();
            recorder.start();

            recorder.setOnInfoListener((mr, what, extra) -> {
                if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                    new Handler().postDelayed(() -> stopRecording(recorder),50);
                }
            });
        }
        catch (IllegalStateException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
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
                recorder.reset();
                recorder.release();
                recorder =null;
            }

        }
        isRecording=false;
        tvTapToRecord.setText(!isRecording?getString(R.string.tap_to_record_notes):getString(R.string.tap_to_stop_recording));
        ivMic.setImageResource(R.drawable.ic_music);
        btnSave.setVisibility(filePath==null?View.GONE:View.VISIBLE);

    }


    private void setLisenter() {

        rlCalendar.setOnClickListener(v -> {
            if (getActivity()!=null) {
                Utils.hidekeyPad(getActivity(), etTitle);
            }
            new DatePickerDialog(getActivity(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        rlTime.setOnClickListener(v -> {
            if (getActivity()!=null) {
                Utils.hidekeyPad(getActivity(), etTitle);
            }
            selectTime();
        });

        btnSave.setOnClickListener(v -> {

            saveReminder();

            if (getActivity()!=null) {
                Utils.showkeyPad(getActivity(), btnSave);
            }
        });

        ivMic.setOnClickListener(v -> {

            if(!isRecording)
                recordReminderNotes();
            else
                stopRecording(recorder);
        });


        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
            if (getActivity()!=null) {
                Utils.showkeyPad(getActivity(), btnCancel);
            }
        });
    }

    private void recordReminderNotes() {
        PermissionUtil.requestPermissions(getActivity(),
                new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionUtil.PermissionCallback() {
                    @Override
                    public void onPermissionsGranted(String permission) {

                    }

                    @Override
                    public void onPermissionsGranted() {
                        startRecording();
                    }

                    @Override
                    public void onPermissionDenied() {
                        Toast.makeText(mBaseActivity, "Permissions required to record reminder", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void selectTime(){

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), (timePicker, selectedHour, selectedMinute) -> {

            // Calendar datetime = Calendar.getInstance();
            myCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
            myCalendar.set(Calendar.MINUTE, selectedMinute);

            String myFormat = "hh:mm a"; // your own format
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
            String  formattedTime = sdf.format(myCalendar.getTime()); //format your time

            tvTime.setText( formattedTime);
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }
    DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
        // TODO Auto-generated method stub
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    };

    private void updateLabel() {

        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        tvDate.setText(sdf.format(myCalendar.getTime()));
    }


    private void saveReminder(){
        if (etTitle.getText().toString().isEmpty()){
            Utils.showAlert(getActivity(),
                    getResources().getString(R.string.add_a_title_reminder)
                    ,getResources().getColor(R.color.reminder_slide_title_color));
            return;
        }
        if(filePath==null)
        {
            Utils.showAlert(getActivity(),
                    "Please record reminder to send"
                    ,getResources().getColor(R.color.reminder_slide_title_color));
            return;
        }
        File note = new File(filePath);
        if(note.length()==0) {
            Utils.showAlert(getActivity(),
                    "Please record reminder to send"
                    ,getResources().getColor(R.color.reminder_slide_title_color));
            return;

        }else
        {
            ReminderEntity newReminder = new ReminderEntity();
            newReminder.setTitle(etTitle.getText().toString());
            newReminder.setDate(tvDate.getText().toString());
            newReminder.setTime(String.valueOf(myCalendar.getTimeInMillis()));
           // newReminder.setReminderNote(Utils.audioFileToBase64(note));
            //newReminder.setIs_repeated(swRepeat.isChecked());
            presenter.saveReminder(newReminder,note);
            dialog.dismiss();
        }

    }



    @OnClick(R.id.btnAddNew)
    public void addNewSlide(){
        showAddDialog("Reminder", false,new IAddOnSlideListener() {
            @Override
            public void onAddSlideClick() {
                SlideItem slideItem = new SlideItem();
                slideItem.setName(getString(R.string.home_title_reminder));
                slideItem.setType(SLIDE_INDEX_REMINDERS);

                presenter.createNewSlide(slideItem);
            }

            @Override
            public void onAddItemClick() {
                if(presenter.canAddOnSlide()) {
                    showAlarmDialog();
                }
            }
        });
        // startActivityForResult(new Intent(getContext(), AppsActivity.class), REQ_APPS);



    }

    @OnClick(R.id.btnRemove)
    public void onRemoveClick()
    {
        if(adapter.inEditMode()) {
            isEditorEnabled=false;
            adapter.setEditMode(isEditorEnabled);
            tvRemove.setText("Remove");
            return;
        }

        showRemoveDialog("Reminder",presenter.isLastSlide(),new IRemoveOnSlideListener() {
            @Override
            public void onRemoveSlideClick() {
                String title = getString(R.string.do_you_want_to_remove_the_slide);
                String body = getString(R.string.remove_slide_alert_desc);
                showDeleteFromSlideDialog(title, body, null, null, (slideId, itemId) -> {
                    presenter.removeSlide();
                });

            }

            @Override
            public void onRemoveItemClick() {
                isEditorEnabled=true;
                tvRemove.setText("Done");
                adapter.setEditMode(isEditorEnabled);
            }

            @Override
            public void onDismiss() {
                isEditorEnabled=false;
                tvRemove.setText("Remove");
                adapter.setEditMode(isEditorEnabled);
            }
        });

    }











    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Unregister");
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
        if(receiveEvent.getNotificationForSlideType()== Constant.SLIDE_INDEX_REMINDERS){
            if(presenter!=null)
                presenter.start();

        }
    }
}

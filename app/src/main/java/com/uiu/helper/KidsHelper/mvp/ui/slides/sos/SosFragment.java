package com.uiu.helper.KidsHelper.mvp.ui.slides.sos;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.uiu.helper.KidsHelper.mvp.BaseFragment;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.events.NotificationReceiveEvent;
import com.uiu.helper.KidsHelper.mvp.events.SlideCreateEvent;
import com.uiu.helper.KidsHelper.mvp.events.SlideEvent;
import com.uiu.helper.KidsHelper.mvp.model.ContactEntity;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;
import com.uiu.helper.KidsHelper.mvp.ui.dashboard.contact.ContactActivity;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.R;

import com.uiu.helper.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;


import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


public class SosFragment extends BaseFragment implements SosContract.View,
        SosListAdapter.Callback {

    private static final String TAG = "SosFragment";
    private static final int REQ_CONTACT = 0x090;
    private SosContract.Presenter presenter;
    private SosListAdapter adapter;
    private List<ContactEntity> entityList;

    @BindView(R.id.rvFavApps)
    public RecyclerView recyclerView;

    @BindView(R.id.tvFavAppsTitle)
    TextView title;

    @BindView(R.id.btnRemove)
    TextView tvRemove;

    private boolean isEditorEnabled=false;

    public static SosFragment newInstance() {
        Bundle args = new Bundle();
        SosFragment instance = new SosFragment();
        instance.setArguments(args);
        return instance;
    }

    @Override
    public int getID() {
        return R.layout.fragment_favorite_slides;
    }


    @Override
    public void initUI(View view) {

        EventBus.getDefault().register(this);
        title.setText(getString(R.string.home_title_sos));
        title.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.sos_slide_title_color));
        setAdapter();
        if(presenter!=null)
        presenter.start();

    }


    private void setAdapter() {
        adapter = new SosListAdapter(getContext(), this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setPresenter(SosContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Override
    public void onSOSListLoaded(List<ContactEntity> sosList) {
        this.entityList = sosList;
        adapter.setSlideItems(sosList);

    }

    @Override
    public void onSosAdded(ContactEntity entity) {

    }

    @Override
    public void slideSerial(int serial,int count) {
        serial++;
        String pageNum = serial+"/"+count;
        ((TextView)getView().findViewById(R.id.tvFavAppsTitle)).setText(getString(R.string.home_title_sos)+" ("+pageNum+")");
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
    public void showMessage(String msg) {
        if(getActivity()!=null)
        Utils.showAlert(getActivity(),msg, ContextCompat.getColor(getContext(),R.color.contacts_bg));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CONTACT) {
            if (resultCode == RESULT_OK) {
                ContactEntity entity = (ContactEntity) data.getSerializableExtra(KEY_SELECTED_CONTACT);
                Uri uri = Uri.parse(entity.getPhotoUri());


                AssetFileDescriptor fd = null;
                try {
                    fd = getContext().getContentResolver().openAssetFileDescriptor(uri, "r");
                    InputStream inputStream = fd.createInputStream();
                    BufferedInputStream buf =new BufferedInputStream(inputStream);
                    Bitmap my_btmp = BitmapFactory.decodeStream(buf);
                    entity.setBase64ProfilePic(Utils.bitmapToBase64(my_btmp));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                presenter.saveSos(entity);
            }
        }

    }

    @Override
    public void onSlideItemClick(ContactEntity slideItem) {
        new Handler().postDelayed(() -> {

            if(slideItem.getId()==null){
                startActivityForResult(new Intent(getContext(), ContactActivity.class), REQ_CONTACT);
            }else {
                slideItem.setUserId(PreferenceUtil.getInstance(getActivity()).getAccount().getId());
                presenter.removeFavoriteSosFromSlide(slideItem);
            }
        }, 1);
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
        if (receiveEvent.getNotificationForSlideType() == Constant.SLIDE_INDEX_SOS) {
            if(presenter!=null)
                presenter.start();

        }

    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Unregister");
        EventBus.getDefault().unregister(this);
    }


    @OnClick(R.id.btnAddNew)
    public void addNewSlide(){

        showAddDialog("SOS", false,new IAddOnSlideListener() {
            @Override
            public void onAddSlideClick() {
                SlideItem slideItem = new SlideItem();
                slideItem.setName(getString(R.string.home_title_sos));
                slideItem.setType(SLIDE_INDEX_SOS);

                presenter.createNewSlide(slideItem);
            }

            @Override
            public void onAddItemClick() {
                if(presenter.canAddOnSlide()) {
                    startActivityForResult(new Intent(getContext(), ContactActivity.class), REQ_CONTACT);
                }
            }
        });
    }

    @OnClick(R.id.btnRemove)
    public void removeSlide(){

        if(adapter.inEditMode()) {
            isEditorEnabled=false;
            adapter.setEditMode(isEditorEnabled);
            tvRemove.setText("Remove");
            return;
        }

        showRemoveDialog("SOS",presenter.isLastSlide(),new IRemoveOnSlideListener() {
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
}



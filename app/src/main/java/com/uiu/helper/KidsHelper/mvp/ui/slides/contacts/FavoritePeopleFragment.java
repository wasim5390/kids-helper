package com.uiu.helper.KidsHelper.mvp.ui.slides.contacts;


import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

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

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class FavoritePeopleFragment extends BaseFragment implements FavoritePeopleAdapter.Callback,
        FavoritePeopleContract.View
{
    private static final int REQ_CONTACT = 0x101;
    public static String TAG ="FavoritePeopleFragment";

    @BindView(R.id.rvFavApps)
    RecyclerView recyclerView;
    @BindView(R.id.tvFavAppsTitle)
    TextView title;

    @BindView(R.id.btnRemove)
    TextView tvRemove;

    private boolean isEditorEnabled=false;

    private FavoritePeopleAdapter adapter;
    FavoritePeopleContract.Presenter mPresenter;

    public static FavoritePeopleFragment newInstance() {
        Bundle args = new Bundle();
        FavoritePeopleFragment homeFragment = new FavoritePeopleFragment();
        homeFragment.setArguments(args);
        return homeFragment;
    }

    @Override
    public int getID() {
        return R.layout.fragment_favorite_slides;
    }

    @Override
    public void initUI(View view) {
        EventBus.getDefault().register(this);
        title.setText(getString(R.string.favorite_ppl));
        title.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.people_slide_title_color));
        setRecyclerView();
        if(mPresenter!=null)
            mPresenter.start();
    }
    public void setRecyclerView(){
        adapter = new FavoritePeopleAdapter(getContext(),this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2, RecyclerView.VERTICAL,false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Unregister");
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSlideItemClick(ContactEntity slideItem) {
        new Handler().postDelayed(() -> {
            if (slideItem.getId() == null) {
                Intent intent = new Intent(getContext(), ContactActivity.class);
                intent.putExtra(Constant.SELECTED_CONTACT, mPresenter.getSelectedUser());
                startActivityForResult(intent, REQ_CONTACT);
            }else {
                if(slideItem.getRegisterId().equals( PreferenceUtil.getInstance(getActivity()).getAccount().getId())
                        ) {
                    showMessage("Unable to remove this contact");
                    return;
                }
                    slideItem.setUserId(PreferenceUtil.getInstance(getActivity()).getAccount().getId());
                    mPresenter.removeFavoritePeople(slideItem);
            }
        },1);

    }

    @Override
    public void slideSerial(int serial,int count) {
        serial++;
        String pageNum = serial+"/"+count;
        ((TextView)getView().findViewById(R.id.tvFavAppsTitle)).setText(getString(R.string.favorite_people)+" ("+pageNum+")");
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
    public void showMessage(String message) {

        Utils.showAlert(getActivity(),message, ContextCompat.getColor(getContext(),R.color.contacts_bg));
    }

    @Override
    public void onFavoritePeopleLoaded(List<ContactEntity> list) {
        adapter.setSlideItems(list);
    }

    @Override
    public void onFavoritePeopleRemoved(ContactEntity entity) {

    }


    @Override
    public void setPresenter(FavoritePeopleContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
        if(receiveEvent.getNotificationForSlideType()==Constant.SLIDE_INDEX_FAV_PEOPLE){
            if(mPresenter!=null)
                mPresenter.start();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQ_CONTACT){
            if(resultCode==RESULT_OK){
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

                mPresenter.saveFavoritePeople(entity, PreferenceUtil.getInstance(getContext()).getAccount().getId());

            }
        }
    }


    @OnClick(R.id.btnAddNew)
    public void addNewSlide(){
        showAddDialog("Contact", false,new IAddOnSlideListener() {
            @Override
            public void onAddSlideClick() {
                SlideItem slideItem = new SlideItem();
                slideItem.setName(getString(R.string.favorite_people));
                slideItem.setType(SLIDE_INDEX_FAV_PEOPLE);

                mPresenter.createNewSlide(slideItem);
            }

            @Override
            public void onAddItemClick() {
                if(mPresenter.canAddOnSlide()) {
                    Intent intent = new Intent(getContext(), ContactActivity.class);
                    intent.putExtra(Constant.SELECTED_CONTACT, mPresenter.getSelectedUser());
                    startActivityForResult(intent, REQ_CONTACT);
                }
            }
        });

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

        showRemoveDialog("Contact",mPresenter.isLastSlide(),new IRemoveOnSlideListener() {
            @Override
            public void onRemoveSlideClick() {
                String title = getString(R.string.do_you_want_to_remove_the_slide);
                String body = getString(R.string.remove_slide_alert_desc);
                showDeleteFromSlideDialog(title, body, null, null, (slideId, itemId) -> {
                    mPresenter.removeSlide();
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

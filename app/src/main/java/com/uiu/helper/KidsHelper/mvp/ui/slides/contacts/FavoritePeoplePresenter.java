package com.uiu.helper.KidsHelper.mvp.ui.slides.contacts;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.util.Log;


import com.uiu.helper.KidsHelper.entities.SlideEntity;

import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.model.ContactEntity;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;
import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.KidsHelper.mvp.model.response.BaseResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetAllSlidesResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetFavContactResponse;
import com.uiu.helper.KidsHelper.mvp.source.DataSource;
import com.uiu.helper.KidsHelper.mvp.source.Repository;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.util.Utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.internal.Util;

import static com.uiu.helper.KidsHelper.mvp.Constant.FAV_PEOPLE_SLIDE_NAME;


public class FavoritePeoplePresenter implements FavoritePeopleContract.Presenter {

    private Repository mRepository;

    private User user;
    private FavoritePeopleContract.View mView;
    private List<ContactEntity> mFavList;
    private List<ContactEntity> mDataList;
    private SlideItem slideEntity;
    private boolean isItemAdded=false;
    private PreferenceUtil preferenceUtil;
    private static final String TAG = "FavoritePeoplePresenter";

    public FavoritePeoplePresenter(FavoritePeopleContract.View view, SlideItem slide, PreferenceUtil preferenceUtil, User user, Repository repository) {
        this.mRepository = repository;
        this.user = user;
        this.preferenceUtil = preferenceUtil;
        this.slideEntity = slide;
        this.mView = view;
        this.mView.setPresenter(this);
    }



    @Override
    public void loadFavoritePeoples(String slideId) {
        mRepository.getContactsFromSlide(slideId,new DataSource.GetDataCallback<GetFavContactResponse>() {

            @Override
            public void onDataReceived(GetFavContactResponse data) {

                if(data.isSuccess()) {
                    mDataList.clear();
                    mDataList.addAll(data.getContactEntityList());
                    mFavList.clear();
                    mFavList.addAll(mDataList);
                    for(int i=0;i<4-mDataList.size();i++)
                        mFavList.add(new ContactEntity());
                    mView.onFavoritePeopleLoaded(mFavList);
                }else{
                    mView.showMessage(data.getResponseMsg());
                }


            }

            @Override
            public void onFailed(int code, String message) {
                Log.i(TAG,"ContactEntity-onDataReceived1"+ message);
                Log.d(TAG, "onFailed: ");
            }
        });
    }

    @Override
    public void removeFavoritePeople(ContactEntity entity) {
        mView.showProgress();
        entity.setRequestStatus(Constant.ACCEPTED);
        mRepository.removeFavContactFromSlide(entity.getId(), preferenceUtil.getAccount().getId(), new DataSource.GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                mView.hideProgress();
                if(getUser().isPrimary())
                mDataList.remove(entity);
                else
                    mView.showMessage("Your request has been sent for approval");
                mFavList.clear();
                mFavList.addAll(mDataList);
                for(int i=0;i<4-mDataList.size();i++)
                    mFavList.add(new ContactEntity());

                mView.onFavoritePeopleLoaded(mFavList);
            }

            @Override
            public void onFailed(int code, String message) {
                mView.hideProgress();
                mView.showMessage(message);
            }
        });

    }

    @Override
    public boolean canAddOnSlide(){
        if(!isLastSlideOfType() && mDataList.size()>=4)
        {
            mView.showMessage(Constant.NO_SPACE_ON_SLIDE);
            return false;
        }
        return true;
    }

    @Override
    public void saveFavoritePeople(ContactEntity entity, String userId) {

        entity.setUserId(userId);
        entity.setName(entity.getName());
        entity.setSlide_id(slideEntity.getId());
        entity.setRequestStatus(Constant.ACCEPTED);
        if(isLastSlideOfType() && mDataList.size()>=4){
            addNewSlide(entity);
            return;
        }else{
            saveFavPeopleOnSlide(entity,null);
        }



    }

    public void saveFavPeopleOnSlide(ContactEntity contact, SlideItem newSlide)
    {
        mView.showProgress();
        mRepository.addFavPeopleToSlide(contact.getSlide_id(),contact,new DataSource.GetDataCallback<GetFavContactResponse>() {
            @Override
            public void onDataReceived(GetFavContactResponse data) {
                mView.hideProgress();
                if(data.isSuccess()) {
                    if(!getUser().isPrimary())
                        mView.showMessage("Your request has been sent for approval");
                    mDataList.add(data.getFavoriteContact());
                    mFavList.clear();
                    mFavList.addAll(mDataList);
                    for(int i=0;i<4-mDataList.size();i++)
                        mFavList.add(new ContactEntity());

                        if(newSlide!=null)
                            mView.itemAddedOnNewSlide(newSlide);
                    mView.onFavoritePeopleLoaded(mFavList);
                }else
                    mView.showMessage(data.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                mView.hideProgress();
                mView.showMessage(message);
                Log.i(TAG,"ContactEntity-onDataReceived1"+ message);
                Log.d(TAG, "onFailed: ");
            }
        });
    }

    @Override
    public User getSelectedUser() {
        return user;
    }


    @Override
    public void createNewSlide(SlideItem slide) {
        slide.setUser_id(user.getId());
        slide.setHelperId(preferenceUtil.getAccount().getId());
        HashMap<String, Object> params = new HashMap<>();
        params.put("slide",slide);
        mRepository.addNewSlide(params, new DataSource.GetDataCallback<GetAllSlidesResponse>() {
            @Override
            public void onDataReceived(GetAllSlidesResponse data) {
               // if(getUser().isPrimary())
               //     mView.showMessage("Your request has been sent for approval");
                    mView.onSlideCreated(data.getNewSlide());


            }

            @Override
            public void onFailed(int code, String message) {
                mView.showMessage(message);
            }
        });
    }

    public void addNewSlide(ContactEntity contactEntity){

        SlideItem newSlide = new SlideItem();
        newSlide.setHelperId(preferenceUtil.getAccount().getId());
        newSlide.setUser_id(user.getId());
        newSlide.setType(Constant.SLIDE_INDEX_FAV_PEOPLE);
        newSlide.setName(FAV_PEOPLE_SLIDE_NAME);
        HashMap<String, Object> params = new HashMap<>();
        params.put("slide",newSlide);
        mRepository.addNewSlide(params, new DataSource.GetDataCallback<GetAllSlidesResponse>() {
            @Override
            public void onDataReceived(GetAllSlidesResponse data) {
                if(data.isSuccess()){
                    mView.onNewSlideCreated(data.getNewSlide());
                    contactEntity.setSlide_id(data.getNewSlide().getId());
                    saveFavPeopleOnSlide(contactEntity,data.getNewSlide());
                }else
                    mView.showMessage(data.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {

            }
        });
    }

    @Override
    public void removeSlide() {
        mRepository.removeSlide(slideEntity.getId(),preferenceUtil.getAccount().getId(), new DataSource.GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if(getUser().isPrimary())
                mView.onSlideRemoved(slideEntity);
                else
                    mView.showMessage("Your request has been sent for approval");
            }

            @Override
            public void onFailed(int code, String message) {
                mView.showMessage(message);
            }
        });
    }

    @Override
    public void start() {

        mFavList = new ArrayList<>();
        mDataList = new ArrayList<>();
        ContactEntity entity = new ContactEntity();
        mFavList.add(entity);
        mFavList.add(entity);
        mFavList.add(entity);
        mFavList.add(entity);

        mView.onFavoritePeopleLoaded(mFavList);
        loadFavoritePeoples(slideEntity.getId());
        mView.slideSerial(slideEntity.getSerial(),slideEntity.getCount());
    }

    public boolean isLastSlide(){

        return slideEntity.getCount()==1;
    }

    public boolean isLastSlideOfType(){
        Integer actualSerial = slideEntity.getSerial()+1;
        return actualSerial>=slideEntity.getCount();
    }

    public User getUser() {
        return user;
    }
}

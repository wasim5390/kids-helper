package com.uiu.helper.KidsHelper.mvp.ui.slides.sos;


import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.model.ContactEntity;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;
import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.KidsHelper.mvp.model.response.BaseResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetAllSlidesResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetSosResponse;
import com.uiu.helper.KidsHelper.mvp.source.DataSource;
import com.uiu.helper.KidsHelper.mvp.source.Repository;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SosPresenter implements SosContract.Presenter {

    public SosContract.View view;
    public Repository repository;
    public SlideItem slideItem;
    private List<ContactEntity> mFavList;
    private List<ContactEntity> mDataList;
    public User user;
    private PreferenceUtil preferenceUtil;


    @Override
    public void start() {
        mFavList = new ArrayList<>();
        mDataList =  new ArrayList<>();
        ContactEntity entity = new ContactEntity();
        mFavList.add(entity);
        mFavList.add(entity);
        mFavList.add(entity);
        mFavList.add(entity);
        loadSosList();
        view.slideSerial(slideItem.getSerial(),slideItem.getCount());
    }


    public SosPresenter(SosContract.View view, SlideItem slideItem, PreferenceUtil preferenceUtil, User User, Repository repository) {
        this.repository = repository;
        this.slideItem = slideItem;
        this.preferenceUtil = preferenceUtil;
        this.user = User;
        this.view = view;
        this.view.setPresenter(this);

    }

    @Override
    public void loadSosList() {

        repository.getSosFromSlide(slideItem.getId(), new DataSource.GetDataCallback<GetSosResponse>() {
            @Override
            public void onDataReceived(GetSosResponse data) {

                if(data.isSuccess()){
                    mDataList.clear();
                    mDataList.addAll(data.getContactEntityList());
                    mFavList.clear();
                    mFavList.addAll(mDataList);
                    for(int i=0;i<4-mDataList.size();i++)
                        mFavList.add(new ContactEntity());
                    view.onSOSListLoaded(mFavList);
                }else{
                    view.showMessage(data.getResponseMsg());
                }

            }

            @Override
            public void onFailed(int code, String message) {

                view.showMessage(message);

            }
        });

    }
    @Override
    public boolean canAddOnSlide(){
        if(!isLastSlideOfType() && mDataList.size()>=4)
        {
            view.showMessage(Constant.NO_SPACE_ON_SLIDE);
            return false;
        }
        return true;
    }
    @Override
    public void saveSos(ContactEntity sos) {

        sos.setSlide_id(slideItem.getId());
        sos.setUserId(preferenceUtil.getAccount().getId());
        sos.setName(sos.getName());
        sos.setRequestStatus(Constant.ACCEPTED);

        if(isLastSlideOfType() && mDataList.size()>=4){
            addNewSlide(sos);
            return;
        }else{
            saveSosOnSlide(sos,null);
        }

    }

    private void saveSosOnSlide(ContactEntity sos, SlideItem newSlide) {
        view.showProgress();
        HashMap<String,Object> params = new HashMap<>();
        params.put("sos",sos);
        repository.addSosOnSlide(params, new DataSource.GetDataCallback<GetSosResponse>() {
            @Override
            public void onDataReceived(GetSosResponse data) {
                view.hideProgress();
                if(data.isSuccess()) {
                    if(!user.isPrimary())
                        view.showMessage("Your request has been sent for approval");
                    mDataList.add(data.getContactEntity());
                    mFavList.clear();
                    mFavList.addAll(mDataList);
                    for(int i=0;i<4-mDataList.size();i++)
                        mFavList.add(new ContactEntity());

                    if(newSlide!=null)
                        view.itemAddedOnNewSlide(newSlide);
                    view.onSOSListLoaded(mFavList);
                }else
                    view.showMessage(data.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                view.hideProgress();
                view.showMessage(message);
            }
        });
    }

    @Override
    public void removeFavoriteSosFromSlide(ContactEntity contactEntity) {
        repository.removeSosFromSlide(contactEntity.getId(), preferenceUtil.getAccount().getId(), new DataSource.GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                view.hideProgress();
                if(user.isPrimary()) {
                    mDataList.remove(contactEntity);
                    view.showMessage(response.getResponseMsg());
                }else{
                    view.showMessage("Your request has been sent for approval");
                }
                mFavList.clear();
                mFavList.addAll(mDataList);
                for(int i=0;i<4-mDataList.size();i++)
                    mFavList.add(new ContactEntity());
                view.onSOSListLoaded(mFavList);

            }

            @Override
            public void onFailed(int code, String message) {
                view.hideProgress();
                view.showMessage(message);
            }
        });
    }
    @Override
    public void createNewSlide(SlideItem slide) {
        slide.setUser_id(user.getId());
        slide.setHelperId(preferenceUtil.getAccount().getId());
        HashMap<String, Object> params = new HashMap<>();
        params.put("slide",slide);
        repository.addNewSlide(params, new DataSource.GetDataCallback<GetAllSlidesResponse>() {
            @Override
            public void onDataReceived(GetAllSlidesResponse data) {
                view.onSlideCreated(data.getNewSlide());
            }

            @Override
            public void onFailed(int code, String message) {
                view.showMessage(message);
            }
        });
    }

    public void addNewSlide(ContactEntity contactEntity){

        SlideItem newSlide = new SlideItem();
        newSlide.setHelperId(preferenceUtil.getAccount().getId());
        newSlide.setUser_id(user.getId());
        newSlide.setType(Constant.SLIDE_INDEX_SOS);
        newSlide.setName("SOS");
        HashMap<String, Object> params = new HashMap<>();
        params.put("slide",newSlide);
        repository.addNewSlide(params, new DataSource.GetDataCallback<GetAllSlidesResponse>() {
            @Override
            public void onDataReceived(GetAllSlidesResponse data) {
                if(data.isSuccess()){
                    view.onNewSlideCreated(data.getNewSlide());
                    contactEntity.setSlide_id(data.getNewSlide().getId());
                    saveSosOnSlide(contactEntity,data.getNewSlide());
                }else
                    view.showMessage(data.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {

            }
        });
    }



    @Override
    public void removeSlide() {
        repository.removeSlide(slideItem.getId(),preferenceUtil.getAccount().getId(), new DataSource.GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if(user.isPrimary())
                view.onSlideRemoved(slideItem);
                else
                    view.showMessage("Your request has been sent for approval");
            }

            @Override
            public void onFailed(int code, String message) {
                view.showMessage(message);
            }
        });
    }

    public boolean isLastSlide(){

        return slideItem.getCount()==1;
    }

    public boolean isLastSlideOfType(){
        Integer actualSerial = slideItem.getSerial()+1;
        return actualSerial>=slideItem.getCount();
    }

}
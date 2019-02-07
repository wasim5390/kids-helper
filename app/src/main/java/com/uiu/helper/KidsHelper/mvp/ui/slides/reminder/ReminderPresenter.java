package com.uiu.helper.KidsHelper.mvp.ui.slides.reminder;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;
import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.KidsHelper.mvp.model.response.BaseResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetAllSlidesResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetReminderResponse;
import com.uiu.helper.KidsHelper.mvp.source.DataSource;
import com.uiu.helper.KidsHelper.mvp.source.Repository;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.uiu.helper.KidsHelper.mvp.Constant.FAV_PEOPLE_SLIDE_NAME;

public class ReminderPresenter implements ReminderContract.Presenter {
    public ReminderContract.View view;
    public User user;
    public SlideItem slideItem;
    public Repository repository;
    public List<ReminderEntity> mFavList;
    public List<ReminderEntity> mDataList;
    private PreferenceUtil preferenceUtil;

    public ReminderPresenter(ReminderContract.View view, SlideItem slideItem,PreferenceUtil preferenceUtil, User user, Repository repository) {
        this.repository = repository;
        this.slideItem = slideItem;
        this.preferenceUtil = preferenceUtil;
        this.user = user;
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        mFavList = new ArrayList<>();
        mDataList = new ArrayList<>();
        ReminderEntity entity = new ReminderEntity();
        mFavList.add(entity);
        mFavList.add(entity);
        mFavList.add(entity);
        mFavList.add(entity);
        onLoadReminderList();
        view.slideSerial(slideItem.getSerial(),slideItem.getCount());
        view.onLoadedReminderList(mFavList);
    }

    @Override
    public void onLoadReminderList() {

        repository.getReminderList(slideItem.getId(), new DataSource.GetDataCallback<GetReminderResponse>() {
            @Override
            public void onDataReceived(GetReminderResponse data) {
                if(data.isSuccess()) {
                    mDataList.clear();
                    mDataList.addAll(data.getReminders());
                    mFavList.clear();
                    mFavList.addAll(mDataList);
                    for(int i=0;i<4-mDataList.size();i++)
                        mFavList.add(new ReminderEntity());
                    view.onLoadedReminderList(mFavList);
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
    public void saveReminder(ReminderEntity entity,File file) {



        entity.setSlide_id(slideItem.getId());
        entity.setHelper_id(preferenceUtil.getAccount().getId());
        entity.setUser_id(user.getId());

        if(isLastSlideOfType() && mDataList.size()>=4){
            addNewSlide(entity,file);
            return;
        }else{
            saveReminderOnSlide(entity,file,null);
        }

    }

    @Override
    public void removeReminderFromSlide(ReminderEntity reminder) {
        view.showProgress();
        repository.deleteReminderFromSlide(reminder.getId(),preferenceUtil.getAccount().getId(), new DataSource.GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                view.hideProgress();
                if(user.isPrimary()) {
                    mDataList.remove(reminder);
                    view.showMessage(response.getResponseMsg());
                }else
                    view.showMessage("Your request has been sent for approval");
                mFavList.clear();
                mFavList.addAll(mDataList);
                for(int i=0;i<4-mDataList.size();i++)
                    mFavList.add(new ReminderEntity());

                view.onLoadedReminderList(mFavList);

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
    public void saveReminderOnSlide(ReminderEntity reminderEntity,File file, SlideItem newSlide)
    {
        RequestBody fBody = RequestBody.create(MediaType.parse("audio/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), fBody);

        view.showProgress();
        repository.addReminderToSlide(body,reminderEntity, new DataSource.GetDataCallback<GetReminderResponse>() {
            @Override
            public void onDataReceived(GetReminderResponse response) {
                view.hideProgress();
                if(response.isSuccess()) {
                    if(!user.isPrimary())
                        view.showMessage("Your request has been sent for approval");
                    mDataList.add(response.getReminder());
                    mFavList.clear();
                    mFavList.addAll(mDataList);
                    for(int i=0;i<4-mDataList.size();i++)
                        mFavList.add(new ReminderEntity());

                    if(newSlide!=null)
                        view.itemAddedOnNewSlide(newSlide);
                    view.onLoadedReminderList(mFavList);
                }else
                    view.showMessage(response.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                view.showMessage(message);
            }
        });
    }
    public void addNewSlide(ReminderEntity reminderEntity,File file){

        SlideItem newSlide = new SlideItem();
        newSlide.setHelperId(preferenceUtil.getAccount().getId());
        newSlide.setUser_id(user.getId());
        newSlide.setType(Constant.SLIDE_INDEX_REMINDERS);
        newSlide.setName("Reminders");
        HashMap<String, Object> params = new HashMap<>();
        params.put("slide",newSlide);
        repository.addNewSlide(params, new DataSource.GetDataCallback<GetAllSlidesResponse>() {
            @Override
            public void onDataReceived(GetAllSlidesResponse data) {
                if(data.isSuccess()){
                    view.onNewSlideCreated(data.getNewSlide());
                    reminderEntity.setSlide_id(data.getNewSlide().getId());
                    saveReminderOnSlide(reminderEntity,file,data.getNewSlide());
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
        repository.removeSlide(slideItem.getId(), preferenceUtil.getAccount().getId(),new DataSource.GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                view.onSlideRemoved(slideItem);
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

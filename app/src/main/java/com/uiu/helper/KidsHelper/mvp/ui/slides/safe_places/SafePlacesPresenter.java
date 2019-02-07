package com.uiu.helper.KidsHelper.mvp.ui.slides.safe_places;

import android.util.Log;

import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;
import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.KidsHelper.mvp.model.response.BaseResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetAllSlidesResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetDirectionsResponse;
import com.uiu.helper.KidsHelper.mvp.source.DataSource;
import com.uiu.helper.KidsHelper.mvp.source.Repository;
import com.uiu.helper.KidsHelper.mvp.ui.tracker.DirectionsItem;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.uiu.helper.KidsHelper.mvp.Constant.DIRECTION_SLIDE_NAME;


public class SafePlacesPresenter implements SafePlacesContract.Presenter {

    private Repository mRepository;

    private User user;
    private SafePlacesContract.View mView;
    private List<DirectionsItem> mFavList;
    private List<DirectionsItem> mDataList;
    private SlideItem slideEntity;
    private boolean isItemAdded=false;
    private PreferenceUtil preferenceUtil;
    private static final String TAG = "SafePlacesPresenter";

    public SafePlacesPresenter(SafePlacesContract.View view, SlideItem slide, PreferenceUtil preferenceUtil, User user, Repository repository) {
        this.mRepository = repository;
        this.user = user;
        this.preferenceUtil = preferenceUtil;
        this.slideEntity = slide;
        this.mView = view;
        this.mView.setPresenter(this);
    }



    @Override
    public void loadDirections(String slideId) {
        mRepository.getDirectionsSlide(slideId,new DataSource.GetDataCallback<GetDirectionsResponse>() {

            @Override
            public void onDataReceived(GetDirectionsResponse data) {

                if(data.isSuccess()) {
                    mDataList.clear();
                    mDataList.addAll(data.getDirectionsList());
                    mFavList.clear();
                    mFavList.addAll(mDataList);
                    for(int i=0;i<4-mDataList.size();i++)
                        mFavList.add(new DirectionsItem());
                    mView.onDirectionsLoaded(mFavList,mDataList);
                }else{
                    mView.showMessage(data.getResponseMsg());
                }


            }

            @Override
            public void onFailed(int code, String message) {
                Log.i(TAG,"DirectionsItem-onDataReceived1"+ message);
                Log.d(TAG, "onFailed: ");
            }
        });
    }

    @Override
    public void removeDirection(DirectionsItem entity) {
        mView.showProgress();
        entity.setRequestStatus(Constant.ACCEPTED);
        mRepository.removeDirectionFromSlide(entity.getId(), preferenceUtil.getAccount().getId(), new DataSource.GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                mView.hideProgress();
                if(user.isPrimary())
                mDataList.remove(entity);
                else
                    mView.showMessage("Your request is sent for approval");
                mFavList.clear();
                mFavList.addAll(mDataList);
                for(int i=0;i<4-mDataList.size();i++)
                    mFavList.add(new DirectionsItem());

                mView.onDirectionsLoaded(mFavList,mDataList);
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
    public void saveDirection(DirectionsItem entity) {

        entity.setUserId(user.getId());
        entity.setHelperId(preferenceUtil.getAccount().getId());
        entity.setSlideId(slideEntity.getId());
        entity.setRequestStatus(Constant.ACCEPTED);
        if(isLastSlideOfType() && mDataList.size()>=4){
            addNewSlide(entity);
            return;
        }else{
            saveDirectionOnSlide(entity,null);
        }



    }

    public void saveDirectionOnSlide(DirectionsItem directionItem, SlideItem newSlide)
    {
        mView.showProgress();
        HashMap<String,Object> params = new HashMap<>();
        params.put("direction",directionItem);
        mRepository.addDirectionToSlide(params,new DataSource.GetDataCallback<GetDirectionsResponse>() {
            @Override
            public void onDataReceived(GetDirectionsResponse data) {
                mView.hideProgress();
                if(data.isSuccess()) {
                    if(!user.isPrimary())
                        mView.showMessage("Your request is sent for approval");
                    mDataList.add(data.getDirection());
                    mFavList.clear();
                    mFavList.addAll(mDataList);
                    for(int i=0;i<4-mDataList.size();i++)
                        mFavList.add(new DirectionsItem());

                    if(newSlide!=null)
                        mView.itemAddedOnNewSlide(newSlide);
                    mView.onDirectionsLoaded(mFavList,mDataList);
                }else
                    mView.showMessage(data.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                mView.hideProgress();
                mView.showMessage(message);
                Log.i(TAG,"DirectionsItem-onDataReceived1"+ message);
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
                mView.onSlideCreated(data.getNewSlide());
            }

            @Override
            public void onFailed(int code, String message) {
                mView.showMessage(message);
            }
        });
    }

    public void addNewSlide(DirectionsItem DirectionsItem){

        SlideItem newSlide = new SlideItem();
        newSlide.setHelperId(preferenceUtil.getAccount().getId());
        newSlide.setUser_id(user.getId());
        newSlide.setType(Constant.SLIDE_INDEX_SAFE_PLACES);
        newSlide.setName(DIRECTION_SLIDE_NAME);
        HashMap<String, Object> params = new HashMap<>();
        params.put("slide",newSlide);
        mRepository.addNewSlide(params, new DataSource.GetDataCallback<GetAllSlidesResponse>() {
            @Override
            public void onDataReceived(GetAllSlidesResponse data) {
                if(data.isSuccess()){
                    mView.onNewSlideCreated(data.getNewSlide());
                    DirectionsItem.setSlideId(data.getNewSlide().getId());
                    saveDirectionOnSlide(DirectionsItem,data.getNewSlide());
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
                if(user.isPrimary())
                mView.onSlideRemoved(slideEntity);
                else
                    mView.showMessage("Your request is sent for approval");
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
        DirectionsItem entity = new DirectionsItem();
        mFavList.add(entity);
        mFavList.add(entity);
        mFavList.add(entity);
        mFavList.add(entity);
        mView.onDirectionsLoaded(mFavList,mDataList);
        loadDirections(slideEntity.getId());
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

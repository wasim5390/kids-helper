package com.uiu.helper.KidsHelper.mvp.ui.slides.applications;


import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.model.AppsEntity;
import com.uiu.helper.KidsHelper.mvp.model.ContactEntity;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;
import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.KidsHelper.mvp.model.response.BaseResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetAllSlidesResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetFavAppsResponse;
import com.uiu.helper.KidsHelper.mvp.source.DataSource;
import com.uiu.helper.KidsHelper.mvp.source.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.uiu.helper.KidsHelper.mvp.Constant.FAV_APP_SLIDE_NAME;
import static com.uiu.helper.KidsHelper.mvp.Constant.FAV_PEOPLE_SLIDE_NAME;

public class FavoriteAppsPresenter implements FavoriteAppContract.Presenter{

    private FavoriteAppContract.View view;
    private Repository repository;
    private User user;
    private SlideItem slideItem;
    private List<AppsEntity> mFavList;
    private List<AppsEntity> mDataList;
    private boolean isAddedItem=false;
    private String helperId;

    public FavoriteAppsPresenter(FavoriteAppContract.View view, SlideItem slideItem,String helperId,User user,
                                 Repository repository) {
        this.repository = repository;
        this.slideItem = slideItem;
        this.helperId = helperId;
        this.user=user;
        this.mFavList = new ArrayList<>();
        this.mDataList = new ArrayList<>();
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        AppsEntity appsEntity = new AppsEntity(null,null);
        appsEntity.setFlagEmptylist(true);
        mFavList.add(appsEntity);
        mFavList.add(appsEntity);
        mFavList.add(appsEntity);
        mFavList.add(appsEntity);
        view.onFavoriteAppsLoaded(mFavList);
        view.slideSerial(slideItem.getSerial(),slideItem.getCount());
        loadFavApps();
    }


    @Override
    public void loadFavApps() {
        repository.getFavApps(slideItem.getId(), new DataSource.GetDataCallback<GetFavAppsResponse>() {
            @Override
            public void onDataReceived(GetFavAppsResponse data) {
                if(data.isSuccess()){
                    mDataList.clear();
                    mDataList.addAll(data.getFavAppsList());
                    mFavList.clear();
                    mFavList.addAll(mDataList);
                    for(int i=0;i<4-mDataList.size();i++)
                        mFavList.add(new AppsEntity(null,null));
                    view.onFavoriteAppsLoaded(mFavList);
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
    public void removeEntity(AppsEntity entity) {
        view.showProgress();
        repository.removeFavAppFromSlide(entity.getId(),helperId, new DataSource.GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                view.hideProgress();
                if(user.isPrimary())
                    mDataList.remove(entity);
                else
                    view.showMessage("Your request has been sent for approval");
                mFavList.clear();
                mFavList.addAll(mDataList);
                for(int i=0;i<4-mDataList.size();i++)
                    mFavList.add(new AppsEntity(null,null));

                view.onFavoriteAppsLoaded(mFavList);
              //  view.showMessage(response.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                view.hideProgress();
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
    public void saveFavoriteApp(AppsEntity entity) {

            entity.setSlideId(slideItem.getId());
            entity.setUserId(helperId);
            entity.setRequestStatus(Constant.ACCEPTED);

        if(isLastSlideOfType() && mDataList.size()>=4){
            addNewSlide(entity);
            return;
        }else{
            saveFavAppOnSlide(entity,null);
        }

    }

    private void saveFavAppOnSlide(AppsEntity entity, SlideItem newSlide) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("application",entity);
        view.showProgress();
        repository.addFavAppToSlide(param, new DataSource.GetDataCallback<GetFavAppsResponse>() {
            @Override
            public void onDataReceived(GetFavAppsResponse data) {
                view.hideProgress();
                if(data.isSuccess())
                {
                    if(!user.isPrimary())
                        view.showMessage("Your request has been sent for approval");
                    mDataList.add(data.getAppsEntity());
                    mFavList.clear();
                    mFavList.addAll(mDataList);
                    for(int i=0;i<4-mDataList.size();i++)
                        mFavList.add(new AppsEntity(null,null));

                    if(newSlide!=null)
                        view.itemAddedOnNewSlide(newSlide);
                    view.onFavoriteAppsLoaded(mFavList);
                }
                else {
                    view.showMessage(data.getResponseMsg());
                }

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
    slide.setHelperId(helperId);
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

    public void addNewSlide(AppsEntity appsEntity){

        SlideItem newSlide = new SlideItem();
        newSlide.setHelperId(helperId);
        newSlide.setUser_id(user.getId());
        newSlide.setType(Constant.SLIDE_INDEX_FAV_APP);
        newSlide.setName(FAV_APP_SLIDE_NAME);
        HashMap<String, Object> params = new HashMap<>();
        params.put("slide",newSlide);
        repository.addNewSlide(params, new DataSource.GetDataCallback<GetAllSlidesResponse>() {
            @Override
            public void onDataReceived(GetAllSlidesResponse data) {
                if(data.isSuccess()){
                    view.onNewSlideCreated(data.getNewSlide());
                    appsEntity.setSlideId(data.getNewSlide().getId());
                    saveFavAppOnSlide(appsEntity,data.getNewSlide());
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
        repository.removeSlide(slideItem.getId(),helperId, new DataSource.GetResponseCallback<BaseResponse>() {
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

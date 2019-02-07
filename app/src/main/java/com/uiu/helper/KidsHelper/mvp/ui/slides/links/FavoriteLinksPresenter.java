package com.uiu.helper.KidsHelper.mvp.ui.slides.links;


import android.net.Uri;
import android.util.Log;

import com.uiu.helper.CompanionApp;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.model.ContactEntity;
import com.uiu.helper.KidsHelper.mvp.model.LinksEntity;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;
import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.KidsHelper.mvp.model.response.BaseResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetAllSlidesResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetFavLinkIconResponce;
import com.uiu.helper.KidsHelper.mvp.model.response.GetFavLinkResponse;
import com.uiu.helper.KidsHelper.mvp.source.DataSource;
import com.uiu.helper.KidsHelper.mvp.source.Repository;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.ponnamkarthik.richlinkpreview.MetaData;
import io.github.ponnamkarthik.richlinkpreview.ResponseListener;
import io.github.ponnamkarthik.richlinkpreview.RichPreview;

import static com.uiu.helper.KidsHelper.mvp.Constant.ACCEPTED;
import static com.uiu.helper.KidsHelper.mvp.Constant.FAV_LINK_SLIDE_NAME;
import static com.uiu.helper.KidsHelper.mvp.Constant.FAV_PEOPLE_SLIDE_NAME;

public class FavoriteLinksPresenter implements FavoriteLinksContract.Presenter {

    public FavoriteLinksContract.View view;
    public SlideItem slideItem;
    public User user;
    public Repository repository;
    public Uri uri;
    public boolean isLinkItemAdded = false;
    private PreferenceUtil preferenceUtil;
    public List<LinksEntity> mFavLinkList;
    public List<LinksEntity> mDataList;


    public FavoriteLinksPresenter(FavoriteLinksContract.View view, SlideItem slideItem,PreferenceUtil preferenceUtil, User entity, Repository repository) {
        this.repository = repository;
        this.slideItem = slideItem;
        this.user = entity;
        this.preferenceUtil = preferenceUtil;
        this.mFavLinkList = new ArrayList<>();
        this.mDataList = new ArrayList<>();
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        LinksEntity linksEntity = new LinksEntity(null, null,null,null);
        linksEntity.setFlagEmptylist(true);
        mFavLinkList.add(linksEntity);
       /* mFavLinkList.add(linksEntity);
        mFavLinkList.add(linksEntity);
        mFavLinkList.add(linksEntity);*/
        view.onFavoriteLinksLoaded(mFavLinkList);
        view.slideSerial(slideItem.getSerial(),slideItem.getCount());
        loadFavLinks();
    }

    @Override
    public void loadFavLinks() {

        repository.getFavLinks(slideItem.getId(), new DataSource.GetDataCallback<GetFavLinkResponse>() {
            @Override
            public void onDataReceived(GetFavLinkResponse data) {
                if (data.isSuccess()) {
                    mDataList.clear();
                    mDataList.addAll(data.getFavLinkList());
                    mFavLinkList.clear();
                    mFavLinkList.addAll(mDataList);
                    if(mFavLinkList.size()<4)
                        mFavLinkList.add(new LinksEntity());
                    view.onFavoriteLinksLoaded(mFavLinkList);
                } else {
                    view.onFavoriteLinksLoaded(mFavLinkList);

                }
            }

            @Override
            public void onFailed(int code, String message) {

                view.showMassage(message);
            }
        });
    }

    @Override
    public void removeFavLinkFromSlide(LinksEntity linksEntity) {
        view.showProgress();
        repository.removeFavLinkFromSlide(linksEntity.getId(), preferenceUtil.getAccount().getId(), new DataSource.GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                view.hideProgress();

                if(user.isPrimary()) {
                    mDataList.remove(linksEntity);
                    view.showMassage(response.getResponseMsg());
                }
                else
                    view.showMassage("Your request has been sent for approval");

                mFavLinkList.clear();
                mFavLinkList.addAll(mDataList);
                if(mFavLinkList.size()<4)
                    mFavLinkList.add(new LinksEntity());
                view.onFavoriteLinksLoaded(mFavLinkList);

            }

            @Override
            public void onFailed(int code, String message) {
                view.hideProgress();
                view.showMassage(message);
            }
        });
    }


    @Override
    public void updateFavLink(LinksEntity favLink) {
        for(LinksEntity linksEntity: mFavLinkList){
            if(linksEntity.getId()!=null && linksEntity.getId().equals(favLink.getId())){
                linksEntity.setRequestStatus(favLink.getRequestStatus());
                break;
            }
        }
        view.onFavoriteLinksLoaded(mFavLinkList);
    }



    @Override
    public void getFavLinkData(String link) {
        isLinkItemAdded = true;
        uri = Uri.parse("https://" + link.toString() + "/favicon.ico");
        Log.e("uri", String.valueOf(uri));

        for (int i = 0; i < mFavLinkList.size(); i++) {

            if (link.equalsIgnoreCase(mFavLinkList.get(i).getShort_url())) {
                isLinkItemAdded = false;
            }
        }
        if (isLinkItemAdded) {
            String prefix = "http";
            if(!link.startsWith(prefix))
                link = "http://"+link;
            getIcon(link);

        } else {
            view.showMassage("You have already add this link");
        }

    }


    private void getIcon(String link) {
        view.showProgress();
        RichPreview richPreview = new RichPreview(new ResponseListener() {
            @Override
            public void onData(MetaData metaData) {
                String imageUrl = metaData.getImageurl();
                String favIcon = metaData.getFavicon();
                if(imageUrl==null || imageUrl.isEmpty())
                    imageUrl = favIcon;
                //Implement your Layout
                LinksEntity linksEntity = composeLinkEntity(metaData.getUrl(), metaData.getTitle(), imageUrl,metaData.getDescription());
                view.onFavoriteLinkDataLoaded(metaData.getUrl(),linksEntity);
            }

            @Override
            public void onError(Exception e) {
                //handle error
                view.onFavoriteLinkDataLoaded(link,null);

            }
        });
        richPreview.getPreview(link);


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
                view.showMassage(message);
            }
        });
    }

    @Override
    public void removeSlide() {
        repository.removeSlide(slideItem.getId(), preferenceUtil.getAccount().getId(),new DataSource.GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if(user.isPrimary())
                view.onSlideRemoved(slideItem);
                else
                    view.showMassage("Your request has been sent for approval");
            }

            @Override
            public void onFailed(int code, String message) {
                view.showMassage(message);
            }
        });
    }

    @Override
    public boolean canAddOnSlide(){
        if(!isLastSlideOfType() && mDataList.size()>=4)
        {
            view.showMassage(Constant.NO_SPACE_ON_SLIDE);
            return false;
        }
        return true;
    }

    @Override
    public void addFavLinkOnSlide(LinksEntity entity) {

        entity.setUser_id(preferenceUtil.getAccount().getId());
        entity.setSlide_id(slideItem.getId());
        entity.setRequestStatus(ACCEPTED);
        if(isLastSlideOfType() && mDataList.size()>=4){
            addNewSlide(entity);
            return;
        }else{
            saveLinkOnSlide(entity,null);
        }

    }

    private void saveLinkOnSlide(LinksEntity entity, SlideItem newSlide) {
        if(!Utils.isNetworkAvailable(CompanionApp.getContext())) {
            view.showNoInternet();
            return;
        }
        HashMap<String,Object> params = new HashMap<>();
        params.put("link",entity);

        repository.addFavLinkToSlide(params, new DataSource.GetDataCallback<GetFavLinkResponse>() {
            @Override
            public void onDataReceived(GetFavLinkResponse data) {
                view.hideProgress();
                if(data.isSuccess()) {
                    if(!user.isPrimary())
                        view.showMassage("Your request has been sent for approval");
                    mDataList.add(data.getLinkEntity());
                    mFavLinkList.clear();
                    mFavLinkList.addAll(mDataList);
                    if(mFavLinkList.size()<4)
                        mFavLinkList.add(new LinksEntity());

                    if(newSlide!=null)
                        view.itemAddedOnNewSlide(newSlide);
                    view.onFavoriteLinksLoaded(mFavLinkList);
                }else{
                    view.showMassage(data.getResponseMsg());
                }

            }
            @Override
            public void onFailed(int code, String message) {
                view.hideProgress();
                view.showMassage(message);

            }
        });
    }

    public void addNewSlide(LinksEntity linksEntity){

        SlideItem newSlide = new SlideItem();
        newSlide.setHelperId(preferenceUtil.getAccount().getId());
        newSlide.setUser_id(user.getId());
        newSlide.setType(Constant.SLIDE_INDEX_FAV_LINKS);
        newSlide.setName(FAV_LINK_SLIDE_NAME);
        HashMap<String, Object> params = new HashMap<>();
        params.put("slide",newSlide);
        repository.addNewSlide(params, new DataSource.GetDataCallback<GetAllSlidesResponse>() {
            @Override
            public void onDataReceived(GetAllSlidesResponse data) {
                if(data.isSuccess()){
                    view.onNewSlideCreated(data.getNewSlide());
                    linksEntity.setSlide_id(data.getNewSlide().getId());
                    saveLinkOnSlide(linksEntity,data.getNewSlide());
                }else
                    view.showMassage(data.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {

            }
        });
    }


    private LinksEntity composeLinkEntity(String link,String title, String imageUrl, String description){
        LinksEntity nodeEntity = new LinksEntity(link,title,description, imageUrl);
        nodeEntity.setShort_url(link);
        nodeEntity.setRequestStatus(1);
        nodeEntity.setSlide_id(slideItem.getId());
        nodeEntity.setUser_id(preferenceUtil.getAccount().getId());
        nodeEntity.setFlagEmptylist(false);
        return nodeEntity;


    }
    public boolean isLastSlide(){

        return slideItem.getCount()==1;
    }

    public boolean isLastSlideOfType(){
        Integer actualSerial = slideItem.getSerial()+1;
        return actualSerial>=slideItem.getCount();
    }
}

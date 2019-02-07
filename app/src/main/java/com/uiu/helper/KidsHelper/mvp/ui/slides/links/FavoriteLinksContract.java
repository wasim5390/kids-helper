package com.uiu.helper.KidsHelper.mvp.ui.slides.links;


import com.uiu.helper.KidsHelper.mvp.BasePresenter;
import com.uiu.helper.KidsHelper.mvp.BaseView;
import com.uiu.helper.KidsHelper.mvp.model.LinksEntity;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;

import java.util.List;

public class FavoriteLinksContract {

    interface View extends BaseView<Presenter>
    {
        void onFavoriteLinksLoaded(List<LinksEntity> linksEntities);
        void showMassage(String msg);
        void slideSerial(int serial,int count);
        void onSlideCreated(SlideItem slide);
        void onSlideRemoved(SlideItem slide);

        void onFavoriteLinkDataLoaded(String url, LinksEntity linksEntity);

        void onNewSlideCreated(SlideItem newSlide);

        void itemAddedOnNewSlide(SlideItem newSlide);
    }
    interface Presenter extends BasePresenter
    {
        void loadFavLinks();
        void removeFavLinkFromSlide(LinksEntity linksEntity);
        void updateFavLink(LinksEntity favLink);
        void getFavLinkData(String link);
        void createNewSlide(SlideItem slide);
        void removeSlide();

        void addFavLinkOnSlide(LinksEntity entity);

        boolean canAddOnSlide();

        boolean isLastSlide();
    }


}

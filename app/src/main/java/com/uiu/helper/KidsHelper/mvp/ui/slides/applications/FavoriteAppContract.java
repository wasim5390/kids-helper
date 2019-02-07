package com.uiu.helper.KidsHelper.mvp.ui.slides.applications;


import com.uiu.helper.KidsHelper.mvp.BasePresenter;
import com.uiu.helper.KidsHelper.mvp.BaseView;
import com.uiu.helper.KidsHelper.mvp.model.AppsEntity;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;

import java.util.List;

public class FavoriteAppContract {

    interface View extends BaseView<Presenter> {
        void showMessage(String message);
        void onFavoriteAppsLoaded(List<AppsEntity> list);
        void onEntityRemoved(AppsEntity entity);
        void slideSerial(int serial, int count);
        void onSlideCreated(SlideItem slide);
        void onSlideRemoved(SlideItem slide);

        void onNewSlideCreated(SlideItem newSlide);

        void itemAddedOnNewSlide(SlideItem newSlide);
    }

    interface Presenter extends BasePresenter
    {
        void loadFavApps();
        void removeEntity(AppsEntity entity);
        void saveFavoriteApp(AppsEntity entity);
        void createNewSlide(SlideItem slide);
        void removeSlide();

        boolean canAddOnSlide();
        boolean isLastSlide();
    }



}

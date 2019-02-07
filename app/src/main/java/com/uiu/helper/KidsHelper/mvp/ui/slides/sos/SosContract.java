package com.uiu.helper.KidsHelper.mvp.ui.slides.sos;


import com.uiu.helper.KidsHelper.mvp.BasePresenter;
import com.uiu.helper.KidsHelper.mvp.BaseView;
import com.uiu.helper.KidsHelper.mvp.model.ContactEntity;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;

import java.util.List;

public class SosContract {

    interface  View extends BaseView<Presenter>
    {
        void onSOSListLoaded(List<ContactEntity> sosList);
        void onSosAdded(ContactEntity entity);
        void showMessage(String msg);
        void slideSerial(int serial, int count);
        void onSlideCreated(SlideItem slide);
        void onSlideRemoved(SlideItem slide);

        void onNewSlideCreated(SlideItem newSlide);

        void itemAddedOnNewSlide(SlideItem newSlide);
    }

    interface Presenter extends BasePresenter
    {
        void loadSosList();
        void saveSos(ContactEntity sos);
        void removeFavoriteSosFromSlide(ContactEntity contactEntity);
        void createNewSlide(SlideItem slide);
        void removeSlide();

        boolean canAddOnSlide();

        boolean isLastSlide();
    }
}

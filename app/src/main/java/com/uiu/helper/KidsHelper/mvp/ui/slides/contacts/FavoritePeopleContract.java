package com.uiu.helper.KidsHelper.mvp.ui.slides.contacts;


import com.uiu.helper.KidsHelper.entities.UserEntity;
import com.uiu.helper.KidsHelper.mvp.BasePresenter;
import com.uiu.helper.KidsHelper.mvp.BaseView;
import com.uiu.helper.KidsHelper.mvp.model.ContactEntity;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;
import com.uiu.helper.KidsHelper.mvp.model.User;

import java.util.List;

public class FavoritePeopleContract {

    interface View extends BaseView<Presenter> {
        void showMessage(String message);
        void onFavoritePeopleLoaded(List<ContactEntity> list);
        void onFavoritePeopleRemoved(ContactEntity entity);
        void slideSerial(int serial,int count);
        void onSlideCreated(SlideItem slide);
        void onSlideRemoved(SlideItem slide);

        void onNewSlideCreated(SlideItem newSlide);
        void itemAddedOnNewSlide(SlideItem newSlide);
    }

    interface Presenter extends BasePresenter {
        void loadFavoritePeoples(String slideId);
        void saveFavoritePeople(ContactEntity entity, String userId);
        void removeFavoritePeople(ContactEntity entity);
        User getSelectedUser();
        void createNewSlide(SlideItem slide);
        void removeSlide();
        boolean canAddOnSlide();

        boolean isLastSlide();
    }
}

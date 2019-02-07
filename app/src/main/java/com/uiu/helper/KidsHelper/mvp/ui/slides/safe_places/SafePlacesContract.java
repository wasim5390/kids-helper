package com.uiu.helper.KidsHelper.mvp.ui.slides.safe_places;


import com.uiu.helper.KidsHelper.mvp.BasePresenter;
import com.uiu.helper.KidsHelper.mvp.BaseView;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;
import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.KidsHelper.mvp.ui.tracker.DirectionsItem;

import java.util.List;

public class SafePlacesContract {

    interface View extends BaseView<Presenter> {
        void showMessage(String message);
        void onDirectionsLoaded(List<DirectionsItem> list,List<DirectionsItem> actualLocations);
        void onDirectionRemoved(DirectionsItem entity);
        void slideSerial(int serial, int count);
        void onSlideCreated(SlideItem slide);
        void onSlideRemoved(SlideItem slide);

        void onNewSlideCreated(SlideItem newSlide);
        void itemAddedOnNewSlide(SlideItem newSlide);
    }

    interface Presenter extends BasePresenter {
        void loadDirections(String slideId);
        void saveDirection(DirectionsItem entity);
        void removeDirection(DirectionsItem entity);
        User getSelectedUser();
        void createNewSlide(SlideItem slide);
        void removeSlide();
        boolean canAddOnSlide();

        boolean isLastSlide();
    }
}

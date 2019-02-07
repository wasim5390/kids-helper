package com.uiu.helper.KidsHelper.mvp.ui.dashboard;


import android.support.v4.app.Fragment;

import com.uiu.helper.KidsHelper.mvp.BasePresenter;
import com.uiu.helper.KidsHelper.mvp.BaseView;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;
import com.uiu.helper.KidsHelper.mvp.model.User;

import java.util.List;

public class DashboardContract {

    interface View extends BaseView<Presenter> {
        void showMessage(String message);
        void onSlidesCreated(List<Fragment> fragments);
        void onSlidesLoaded(User selectedUser, List<SlideItem> slideItems);
        void onSlidesUpdated(List<SlideItem> slides);
    }

    interface Presenter extends BasePresenter {
        void addSlide(SlideItem slideItem);
        void removeSlide(SlideItem slideItem);
        void getUserSlides();
        void convertSlidesToFragment(List<SlideItem> slides);

    }

}

package com.uiu.helper.KidsHelper.mvp.ui.slides.reminder;

import android.content.Context;


import com.uiu.helper.KidsHelper.mvp.BasePresenter;
import com.uiu.helper.KidsHelper.mvp.BaseView;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class ReminderContract {

    interface View extends BaseView<Presenter> {
        void onLoadedReminderList(List<ReminderEntity> list);
        void showMessage(String msg);
        void slideSerial(int serial,int count);
        void onSlideCreated(SlideItem slide);
        void onSlideRemoved(SlideItem slide);

        void onNewSlideCreated(SlideItem newSlide);

        void itemAddedOnNewSlide(SlideItem newSlide);
    }

    interface Presenter extends BasePresenter {
        void onLoadReminderList();
        void saveReminder(ReminderEntity entity,File file);
        void removeReminderFromSlide(ReminderEntity reminderId);
        void createNewSlide(SlideItem slide);
        void removeSlide();

        boolean canAddOnSlide();

        boolean isLastSlide();
    }
}

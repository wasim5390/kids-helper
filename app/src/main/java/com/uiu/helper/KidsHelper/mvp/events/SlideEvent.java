package com.uiu.helper.KidsHelper.mvp.events;

import android.support.constraint.ConstraintLayout;

import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;

public class SlideEvent implements Constant {

    SlideItem slideItem;
    boolean isAddingSlide;

    public SlideEvent(SlideItem slideItem, boolean isAddingSlide) {
        this.slideItem = slideItem;
        this.isAddingSlide = isAddingSlide;
    }


    public boolean isAddingSlide() {
        return isAddingSlide;
    }

    public void setAddingSlide(boolean addingSlide) {
        isAddingSlide = addingSlide;
    }



    public SlideItem getSlideItem() {
        return slideItem;
    }

    public void setSlideItem(SlideItem slideItem) {
        this.slideItem = slideItem;
    }

}

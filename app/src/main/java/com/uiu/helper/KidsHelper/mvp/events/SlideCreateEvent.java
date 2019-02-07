package com.uiu.helper.KidsHelper.mvp.events;

import com.uiu.helper.KidsHelper.mvp.model.SlideItem;

public class SlideCreateEvent {

    SlideItem slide;

    public SlideCreateEvent(SlideItem slide) {
        this.slide = slide;
    }

    public SlideItem getSlide() {
        return slide;
    }
}

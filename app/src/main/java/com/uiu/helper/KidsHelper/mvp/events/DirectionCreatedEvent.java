package com.uiu.helper.KidsHelper.mvp.events;

import com.uiu.helper.KidsHelper.mvp.ui.tracker.DirectionsItem;

public class DirectionCreatedEvent {


    DirectionsItem item;

    public DirectionCreatedEvent(DirectionsItem item) {
        this.item = item;
    }

    public DirectionsItem getItem() {
        return item;
    }
}

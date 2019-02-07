package com.uiu.helper.KidsHelper.mvp.events;

public class MainUIActionEvent {
    public MainUIActionEvent(int showActionBar) {
        this.showActionBar = showActionBar;
    }

    public int getShowActionBar() {
        return showActionBar;
    }

    public int showActionBar;
    int actionBarColor;

}

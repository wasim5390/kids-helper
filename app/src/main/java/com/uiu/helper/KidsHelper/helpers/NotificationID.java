package com.uiu.helper.KidsHelper.helpers;

import java.util.Date;

public class NotificationID{
    public static int getID() {
        return (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
    }
}
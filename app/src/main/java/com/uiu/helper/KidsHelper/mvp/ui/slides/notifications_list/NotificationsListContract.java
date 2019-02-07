package com.uiu.helper.KidsHelper.mvp.ui.slides.notifications_list;


import com.uiu.helper.KidsHelper.mvp.BasePresenter;
import com.uiu.helper.KidsHelper.mvp.BaseView;
import com.uiu.helper.KidsHelper.mvp.model.Data;
import com.uiu.helper.KidsHelper.mvp.model.LinksEntity;
import com.uiu.helper.KidsHelper.mvp.model.NotificationsItem;
import com.uiu.helper.KidsHelper.mvp.model.NotificationsListResponse;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;
import com.uiu.helper.KidsHelper.services.response.BaseResponse;

import java.util.HashMap;
import java.util.List;

public class NotificationsListContract {

    interface View extends BaseView<Presenter>
    {
        void onLoadedMore(List<NotificationsItem> notificationsItems);
        void onNotificationLoaded(List<NotificationsItem> notificationsItems);
        void onNotificationHistoryLoaded(List<NotificationsItem> notificationsItems);
        void removeNotification(NotificationsItem notificationsItem);
        void showMassage(String msg);

    }
    interface Presenter extends BasePresenter
    {
        void loadNotifications();
        void loadHistoryNotifications();
        void loadMoreNotifications();
        void updateStatus(HashMap<String,Object> params,NotificationsItem item);
        void removeNotification(NotificationsItem notificationsItem);
    }


}

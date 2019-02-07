package com.uiu.helper.KidsHelper.mvp.ui.slides.notifications_list;


import com.uiu.helper.KidsHelper.mvp.model.NotificationsItem;
import com.uiu.helper.KidsHelper.mvp.model.NotificationsListResponse;
import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.KidsHelper.mvp.model.response.BaseResponse;
import com.uiu.helper.KidsHelper.mvp.source.DataSource;
import com.uiu.helper.KidsHelper.mvp.source.Repository;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotificationsListPresenter implements NotificationsListContract.Presenter {

    public NotificationsListContract.View view;
    public User user;
    public Repository repository;
    private PreferenceUtil preferenceUtil;
    private int totalPages =1;
    private int currentPage=1;
    private boolean isRequesting = false;
    private boolean isHistory=false;

    public List<NotificationsItem> mDataList;


    public NotificationsListPresenter(NotificationsListContract.View view, PreferenceUtil preferenceUtil, User entity, Repository repository) {
        this.repository = repository;
        this.user = entity;
        this.preferenceUtil = preferenceUtil;
        this.mDataList = new ArrayList<>();
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void loadNotifications() {
        isHistory=false;
        loadNotifications(0,isHistory);
    }

    @Override
    public void loadHistoryNotifications() {
        isHistory=true;
        loadNotifications(0,isHistory);
    }

    @Override
    public void loadMoreNotifications() {
        if (currentPage >= totalPages-1) return;

        int nextPage = currentPage + 1;
        loadNotifications(nextPage,isHistory);
    }

    @Override
    public void updateStatus(HashMap<String, Object> params, NotificationsItem notificationsItem) {
        repository.updateSlideObjStatus(params, new DataSource.GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                mDataList.remove(notificationsItem);
                view.removeNotification(notificationsItem);
            }

            @Override
            public void onFailed(int code, String message) {

            }
        });
    }

    @Override
    public void removeNotification(NotificationsItem notificationsItem) {
        repository.removeNotification(notificationsItem.getId(), new DataSource.GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                mDataList.remove(notificationsItem);
                view.removeNotification(notificationsItem);
            }

            @Override
            public void onFailed(int code, String message) {

            }
        });
    }

    private void loadNotifications(int page,boolean historyToLoad){
        if(isRequesting)
            return;
        isRequesting = true;

        repository.getNotifications(!historyToLoad,user.getId(),preferenceUtil.getAccount().getId(), page,
                new DataSource.GetResponseCallback<NotificationsListResponse>() {
                    @Override
                    public void onSuccess(NotificationsListResponse response) {
                        isRequesting = false;
                        currentPage = response.getData().getCurrentPage();
                        totalPages = response.getData().getTotalPages();

                        if(currentPage==0) {
                            mDataList.clear();
                            mDataList.addAll(response.getData().getNotifications());
                            if(historyToLoad)
                                view.onNotificationHistoryLoaded(mDataList);
                            else
                            view.onNotificationLoaded(mDataList);
                        }
                        else {
                            mDataList.addAll(response.getData().getNotifications());
                            view.onLoadedMore(response.getData().getNotifications());
                        }
                    }

                    @Override
                    public void onFailed(int code, String message) {
                        isRequesting = false;
                        view.showMassage(message);
                    }
                });
    }

    @Override
    public void start() {

    }
}

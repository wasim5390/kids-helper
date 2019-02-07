package com.uiu.helper.KidsHelper.mvp.ui.slides.notifications_list;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.uiu.helper.KidsHelper.constants.AppConstants;
import com.uiu.helper.KidsHelper.mvp.BaseFragment;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.events.NotificationReceiveEvent;
import com.uiu.helper.KidsHelper.mvp.model.LinksEntity;
import com.uiu.helper.KidsHelper.mvp.model.NotificationInnerObject;
import com.uiu.helper.KidsHelper.mvp.model.NotificationsItem;
import com.uiu.helper.KidsHelper.mvp.ui.EndlessRecyclerViewScrollListener;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.R;
import com.uiu.helper.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.v7.widget.DividerItemDecoration.HORIZONTAL;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class NotificationsListFragment extends BaseFragment implements NotificationsListContract.View,
        EndlessRecyclerViewScrollListener.OnLoadMore,
        NotificationsListAdapter.Callback{

    private static final String TAG = "NotificationsFragment";
    @BindView(R.id.tvFavAppsTitle)
    TextView title;
    @BindView(R.id.editor_views)
    View editorViews;
    @BindView(R.id.rvFavApps)
    RecyclerView rvNotifications;

    @BindView(R.id.btnAddNew)
    Button buttonHistory;
    public NotificationsListContract.Presenter presenter;

    public NotificationsListAdapter adapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private  boolean hasBeenLoaded = false;
    private boolean historyView=false;

    public static NotificationsListFragment newInstance()
    {
        Bundle args=new Bundle();
        NotificationsListFragment instance=new NotificationsListFragment();
        instance.setArguments(args);
        return instance;
    }

    @Override
    public int getID() {
        return R.layout.fragment_favorite_slides;
    }

    @Override
    public void initUI(View view) {
        EventBus.getDefault().register(this);
        view.findViewById(R.id.verticalSeparator).setVisibility(View.GONE);
        view.findViewById(R.id.btnRemove).setVisibility(View.GONE);

        title.setText("Notifications");
        title.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.PrimaryDarkColor));
        updateListView(historyView);
        setAdapter();
        if(presenter!=null && !hasBeenLoaded)
        {
            hasBeenLoaded=true;
            onHistoryBtnClick();
        }


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Unregister");
        EventBus.getDefault().unregister(this);
    }

    private void updateListView(boolean isHistoryLoaded){
        try {
            buttonHistory.setBackground(isHistoryLoaded ? ContextCompat.getDrawable(getActivity(), R.drawable.history_btn_selector) :
                    ContextCompat.getDrawable(getActivity(), R.drawable.notificaton_list_selector)
            );
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }


    private void setAdapter() {
        adapter = new NotificationsListAdapter(getContext(),new ArrayList<>(),this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false);
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager,this);
        rvNotifications.setLayoutManager(layoutManager);
        rvNotifications.addOnScrollListener(scrollListener);
        rvNotifications.setHasFixedSize(true);
        rvNotifications.setAdapter(adapter);
        rvNotifications.setItemAnimator(new DefaultItemAnimator());
        rvNotifications.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));

    }

    @Override
    public void setPresenter(NotificationsListContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void showNoInternet() {

    }



    @Override
    public void onLoadedMore(List<NotificationsItem> notificationsItems) {
        adapter.addItems(notificationsItems);
    }

    @Override
    public void onNotificationLoaded(List<NotificationsItem> notificationsItems) {

        adapter.setSlideItems(notificationsItems);
        getView().findViewById(R.id.tvMsg).setVisibility(notificationsItems.isEmpty()?View.VISIBLE:View.GONE);
        updateListView(historyView);
    }

    @Override
    public void onNotificationHistoryLoaded(List<NotificationsItem> notificationsItems) {

        adapter.setSlideItems(notificationsItems);
        getView().findViewById(R.id.tvMsg).setVisibility(notificationsItems.isEmpty()?View.VISIBLE:View.GONE);
        updateListView(historyView);
    }

    @Override
    public void removeNotification(NotificationsItem notificationsItem) {
        adapter.removeItem(notificationsItem);
        if(adapter.getItemCount()<1)
            getView().findViewById(R.id.tvMsg).setVisibility(View.VISIBLE);
        EventBus.getDefault().post(new NotificationReceiveEvent(notificationsItem.getNotificationType()));
    }

    @Override
    public void showMassage(String msg) {
        Utils.showAlert(getActivity(),msg, ContextCompat.getColor(getContext(),R.color.contacts_bg));
    }


    @Override
    public void onSlideItemClick(NotificationsItem notification,int status) {
        HashMap<String,Object> params = new HashMap<>();
        int notificationType = notification.getNotificationType();
        if(notification.getNotificationInnerObject()!=null)
        {
            try {
                NotificationInnerObject innerObject = notification.getNotificationInnerObject();

                params.put("slide_id",innerObject.getSlide_id());
                params.put("object_id",innerObject.getId());
                params.put("request_status",status);
                params.put("helper_id", PreferenceUtil.getInstance(getContext()).getAccount().getId());

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        presenter.updateStatus(params,notification);

    }

    @Override
    public void onDeleteSlideItem(NotificationsItem notification, int status) {

        presenter.removeNotification(notification);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
        if(receiveEvent.getStatus()==Constant.REQ_REQUESTED)
        if(presenter!=null)
            presenter.loadNotifications();

    }


    @Override
    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        presenter.loadMoreNotifications();
    }

    @OnClick(R.id.btnAddNew)
    public void onHistoryBtnClick(){
        if(!historyView)
            presenter.loadNotifications();
        else
            presenter.loadHistoryNotifications();
        historyView=!historyView;
        updateListView(historyView);
    }
}

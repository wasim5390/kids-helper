package com.uiu.helper.KidsHelper.mvp.ui.slides.applications;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uiu.helper.KidsHelper.mvp.BaseFragment;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.events.NotificationReceiveEvent;
import com.uiu.helper.KidsHelper.mvp.events.SlideCreateEvent;
import com.uiu.helper.KidsHelper.mvp.events.SlideEvent;
import com.uiu.helper.KidsHelper.mvp.model.AppsEntity;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;
import com.uiu.helper.KidsHelper.mvp.ui.ConfirmationCallback;
import com.uiu.helper.KidsHelper.mvp.ui.dashboard.apps.AppsActivity;
import com.uiu.helper.R;
import com.uiu.helper.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class FavoriteAppFragment extends BaseFragment implements FavoriteAppContract.View, FavoriteAppsAdapter.Callback{

    private static final int REQ_APPS = 987;
    private static final String TAG = "FavoriteAppFragment";
    private FavoriteAppContract.Presenter presenter;
    private FavoriteAppsAdapter adapter;
    @BindView(R.id.rvFavApps)
    RecyclerView rvFavoriteApps;
    @BindView(R.id.btnRemove)
    TextView tvRemove;

    private boolean isEditorEnabled=false;


    public static FavoriteAppFragment newInstance() {
        Bundle args = new Bundle();
        FavoriteAppFragment instance = new FavoriteAppFragment();
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
        setRecyclerView();
        if(presenter!=null)
        presenter.start();

}



    public void setRecyclerView() {
        adapter = new FavoriteAppsAdapter(getContext(),new ArrayList<>(),this);
        rvFavoriteApps.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        rvFavoriteApps.setHasFixedSize(true);
        rvFavoriteApps.setAdapter(adapter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Unregister");
        EventBus.getDefault().unregister(this);
    }
    @Override
    public void setPresenter(FavoriteAppContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void slideSerial(int serial,int count) {

        serial++;
        String pageNum = serial+"/"+count;
        ((TextView)getView().findViewById(R.id.tvFavAppsTitle)).setText(getString(R.string.favorite_apps)+" ("+pageNum+")");

    }

    @Override
    public void onSlideCreated(SlideItem slide) {
        EventBus.getDefault().post(new SlideEvent(slide,true));
    }

    @Override
    public void onSlideRemoved(SlideItem slide) {
        EventBus.getDefault().post(new SlideEvent(slide,false));
    }

    @Override
    public void onNewSlideCreated(SlideItem newSlide) {

    }

    @Override
    public void itemAddedOnNewSlide(SlideItem newSlide) {
        EventBus.getDefault().postSticky(new SlideCreateEvent(newSlide));
    }

    @Override
    public void showNoInternet() {

    }

    @Override
    public void showMessage(String message) {
        Utils.showAlert(getActivity(),message, ContextCompat.getColor(getActivity(),R.color.contacts_bg));

    }

    @Override
    public void onFavoriteAppsLoaded(List<AppsEntity> list) {
         adapter.setSlideItems(list);

    }

    @Override
    public void onEntityRemoved(AppsEntity entity) {
        List<AppsEntity> adapterList = adapter.getItems();
        adapterList.remove(entity);
        adapter.setSlideItems(adapterList);

    }


    @Override
    public void onSlideItemClick(AppsEntity slideItem) {
        new Handler().postDelayed(() -> {
            if (slideItem.getPackageName() == null) {
                startActivityForResult(new Intent(getContext(), AppsActivity.class), REQ_APPS);
            }else {
                    presenter.removeEntity(slideItem);
            }
        }, 100);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
        if(receiveEvent.getNotificationForSlideType()==Constant.SLIDE_INDEX_FAV_APP){
            if(presenter!=null)
                presenter.loadFavApps();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQ_APPS) {
            if (resultCode == RESULT_OK) {
                AppsEntity entity = (AppsEntity) data.getSerializableExtra(Constant.KEY_SELECTED_APP);
                presenter.saveFavoriteApp(entity);
            }
        }
    }

    @OnClick(R.id.btnAddNew)
    public void addNewSlide(){
        showAddDialog("Application", false,new IAddOnSlideListener() {
            @Override
            public void onAddSlideClick() {
                SlideItem slideItem = new SlideItem();
                slideItem.setName(getString(R.string.favorite_apps));
                slideItem.setType(SLIDE_INDEX_FAV_APP);

                presenter.createNewSlide(slideItem);
            }

            @Override
            public void onAddItemClick() {
                if(presenter.canAddOnSlide()) {
                    startActivityForResult(new Intent(getContext(), AppsActivity.class), REQ_APPS);
                }
            }
        });
        // startActivityForResult(new Intent(getContext(), AppsActivity.class), REQ_APPS);



    }
    @OnClick(R.id.btnRemove)
    public void onRemoveClick()
    {
        if(adapter.inEditMode()) {
            isEditorEnabled=false;
            adapter.setEditMode(isEditorEnabled);
            tvRemove.setText("Remove");
            return;
        }

        showRemoveDialog("Application",presenter.isLastSlide(),new IRemoveOnSlideListener() {
            @Override
            public void onRemoveSlideClick() {

                String title = getString(R.string.do_you_want_to_remove_the_slide);
                String body = getString(R.string.remove_slide_alert_desc);
                showDeleteFromSlideDialog(title, body, null, null, (slideId, itemId) -> {
                    presenter.removeSlide();
                });

            }

            @Override
            public void onRemoveItemClick() {
                isEditorEnabled=true;
                tvRemove.setText("Done");
                adapter.setEditMode(isEditorEnabled);
            }

            @Override
            public void onDismiss() {
                isEditorEnabled=false;
                tvRemove.setText("Remove");
                adapter.setEditMode(isEditorEnabled);
            }
        });

    }
}

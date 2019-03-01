package com.uiu.helper.KidsHelper.mvp.ui.slides.safe_places;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.uiu.helper.KidsHelper.mvp.BaseFragment;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.events.DirectionCreatedEvent;
import com.uiu.helper.KidsHelper.mvp.events.NotificationReceiveEvent;
import com.uiu.helper.KidsHelper.mvp.events.SlideCreateEvent;
import com.uiu.helper.KidsHelper.mvp.events.SlideEvent;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;
import com.uiu.helper.KidsHelper.mvp.ui.tracker.Direction;
import com.uiu.helper.KidsHelper.mvp.ui.tracker.DirectionsItem;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.R;
import com.uiu.helper.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SafePlacesFragment extends BaseFragment implements SafePlacesAdapter.Callback,
        SafePlacesContract.View
{
    private static final int REQ_CONTACT = 0x101;
    public static String TAG ="SafePlacesFragment";

    @BindView(R.id.rvFavApps)
    RecyclerView recyclerView;
    @BindView(R.id.tvFavAppsTitle)
    TextView title;

    @BindView(R.id.btnRemove)
    TextView tvRemove;

    private boolean isEditorEnabled=false;

    private SafePlacesAdapter adapter;
    SafePlacesContract.Presenter mPresenter;

    public static SafePlacesFragment newInstance() {
        Bundle args = new Bundle();
        SafePlacesFragment homeFragment = new SafePlacesFragment();
        homeFragment.setArguments(args);
        return homeFragment;
    }

    @Override
    public int getID() {
        return R.layout.fragment_favorite_slides;
    }

    @Override
    public void initUI(View view) {
        EventBus.getDefault().register(this);
        title.setText(getString(R.string.home_title_safe_places));
        title.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.directions_slide_title_colorDark));
        setRecyclerView();
        if(mPresenter!=null)
            mPresenter.start();
    }
    public void setRecyclerView(){
        adapter = new SafePlacesAdapter(getContext(),this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2, GridLayoutManager.VERTICAL,false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Unregister");
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSlideItemClick(DirectionsItem slideItem) {
        new Handler().postDelayed(() -> {
            if (slideItem.getId() == null) {
            loadDestinationFragment();

            }else {
                slideItem.setUserId(PreferenceUtil.getInstance(getActivity()).getAccount().getId());
                mPresenter.removeDirection(slideItem);
            }
        },1);

    }


    @Override
    public void slideSerial(int serial,int count) {
        serial++;
        String pageNum = serial+"/"+count;
        ((TextView)getView().findViewById(R.id.tvFavAppsTitle)).setText(getString(R.string.home_title_safe_places)+" ("+pageNum+")");
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
    public void showMessage(String message) {

        Utils.showAlert(getActivity(),message, ContextCompat.getColor(getContext(),R.color.contacts_bg));
    }

    @Override
    public void onDirectionsLoaded(List<DirectionsItem> list,List<DirectionsItem> actualList) {

        adapter.setSlideItems(list);
        Direction direction = new Direction();
        direction.setDirections(actualList);
        EventBus.getDefault().postSticky(direction);
    }

    @Override
    public void onDirectionRemoved(DirectionsItem entity) {

    }


    @Override
    public void setPresenter(SafePlacesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
        if(receiveEvent.getNotificationForSlideType()==Constant.SLIDE_INDEX_SAFE_PLACES){
            if(mPresenter!=null)
                mPresenter.start();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DirectionCreatedEvent receiveEvent) {
        if(receiveEvent.getItem()!=null){
            if(mPresenter!=null)
                mPresenter.saveDirection(receiveEvent.getItem());
        }

    }



    @OnClick(R.id.btnAddNew)
    public void addNewSlide(){
        showAddDialog("Safe Place", false,new IAddOnSlideListener() {
            @Override
            public void onAddSlideClick() {
                SlideItem slideItem = new SlideItem();
                slideItem.setName(getString(R.string.home_title_safe_places));
                slideItem.setType(SLIDE_INDEX_SAFE_PLACES);

                mPresenter.createNewSlide(slideItem);
            }

            @Override
            public void onAddItemClick() {
                if(mPresenter.canAddOnSlide()) {
                    loadDestinationFragment();
                }
            }
        });

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

        showRemoveDialog("Safe Place",mPresenter.isLastSlide(),new IRemoveOnSlideListener() {
            @Override
            public void onRemoveSlideClick() {
                String title = getString(R.string.do_you_want_to_remove_the_slide);
                String body = getString(R.string.remove_slide_alert_desc);
                showDeleteFromSlideDialog(title, body, null, null, (slideId, itemId) -> {
                    mPresenter.removeSlide();
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

    private void loadDestinationFragment() {
        ChooseSafePlaceFragment fragment = ChooseSafePlaceFragment.newInstance();
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment,ChooseSafePlaceFragment.TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}

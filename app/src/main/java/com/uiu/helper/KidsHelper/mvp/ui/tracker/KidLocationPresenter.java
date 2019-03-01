package com.uiu.helper.KidsHelper.mvp.ui.tracker;

import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.KidsHelper.mvp.model.response.GetDirectionsResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetKidTrackerResponse;
import com.uiu.helper.KidsHelper.mvp.source.DataSource;
import com.uiu.helper.KidsHelper.mvp.source.Repository;

import java.util.ArrayList;
import java.util.List;

public class KidLocationPresenter implements KidLocationContract.Presenter {

    private User userEntity;
    public Repository repository;
    KidLocationContract.View mView;
    List<DirectionsItem> directionsItems;

    public KidLocationPresenter(KidLocationContract.View mView,User userEntity, Repository repository) {
        this.userEntity = userEntity;
        this.repository = repository;
        this.mView = mView;
        this.mView.setPresenter(this);
    }


    @Override
    public void loadGeofences() {
        repository.getDirections(userEntity.getId(), new DataSource.GetDataCallback<GetDirectionsResponse>() {
            @Override
            public void onDataReceived(GetDirectionsResponse data) {
                directionsItems.clear();
                directionsItems.addAll(data.getDirectionsList());
                mView.onGeofenceLoaded(directionsItems,true);
            }

            @Override
            public void onFailed(int code, String message) {
                mView.showMessage(message);
            }
        });
    }

    @Override
    public void getKidLocation() {
        repository.getKidLocation(userEntity.getId(), new DataSource.GetDataCallback<GetKidTrackerResponse>() {
            @Override
            public void onDataReceived(GetKidTrackerResponse data) {
                if(data.isSuccess())
                    mView.onKidLocationReceived(data.getTracker());
                else
                    mView.showMessage(data.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                mView.showMessage(message);
            }
        });
    }


    @Override
    public void start() {
        directionsItems = new ArrayList<>();
      //  mView.onGeofenceLoaded(directionsItems);
        loadGeofences();
    }
}

package com.uiu.helper.KidsHelper.mvp.ui.tracker;

import com.uiu.helper.KidsHelper.mvp.BasePresenter;
import com.uiu.helper.KidsHelper.mvp.BaseView;
import com.uiu.helper.KidsHelper.mvp.model.response.Tracker;

import java.util.List;

public class KidLocationContract  {
    interface View extends BaseView<Presenter>{
        void showMessage(String message);
        void onGeofenceLoaded(List<DirectionsItem> directions);
        void onKidLocationReceived(Tracker tracker);
    }

    interface Presenter extends BasePresenter{
        void loadGeofences();
        void getKidLocation();
    }
}

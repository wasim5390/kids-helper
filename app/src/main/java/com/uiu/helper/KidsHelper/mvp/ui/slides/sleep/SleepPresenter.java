package com.uiu.helper.KidsHelper.mvp.ui.slides.sleep;

import com.uiu.helper.CompanionApp;
import com.uiu.helper.KidsHelper.mvp.model.Setting;
import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.KidsHelper.mvp.model.response.BaseResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetSettingsResponse;
import com.uiu.helper.KidsHelper.mvp.source.DataSource;
import com.uiu.helper.KidsHelper.mvp.source.Repository;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.util.Utils;

import java.util.HashMap;


public class SleepPresenter implements SleepContract.Presenter {

    public SleepContract.View view;
    public Repository repository;
    private String kidId;
    private PreferenceUtil preferenceUtil;

    public SleepPresenter(SleepContract.View view, String kidId, PreferenceUtil preferenceUtil, Repository repository) {
        this.view = view;
        this.repository = repository;
        this.kidId = kidId;
        this.preferenceUtil = preferenceUtil;
        view.setPresenter(this);
    }

    @Override
    public void getSettings() {
    if(!Utils.isNetworkAvailable(CompanionApp.getContext())){
        return;
    }
    repository.getKidDeviceSettings(kidId, new DataSource.GetDataCallback<GetSettingsResponse>() {
        @Override
        public void onDataReceived(GetSettingsResponse data) {

            if(data.isSuccess()){
                view.onSettingsLoaded(data.getSettings());
            }
                else{
                view.showMessage("Internet not available!");
            }
        }

        @Override
        public void onFailed(int code, String message) {
            view.showMessage(message);
        }
    });
    }

    @Override
    public void updateSettings(HashMap<String,Object> settingParams) {

        if(!Utils.isNetworkAvailable(CompanionApp.getContext()))
            return;

        HashMap<String,Object> params = new HashMap<>();
        params.put("kid_id",kidId);
        params.put("helper_id",preferenceUtil.getAccount().getId());
        params.put("setting",settingParams);
        view.showProgress();
        repository.updateKidSettings(params, new DataSource.GetDataCallback<GetSettingsResponse>() {
            @Override
            public void onDataReceived(GetSettingsResponse data) {
                view.hideProgress();
                if(data.isSuccess()) {
                    preferenceUtil.saveKidsDeviceSettings(kidId,data.getSettings());
                    view.onSettingsUpdated(data.getSettings());
                }
                else
                    view.showMessage(data.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                view.hideProgress();
                view.showMessage(message);
            }
        });
    }


    @Override
    public void start() {

    }
}

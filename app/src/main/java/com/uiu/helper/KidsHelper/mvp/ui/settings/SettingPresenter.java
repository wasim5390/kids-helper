package com.uiu.helper.KidsHelper.mvp.ui.settings;

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


public class SettingPresenter implements SettingContract.Presenter {

    public SettingContract.View view;
    public Repository repository;
    private User userEntity;
    private PreferenceUtil preferenceUtil;

    public SettingPresenter(SettingContract.View view, User userEntity, PreferenceUtil preferenceUtil, Repository repository) {
        this.view = view;
        this.repository = repository;
        this.userEntity = userEntity;
        this.preferenceUtil = preferenceUtil;
        view.setPresenter(this);
    }

    @Override
    public void getSettings(String kidId) {
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
    public void updateSettings(Setting setting) {
        setting.setBatteryLevel(null);
        if(!Utils.isNetworkAvailable(CompanionApp.getContext()))
            return;

        HashMap<String,Object> params = new HashMap<>();
        params.put("kid_id",userEntity.getId());
        params.put("helper_id",preferenceUtil.getAccount().getId());
        params.put("setting",setting);
        view.showProgress();
        repository.updateKidSettings(params, new DataSource.GetDataCallback<GetSettingsResponse>() {
            @Override
            public void onDataReceived(GetSettingsResponse data) {
                view.hideProgress();
                if(data.isSuccess()) {
                    preferenceUtil.saveKidsDeviceSettings(userEntity.getId(),setting);
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
    public void findPhone(String kidId,String helperId) {
        if(!Utils.isNetworkAvailable(CompanionApp.getContext()))
            return;
        repository.beepKidsDevice(kidId, helperId, new DataSource.GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {

            }

            @Override
            public void onFailed(int code, String message) {

            }
        });
    }

    @Override
    public void start() {

    }
}

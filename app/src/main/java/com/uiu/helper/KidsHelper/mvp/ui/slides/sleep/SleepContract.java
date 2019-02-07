package com.uiu.helper.KidsHelper.mvp.ui.slides.sleep;

import com.uiu.helper.KidsHelper.mvp.BasePresenter;
import com.uiu.helper.KidsHelper.mvp.BaseView;
import com.uiu.helper.KidsHelper.mvp.model.Setting;
import com.uiu.helper.KidsHelper.mvp.model.SleepSetting;

import java.util.HashMap;

public class SleepContract {

    public interface View extends BaseView<SleepContract.Presenter> {
        void showMessage(String message);
        void onSettingsLoaded(Setting settings);
        void onSettingsUpdated(Setting settings);
    }

    public interface Presenter extends BasePresenter {
        void getSettings();
        void updateSettings(HashMap<String,Object> settingParams);
    }
}

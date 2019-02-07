package com.uiu.helper.KidsHelper.mvp.ui.settings;

import com.uiu.helper.KidsHelper.mvp.BasePresenter;
import com.uiu.helper.KidsHelper.mvp.BaseView;
import com.uiu.helper.KidsHelper.mvp.model.Setting;

import java.util.List;
import java.util.Set;

public class SettingContract {

    public interface View extends BaseView<SettingContract.Presenter> {
        void showMessage(String message);
        void onSettingsLoaded(Setting settings);
        void onSettingsUpdated(Setting settings);
    }

    public interface Presenter extends BasePresenter {
        void getSettings(String kidId);
        void updateSettings(Setting setting);
        void findPhone(String kidId,String helperId);
    }
}

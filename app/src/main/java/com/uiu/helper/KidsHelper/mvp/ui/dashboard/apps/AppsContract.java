package com.uiu.helper.KidsHelper.mvp.ui.dashboard.apps;


import com.uiu.helper.KidsHelper.mvp.BasePresenter;
import com.uiu.helper.KidsHelper.mvp.BaseView;
import com.uiu.helper.KidsHelper.mvp.model.AppsEntity;

import java.util.List;

public class AppsContract {

    interface View extends BaseView<Presenter>
    {
        void onAppListLoaded(List<AppsEntity> appslist);

    }

    interface Presenter extends BasePresenter
    {
        void loadApps(List<AppsEntity> list);
         void searchApps(String app);
    }
}

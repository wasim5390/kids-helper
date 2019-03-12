package com.uiu.helper.KidsHelper.mvp.ui.c_me.history;


import com.uiu.helper.KidsHelper.mvp.BasePresenter;
import com.uiu.helper.KidsHelper.mvp.BaseView;
import com.uiu.helper.KidsHelper.mvp.model.CmeeChatModel;

public class CmeeHistoryContract {

    interface View extends BaseView<Presenter>
    {
        void onHistoryLoaded(CmeeChatModel chatModel);
        void showMessage(String message);
    }

    interface Presenter extends BasePresenter
    {
        void fetchCmeeHistory();
    }

}

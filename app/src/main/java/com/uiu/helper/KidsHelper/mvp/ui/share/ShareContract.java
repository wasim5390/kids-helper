package com.uiu.helper.KidsHelper.mvp.ui.share;


import com.uiu.helper.KidsHelper.mvp.BasePresenter;
import com.uiu.helper.KidsHelper.mvp.BaseView;
import com.uiu.helper.KidsHelper.mvp.model.ContactEntity;

import java.util.List;

public class ShareContract {

    interface View extends BaseView<Presenter>
    {
       void loadPeople(List<ContactEntity> list);
       void showMessage(String msg);
    }
    interface Presenter extends BasePresenter
    {
        void loadRegisterPeople();
    }
}

package com.uiu.helper.KidsHelper.mvp.ui.dashboard.regd_peoples;


import com.uiu.helper.KidsHelper.mvp.BasePresenter;
import com.uiu.helper.KidsHelper.mvp.BaseView;
import com.uiu.helper.KidsHelper.mvp.model.ContactEntity;

import java.util.List;

public class RegdContactContract {
    interface View extends BaseView<Presenter> {
        void showMessage(String message);
        void onContactsLoaded(List<ContactEntity> list);
        void onEmptySearchResult();
    }

    interface Presenter extends BasePresenter {
        void loadContacts();
        void searchContacts(String contact);
    }
}

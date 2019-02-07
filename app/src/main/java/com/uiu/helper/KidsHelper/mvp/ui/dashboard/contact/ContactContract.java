package com.uiu.helper.KidsHelper.mvp.ui.dashboard.contact;


import com.uiu.helper.KidsHelper.mvp.BasePresenter;
import com.uiu.helper.KidsHelper.mvp.BaseView;
import com.uiu.helper.KidsHelper.mvp.model.ContactEntity;

import java.util.List;

public class ContactContract {
    interface View extends BaseView<Presenter> {
        void showMessage();
        void onContactsLoaded(List<ContactEntity> list);
        void onEmptySearchResult();
    }

    interface Presenter extends BasePresenter {
        void loadContacts();
        void searchContacts(String contact);
    }
}

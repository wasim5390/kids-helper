package com.uiu.helper.KidsHelper.mvp.ui.dashboard.regd_peoples;


import com.uiu.helper.KidsHelper.entities.UserEntity;
import com.uiu.helper.KidsHelper.mvp.model.ContactEntity;
import com.uiu.helper.KidsHelper.mvp.model.response.GetFavContactResponse;
import com.uiu.helper.KidsHelper.mvp.source.DataSource;
import com.uiu.helper.KidsHelper.mvp.source.Repository;

import java.util.ArrayList;
import java.util.List;

public class RegdContactPresenter implements RegdContactContract.Presenter {

    private RegdContactContract.View view;
    private Repository repository;
    private UserEntity user;
    private List<ContactEntity> contactList;


    public RegdContactPresenter(RegdContactContract.View view, UserEntity userEntity, Repository repository) {
        this.view = view;
        this.repository = repository;
        this.user = userEntity;
        this.view.setPresenter(this);
    }

    @Override
    public void loadContacts() {
        repository.getRegdContacts(user.getUserId(), new DataSource.GetDataCallback<GetFavContactResponse>() {
            @Override
            public void onDataReceived(GetFavContactResponse data) {
                if(data.isSuccess())
                    contactList.addAll(data.getRegdContactEntityList());
                view.onContactsLoaded(contactList);
            }

            @Override
            public void onFailed(int code, String message) {
                view.showMessage(message);
            }
        });


    }

    @Override
    public void searchContacts(String contact) {
        if (!contact.trim().isEmpty()) {
            List<ContactEntity> searchResults = new ArrayList<>();
            for (int i = 0; i < contactList.size(); i++) {
                if (contactList.get(i).getName().toLowerCase().contains(contact.toLowerCase())) {
                    searchResults.add(contactList.get(i));
                }
            }
            if(!searchResults.isEmpty())
            view.onContactsLoaded(searchResults);
            else
                view.onEmptySearchResult();
        } else {
            if(!contactList.isEmpty())
            view.onContactsLoaded(contactList);
        }
    }

    @Override
    public void start() {
        contactList = new ArrayList<>();
        loadContacts();
    }
}

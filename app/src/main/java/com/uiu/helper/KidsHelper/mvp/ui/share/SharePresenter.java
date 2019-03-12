package com.uiu.helper.KidsHelper.mvp.ui.share;

import com.uiu.helper.KidsHelper.mvp.model.ContactEntity;
import com.uiu.helper.KidsHelper.mvp.model.response.GetFavContactResponse;
import com.uiu.helper.KidsHelper.mvp.source.DataSource;
import com.uiu.helper.KidsHelper.mvp.source.Repository;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class SharePresenter implements ShareContract.Presenter {

    public ShareContract.View view;
    public PreferenceUtil preferenceUtil;
    public Repository repository;
    public List<ContactEntity> registerContactList;

    public SharePresenter(ShareContract.View view, PreferenceUtil preferenceUtil, Repository repository) {
        this.view = view;
        this.preferenceUtil = preferenceUtil;
        this.repository = repository;
        view.setPresenter(this);
    }





    @Override
    public void start() {
        registerContactList = new ArrayList<>();
        loadRegisterPeople();
    }

    @Override
    public void loadRegisterPeople() {
        view.showProgress();
        repository.getFavContacts(preferenceUtil.getAccount().getId(), new DataSource.GetDataCallback<GetFavContactResponse>() {
            @Override
            public void onDataReceived(GetFavContactResponse data) {
                view.hideProgress();
                if (data != null) {
                    if (data.isSuccess()) {
                        registerContactList.clear();
                        registerContactList.addAll(data.getRegdContactEntityList());
                        view.loadPeople(registerContactList);
                    }
                }


            }

            @Override
            public void onFailed(int code, String message) {
                view.hideProgress();
                view.showMessage(message);
            }
        });

    }

}

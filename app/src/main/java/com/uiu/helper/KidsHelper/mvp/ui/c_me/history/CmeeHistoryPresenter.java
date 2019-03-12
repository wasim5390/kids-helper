package com.uiu.helper.KidsHelper.mvp.ui.c_me.history;

import com.uiu.helper.KidsHelper.mvp.model.CmeeChatModel;
import com.uiu.helper.KidsHelper.mvp.model.File;
import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.KidsHelper.mvp.source.DataSource;
import com.uiu.helper.KidsHelper.mvp.source.Repository;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class CmeeHistoryPresenter implements CmeeHistoryContract.Presenter {

    public CmeeHistoryContract.View view;
    public Repository repository;
    public PreferenceUtil preferenceUtil;
    public List<File> cmeeHistoryList;
    private User account;
    private String kidId;
    private int totalPageCount=1;
    private int currentPage=0;

    public CmeeHistoryPresenter(CmeeHistoryContract.View view,String kidId, PreferenceUtil preferenceUtil, Repository repository) {
        this.view = view;
        this.repository = repository;
        this.preferenceUtil = preferenceUtil;
        this.account = preferenceUtil.getAccount();
        this.kidId = kidId==null?this.account.getId():kidId;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        view.showProgress();
        cmeeHistoryList= new ArrayList<>();
        fetchCmeeHistory();
    }

    @Override
    public void fetchCmeeHistory() {
      //  if(currentPage>=totalPageCount)
      //      return;
        repository.getCmeeHistoryList(account.getId(),kidId, String.valueOf(currentPage), new DataSource.GetDataCallback<CmeeChatModel>() {
            @Override
            public void onDataReceived(CmeeChatModel data) {
                currentPage++;
               // totalPageCount = data.getData().getPageCount();
                view.onHistoryLoaded(data);
                view.hideProgress();
            }

            @Override
            public void onFailed(int code, String message) {
                view.showMessage(message);
                view.hideProgress();
            }
        });
    }
}

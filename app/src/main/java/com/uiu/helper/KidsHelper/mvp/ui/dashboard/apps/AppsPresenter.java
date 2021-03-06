package com.uiu.helper.KidsHelper.mvp.ui.dashboard.apps;


import com.uiu.helper.KidsHelper.mvp.model.AppsEntity;
import com.uiu.helper.KidsHelper.mvp.source.Repository;

import java.util.ArrayList;
import java.util.List;

public class AppsPresenter implements AppsContract.Presenter {

    private AppsContract.View view;
    private Repository repository;
    private List<AppsEntity> applist =  new ArrayList<>();

    @Override
    public void start() {

    }
    public AppsPresenter(AppsContract.View view, Repository repository) {
        this.view = view;
        this.repository = repository;
        this.view.setPresenter(this);
    }

    @Override
    public void loadApps(List<AppsEntity> list) {
        applist.clear();
        applist.addAll(list);
        view.onAppListLoaded(applist);
    }

    @Override
    public void searchApps(String app) {
        if (!app.trim().isEmpty()) {
            List<AppsEntity> searchResults = new ArrayList<>();
            for (int i = 0; i < applist.size(); i++) {
                if (applist.get(i).getName().toLowerCase().contains(app.toLowerCase())) {
                    searchResults.add(applist.get(i));
                }
            }
            view.onAppListLoaded(searchResults);
        } else {
            view.onAppListLoaded(applist);
        }
    }

}

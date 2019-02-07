package com.uiu.helper.KidsHelper.mvp.ui.dashboard.apps;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import com.uiu.helper.KidsHelper.mvp.BaseActivity;
import com.uiu.helper.KidsHelper.mvp.Injection;
import com.uiu.helper.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.header_btn_right)
    ImageView btnRight;


    private AppsPresenter appsPresenter;
    private AppsFragment appsFragment;


    @Override
    public int getID() {
        return R.layout.activity_dashboard;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setToolBar(toolbar, "", true);
        btnRight.setVisibility(View.GONE);
        loadAppsFragment();
    }

    private void loadAppsFragment() {
        appsFragment = appsFragment != null ? appsFragment : AppsFragment.newInstance();
        appsPresenter = appsPresenter != null ? appsPresenter : new AppsPresenter(appsFragment, Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, appsFragment);
        fragmentTransaction.commit();
    }

    @OnClick(R.id.header_btn_left)
    public void onBackClick() {
        onBackPressed();
    }



}

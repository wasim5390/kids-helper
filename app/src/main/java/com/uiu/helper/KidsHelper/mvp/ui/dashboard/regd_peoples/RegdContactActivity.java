package com.uiu.helper.KidsHelper.mvp.ui.dashboard.regd_peoples;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.uiu.helper.KidsHelper.entities.UserEntity;
import com.uiu.helper.KidsHelper.mvp.BaseActivity;
import com.uiu.helper.KidsHelper.mvp.Injection;
import com.uiu.helper.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegdContactActivity extends BaseActivity {
    RegdContactFragment regdContactFragment;
    RegdContactPresenter regdContactPresenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    UserEntity user;

    @Override
    public int getID() {
        return R.layout.activity_dashboard;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setToolBar(toolbar,"",true);
        toolbar.findViewById(R.id.header_btn_right).setVisibility(View.GONE);
        user = (UserEntity) getIntent().getSerializableExtra(SELECTED_CONTACT);
        loadContactFragment();
    }

    private void loadContactFragment() {
        regdContactFragment = regdContactFragment !=null? regdContactFragment : RegdContactFragment.newInstance();
        regdContactPresenter = regdContactPresenter !=null? regdContactPresenter : new RegdContactPresenter(regdContactFragment,user , Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, regdContactFragment);
        fragmentTransaction.commit();
    }

    @OnClick(R.id.header_btn_left)
    public void onBackClick(){
        onBackPressed();
    }
}

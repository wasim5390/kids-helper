package com.uiu.helper.KidsHelper.mvp.ui.dashboard.contact;

import android.os.Bundle;
import android.view.View;


import com.uiu.helper.KidsHelper.mvp.BaseActivity;

import com.uiu.helper.R;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactActivity extends BaseActivity {
    ContactFragment contactFragment;
    ContactPresenter contactPresenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public int getID() {
        return R.layout.activity_dashboard;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setToolBar(toolbar,"",true);
        toolbar.findViewById(R.id.header_btn_right).setVisibility(View.GONE);
        loadContactFragment();
    }

    private void loadContactFragment() {
        contactFragment = contactFragment !=null? contactFragment : ContactFragment.newInstance();
        contactPresenter = contactPresenter !=null? contactPresenter : new ContactPresenter(contactFragment, ContactLoader.getInstance(this));
       // ContactsFragment fragment = new ContactsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, contactFragment);
        fragmentTransaction.commit();
    }

    @OnClick(R.id.header_btn_left)
    public void onBackClick(){
        onBackPressed();
    }
}

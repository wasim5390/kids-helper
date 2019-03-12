package com.uiu.helper.KidsHelper.mvp.ui.settings;

import android.os.Bundle;
import android.view.View;

import com.uiu.helper.KidsHelper.entities.UserEntity;
import com.uiu.helper.KidsHelper.mvp.BaseActivity;
import com.uiu.helper.KidsHelper.mvp.Injection;
import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.R;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

   private User user;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public int getID() {
        return R.layout.activity_dashboard;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        user = (User)getIntent().getSerializableExtra(SELECTED_CONTACT);
        toolbar.findViewById(R.id.header_btn_right).setVisibility(View.GONE);
        setToolBar(toolbar,getString(R.string.setting_title),true);
        loadSettingsFragment();
    }

    private void loadSettingsFragment() {

        SettingFragment fragment = SettingFragment.newInstance(user.getId());
        SettingPresenter presenter = new SettingPresenter(fragment, user, PreferenceUtil.getInstance(this),Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @OnClick(R.id.header_btn_left)
    public void onBackClick(){
        onBackPressed();
    }






}

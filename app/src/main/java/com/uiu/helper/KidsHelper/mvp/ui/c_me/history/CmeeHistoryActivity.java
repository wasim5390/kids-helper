package com.uiu.helper.KidsHelper.mvp.ui.c_me.history;

import android.os.Bundle;

import com.uiu.helper.KidsHelper.mvp.BaseActivity;
import com.uiu.helper.KidsHelper.mvp.Injection;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.R;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CmeeHistoryActivity extends BaseActivity {

    private CmeeHistoryFragment historyFragment;
    private CmeeHistoryPresenter historyPresenter;
    String kidId;
    @Override
    public int getID() {
        return R.layout.activity_chat_history;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        if(getIntent().hasExtra("KidId"))
        kidId = getIntent().getStringExtra("KidId");
        loadHistoryFragment();
    }

    private void loadHistoryFragment() {
        historyFragment = historyFragment != null ? historyFragment : CmeeHistoryFragment.newInstance();
        historyPresenter = historyPresenter != null ? historyPresenter : new CmeeHistoryPresenter(historyFragment,kidId,
                PreferenceUtil.getInstance(this), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, historyFragment,"CmeeHistoryFragment");
        fragmentTransaction.commit();
    }


    @OnClick(R.id.btnBack)
    public void onBackClick(){
        onBackPressed();
    }
}

package com.uiu.helper.KidsHelper.mvp.ui.share;

import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;


import com.uiu.helper.KidsHelper.mvp.BaseActivity;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.Injection;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.R;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.ButterKnife;

public class ShareActivity extends BaseActivity {

    public ShareFragment shareFragment;
    public SharePresenter sharePresenter;

    int fileType =4;
    String filePath=null;

    @Override
    public int getID() {
        return R.layout.activity_message2;
    }

    @Override
    public void created(Bundle savedInstanceState) {

        ButterKnife.bind(this);

        fileType = getIntent().getIntExtra(Constant.RECORDED_FILE_TYPE,MEDIA_FILE);
        filePath = getIntent().getStringExtra(Constant.RECORDED_FILE_PATH);
        loadMessageFragment();

    }



    private void loadMessageFragment() {
        shareFragment = shareFragment != null ? shareFragment : ShareFragment.newInstance(filePath,fileType);
        sharePresenter = sharePresenter != null ? sharePresenter : new SharePresenter(shareFragment,
                PreferenceUtil.getInstance(this), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.chatframeLayout, shareFragment,"shareFragment");
        fragmentTransaction.commit();
    }


    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onStop() {

        super.onStop();

    }


}

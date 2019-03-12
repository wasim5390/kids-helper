package com.uiu.helper.KidsHelper.mvp.ui.camera.editor;

import android.os.Bundle;

import com.uiu.helper.KidsHelper.mvp.BaseActivity;
import com.uiu.helper.KidsHelper.mvp.Injection;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.R;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class PhotoEditorActivity extends BaseActivity {

    private static final String TAG = PhotoEditorActivity.class.getSimpleName();

    PhotoEditorFragment photoEditorFragment;
    PhotoEditorPresenter presenter;
    private String senderId;
    @Override
    public int getID() {
        return R.layout.activity_photo_edit;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        String path = getIntent().getStringExtra(CAMERA_IMG_PATH);
        if(getIntent().hasExtra("SenderId"))
            senderId = getIntent().getStringExtra("SenderId");
        photoEditorFragment = photoEditorFragment != null ? photoEditorFragment : PhotoEditorFragment.newInstance(path,senderId);
        presenter = presenter!=null ? presenter: new PhotoEditorPresenter(photoEditorFragment, PreferenceUtil.getInstance(this).getAccount().getId(), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, photoEditorFragment);
        fragmentTransaction.commit();

    }



}

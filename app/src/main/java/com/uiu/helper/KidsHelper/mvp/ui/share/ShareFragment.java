package com.uiu.helper.KidsHelper.mvp.ui.share;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.uiu.helper.KidsHelper.mvp.BaseFragment;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.model.ContactEntity;
import com.uiu.helper.KidsHelper.mvp.source.Repository;
import com.uiu.helper.KidsHelper.mvp.ui.media_notification.UploadFileService;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.R;

import java.util.List;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareFragment extends BaseFragment implements ShareContract.View, ShareToAdapterList.Callback {
    public ShareContract.Presenter presenter;

    @BindView(R.id.rvMessage)
    public RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    ContentLoadingProgressBar progressBar;
    public ShareToAdapterList adapter;


     String FILE_PATH =null;
     int FILE_TYPE =4;

    public static ShareFragment newInstance(String filePath, int fileType) {
        Bundle args = new Bundle();
        ShareFragment instance = new ShareFragment();
        args.putString(Constant.RECORDED_FILE_PATH,filePath);
        args.putInt(Constant.RECORDED_FILE_TYPE,fileType);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public int getID() {
        return R.layout.fragment_message;
    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(getActivity());
         FILE_PATH = getArguments().getString(Constant.RECORDED_FILE_PATH);
         FILE_TYPE = getArguments().getInt(Constant.RECORDED_FILE_TYPE);
         if(presenter==null)
             presenter = new SharePresenter(this, PreferenceUtil.getInstance(getContext()), Repository.getInstance());
        presenter.start();
        setAdapter();
    }

    private void setAdapter() {
        adapter = new ShareToAdapterList(getContext(), this);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setPresenter(ShareContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Override
    public void showProgress() {
        progressBar.show();
    }

    @Override
    public void hideProgress() {
        progressBar.hide();
    }

    @Override
    public void onSlideItemClick(ContactEntity slideItem, boolean isSelected) {

        if(FILE_PATH==null)
            return;
        UploadFileService.uploadMedia(getActivity().getApplicationContext(),FILE_PATH,FILE_TYPE,slideItem.getId());
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void loadPeople(List<ContactEntity> list) {
        adapter.setSlideItems(list);
    }

    @Override
    public void showMessage(String msg) {

        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }



}
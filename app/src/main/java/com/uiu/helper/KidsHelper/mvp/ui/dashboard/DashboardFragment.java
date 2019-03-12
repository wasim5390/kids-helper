package com.uiu.helper.KidsHelper.mvp.ui.dashboard;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uiu.helper.KidsHelper.mvp.BaseFragment;
import com.uiu.helper.KidsHelper.mvp.Injection;
import com.uiu.helper.KidsHelper.mvp.events.SlideCreateEvent;
import com.uiu.helper.KidsHelper.mvp.events.SlideEvent;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;
import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

import static com.uiu.helper.KidsHelper.mvp.model.response.GetAllSlidesResponse.SlideSerialComparator;


public class DashboardFragment extends BaseFragment implements DashboardContract.View,
        View.OnClickListener {

    DashboardContract.Presenter presenter;
    List<SlideItem> slides;

    @BindView(R.id.dashboard_pager)
    ViewPager fragmentPager;
    PagerAdapter pagerAdapter;

    @BindView(R.id.home_left_btn)
    public ImageView hLefttBtn;

    @BindView(R.id.home_right_btn)
    public ImageView hRightBtn;
    User userEntity;

    @Override
    public int getID() {
        return R.layout.fragment_dashboard;
    }

    @Override
    public void initUI(View view) {
        EventBus.getDefault().register(this);
        if(presenter!=null)
            presenter.getUserSlides();
        addListner();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void addListner() {
        hLefttBtn.setOnClickListener(this);
        hRightBtn.setOnClickListener(this);
    }

    public static DashboardFragment newInstance() {
        Bundle args = new Bundle();
        DashboardFragment dashboardFragment = new DashboardFragment();
        dashboardFragment.setArguments(args);
        return dashboardFragment;
    }


    private void setViewPager(User selectedUser,List<SlideItem> slides) {
        this.slides = slides;
        hLefttBtn.setVisibility(View.GONE);
        if (pagerAdapter != null)
            pagerAdapter.setSlides(slides);
        else {
            pagerAdapter = new PagerAdapter(getChildFragmentManager(), slides,selectedUser,Injection.provideRepository(getContext()),PreferenceUtil.getInstance(getContext()));

            fragmentPager.setAdapter(pagerAdapter);
            fragmentPager.setOffscreenPageLimit(pagerAdapter.getCount() - 1);
            fragmentPager.setCurrentItem(0);
            pagerAdapter.notifyDataSetChanged();
            fragmentPager.setPageMargin(32);
            fragmentPager.invalidate();
        }

        fragmentPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int total =fragmentPager.getAdapter().getCount();
                if(position>=total-1){
                    hRightBtn.setVisibility(View.GONE);
                }else if(position==0){
                    hLefttBtn.setVisibility(View.GONE);
                }else{
                    hLefttBtn.setVisibility(View.VISIBLE);
                    hRightBtn.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }



    @Override
    public void showMessage(String message) {
        Toast.makeText(mBaseActivity, message, Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onSlidesLoaded(User user, List<SlideItem> slideItems) {
        try {
            userEntity = user;
            setViewPager(user,slideItems);
            moveToSlide(SLIDE_INDEX_NOTIFICATIONS);
        }catch (Exception e){
            Log.v("DashboardFragment",e.getMessage());
        }


    }


    public void moveToSlide(int slideType) {

        int slideIndexToMove =pagerAdapter.getSlideIndex(slideType);
        fragmentPager.setCurrentItem(slideIndexToMove,true);
    }

    @Override
    public void onSlidesUpdated(List<SlideItem> slides) {

        pagerAdapter.setSlides(slides);
        fragmentPager.setSaveFromParentEnabled(false);
        fragmentPager.invalidate();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SlideCreateEvent receiveEvent) {
        presenter.addSlide(receiveEvent.getSlide());
    }

    @Override
    public void setPresenter(DashboardContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.home_right_btn:
                // fragmentPager.arrowScroll(ViewPager.FOCUS_RIGHT);
                fragmentPager.setCurrentItem(fragmentPager.getCurrentItem()+1,true);
                Log.e("current item", String.valueOf(fragmentPager.getOffscreenPageLimit()));
                break;

            case R.id.home_left_btn:
                fragmentPager.setCurrentItem(fragmentPager.getCurrentItem()-1,true);
                // fragmentPager.arrowScroll(ViewPager.FOCUS_LEFT);
                break;
        }
    }

    @Override
    public void hideProgress() {
        try {
            mBaseActivity.hideProgress();
        }catch (Exception e){

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SlideEvent receiveEvent) {
        if(receiveEvent.isAddingSlide())
        {
            presenter.addSlide(receiveEvent.getSlideItem());

        }
        if(!receiveEvent.isAddingSlide())
        {
            presenter.removeSlide(receiveEvent.getSlideItem());
        }
    }

}

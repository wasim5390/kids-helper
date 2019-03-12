package com.uiu.helper.KidsHelper.mvp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.uiu.helper.KidsHelper.mvp.BaseFragment;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.Injection;
import com.uiu.helper.KidsHelper.mvp.events.NotificationReceiveEvent;
import com.uiu.helper.KidsHelper.mvp.events.ShareEvent;
import com.uiu.helper.KidsHelper.mvp.model.Invitation;
import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.KidsHelper.mvp.ui.InvitationConfirmationCallback;
import com.uiu.helper.KidsHelper.mvp.ui.dashboard.DashboardActivity;
import com.uiu.helper.KidsHelper.mvp.ui.home.secondary_helper.SecondaryHelpersFragment;
import com.uiu.helper.KidsHelper.mvp.ui.invite.InviteFragment;
import com.uiu.helper.KidsHelper.mvp.ui.invite.InvitePresenter;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeListFragment extends BaseFragment implements HomeContract.View,HomeListAdapter.Callback,
        InvitationConfirmationCallback{

    private static final String TAG = "HomeListFragment";

    public HomeContract.Presenter presenter;
    public HomeListAdapter adapter;

    public User selectedUser;

    @BindView(R.id.rvKids)
    RecyclerView recyclerView;

    @BindView(R.id.message)
    public TextView tvMessage;

    @BindView(R.id.progressBar)
    ContentLoadingProgressBar progressBar;

    @BindView(R.id.btnConnectKids)
    public Button btnConnectKids;

    public static HomeListFragment newInstance()
    {
        Bundle args=new Bundle();
        HomeListFragment instance=new HomeListFragment();
        instance.setArguments(args);
        return instance;
    }

    @Override
    public int getID() {
        return R.layout.fragment_home;
    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(getActivity());
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        setAdapter();
        if(presenter!=null)
        presenter.start();
    }

    private void setAdapter() {
        adapter = new HomeListAdapter(getContext(), new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Unregister");
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
       // hideProgress();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(mBaseActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInvitesLoaded(List<Invitation> list) {

        tvMessage.setVisibility(!list.isEmpty()?View.GONE:View.VISIBLE);
        btnConnectKids.setVisibility(!list.isEmpty()?View.GONE:View.VISIBLE);
        adapter.setItems(list);
    }

    @Override
    public void onSecondaryHelpersLoaded(List<User> helpers,Invitation invitation,boolean switchRoleWithDisconnect) {
        if(helpers!=null && !helpers.isEmpty()) {
            SecondaryHelpersFragment fragment = SecondaryHelpersFragment.getInstance();
            fragment.setSecondaryHelpers(helpers);
            fragment.setInvite(invitation,switchRoleWithDisconnect);
            fragment.setCallback(this);
            fragment.show(getChildFragmentManager(), null);
        }else{
            showMessage(invitation.getInvitee().getEmail()+" don't have any other helper connected.");
          //  presenter.rejectInvitation(invitation,null);
        }
    }


    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Override
    public void onItemClick(Invitation slideItem) {
        if(slideItem.getStatus()==INVITE.CONNECTED) {
            slideItem.getInvitee().setPrimary(slideItem.isPrimary());
            Intent intent = new Intent(getActivity(), DashboardActivity.class);
            intent.putExtra(SELECTED_CONTACT, slideItem.getInvitee());
            startActivity(intent);
        }
        if(slideItem.getStatus() == INVITE.INVITED || slideItem.getStatus() == INVITE.REJECTED){
            mBaseActivity.showInvitationActionsDialog(getContext(),slideItem,this);
        }
    }

    @Override
    public void onItemLongClick(final Invitation slideItem) {
        mBaseActivity.showInvitationActionsDialog(getContext(),slideItem,this);
    }

    @OnClick(R.id.btnConnectKids)
    public void onKidConnectClick(){
        InviteFragment fragment  = InviteFragment.newInstance();
        InvitePresenter presenter = new InvitePresenter(fragment, Injection.provideRepository(getContext()), PreferenceUtil.getInstance(getContext()).getAccount());
        mBaseActivity.replaceFragment(fragment);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
        if(receiveEvent.getNotificationForSlideType()== Constant.INVITE_CODE){
           /* JSONObject jsonObject = receiveEvent.getNotificationResponse();
           AppsEntity entity =  new Gson().fromJson(jsonObject.toString(),AppsEntity.class);
           if(entity.hasAccess()){
               presenter.updateEntity(entity);
           }*/
            presenter.getInvites();
        }

    }


    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.show();
    }

    @Override
    public void hideProgress() {
        progressBar.hide();
    }

    @Override
    public void onResendInvitation(Invitation invitation) {
        presenter.resendInvitation(invitation);
    }

    @Override
    public void onRejectInvitation(Invitation invitation) {

        if(invitation.isPrimary())
            presenter.getSecondaryHelpers(invitation,true);
        else {
            presenter.rejectInvitation(invitation,null,true);
        }
    }

    @Override
    public void onDeleteInvitation(Invitation invitation) {

        presenter.disconnect(invitation);
    }

    @Override
    public void onInvitationSwitchWithDisconnect(Invitation invitation, User switchingUser, boolean switchRoleWithDisconnect) {
        presenter.rejectInvitation(invitation,switchingUser.getId(),switchRoleWithDisconnect);
    }

    @Override
    public void onInvitationSwitchClick(Invitation invitation) {
        presenter.getSecondaryHelpers(invitation,false);
    }
}

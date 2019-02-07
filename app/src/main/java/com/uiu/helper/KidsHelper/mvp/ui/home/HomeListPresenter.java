package com.uiu.helper.KidsHelper.mvp.ui.home;

import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.model.Invitation;
import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.KidsHelper.mvp.model.response.BaseResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetSecondaryHelperResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.InvitationResponse;
import com.uiu.helper.KidsHelper.mvp.source.DataSource;
import com.uiu.helper.KidsHelper.mvp.source.Repository;

import java.util.ArrayList;
import java.util.List;

public class HomeListPresenter implements HomeContract.Presenter {

    public HomeContract.View view;
    public Repository repository;

    public List<Invitation> mInviteList;

    private User userEntity;

    public HomeListPresenter(HomeContract.View view, Repository repository, User mUserObj) {
        this.view = view;
        this.repository = repository;
        this.userEntity = mUserObj;
        view.setPresenter(this);
    }

    @Override
    public void getInvites() {
        view.showProgress();
        repository.getInvites(userEntity.getId(), new DataSource.GetDataCallback<InvitationResponse>() {
            @Override
            public void onDataReceived(InvitationResponse data) {
                view.hideProgress();
                mInviteList.clear();
                mInviteList.addAll(data.getInvitationList());
                view.onInvitesLoaded(mInviteList);
            }

            @Override
            public void onFailed(int code, String message) {
                view.hideProgress();
                view.showMessage(message);
            }
        });
    }

    @Override
    public void disconnect(Invitation invitation) {
        view.showProgress();
        repository.disconnect(invitation.getInviteId(), new DataSource.GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                view.hideProgress();
                mInviteList.remove(invitation);
                view.onInvitesLoaded(mInviteList);
            }

            @Override
            public void onFailed(int code, String message) {
                view.hideProgress();
                view.showMessage(message);
            }
        });
    }

    @Override
    public void resendInvitation(Invitation invitation) {
        view.showProgress();
        repository.resendInvite(invitation.getInviteId(), new DataSource.GetResponseCallback<InvitationResponse>() {
            @Override
            public void onSuccess(InvitationResponse response) {
                view.hideProgress();
                view.showMessage("Just resent an invite to "+response.getInvitation().getInvitee().getEmail());
                getInvites();
                //view.onInvitesLoaded(mInviteList);
            }

            @Override
            public void onFailed(int code, String message) {
                view.hideProgress();
                view.showMessage(message);
            }
        });
    }

    @Override
    public void rejectInvitation(Invitation invitation,String secondaryHelperToSwitch,boolean switchRoleWithDisconnect) {
        if(switchRoleWithDisconnect)
            invitation.setStatus(Constant.INVITE.REJECTED);
        view.showProgress();
        repository.updateInvite(invitation.getInviteId(),userEntity.getId(),secondaryHelperToSwitch,invitation.getStatus(), new DataSource.GetResponseCallback<InvitationResponse>() {
            @Override
            public void onSuccess(InvitationResponse response) {
                view.hideProgress();
                getInvites();

            }

            @Override
            public void onFailed(int code, String message) {
                view.hideProgress();
                view.showMessage(message);
            }
        });
    }

    @Override
    public void getSecondaryHelpers(Invitation invitation, boolean switchRoleWithDisconnect) {
        view.showProgress();
        repository.getSecondaryHelper(invitation.getInvitee().getId(), new DataSource.GetDataCallback<GetSecondaryHelperResponse>() {
            @Override
            public void onDataReceived(GetSecondaryHelperResponse data) {
                view.hideProgress();
                if(data.isSuccess())
                    view.onSecondaryHelpersLoaded(data.getSecondaryHelpers(),invitation,switchRoleWithDisconnect);
                else
                    view.showMessage(data.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                view.hideProgress();
                view.showMessage(message);
            }
        });
    }

    @Override
    public void start() {
        mInviteList  = new ArrayList<>();
        getInvites();
    }
}

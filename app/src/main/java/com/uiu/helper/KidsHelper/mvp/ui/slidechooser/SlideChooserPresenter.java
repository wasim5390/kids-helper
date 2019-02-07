package com.uiu.helper.KidsHelper.mvp.ui.slidechooser;

import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.KidsHelper.mvp.model.response.InvitationResponse;
import com.uiu.helper.KidsHelper.mvp.source.DataSource;
import com.uiu.helper.KidsHelper.mvp.source.Repository;


public class SlideChooserPresenter implements SlideChooserContract.Presenter {

    public SlideChooserContract.View view;
    public Repository repository;

    private User userEntity;

    public SlideChooserPresenter(SlideChooserContract.View view, Repository repository, User mHelper) {
        this.view = view;
        this.repository = repository;
        this.userEntity = mHelper;
        view.setPresenter(this);
    }



    @Override
    public void start() {
    }

    @Override
    public void sendInvite(String email, boolean isPrimary) {
        view.showProgress();
        repository.sendInvite(email,userEntity.getId(), new DataSource.GetResponseCallback<InvitationResponse>() {
            @Override
            public void onSuccess(InvitationResponse data) {
                view.hideProgress();
                if(data.getInvitation()==null)
                    view.onInviteSent(data.getResponseMsg());
                else
                view.onInviteExist(data.getInvitation());
            }

            @Override
            public void onFailed(int code, String message) {
                view.hideProgress();
                view.showMessage(message);
            }
        });
    }
}

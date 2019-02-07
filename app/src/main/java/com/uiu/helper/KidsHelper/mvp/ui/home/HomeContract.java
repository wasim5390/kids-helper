package com.uiu.helper.KidsHelper.mvp.ui.home;

import android.content.Intent;

import com.uiu.helper.KidsHelper.entities.UserEntity;
import com.uiu.helper.KidsHelper.mvp.BasePresenter;
import com.uiu.helper.KidsHelper.mvp.BaseView;
import com.uiu.helper.KidsHelper.mvp.model.Invitation;
import com.uiu.helper.KidsHelper.mvp.model.User;

import java.util.List;

public class HomeContract {

   public interface View extends BaseView<Presenter> {
        void showMessage(String message);
        void onInvitesLoaded(List<Invitation> list);
        void onSecondaryHelpersLoaded(List<User> helpers,Invitation invitation,boolean switchRoleWithDisconnect);
    }

   public interface Presenter extends BasePresenter {
        void getInvites();
        void disconnect(Invitation invitation);
        void resendInvitation(Invitation invitation);
        void rejectInvitation(Invitation invitation, String secondaryHelperId,boolean switchRoleWithDisconnect);
        void getSecondaryHelpers(Invitation invitation,boolean switchRoleWithDisconnect);
    }
}

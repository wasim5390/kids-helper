package com.uiu.helper.KidsHelper.mvp.ui.slidechooser;

import com.uiu.helper.KidsHelper.mvp.BasePresenter;
import com.uiu.helper.KidsHelper.mvp.BaseView;
import com.uiu.helper.KidsHelper.mvp.model.Invitation;

public class SlideChooserContract {

   public interface View extends BaseView<Presenter> {
        void showMessage(String message);
        void onInviteSent(String message);
        void onInviteExist(Invitation invitation);
    }

   public interface Presenter extends BasePresenter {
        void sendInvite(String email, boolean isPrimary);
    }
}

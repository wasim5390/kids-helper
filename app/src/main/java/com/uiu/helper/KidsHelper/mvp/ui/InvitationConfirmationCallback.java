package com.uiu.helper.KidsHelper.mvp.ui;

import com.uiu.helper.KidsHelper.entities.UserEntity;
import com.uiu.helper.KidsHelper.mvp.model.Invitation;
import com.uiu.helper.KidsHelper.mvp.model.User;

public interface InvitationConfirmationCallback {
    void onResendInvitation(Invitation invitation);
    void onRejectInvitation(Invitation invitation);
    void onDeleteInvitation(Invitation invitation);
    void onInvitationSwitchWithDisconnect(Invitation invitation, User switchingUser,boolean switchRoleWithDisconnect);
    void onInvitationSwitchClick(Invitation invitation);
}

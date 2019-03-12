package com.uiu.helper.KidsHelper.mvp.ui.invite;

import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uiu.helper.KidsHelper.mvp.BaseFragment;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.events.MainUIActionEvent;
import com.uiu.helper.KidsHelper.mvp.model.Invitation;
import com.uiu.helper.KidsHelper.mvp.ui.dashboard.DashboardActivity;
import com.uiu.helper.R;
import com.uiu.helper.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.AppCompatEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class InviteFragment extends BaseFragment implements InviteContract.View{

    public InviteContract.Presenter presenter;

    @BindView(R.id.invite_email_input_field)
    AppCompatEditText autoComplete;

    @BindView(R.id.tvCloseHelp)
    TextView tvCloseHelp;
    @BindView(R.id.rlWantHelp)
    RelativeLayout rlWantHelp;
    @BindView(R.id.rlCloseHelp)
    RelativeLayout rlCloseHelp;
    @BindView(R.id.llHelpContent)
    LinearLayout llHelpContent;

    public static InviteFragment newInstance()
    {
        Bundle args=new Bundle();
        InviteFragment instance=new InviteFragment();
        instance.setArguments(args);
        return instance;
    }

    @Override
    public int getID() {
        return R.layout.fragment_invite;
    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(getActivity());
        EventBus.getDefault().post(new MainUIActionEvent(View.VISIBLE));

    }


    @Override
    public void showMessage(String message) {
     //   Toast.makeText(mBaseActivity, message, Toast.LENGTH_SHORT).show();
        Utils.showAlert(getActivity(),message, getResources().getColor(R.color.PrimaryColor));
    }

    @Override
    public void onInviteSent(String message) {
        Utils.showAlert(getActivity(),message, getResources().getColor(R.color.PrimaryColor));
        mBaseActivity.loadHomeFragment();
    }

    @Override
    public void onInviteExist(Invitation invitation) {
        String invitationStatus = "";
        switch (invitation.getStatus()) {

            case INVITE.CONNECTED:

                invitationStatus = getResources().getString(R.string.status_connected);
                break;

            case INVITE.INVITED:

                invitationStatus = getResources().getString(R.string.status_sent_from_companion);
                break;

            case INVITE.REJECTED:
                invitationStatus = getResources().getString(R.string.status_rejected);
                break;

        }

        Utils.showAlert(getActivity(),invitationStatus, getResources().getColor(R.color.PrimaryColor));

    }




    @Override
    public void setPresenter(InviteContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    private void inviteContact() {
        String email = autoComplete.getText().toString();

        if(!Utils.isValidEmail(email)) {
            Utils.showAlert(getActivity(),"Please enter valid email!",
                    getResources().getColor(R.color.PrimaryColor));
            return;
        }
        else
        {
            presenter.sendInvite(email);
        }

    }


    @OnClick(R.id.invite_list_invite_btn)
    public void onSendInviteClick(){
        inviteContact();
    }

    @OnLongClick(R.id.invite_list_invite_btn)
    public boolean onSendInviteLongClick(){
        mBaseActivity.loadHomeFragment();
        return true;
    }


}

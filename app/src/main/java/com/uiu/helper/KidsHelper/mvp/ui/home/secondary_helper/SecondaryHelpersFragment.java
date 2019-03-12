package com.uiu.helper.KidsHelper.mvp.ui.home.secondary_helper;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.model.Invitation;
import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.KidsHelper.mvp.ui.InvitationConfirmationCallback;
import com.uiu.helper.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SecondaryHelpersFragment extends DialogFragment implements Constant,SecondaryHelperListAdapter.Callback {

    RecyclerView rvSecondaryHelper;


    List<User> mSecondaryHelpers;

    Invitation mInvite;
    boolean switchRoleWithDisconnect;

    public void setCallback(InvitationConfirmationCallback mCallback) {
        this.mCallback = mCallback;
    }

    InvitationConfirmationCallback mCallback;

    private static SecondaryHelpersFragment mInstance;

    public static SecondaryHelpersFragment getInstance(){
        if(mInstance==null)
            mInstance = new SecondaryHelpersFragment();
        return mInstance;

    }

    public void setSecondaryHelpers(List<User> mSecondaryHelpers) {
        this.mSecondaryHelpers = mSecondaryHelpers;
    }

    public List<User> getSecondaryHelpers() {
        return mSecondaryHelpers;
    }

    public Invitation getInvite() {
        return mInvite;
    }

    public void setInvite(Invitation mInvite, boolean switchRoleWithDisconnect) {
        this.mInvite = mInvite;
        this.switchRoleWithDisconnect = switchRoleWithDisconnect;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_secondary_helper, null);

        rvSecondaryHelper = (RecyclerView)v.findViewById(R.id.rvSecondaryHelper);
        rvSecondaryHelper.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        final SecondaryHelperListAdapter adapter = new SecondaryHelperListAdapter(getContext(),getSecondaryHelpers() , this);

        rvSecondaryHelper.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Choose any Other as Primary Helper").setView(v);

        return builder.create();
    }

    @Override
    public void onItemClick(User slideItem) {
        mCallback.onInvitationSwitchWithDisconnect(getInvite(),slideItem,switchRoleWithDisconnect);
        dismiss();
    }
}

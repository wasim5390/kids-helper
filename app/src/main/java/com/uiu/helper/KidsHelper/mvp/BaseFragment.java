package com.uiu.helper.KidsHelper.mvp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.uiu.helper.KidsHelper.mvp.events.ShareEvent;
import com.uiu.helper.KidsHelper.mvp.model.Invitation;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;
import com.uiu.helper.KidsHelper.mvp.ui.ConfirmationCallback;
import com.uiu.helper.KidsHelper.mvp.ui.InvitationConfirmationCallback;
import com.uiu.helper.KidsHelper.mvp.ui.dashboard.GoogleLoginDialog;
import com.uiu.helper.R;
import com.uiu.helper.util.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;

import static com.uiu.helper.KidsHelper.mvp.model.response.GetAllSlidesResponse.SlideSerialComparator;

/**
 * Created by sidhu on 4/12/2018.
 */

public abstract class BaseFragment  extends Fragment implements Constant{
    public BaseActivity mBaseActivity;
    public View view;
    public Animation animScale;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            mBaseActivity = (BaseActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(getID(), container, false);
        ButterKnife.bind(this,view);
        this.view = view;
        animScale = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale);
        initUI(view);
        return view;
    }

    public abstract int getID();

    public abstract void initUI(View view);

    public View getView(){
        return this.view;
    }

    public void showProgress() {
        try {
            if (mBaseActivity != null)
                mBaseActivity.showProgress();
        }catch (Exception e){
            Log.e("ProgresBar",e.getMessage());
        }
    }

    public void hideProgress() {
        mBaseActivity.hideProgress();
    }

    public void showDeleteFromSlideDialog(String title, String body, String slideId, String itemId, ConfirmationCallback callback){
        mBaseActivity.showConfirmationDialog(title,body,slideId,itemId,callback);
    }

    protected File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }


    protected void updateSearchView(SearchView searchView){
        EditText searchEditText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.Gray));
        searchEditText.setHintTextColor(getResources().getColor(R.color.Black));
        searchEditText.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 28F, getResources().getDisplayMetrics()));
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.helvetica_neue_lt_light);
        searchEditText.setTypeface(typeface);
    }

    protected void showInvitationDialog(Invitation invitation, InvitationConfirmationCallback callback){
        mBaseActivity.showInvitationActionsDialog(getActivity(),invitation,callback);
    }

    public void sortSlides(List<SlideItem> slides){
        Collections.sort(slides,SlideSerialComparator);
    }


    protected void showRemoveDialog(String slideTitle,boolean isLastSlide,IRemoveOnSlideListener listener) {
        Dialog customDialog;
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.remove_onslide_dialog, null);
        ((TextView)dialogView.findViewById(R.id.slide_remove_item)).setText("Remove "+slideTitle);
        customDialog = Utils.showHeaderDialog(getActivity(),dialogView);
        dialogView.findViewById(R.id.remove_slide_view).setVisibility(isLastSlide?View.GONE:View.VISIBLE);
        customDialog.findViewById(R.id.slide_cancel).setOnClickListener(v -> {
            customDialog.dismiss();
        });
        customDialog.findViewById(R.id.slide_remove_item).setOnClickListener(v -> {
            listener.onRemoveItemClick();
            customDialog.cancel();
        });
        customDialog.findViewById(R.id.remove_slide).setOnClickListener(v -> {
            listener.onRemoveSlideClick();
            customDialog.cancel();
        });
        customDialog.setOnDismissListener(dialog -> {

        });
    }

    protected void showAddDialog(String slideTitle,boolean showAddNewItem,IAddOnSlideListener listener) {
        Dialog customDialog;
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.add_onslide_dialog, null);
        ((TextView)dialogView.findViewById(R.id.slide_add_item)).setText("Add "+slideTitle);
         //   dialogView.findViewById(R.id.layout_add_item).setVisibility(showAddNewItem?View.VISIBLE:View.GONE);
        customDialog = Utils.showHeaderDialog(getActivity(),dialogView);
        customDialog.findViewById(R.id.slide_cancel).setOnClickListener(v -> {
            customDialog.dismiss();
        });
        customDialog.findViewById(R.id.slide_add_item).setOnClickListener(v -> {
            listener.onAddItemClick();
            customDialog.dismiss();
        });
        customDialog.findViewById(R.id.add_slide).setOnClickListener(v -> {
            listener.onAddSlideClick();
            customDialog.dismiss();
        });
    }

    public void showMediaNotification(ShareEvent event){
        mBaseActivity.showMediaNotification(event);
    }

    public interface IRemoveOnSlideListener{
        void onRemoveSlideClick();
        void onRemoveItemClick();
        void onDismiss();
    }

    public interface IAddOnSlideListener{
        void onAddSlideClick();
        void onAddItemClick();
    }

    public interface INavigation{
        void moveToSlide(int slideType);
    }
}

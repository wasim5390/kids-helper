package com.uiu.helper.KidsHelper.mvp.ui.slides.links;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uiu.helper.KidsHelper.mvp.BaseFragment;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.events.NotificationReceiveEvent;
import com.uiu.helper.KidsHelper.mvp.events.SlideCreateEvent;
import com.uiu.helper.KidsHelper.mvp.events.SlideEvent;
import com.uiu.helper.KidsHelper.mvp.model.LinksEntity;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;
import com.uiu.helper.KidsHelper.mvp.ui.ConfirmationCallback;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.R;
import com.uiu.helper.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FavoriteLinksFragment extends BaseFragment implements FavoriteLinksContract.View,FavoriteLinksAdapter.Callback{

    private static final String TAG = "FavoriteLinksFragment";
    @BindView(R.id.tvFavAppsTitle)
    TextView title;
    @BindView(R.id.btnRemove)
    TextView tvRemove;

    private boolean isEditorEnabled=false;
    public FavoriteLinksContract.Presenter presenter;
    public RecyclerView rvFavoriteLinks;
    public FavoriteLinksAdapter adapter;

    public static FavoriteLinksFragment newInstance()
    {
        Bundle args=new Bundle();
        FavoriteLinksFragment instance=new FavoriteLinksFragment();
        instance.setArguments(args);
        return instance;
    }

    @Override
    public int getID() {
        return R.layout.fragment_favorite_slides;
    }

    @Override
    public void initUI(View view) {
        EventBus.getDefault().register(this);
        init(view);
        title.setText(R.string.favorite_links);
        title.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.links_slide_title_color));
        setAdapter();
        if(presenter!=null)
            presenter.start();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Unregister");
        EventBus.getDefault().unregister(this);
    }
    private void init(View view) {
        rvFavoriteLinks=(RecyclerView) view.findViewById(R.id.rvFavApps);
    }

    private void setAdapter() {
        adapter = new FavoriteLinksAdapter(getContext(),new ArrayList<>(),this);
        rvFavoriteLinks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
        rvFavoriteLinks.setHasFixedSize(true);
        rvFavoriteLinks.setAdapter(adapter);

    }

    @Override
    public void setPresenter(FavoriteLinksContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Override
    public void onFavoriteLinksLoaded(List<LinksEntity> linksEntities) {
        adapter.setSlideItems(linksEntities);
    }


    @Override
    public void showMassage(String msg) {
        Utils.showAlert(getActivity(),msg, ContextCompat.getColor(getContext(),R.color.contacts_bg));
    }

    @Override
    public void slideSerial(int serial,int count) {
        serial++;
        String pageNum = serial+"/"+count;
        ((TextView)getView().findViewById(R.id.tvFavAppsTitle)).setText(getString(R.string.favorite_links)+" ("+pageNum+")");
    }

    @Override
    public void onSlideCreated(SlideItem slide) {
        EventBus.getDefault().post(new SlideEvent(slide,true));
    }

    @Override
    public void onSlideRemoved(SlideItem slide) {
        EventBus.getDefault().post(new SlideEvent(slide,false));
    }

    @Override
    public void onFavoriteLinkDataLoaded(String originalLink, LinksEntity entity) {
        if(entity==null)
        {
            entity = new LinksEntity(originalLink,"","","");
        }
        presenter.addFavLinkOnSlide(entity);
    }

    @Override
    public void onNewSlideCreated(SlideItem newSlide) {

    }

    @Override
    public void itemAddedOnNewSlide(SlideItem newSlide) {
        EventBus.getDefault().postSticky(new SlideCreateEvent(newSlide));
    }

    @Override
    public void onSlideItemClick(LinksEntity slideItem) {

        new Handler().postDelayed(() -> {

            if(slideItem.getId()==null){
                dialogWindowForLink();
            }else{
                onSlideItemRemove(slideItem);
            }
        }, 100);
    }

    @Override
    public void onSlideItemRemove(LinksEntity slideItem) {
        slideItem.setUser_id(PreferenceUtil.getInstance(getActivity()).getAccount().getId());
        presenter.removeFavLinkFromSlide(slideItem);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
        if(receiveEvent.getNotificationForSlideType()== Constant.SLIDE_INDEX_FAV_LINKS){
            if(presenter!=null)
                presenter.start();
        }

    }

    private void dialogWindowForLink() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(mView);
        final EditText userInputDialogEditText = mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Send", (dialogBox, id) -> {

                    if(Patterns.WEB_URL.matcher(userInputDialogEditText.getText().toString()).matches()) {
                        presenter.getFavLinkData(userInputDialogEditText.getText().toString());
                    }
                    else
                    {
                        Toast.makeText(getContext(), "url doesn't match", Toast.LENGTH_SHORT).show();

                    }

                })
                .setNegativeButton("Cancel",
                        (dialogBox, id) -> dialogBox.cancel());

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    @OnClick(R.id.btnAddNew)
    public void onAddNewLinkClick(){

        showAddDialog("Link", false,new IAddOnSlideListener() {
            @Override
            public void onAddSlideClick() {
                SlideItem slideItem = new SlideItem();
                slideItem.setName(getString(R.string.favorite_links));
                slideItem.setType(SLIDE_INDEX_FAV_LINKS);
                presenter.createNewSlide(slideItem);
            }

            @Override
            public void onAddItemClick() {
                if(presenter.canAddOnSlide()) {
                    dialogWindowForLink();
                }
            }
        });
    }

    @OnClick(R.id.btnRemove)
    public void removeSlide(){
        if(adapter.inEditMode()) {
            isEditorEnabled=false;
            adapter.setEditMode(isEditorEnabled);
            tvRemove.setText("Remove");
            return;
        }

        showRemoveDialog("Link",presenter.isLastSlide(),new IRemoveOnSlideListener() {
            @Override
            public void onRemoveSlideClick() {
                String title = getString(R.string.do_you_want_to_remove_the_slide);
                String body = getString(R.string.remove_slide_alert_desc);
                showDeleteFromSlideDialog(title, body, null, null, (slideId, itemId) -> {
                    presenter.removeSlide();
                });

            }

            @Override
            public void onRemoveItemClick() {
                isEditorEnabled=true;
                tvRemove.setText("Done");
                adapter.setEditMode(isEditorEnabled);
            }

            @Override
            public void onDismiss() {
                isEditorEnabled=false;
                tvRemove.setText("Remove");
                adapter.setEditMode(isEditorEnabled);
            }
        });

    }
}

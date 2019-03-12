package com.uiu.helper.KidsHelper.mvp.ui.camera.editor;

import com.uiu.helper.KidsHelper.mvp.BasePresenter;
import com.uiu.helper.KidsHelper.mvp.BaseView;
import com.uiu.helper.KidsHelper.mvp.model.ContactEntity;

import java.io.File;
import java.util.List;

public class PhotoEditorContract {

    interface View extends BaseView<Presenter> {
        void showMessage(String message);
        void onPicShared();
        void onFavPeopleLoaded(List<ContactEntity> contactEntities);
    }

    interface Presenter extends BasePresenter {
        void sharePicToFav(String contact, int media_type, File file);
        void loadFavPeoples();
    }
}

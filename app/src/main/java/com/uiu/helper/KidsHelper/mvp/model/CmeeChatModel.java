
package com.uiu.helper.KidsHelper.mvp.model;

import com.google.gson.annotations.SerializedName;
import com.uiu.helper.KidsHelper.mvp.model.response.BaseResponse;


import java.util.List;

public class CmeeChatModel extends BaseResponse {

    @SerializedName("files")
    private List<File> mFiles;


    public List<File> getFiles() {
        return mFiles;
    }

    public void setFiles(List<File> files) {
        mFiles = files;
    }


}

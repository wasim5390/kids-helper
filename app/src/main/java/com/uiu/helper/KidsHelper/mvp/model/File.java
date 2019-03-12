
package com.uiu.helper.KidsHelper.mvp.model;

import com.google.gson.annotations.SerializedName;

public class File {

    @SerializedName("created_at")
    private Long mCreatedAt;
    @SerializedName("file_duration")
    private Object mFileDuration;
    @SerializedName("file_url")
    private String mFileUrl;
    @SerializedName("id")
    private String mId;
    @SerializedName("receiver")
    private Receiver mReceiver;
    @SerializedName("sender")
    private Sender mSender;
    @SerializedName("thumbnail")
    private String mThumbnail;
    @SerializedName("type")
    private Integer mType;

    public Long getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Long createdAt) {
        mCreatedAt = createdAt;
    }

    public Object getFileDuration() {
        return mFileDuration;
    }

    public void setFileDuration(Object fileDuration) {
        mFileDuration = fileDuration;
    }

    public String getFileUrl() {
        return mFileUrl;
    }

    public void setFileUrl(String fileUrl) {
        mFileUrl = fileUrl;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public Receiver getReceiver() {
        return mReceiver;
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    public Sender getSender() {
        return mSender;
    }

    public void setSender(Sender sender) {
        mSender = sender;
    }

    public String getThumbnail() {
        return mThumbnail==null?"www.empty":mThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        mThumbnail = thumbnail;
    }

    public Integer getType() {
        return mType;
    }

    public void setType(Integer type) {
        mType = type;
    }

}

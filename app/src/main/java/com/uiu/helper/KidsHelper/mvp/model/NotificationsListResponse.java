package com.uiu.helper.KidsHelper.mvp.model;

import com.google.gson.annotations.SerializedName;
import com.uiu.helper.KidsHelper.mvp.model.response.BaseResponse;

public class NotificationsListResponse extends BaseResponse {

	@SerializedName("data")
	private Data data;

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"NotificationsListResponse{" + 
			"data = '" + data + '\'' + 
			"}";
		}
}
package com.uiu.helper.KidsHelper.mvp.model;

import com.google.gson.annotations.SerializedName;

public class NotificationInnerObject {

	@SerializedName("id")
	String id;
	@SerializedName("request_status")
	int request_status=0;
	@SerializedName("user_id")
	String user_id;
	@SerializedName("slide_id")
	String slide_id;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getRequest_status() {
		return request_status;
	}

	public void setRequest_status(int request_status) {
		this.request_status = request_status;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getSlide_id() {
		return slide_id;
	}

	public void setSlide_id(String slide_id) {
		this.slide_id = slide_id;
	}


	@Override
 	public String toString(){
		return 
			"NotificationInnerObject{" +
			"}";
		}
}
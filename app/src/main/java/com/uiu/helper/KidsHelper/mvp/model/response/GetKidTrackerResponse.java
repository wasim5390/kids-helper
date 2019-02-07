package com.uiu.helper.KidsHelper.mvp.model.response;


import com.google.gson.annotations.SerializedName;

public class GetKidTrackerResponse extends BaseResponse{

	@SerializedName("tracker")
	private Tracker tracker;

	public void setTracker(Tracker tracker){
		this.tracker = tracker;
	}

	public Tracker getTracker(){
		return tracker;
	}

	@Override
 	public String toString(){
		return 
			"GetKidTrackerResponse{" + 
			"tracker = '" + tracker + '\'' + 
			"}";
		}
}
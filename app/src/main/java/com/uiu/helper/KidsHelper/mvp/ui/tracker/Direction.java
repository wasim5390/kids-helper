package com.uiu.helper.KidsHelper.mvp.ui.tracker;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.uiu.helper.KidsHelper.mvp.model.response.BaseResponse;

public class Direction extends BaseResponse{

	@SerializedName("directions")
	private List<DirectionsItem> directions;

	public void setDirections(List<DirectionsItem> directions){
		this.directions = directions;
	}

	public List<DirectionsItem> getDirections(){
		return directions;
	}

	@Override
 	public String toString(){
		return 
			"Direction{" + 
			"directions = '" + directions + '\'' + 
			"}";
		}
}
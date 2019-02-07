package com.uiu.helper.KidsHelper.mvp.model.response;

import com.google.gson.annotations.SerializedName;
import com.uiu.helper.KidsHelper.mvp.model.Location;

public class Tracker{

	@SerializedName("location")
	private Location location;

	@SerializedName("id")
	private String id;

	public void setLocation(Location location){
		this.location = location;
	}

	public Location getLocation(){
		return location;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"Tracker{" + 
			"location = '" + location + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}
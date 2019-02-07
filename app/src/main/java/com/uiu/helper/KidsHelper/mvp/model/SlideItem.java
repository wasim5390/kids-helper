package com.uiu.helper.KidsHelper.mvp.model;


import android.content.Intent;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SlideItem implements Serializable{

	@SerializedName("serial")
	private Integer serial;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;

	@SerializedName("user_id")
	private String user_id;

	@SerializedName("helper_id")
	private String helperId;

	@SerializedName("type")
	private Integer type;

	private int count;

	public SlideItem(){

	}

	public SlideItem(int slideIndexNotifications) {
		this.setType(slideIndexNotifications);
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}





	public void setSerial(int serial){
		this.serial = serial;
	}

	public Integer getSerial(){
		return serial;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public String getUserId() {
		return user_id;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setType(int type){
		this.type = type;
	}

	public int getType(){
		return type;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}



	public String getHelperId() {
		return helperId;
	}

	public void setHelperId(String helperId) {
		this.helperId = helperId;
	}

	@Override
	public boolean equals(Object obj) {
		return this.id == ((SlideItem)obj).id;
	}
	@Override
	public int hashCode()
	{
		return type==null?20:type.hashCode();
	}

	@Override
 	public String toString(){
		return
			"slide{" +
			"serial = '" + serial + '\'' +
			",name = '" + name + '\'' +
			",id = '" + id + '\'' +
			",type = '" + type + '\'' +
					",user_id = '" + user_id + '\'' +
			"}";
		}
}
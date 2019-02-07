package com.uiu.helper.KidsHelper.mvp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("page_count")
	private int pageCount;

	@SerializedName("total_pages")
	private int totalPages;

	@SerializedName("current_page")
	private int currentPage;

	@SerializedName("notifications")
	private List<NotificationsItem> notifications;


	public int getTotalPages() {
		return totalPages;
	}


	public void setPageCount(int pageCount){
		this.pageCount = pageCount;
	}

	public int getPageCount(){
		return pageCount;
	}

	public void setCurrentPage(int currentPage){
		this.currentPage = currentPage;
	}

	public int getCurrentPage(){
		return currentPage;
	}

	public void setNotifications(List<NotificationsItem> notifications){
		this.notifications = notifications;
	}

	public List<NotificationsItem> getNotifications(){
		return notifications;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"page_count = '" + pageCount + '\'' + 
			",current_page = '" + currentPage + '\'' + 
			",notifications = '" + notifications + '\'' + 
			"}";
		}
}
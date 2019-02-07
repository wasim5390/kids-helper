package com.uiu.helper.KidsHelper.mvp.ui.tracker;


import com.google.gson.annotations.SerializedName;

public class DirectionsItem{

	@SerializedName("helper_id")
	private String helperId;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("id")
	private String id;

	@SerializedName("location_id")
	private String locationId;

	@SerializedName("longitude")
	private String longitude;

	@SerializedName("request_status")
    private int requestStatus;

    @SerializedName("slide_id")
    private String slideId;

    @SerializedName("title")
    private String title;

    @SerializedName("address")
    private String address;

	@SerializedName("image")
	private String image;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setHelperId(String helperId){
		this.helperId = helperId;
	}

	public String getHelperId(){
		return helperId;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setLatitude(String latitude){
		this.latitude = latitude;
	}

	public String getLatitude(){
		return latitude;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setLocationId(String locationId){
		this.locationId = locationId;
	}

	public String getLocationId(){
		return locationId;
	}

	public void setLongitude(String longitude){
		this.longitude = longitude;
	}

	public String getLongitude(){
		return longitude;
	}

	@Override
 	public String toString(){
		return 
			"DirectionsItem{" + 
			"helper_id = '" + helperId + '\'' + 
			",user_id = '" + userId + '\'' + 
			",latitude = '" + latitude + '\'' + 
			",id = '" + id + '\'' + 
			",location_id = '" + locationId + '\'' + 
			",longitude = '" + longitude + '\'' + 
			"}";
		}

	public void setRequestStatus(int requestStatus) {
		this.requestStatus = requestStatus;

	}

    public void setSlideId(String slideId) {
        this.slideId = slideId;
    }

    public String getSlideId() {
        return slideId;
    }
}
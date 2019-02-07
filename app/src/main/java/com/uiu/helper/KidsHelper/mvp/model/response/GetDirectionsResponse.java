package com.uiu.helper.KidsHelper.mvp.model.response;

import com.google.gson.annotations.SerializedName;
import com.uiu.helper.KidsHelper.mvp.ui.tracker.DirectionsItem;

import java.util.List;

public class GetDirectionsResponse extends BaseResponse {


    @SerializedName("directions")
    private List<DirectionsItem> directionsList;

    @SerializedName("direction")
    private DirectionsItem direction;

    public DirectionsItem getDirection() {
        return direction;
    }



    public List<DirectionsItem> getDirectionsList() {
        return directionsList;
    }
}

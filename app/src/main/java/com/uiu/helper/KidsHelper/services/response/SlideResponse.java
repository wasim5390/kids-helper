package com.uiu.helper.KidsHelper.services.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.uiu.helper.KidsHelper.constants.KeyConstants;
import com.uiu.helper.KidsHelper.entities.SlideEntity;
import com.uiu.helper.KidsHelper.framework.utilities.JsonUtility;
import com.uiu.helper.KidsHelper.framework.utilities.StringUtility;

/**
 * Created by Awais on 31/05/2018.
 */
public class SlideResponse extends BaseResponse {

    private SlideEntity slideEntity;
    private String message;
    private boolean success;

    @Override
    public void set(String input) {

        try {

            JsonObject jsonObject = JsonUtility.parseToJsonObject(input);

            if (jsonObject.has(KeyConstants.KEY_SUCCESS)) {
                success = JsonUtility.getBoolean(jsonObject, KeyConstants.KEY_SUCCESS);
            }
            if (jsonObject.has(KeyConstants.KEY_MESSAGE)) {
                message = JsonUtility.getString(jsonObject, KeyConstants.KEY_MESSAGE);
            }
            if (jsonObject.has(KeyConstants.KEY_SLIDES)) {
                JsonArray jsonArray = JsonUtility.getJsonArray(jsonObject, KeyConstants.KEY_SLIDES);
                slideEntity = new SlideEntity();
                slideEntity.set(jsonArray.toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public SlideEntity getSlideEntity() {
        return slideEntity;
    }

    public String getMessage() {
        return StringUtility.validateEmptyString(message);
    }

    public boolean getSuccess() {
        return success;
    }
}

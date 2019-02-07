package unittests.interfaces;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;
import unittests.model.ResultModel;
import unittests.util.Constants;

/**
 * Created by Android on 2/1/2018.
 */

public interface SettingInterface {
    /**
     * Get All Settings
     *
     * @return JSON Result
     */
    @GET
    Call<ResultModel> getAllSettings(@Url String url);

    /**
     * Add setting
     *
     * @return JSON Result
     */
    @FormUrlEncoded
    @POST(Constants.TEST_USER_SETTING_URL)
    Call<ResultModel> addSetting(@Field(value = "setting_type") String setting);

    /**
     * Delete Setting
     *
     * @return JSON Result
     */
    @DELETE(Constants.TEST_USER_SETTING_URL+"/{objectId}")
    Call<ResultModel> deleteSetting(@Path("objectId") String object_id);
}


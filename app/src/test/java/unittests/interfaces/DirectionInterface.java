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

public interface DirectionInterface {
    /**
     * Get All Directions
     *
     * @return JSON Result
     */
    @GET
    Call<ResultModel> getAllDirections(@Url String url);

    /**
     * Add Direction
     *
     * @return JSON Result
     */
    @FormUrlEncoded
    @POST(Constants.TEST_DIRECTION_URL)
    Call<ResultModel> addDirection(@Field(value = "latitude") String latitude,
                                   @Field(value = "longitude") String longitude,
                                   @Field(value = "name") String name,
                                   @Field(value = "address") String address);

    /**
     * Delete Direction
     *
     * @return JSON Result
     */
    @DELETE(Constants.TEST_DIRECTION_URL+"/{objectId}")
    Call<ResultModel> deleteDirection(@Path("objectId") String object_id);
}

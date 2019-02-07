package unittests.interfaces;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;
import unittests.model.ResultModel;
import unittests.util.Constants;

/**
 * Created by Android on 2/1/2018.
 */

public interface PushInterface {
    /**
     * Register Device
     *
     * @return JSON Result
     */
    @POST(Constants.TEST_INSTALLATION_URL)
    Call<ResultModel> registerDevice(@Body HashMap<String, Object> body);

    /**
     * Send Push
     *
     * @return JSON Result
     */
    @POST(Constants.TEST_PUSH_URL)
    Call<ResultModel> sendPush(@Body HashMap<String, Object> body);

    /**
     * Delete Installation
     *
     * @return JSON Result
     */
    @DELETE(Constants.TEST_INSTALLATION_URL+"/{objectId}")
    Call<ResultModel> deleteInvitation(@Path("objectId") String object_id);
}

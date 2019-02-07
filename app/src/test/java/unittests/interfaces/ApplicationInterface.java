package unittests.interfaces;


import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;
import unittests.model.ResultModel;
import unittests.util.Constants;

/**
 * Interface with endpoints
 *
 * @author android
 */
public interface ApplicationInterface {

    /**
     * Get All Applications
     *
     * @return JSON Result
     */
    @GET
    Call<ResultModel> getAllApplications(@Url String url);

    /**
     * Add Application
     *
     * @return JSON Result
     */
    @POST(Constants.TEST_APP_URL)
    Call<ResultModel> addApplication(@Body HashMap<String, Object> body);

    /**
     * Add File/Image
     *
     * @return JSON Result
     */
    @Multipart
    @POST("/files/img.png")
    Call<ResultModel> uploadFile(@Part MultipartBody.Part file,
                                 @Part("appIcon") RequestBody name);

    /**
     * Delete Application
     *
     * @return JSON Result
     */
    @DELETE(Constants.TEST_APP_URL+"/{objectId}")
    Call<ResultModel> deleteApplication(@Path("objectId") String object_id);

    /**
     * Delete File
     *
     * @return JSON Result
     */
    @DELETE("/files/{filename}")
    Call<Void> deleteFile(@Path("filename") String filename);
}

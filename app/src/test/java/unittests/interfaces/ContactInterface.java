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

public interface ContactInterface {

    /**
     * Get All Contacts
     *
     * @return JSON Result
     */
    @GET
    Call<ResultModel> getAllContacts(@Url String url);

    /**
     * Add Contact
     *
     * @return JSON Result
     */
    @FormUrlEncoded
    @POST(Constants.TEST_CONTACT_URL)
    Call<ResultModel> addContact(@Field(value = "wiserId") String wiserId,
                                 @Field(value = "name") String name);

    /**
     * Delete Application
     *
     * @return JSON Result
     */
    @DELETE(Constants.TEST_CONTACT_URL+"/{objectId}")
    Call<ResultModel> deleteContact(@Path("objectId") String object_id);
}

package unittests.interfaces;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;
import unittests.model.ResultModel;
import unittests.util.Constants;

/**
 * Created by Android on 2/1/2018.
 */

public interface InvitationInterface {
    /**
     * Get All Invitation
     *
     * @return JSON Result
     */
    @GET
    Call<ResultModel> getAllInvitations(@Url String url);

    /**
     * Add Invitation
     *
     * @return JSON Result
     */
    @FormUrlEncoded
    @POST(Constants.TEST_INVITATION_URL)
    Call<ResultModel> addInvitation(@Field(value = "wiser_id") String wiser_id,
                                    @Field(value = "companion_id") String companion_id,
                                    @Field(value = "status") int status);

    /**
     * Change Invitation Status
     *
     * @return JSON Result
     */
    @FormUrlEncoded
    @PUT(Constants.TEST_INVITATION_URL+"/{objectId}")
    Call<ResultModel> changeInvitationStatus(@Path("objectId") String object_id,
                                             @Field(value = "status") int status);

    /**
     * Delete Invitation
     *
     * @return JSON Result
     */
    @DELETE(Constants.TEST_INVITATION_URL+"/{objectId}")
    Call<ResultModel> deleteInvitation(@Path("objectId") String object_id);
}

package unittests.interfaces;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import unittests.model.ParseUserModel;
import unittests.util.Constants;

/**
 * Interface with endpoints
 *
 * @author android
 */
public interface ParseUserInterface {

    /**
     * Login Parse User
     *
     * @return JSON Result
     */
    @GET
    Call<ParseUserModel> loginParseUser(@Url String url);

    /**
     * Is Exist Parse User
     *
     * @return JSON Result
     */
    @GET
    Call<ParseUserModel> isExistParseUser(@Url String userName);

    /**
     * Sign Up parse User
     *
     * @return JSON Result
     */
    @FormUrlEncoded
    @POST(Constants.TEST_USER_URL)
    Call<ParseUserModel> signupParseUser(@Field(value = "username") String userName,
                                         @Field(value = "password") String passWord);

}

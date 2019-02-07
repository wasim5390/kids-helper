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

public interface ReminderInterface {
    /**
     * Get All Reminders
     *
     * @return JSON Result
     */
    @GET
    Call<ResultModel> getAllReminders(@Url String url);

    /**
     * Add Reminder
     *
     * @return JSON Result
     */
    @FormUrlEncoded
    @POST(Constants.TEST_REMINDER_URL)
    Call<ResultModel> addReminder(@Field(value = "time") String time,
                                  @Field(value = "date") String date,
                                  @Field(value = "title") String title);

    /**
     * Delete Reminder
     *
     * @return JSON Result
     */
    @DELETE(Constants.TEST_REMINDER_URL+"/{objectId}")
    Call<ResultModel> deleteReminder(@Path("objectId") String object_id);
}

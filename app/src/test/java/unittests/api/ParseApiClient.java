package unittests.api;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import unittests.interfaces.ApplicationInterface;
import unittests.interfaces.ContactInterface;
import unittests.interfaces.DirectionInterface;
import unittests.interfaces.InvitationInterface;
import unittests.interfaces.ParseUserInterface;
import unittests.interfaces.PushInterface;
import unittests.interfaces.ReminderInterface;
import unittests.interfaces.SettingInterface;
import unittests.util.Constants;

/**
 * API Client helper class used to configure Retrofit object.
 *
 * @author android
 */

public class ParseApiClient {

    private Retrofit retrofit;
    private boolean isDebug;
    private HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

    /**
     * Set the {@link Retrofit} log level.
     *
     * @param isDebug If true, the log level is set to
     *                {@link HttpLoggingInterceptor.Level#BODY}. Otherwise
     *                {@link HttpLoggingInterceptor.Level#NONE}.
     */
    public ParseApiClient setIsDebug(boolean isDebug) {
        this.isDebug = isDebug;
        if (retrofit != null) {
            httpLoggingInterceptor.
                    setLevel(isDebug ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        }
        return this;
    }

    /**
     * Configure OkHttpClient
     *
     * @return OkHttpClient
     */
    private OkHttpClient okHttpClient() {

        return new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor())
                .connectTimeout(Constants.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    /**
     * Create a new {@link Retrofit.Builder}. Override this to e.g. set your own client or executor.
     *
     * @return A {@link Retrofit.Builder} with no modifications.
     */
    private Retrofit.Builder newRestAdapterBuilder() {
        return new Retrofit.Builder();
    }

    /**
     * Return the current {@link Retrofit} instance. If none exists (first call, API key changed),
     * builds a new one.
     * <p/>
     * When building, sets the endpoint and a {@link HttpLoggingInterceptor} which adds the API key as query param.
     */
    private Retrofit getRestAdapter() {

        Retrofit.Builder builder = null;
        if (retrofit == null) {

            //Create a new instance of the Rest Adapter class
            builder = newRestAdapterBuilder();

            builder.baseUrl(Constants.PARSE_SERVER_URL);
            builder.client(okHttpClient());
            builder.addConverterFactory(GsonConverterFactory.create());
        }

        if (isDebug) {
            if (builder != null) {
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            }
        }
        if (builder != null) {
            retrofit = builder.build();
        }
        return retrofit;
    }

    /**
     * Create ParseUser service instance.
     *
     * @return ParseUser Service.
     */
    public ParseUserInterface parseUserInterface() {
        return getRestAdapter().create(ParseUserInterface.class);
    }
    /**
     * Create Apps service instance.
     *
     * @return Apps Service.
     */
    public ApplicationInterface applicationInterface() {
        return getRestAdapter().create(ApplicationInterface.class);
    }
    /**
     * Create Apps service instance.
     *
     * @return Apps Service.
     */
    public ContactInterface contactInterface() {
        return getRestAdapter().create(ContactInterface.class);
    }
    /**
     * Create Setting service instance.
     *
     * @return Apps Service.
     */
    public SettingInterface settingInterface() {
        return getRestAdapter().create(SettingInterface.class);
    }
    /**
     * Create Direction service instance.
     *
     * @return Apps Service.
     */
    public DirectionInterface directionInterface() {
        return getRestAdapter().create(DirectionInterface.class);
    }
    /**
     * Create Reminder service instance.
     *
     * @return Apps Service.
     */
    public ReminderInterface reminderInterface() {
        return getRestAdapter().create(ReminderInterface.class);
    }
    /**
     * Create Invitation service instance.
     *
     * @return Apps Service.
     */
    public InvitationInterface invitationInterface() {
        return getRestAdapter().create(InvitationInterface.class);
    }
    /**
     * Create Invitation service instance.
     *
     * @return Apps Service.
     */
    public PushInterface pushInterface() {
        return getRestAdapter().create(PushInterface.class);
    }
}

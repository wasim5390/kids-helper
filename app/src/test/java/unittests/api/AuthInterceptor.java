package unittests.api;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import unittests.util.Constants;

/**
 * This class add information (API Key) to {@link okhttp3.OkHttpClient} which is passed in
 * {@link ParseApiClient#getRestAdapter()} which is required when making a request.
 *
 * @author android
 */
public class AuthInterceptor implements Interceptor {

    /**
     * Default constructor.
     */
    public AuthInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Request newRequest;
        newRequest = request.newBuilder()
                .addHeader("X-Parse-Application-Id", Constants.Back4App_ID)
                .addHeader("X-Parse-REST-API-Key", Constants.Back4App_REST_API)
                .addHeader("X-Parse-Master-Key", Constants.Back4App_MASTER_API)
                .build();
        return chain.proceed(newRequest);
    }
}

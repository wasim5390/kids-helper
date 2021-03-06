package com.uiu.helper.KidsHelper.mvp.source;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.model.AnnotationExclusionStrategy;
import com.uiu.helper.util.Utils;


import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



/**
 * Created by sidhu on 4/11/2018.
 */

public class RetrofitHelper implements Constant {

   public static final String BASE_URL = "https://kidslauncherapi.herokuapp.com/api/";
    public static final String BASE_URL_DEV = "https://kidslauncherapi.herokuapp.com/api/";

    private static RetrofitHelper instance;
    public Retrofit retrofit;
    API service;

    private static final String TAG = "RetrofitHelper";
    private RetrofitHelper () {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new AuthorizationInterceptor());
       Gson builder = new GsonBuilder().setExclusionStrategies(new AnnotationExclusionStrategy()).create();
        retrofit = new Retrofit.Builder()
                .baseUrl(true?BASE_URL:BASE_URL_DEV)
                .addConverterFactory(GsonConverterFactory.create(builder))
                .client(okHttpClientBuilder.build())
                .build();

        service = retrofit.create(API.class);
    }

    public static RetrofitHelper getInstance() {
        if (instance==null) {
            instance = new RetrofitHelper();
        }
        return instance;
    }



    public API getApi() {
        return service;
    }

    public static API getIconApi()
    {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor);
        Gson builder = new GsonBuilder().setExclusionStrategies(new AnnotationExclusionStrategy()).create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://besticon-demo.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create(builder))
                .client(okHttpClientBuilder.build())
                .build();

        return retrofit.create(API.class);
    }

    public class AuthorizationInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();

            String header = Utils.getAuthorizationHeader();
            if (header!=null) {
                Request request = original.newBuilder()
                        .header("Authorization", "Basic " + header).build();
                Response response = chain.proceed(request);
                if (response.code()==401) {
                   // EventBus.getDefault().post(new LoginFailEvent());
                }

                return response;
            } else {
                return chain.proceed(original);
            }



        }
    }


}

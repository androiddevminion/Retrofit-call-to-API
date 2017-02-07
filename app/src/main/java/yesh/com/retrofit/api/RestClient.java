package yesh.com.retrofit.api;

/**
 * Created by yesh on 2/6/17.
 */

import android.content.Context;

import yesh.com.retrofit.interceptor.FakeInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 */
public final class RestClient {

    private static RestService mRestService = null;

    public static RestService getClient(Context context) {
        if (mRestService == null) {
            final OkHttpClient client = new OkHttpClient
                    .Builder()
                    .addInterceptor(new FakeInterceptor(context))
                    .build();

            final Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://mock.api")
                    .client(client)
                    .build();

            mRestService = retrofit.create(RestService.class);
        }
        return mRestService;
    }
}

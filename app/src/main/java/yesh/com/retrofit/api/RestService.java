package yesh.com.retrofit.api;

/**
 * Created by yesh on 2/6/17.
 */

import yesh.com.retrofit.models.ResponseData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 *
 */
public interface RestService {

    @GET("/user/login")
    Call<ResponseData> login(@Query("username") final String id,
                             @Query("pwd") final String pwd);

    @GET("/content/content")
    Call<ResponseData> content(@Query("param1") final String param1);

    @POST("/content/content")
    @FormUrlEncoded
    Call<ResponseData> postContent(@Field("param1") final String param1);
}

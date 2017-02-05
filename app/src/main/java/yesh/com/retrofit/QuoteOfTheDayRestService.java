package yesh.com.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import yesh.com.retrofit.pojo.QuoteOfTheDayResponse;

/**
 * Created by yesh on 2/4/17.
 */
public interface QuoteOfTheDayRestService {
    @GET("/qod.json")
    Call<QuoteOfTheDayResponse> getQuoteOfTheDay();
}

package yesh.com.retrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import yesh.com.retrofit.api.RestClient;
import yesh.com.retrofit.models.ResponseData;
import yesh.com.retrofit.pojo.QuoteOfTheDayErrorResponse;
import yesh.com.retrofit.pojo.QuoteOfTheDayResponse;
import yesh.com.retrofit.interceptor.LoggingInterceptor;

public class MainActivity extends AppCompatActivity {

    private TextView textViewQuoteOfTheDay;
    private Button buttonRetry;

    private static final String TAG = "MainActivity";
    private QuoteOfTheDayRestService service;
    private Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewQuoteOfTheDay = (TextView) findViewById(R.id.text_view_quote);
        buttonRetry = (Button) findViewById(R.id.button_retry);
        buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQuoteOfTheDay();
            }
        });

        //call Mocked response
        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase("mocked")) {
            RestClient.getClient(this).login("username", "password").enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    Log.d("sample response yesh", response.body().getData().toString());
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {

                }
            });
        } else if (BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug")) {

            //Call Actual service
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new LoggingInterceptor()).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(QuoteOfTheDayConstants.BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .client(client)
                    .build();
            service = retrofit.create(QuoteOfTheDayRestService.class);
            getQuoteOfTheDay();

        }
    }


    private void getQuoteOfTheDay() {
        Call<QuoteOfTheDayResponse> call =
                service.getQuoteOfTheDay();

        call.enqueue(new Callback<QuoteOfTheDayResponse>() {

            @Override
            public void onResponse(Call<QuoteOfTheDayResponse> call, Response<QuoteOfTheDayResponse> response) {
                if (response.isSuccessful()) {
                    textViewQuoteOfTheDay.setText(response.body().getContents().getQuotes().get(0).getQuote());

                } else {
                    try {
                        Converter<ResponseBody, QuoteOfTheDayErrorResponse> errorConverter = retrofit.responseBodyConverter(QuoteOfTheDayErrorResponse.class, new Annotation[0]);
                        QuoteOfTheDayErrorResponse error = errorConverter.convert(response.errorBody());
                        showRetry(error.getError().getMessage());

                    } catch (IOException e) {
                        Log.e(TAG, "IOException parsing error:", e);
                    }

                }
            }

            @Override
            public void onFailure(Call<QuoteOfTheDayResponse> call, Throwable t) {
                //Transport level errors such as no internet etc.
            }
        });


    }

    private void showRetry(String error) {
        textViewQuoteOfTheDay.setText(error);
        buttonRetry.setVisibility(View.VISIBLE);

    }
}

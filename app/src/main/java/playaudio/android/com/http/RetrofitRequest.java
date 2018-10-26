package playaudio.android.com.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by baina on 18/05/08.
 */

public class RetrofitRequest {

    private static final String API_HOST = "http://api.u-launcher.com/";
    private static final int DEFAULT_TIMEOUT = 6;

    private static RetrofitRequest mInstance;

    private RetrofitApi mRetrofitApi;

    private RetrofitRequest() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(interceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_HOST)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mRetrofitApi = retrofit.create(RetrofitApi.class);
    }

    public static synchronized RetrofitRequest getInstance() {
        if (null == mInstance) {
            mInstance = new RetrofitRequest();
        }
        return mInstance;
    }

    public RetrofitApi getRetrofitApi() {
        return mRetrofitApi;
    }
}

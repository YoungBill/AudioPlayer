package playaudio.android.com.http;

import java.util.Map;

import playaudio.android.com.bean.RadioResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by baina on 18/05/08.
 */

public interface RetrofitApi {

    @GET("client/v2/radio/items.json")
    Call<RadioResponse> getRadioList(@QueryMap Map<String, String> params);
}

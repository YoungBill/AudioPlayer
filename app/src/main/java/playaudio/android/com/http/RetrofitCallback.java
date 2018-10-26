package playaudio.android.com.http;

import android.util.Log;

import java.io.IOException;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by taochen on 18-5-8.
 */

public abstract class RetrofitCallback<T> implements Callback<T> {

    private static final String TAG = "RetrofitCallback";

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (!response.isSuccessful()) {
            this.onFailure(new IOException(String.format(Locale.US, "Server return response with code %d", response.code())));
            return;
        }
        if (response.body() == null) {
            this.onFailure(new IOException("Server return success without any data, a bug should be reported to server side"));
            return;
        }
        onResponse(response.body());
    }

    public abstract void onResponse(T response);

    public void onFailure(Throwable t) {
        Log.d(TAG, "" + t.getMessage());
    }
}
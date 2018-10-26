package playaudio.android.com;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import playaudio.android.com.bean.RadioResponse;
import playaudio.android.com.http.RetrofitCallback;
import playaudio.android.com.http.RetrofitRequest;
import retrofit2.Call;

/**
 * Created by taochen on 18-10-25.
 */

public class MusicService extends IntentService implements MusicController {

    private static final String TAG = MusicService.class.getSimpleName();
    private static MusicCallback sMusicCallback;

    private IBinder mBinder = new ServiceBinder();
    private List<RadioResponse.DataBean.ItemsBean> mRadioList;
    private RadioResponse.DataBean.ItemsBean mCurrentMusic;

    public static void bindService(Context context, ServiceConnection serviceConnection) {
        Intent intent = new Intent(context, MusicService.class);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public static void unBindService(Context context, ServiceConnection serviceConnection) {
        context.unbindService(serviceConnection);
    }

    public static void startService(Context context) {
        Intent intent = new Intent(context, MusicService.class);
        context.startService(intent);
    }

    public static void stopService(Context context) {
        Intent intent = new Intent(context, MusicService.class);
        context.stopService(intent);
    }

    public MusicService() {
        super("playaudio.android.com.MusicService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MusicService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mRadioList = new ArrayList<>();
        initPlayer();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return mBinder;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent");
        getRadioList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void playOrPause() {
        Log.d(TAG, "playOrPause");
    }

    @Override
    public void switchPrevious() {
        Log.d(TAG, "switchPrevious");
    }

    @Override
    public void switchNext() {
        Log.d(TAG, "switchNext");
    }

    private void initPlayer() {
        Log.d(TAG, "initPlayer");
    }

    private void getRadioList() {
        Log.d(TAG,"getRadioList");
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("packageName", getPackageName());
        queryParams.put("language", Locale.getDefault().getLanguage());
        queryParams.put("country", Locale.getDefault().getCountry());
        Call<RadioResponse> call = RetrofitRequest.getInstance().getRetrofitApi().getRadioList(queryParams);
        call.enqueue(new RetrofitCallback<RadioResponse>() {
            @Override
            public void onResponse(RadioResponse response) {
                if (response.getData().getCode() == 100) {
                    List<RadioResponse.DataBean.ItemsBean> radioList = response.getData().getItems();
                    mRadioList.clear();
                    mRadioList.addAll(radioList);
                    if (mRadioList.size() > 0) {
                        mCurrentMusic = mRadioList.get(0);
                        Log.d(TAG,"onResponse");
                        if (sMusicCallback != null) {
                            sMusicCallback.onLoading(mCurrentMusic);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RadioResponse> call, Throwable t) {

            }
        });
    }

    public static void registerMusicCallback(MusicCallback callback) {
        sMusicCallback = callback;
    }

    public static void unRegisterMusicCallback() {
        sMusicCallback = null;
    }

    public class ServiceBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }
}

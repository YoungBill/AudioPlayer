package playaudio.android.com;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
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
    private MediaPlayer mMediaPlayer;
    // 已经初始化资源
    private boolean mIsInitialized;
    // 正在加载音频流文件
    private boolean mIsLoading;

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
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();
    }

    @Override
    public void playOrPause() {
        Log.d(TAG, "playOrPause");
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            if (sMusicCallback != null) {
                sMusicCallback.onPause(mCurrentMusic);
            }
        } else {
            if (!mIsInitialized) {
                prepareMusic();
            } else {
                if (mIsLoading) {
                    return;
                }
                mMediaPlayer.start();
                if (sMusicCallback != null) {
                    sMusicCallback.onPlay(mCurrentMusic);
                }
            }
        }
    }

    @Override
    public void switchPrevious() {
        Log.d(TAG, "switchPrevious");
        if (mRadioList.size() > 0) {
            int currentPosition = mRadioList.indexOf(mCurrentMusic);
            if (currentPosition == 0) {
                currentPosition = mRadioList.size() - 1;
            } else {
                currentPosition--;
            }
            mCurrentMusic = mRadioList.get(currentPosition);
            prepareMusic();
        }
    }

    @Override
    public void switchNext() {
        Log.d(TAG, "switchNext");
        if (mRadioList.size() > 0) {
            int currentPosition = mRadioList.indexOf(mCurrentMusic);
            if (currentPosition == mRadioList.size() - 1) {
                currentPosition = 0;
            } else {
                currentPosition++;
            }
            mCurrentMusic = mRadioList.get(currentPosition);
            prepareMusic();
        }
    }

    private void initPlayer() {
        Log.d(TAG, "initPlayer");
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                mIsLoading = false;
                if (sMusicCallback != null) {
                    sMusicCallback.onPlay(mCurrentMusic);
                }
            }
        });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d(TAG, "onError: " + what);
                return false;
            }
        });
    }

    private void prepareMusic() {
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(mCurrentMusic.getStreams().get(0).getUrl());
            mMediaPlayer.prepareAsync();
            mIsInitialized = true;
            if (sMusicCallback != null) {
                sMusicCallback.onLoading(mCurrentMusic);
            }
            mIsLoading = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getRadioList() {
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
                        if (sMusicCallback != null) {
                            sMusicCallback.onFinishLoadingMusicList(mCurrentMusic);
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

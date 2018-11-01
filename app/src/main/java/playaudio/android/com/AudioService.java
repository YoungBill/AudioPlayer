package playaudio.android.com;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
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

public class AudioService extends IntentService implements AudioController {

    private static final String TAG = AudioService.class.getSimpleName();
    private static List<AudioCallback> sMusicCallbackList;

    private IBinder mBinder = new ServiceBinder();
    private Handler mMainHandler;
    private List<RadioResponse.DataBean.ItemsBean> mRadioList;
    private RadioResponse.DataBean.ItemsBean mCurrentMusic;
    private MediaPlayer mMediaPlayer;
    /**
     * 播放器状态
     * int radio_state_idle = 0;
     * <p>
     * int radio_state_initialized = 1;
     * <p>
     * int radio_state_loading = 2;
     * <p>
     * int radio_state_playing = 3;
     * <p>
     * int radio_state_paused = 4;
     */
    private int mStatus;
    private AudioManager mAudioManager;
    private AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener;
    private boolean mShouldResumePlay;

    public static void bindService(Context context, ServiceConnection serviceConnection) {
        Intent intent = new Intent(context, AudioService.class);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public static void unBindService(Context context, ServiceConnection serviceConnection) {
        context.unbindService(serviceConnection);
    }

    public static void startService(Context context) {
        Intent intent = new Intent(context, AudioService.class);
        context.startService(intent);
    }

    public static void stopService(Context context) {
        Intent intent = new Intent(context, AudioService.class);
        context.stopService(intent);
    }

    public AudioService() {
        super("playaudio.android.com.MusicService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AudioService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mRadioList = new ArrayList<>();
        mMainHandler = new Handler(Looper.getMainLooper());
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
        if (mRadioList.size() == 0) {
            getRadioList();
        } else {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (AudioCallback callback : sMusicCallbackList) {
                        switch (mStatus) {
                            case AudioCallback.radio_state_idle:
                                callback.onFinishLoadingMusicList(mCurrentMusic);
                                break;
                            case AudioCallback.radio_state_loading:
                                callback.onLoading(mCurrentMusic);
                                break;
                            case AudioCallback.radio_state_playing:
                                callback.onPlay(mCurrentMusic);
                                break;
                            case AudioCallback.radio_state_paused:
                                callback.onPause(mCurrentMusic);
                                break;
                        }

                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();
        abandonFocus();
    }

    @Override
    public void playOrPause() {
        Log.d(TAG, "playOrPause");
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            for (AudioCallback callback : sMusicCallbackList) {
                callback.onPause(mCurrentMusic);
            }
            mStatus = AudioCallback.radio_state_paused;
        } else {
            if (mStatus == AudioCallback.radio_state_idle) {
                prepareMusic();
            } else {
                if (mStatus == AudioCallback.radio_state_loading) {
                    return;
                }
                mMediaPlayer.start();
                for (AudioCallback callback : sMusicCallbackList) {
                    callback.onPlay(mCurrentMusic);
                }
                mStatus = AudioCallback.radio_state_playing;
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
                if (requestFocus()) {
                    mediaPlayer.start();
                    for (AudioCallback callback : sMusicCallbackList) {
                        callback.onPlay(mCurrentMusic);
                    }
                    mStatus = AudioCallback.radio_state_playing;
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
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        }
        if (mAudioFocusChangeListener == null) {
            mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    switch (focusChange) {
                        case AudioManager.AUDIOFOCUS_GAIN:
                        case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                        case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                            //播放操作
                            if (mShouldResumePlay) {
                                mMediaPlayer.start();
                                mShouldResumePlay = false;
                            }
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS:
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                            //暂停操作
                            if (mMediaPlayer.isPlaying()) {
                                mMediaPlayer.pause();
                                mShouldResumePlay = true;
                            }
                            break;
                        default:
                            break;
                    }
                }
            };
        }
    }

    private void prepareMusic() {
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(mCurrentMusic.getStreams().get(0).getUrl());
            mMediaPlayer.prepareAsync();
            mStatus = AudioCallback.radio_state_initialized;
            for (AudioCallback callback : sMusicCallbackList) {
                callback.onLoading(mCurrentMusic);
            }
            mStatus = AudioCallback.radio_state_loading;
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
                        for (AudioCallback callback : sMusicCallbackList) {
                            callback.onFinishLoadingMusicList(mCurrentMusic);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RadioResponse> call, Throwable t) {

            }
        });
    }

    private boolean requestFocus() {
        if (mAudioFocusChangeListener != null) {
            return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == mAudioManager.requestAudioFocus(
                    mAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN);
        }
        return false;
    }

    private boolean abandonFocus() {
        if (mAudioFocusChangeListener != null) {
            return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
        }
        return false;
    }

    public static void registerMusicCallback(AudioCallback callback) {
        if (sMusicCallbackList == null) {
            sMusicCallbackList = new ArrayList<>();
        }
        sMusicCallbackList.add(callback);
    }

    public static void unRegisterMusicCallback(AudioCallback callback) {
        sMusicCallbackList.remove(callback);
    }

    public class ServiceBinder extends Binder {
        AudioService getService() {
            return AudioService.this;
        }
    }
}

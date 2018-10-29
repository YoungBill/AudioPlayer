package playaudio.android.com;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import playaudio.android.com.bean.RadioResponse;

/**
 * Created by taochen on 18-10-25.
 */

public class MusicView extends LinearLayout implements View.OnClickListener {

    private MarqueeTextView mFrequencyTv;
    private MarqueeTextView mTitleTv;
    private PlayOrPauseSwitch mPlayOrPauseSwitch;
    private ImageButton mPreviewSwitch;
    private ImageButton mNextSwitch;
    private MusicService mMusicService;
    private MusicCallback mMusicCallback;
    private ServiceConnection mServiceConnection;

    public MusicView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        MusicService.registerMusicCallback(mMusicCallback);
        MusicService.bindService(getContext(), mServiceConnection);
        MusicService.startService(getContext());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        MusicService.unRegisterMusicCallback();
        MusicService.unBindService(getContext(), mServiceConnection);
        MusicService.stopService(getContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_or_pause_switch:
                mMusicService.playOrPause();
                break;
            case R.id.preview_switch:
                mMusicService.switchPrevious();
                break;
            case R.id.next_switch:
                mMusicService.switchNext();
                break;
        }
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_music, this);
        mFrequencyTv = findViewById(R.id.frequencyTv);
        mTitleTv = findViewById(R.id.titleTv);
        mPlayOrPauseSwitch = findViewById(R.id.play_or_pause_switch);
        mPlayOrPauseSwitch.setOnClickListener(this);
        mPreviewSwitch = findViewById(R.id.preview_switch);
        mPreviewSwitch.setOnClickListener(this);
        mNextSwitch = findViewById(R.id.next_switch);
        mNextSwitch.setOnClickListener(this);
        mMusicCallback = new MusicCallback() {

            @Override
            public void onFinishLoadingMusicList(RadioResponse.DataBean.ItemsBean firstMusicBean) {
                mFrequencyTv.setText(firstMusicBean.getFrequency());
                String title = getContext().getString(R.string.audio_title, firstMusicBean.getTitle());
                mTitleTv.setText(title);
                mPlayOrPauseSwitch.setOuterImageResource(R.mipmap.play_status_playing);
                mPlayOrPauseSwitch.setInnerImageResource(R.mipmap.play_btn_play);
            }

            @Override
            public void onLoading(RadioResponse.DataBean.ItemsBean musicBean) {
                mFrequencyTv.setText(musicBean.getFrequency());
                String title = getContext().getString(R.string.audio_title, musicBean.getTitle());
                mTitleTv.setText(title);
                mTitleTv.stopScroll();
                mPlayOrPauseSwitch.setOuterImageResource(R.mipmap.play_status_loading);
                mPlayOrPauseSwitch.setInnerImageResource(R.mipmap.play_btn_loading);
                mPlayOrPauseSwitch.setShowLoading(true);
            }

            @Override
            public void onPlay(RadioResponse.DataBean.ItemsBean musicBean) {
                mTitleTv.resumeScroll();
                mPlayOrPauseSwitch.setOuterImageResource(R.mipmap.play_status_playing);
                mPlayOrPauseSwitch.setInnerImageResource(R.mipmap.play_btn_pause);
                mPlayOrPauseSwitch.setShowLoading(false);
            }

            @Override
            public void onPause(RadioResponse.DataBean.ItemsBean musicBean) {
                mTitleTv.pauseScroll();
                mPlayOrPauseSwitch.setOuterImageResource(R.mipmap.play_status_playing);
                mPlayOrPauseSwitch.setInnerImageResource(R.mipmap.play_btn_play);
                mPlayOrPauseSwitch.setShowLoading(false);
            }

            @Override
            public void onError(int what, String message) {

            }
        };
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mMusicService = ((MusicService.ServiceBinder) iBinder).getService();

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mMusicService = null;
            }
        };
    }
}

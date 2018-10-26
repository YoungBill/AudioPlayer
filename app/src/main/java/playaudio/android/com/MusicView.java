package playaudio.android.com;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import playaudio.android.com.bean.RadioResponse;

/**
 * Created by taochen on 18-10-25.
 */

public class MusicView extends LinearLayout implements View.OnClickListener {

    private TextView mTitleTv;
    private ImageButton mPlayOrPauseSwitch;
    private ImageButton mPreviewSwitch;
    private ImageButton mNextSwitch;
    private MusicService mMusicService;
    private MusicCallback mMusicCallback = new MusicCallback() {
        @Override
        public void onLoading(RadioResponse.DataBean.ItemsBean musicBean) {
            mTitleTv.setText(musicBean.getTitle());
            mPlayOrPauseSwitch.setBackground(ContextCompat.getDrawable(getContext(), R.mipmap.play_status_playing));
            mPlayOrPauseSwitch.setImageResource(R.mipmap.play_btn_play);
        }

        @Override
        public void onPlay(RadioResponse.DataBean.ItemsBean musicBean) {

        }

        @Override
        public void onPause(RadioResponse.DataBean.ItemsBean musicBean) {

        }

        @Override
        public void onError(int what, String message) {

        }
    };
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mMusicService = ((MusicService.ServiceBinder) iBinder).getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mMusicService = null;
        }
    };

    public MusicView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_music, this);
        mTitleTv = findViewById(R.id.titleTv);
        mPlayOrPauseSwitch = findViewById(R.id.play_or_pause_switch);
        mPlayOrPauseSwitch.setOnClickListener(this);
        mPreviewSwitch = findViewById(R.id.preview_switch);
        mPreviewSwitch.setOnClickListener(this);
        mNextSwitch = findViewById(R.id.next_switch);
        mNextSwitch.setOnClickListener(this);
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
}

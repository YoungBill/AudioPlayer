package playaudio.android.com;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by taochen on 18-10-26.
 */

public class PlayOrPauseSwitch extends FrameLayout {

    private ImageView mOuterIv;
    private ImageView mInnerIv;

    public PlayOrPauseSwitch(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_play_or_pause_switch, this);
        mOuterIv = findViewById(R.id.outerIv);
        mInnerIv = findViewById(R.id.innerIv);
    }

    public void setOuterImageResource(int resId) {
        mOuterIv.setImageResource(resId);
    }

    public void setInnerImageResource(int resId) {
        mInnerIv.setImageResource(resId);
    }

    public void setShowLoading(boolean show) {
        if (show) {
            mInnerIv.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.loading));
        } else {
            mInnerIv.clearAnimation();
        }
    }
}

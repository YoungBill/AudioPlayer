package playaudio.android.com;

import playaudio.android.com.bean.RadioResponse;

/**
 * Created by taochen on 18-10-25.
 * Player status callback
 */

public interface AudioCallback {

    int radio_state_idle = 0;

    int radio_state_initialized = 1;

    int radio_state_loading = 2;

    int radio_state_playing = 3;

    int radio_state_paused = 4;

    void onFinishLoadingMusicList(RadioResponse.DataBean.ItemsBean musicBean);

    void onLoading(RadioResponse.DataBean.ItemsBean musicBean);

    void onPlay(RadioResponse.DataBean.ItemsBean musicBean);

    void onPause(RadioResponse.DataBean.ItemsBean musicBean);

    void onError(int what, String message);
}

package playaudio.android.com;

import playaudio.android.com.bean.RadioResponse;

/**
 * Created by taochen on 18-10-25.
 * Player status callback
 */

public interface MusicCallback {

    void onFinishLoadingMusicList(RadioResponse.DataBean.ItemsBean firstMusicBean);

    void onLoading(RadioResponse.DataBean.ItemsBean musicBean);

    void onPlay(RadioResponse.DataBean.ItemsBean musicBean);

    void onPause(RadioResponse.DataBean.ItemsBean musicBean);

    void onError(int what, String message);
}

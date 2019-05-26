package com.luckyliuqs.mymusic.listener;

import com.luckyliuqs.mymusic.domain.Song;

/**
 * 歌曲播放列表监听器接口类
 */
public interface PlayListListener {

    /**
     * 数据准备好了（歌词），后面可能会用到其他数据
     * @param song
     */
    void onDataReady(Song song);

}

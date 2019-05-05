package com.luckyliuqs.mymusic.manager;

import android.media.MediaPlayer;

import com.luckyliuqs.mymusic.domain.Song;
import com.luckyliuqs.mymusic.listener.OnMusicPlayerListener;

/**
 * 音乐播放管理器接口类：对外暴露的接口
 */
public interface MusicPlayerManager {
    /**
     * 音乐播放
     * @param uri
     * @param data
     */
    void play(String uri, Song data);

    /**
     * 音乐是否正在播放
     */
    boolean isPlaying();

    /**
     * 音乐暂停
     */
    void pause();

    /**
     * 音乐继续播放
     */
    void resume();

    /**
     * 拖到进度条，将音乐从指定位置播放
     * @param progress
     */
    void seekTo(int progress);

    /**
     * 添加音乐播放状态监听器
     * @param listener
     */
    void addOnMusicPlayerListener(OnMusicPlayerListener listener);

    /**
     * 移除音乐播放状态监听器
     * @param listener
     */
    void removeOnMusicPlayerListener(OnMusicPlayerListener listener);

    /**
     * 设置循环播放模式
     * @param looping
     */
    void setLooping(boolean looping);

    /**
     * 销毁
     */
    void destroy();

}

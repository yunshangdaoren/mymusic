package com.luckyliuqs.mymusic.listener;

import android.media.MediaPlayer;

import com.luckyliuqs.mymusic.domain.Song;

/**
 * 音乐播放状态监听器接口类
 */
public interface OnMusicPlayerListener {

    /**
     * 音乐播放器初始化完成
     * @param mediaPlayer
     * @param data
     */
    void onPrepared(MediaPlayer mediaPlayer, Song data);

    /**
     * 进度通知
     * @param progress
     * @param total
     */
    void onProgress(long progress, long total);

    /**
     * 音乐暂停了
     * @param data
     */
    void onPaused(Song data);

    /**
     * 音乐正在播放
     * @param data
     */
    void onPlaying(Song data);

    /**
     * 音乐播放完毕了
     * @param mediaPlayer
     */
    void onCompletion(MediaPlayer mediaPlayer);

    /**
     * 音乐播放发生错误了
     * @param mediaPlayer
     * @param what
     * @param extra
     */
    void onError(MediaPlayer mediaPlayer, int what, int extra);

}

package com.luckyliuqs.mymusic.manager;

import com.luckyliuqs.mymusic.domain.Song;
import com.luckyliuqs.mymusic.listener.PlayListListener;

import java.util.List;

/**
 * 歌曲播放列表manager接口类
 */
public interface PlayListManager {
    /**
     * @return 歌曲播放列表
     */
    List<Song> getPlayList();

    /**
     * 设置歌曲播放列表
     * @param songList
     */
    void setPlayList(List<Song> songList);

    /**
     * 播放指定歌曲
     * @param song
     */
    void play(Song song);

    /**
     * 暂停播放歌曲
     */
    void pause();

    /**
     * 重新播放歌曲
     */
    void resume();

    /**
     * 从播放列表中删除指定歌曲
     * @param song
     */
    void delete(Song song);

    /**
     * @return 歌曲播放信息
     */
    Song getPlayData();

    /**
     * @return 下一首歌曲
     */
    Song next();

    /**
     * @return 上一首歌曲
     */
    Song previous();

    /**
     * @return 歌曲播放模式
     */
    int getLoopModel();

    /**
     * @return 改变后的歌曲播放模式
     */
    int changeLoopModel();

    void addPlayListListener(PlayListListener playListListener);

    void removePlayListListener(PlayListListener playListListener);

    void destroy();

    /**
     * 设置指定歌曲为下一首播放
     * @param song
     */
    void nextPlay(Song song);

}

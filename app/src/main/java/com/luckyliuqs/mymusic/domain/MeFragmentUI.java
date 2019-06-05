package com.luckyliuqs.mymusic.domain;

import java.util.List;

/**
 * 创建的歌单和收藏的歌单列表数据
 */
public class MeFragmentUI {
    /**
     * 标题：我创建的歌单或我收藏的歌单
     */
    private String title;

    /**
     * 歌单列表
     */
    private List<SongList> list;

    public MeFragmentUI(String title, java.util.List<SongList> list) {
        this.title = title;
        this.list = list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public java.util.List<SongList> getList() {
        return list;
    }

    public void setList(java.util.List<SongList> list) {
        this.list = list;
    }
}

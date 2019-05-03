package com.luckyliuqs.mymusic.domain;


import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * 专辑实体类
 */
public class Album extends Base{

    /**
     * 专辑ID
     */
    private String id;

    /**
     * 专辑名称
     */
    private String title;

    /**
     * 专辑封面
     */
    private String banner;

    /**
     * 专辑发行日期
     */
    private String released_at;

    /**
     * 专辑被评论数
     */
    private long comments_count;

    /**
     * 专辑被收藏次数
     */
    private long collections_count;

    /**
     * 专辑的歌手
     */
    private User artist;

    /**
     * 专辑下面的所有歌曲集合
     */
    private ArrayList<Song> songs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getReleased_at() {
        return released_at;
    }

    public void setReleased_at(String released_at) {
        this.released_at = released_at;
    }

    public long getComments_count() {
        return comments_count;
    }

    public void setComments_count(long comments_count) {
        this.comments_count = comments_count;
    }

    public long getCollections_count() {
        return collections_count;
    }

    public void setCollections_count(long collections_count) {
        this.collections_count = collections_count;
    }

    public User getArtist() {
        return artist;
    }

    public void setArtist(User artist) {
        this.artist = artist;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

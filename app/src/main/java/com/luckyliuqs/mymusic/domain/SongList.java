package com.luckyliuqs.mymusic.domain;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * 歌单实体类
 */
public class SongList extends Base{
    /**
     * 歌单ID
     */
    private String id;

    /**
     * 歌单名称
     */
    private String title;

    /**
     * 歌单封面
     */
    private String banner;

    /**
     * 歌单点击次数
     */
    private long clicks_count;

    /**
     * 歌单评论数
     */
    private long comments_count ;

    /**
     * 歌单被收藏次数
     */
    private long collections_count;

    /**
     * 歌单歌曲数量
     */
    private long songs_count;
    /**
     * 歌单描述
     */
    private String description;

    /**
     * 歌单创建人
     */
    private User user;

    /**
     * 歌单创建时间:IS08601格式
     */
    private String created_at;

    /**
     * 歌单里面的歌曲列表集合
     */
    private ArrayList<Song> songs;

    /**
     * 歌单是否被收藏:非null表示被收藏，null表示未被收藏
     */
    private String collection_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public long getClicks_count() {
        return clicks_count;
    }

    public void setClicks_count(long clicks_count) {
        this.clicks_count = clicks_count;
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

    public long getSongs_count() {
        return songs_count;
    }

    public void setSongs_count(long songs_count) {
        this.songs_count = songs_count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public String getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(String collection_id) {
        this.collection_id = collection_id;
    }

    public boolean isCollection(){
        return StringUtils.isNotBlank(collection_id);
    }

}

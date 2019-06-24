package com.luckyliuqs.mymusic.domain;


import java.util.List;

/**
 * 用户动态实体类
 */
public class Feed extends Base{
    /**
     * 用户动态id
     */
    private String id;

    /**
     * 用户动态内容
     */
    private String content;

    /**
     * 该动态的用户
     */
    private User user;

    /**
     * 用户动态的歌曲信息
     */
    private Song song;

    /**
     * 用户动态的视频信息
     */
    private Video video;

    /**
     * 用户动态图片
     */
    private List<Image> images;

    /**
     * 用户动态创建日期
     */
    private String created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}

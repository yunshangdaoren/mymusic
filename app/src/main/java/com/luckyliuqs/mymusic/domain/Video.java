package com.luckyliuqs.mymusic.domain;

/**
 * 视屏实体类
 */
public class Video extends Base{
    /**
     * 视频id
     */
    private String id;

    /**
     * 视频标题
     */
    private String title;

    /**
     * 视频地址
     */
    private String uri;

    /**
     * 视频封面图片
     */
    private String banner;

    /**
     * 视频创建日期
     */
    private String created_at;

    /**
     * 视频点击数量
     */
    private long clicks_count;

    /**
     * 视频播放时长
     */
    private long duration;

    /**
     * 视频点赞数量
     */
    private long likes_count;

    /**
     * 视频评论数量
     */
    private long comments_count;

    /**
     * 视频发布者
     */
    private User user;

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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public long getClicks_count() {
        return clicks_count;
    }

    public void setClicks_count(long clicks_count) {
        this.clicks_count = clicks_count;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(long likes_count) {
        this.likes_count = likes_count;
    }

    public long getComments_count() {
        return comments_count;
    }

    public void setComments_count(long comments_count) {
        this.comments_count = comments_count;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

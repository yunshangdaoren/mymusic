package com.luckyliuqs.mymusic.domain;

import org.apache.commons.lang3.StringUtils;

/**
 * 用户的评论实体类
 */
public class Comment extends Base{
    /**
     * 用户的id
     */
    private String id;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞数量
     */
    private long likes_count;

    /**
     * 创建日期
     */
    private String created_at;

    /**
     * 评论类型：0-视频， 10-单曲， 20-专辑， 30-歌单
     */
    private int style;

    /**
     * 歌单id
     */
    private String sheet_id;

    /**
     * 是否点赞，有值表示点赞， null表示没有点赞
     */
    private String like_id;

    /**
     * 回复评论的id
     */
    private String parent_id;

    /**
     * 回复的评论，服务端返回的数据
     */
    private Comment parent;

    /**
     * 用户实体
     */
    private User user;

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

    public long getLike_count() {
        return likes_count;
    }

    public void setLike_count(long like_count) {
        this.likes_count = like_count;
    }

    public String getCreate_at() {
        return created_at;
    }

    public void setCreate_at(String create_at) {
        this.created_at = create_at;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public String getSheet_id() {
        return sheet_id;
    }

    public void setSheet_id(String sheet_id) {
        this.sheet_id = sheet_id;
    }

    public String getLike_id() {
        return like_id;
    }

    public void setLike_id(String like_id) {
        this.like_id = like_id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isLiked(){
        return StringUtils.isNotBlank(like_id);
    }
}

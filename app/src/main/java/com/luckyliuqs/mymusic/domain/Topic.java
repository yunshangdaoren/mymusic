package com.luckyliuqs.mymusic.domain;

/**
 * 话题实体类
 */
public class Topic extends Base{
    /**
     * 话题ID
     */
    private String id;

    /**
     * 话题名称
     */
    private String title;

    /**
     * 话题封面图片
     */
    private String banner;

    /**
     * 话题描述
     */
    private String description;

    /**
     * 话题参与人数
     */
    private int joins_count;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getJoins_count() {
        return joins_count;
    }

    public void setJoins_count(int joins_count) {
        this.joins_count = joins_count;
    }
}

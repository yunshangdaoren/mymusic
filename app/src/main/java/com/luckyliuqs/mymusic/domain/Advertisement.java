package com.luckyliuqs.mymusic.domain;

/**
 * 广告实体类
 */
public class Advertisement extends Base {
    /**
     * 广告封面
     */
    private String banner;

    /**
     * 点击封面后跳转的地址
     */
    private String uri;

    /**
     * 广告的标题
     */
    private String title;

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

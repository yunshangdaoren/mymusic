package com.luckyliuqs.mymusic.domain;

/**
 * 热门搜索
 */
public class HotSearch extends Base{
    /**
     * id
     */
    private String id;

    /**
     * 标题内容
     */
    private String content;

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
}

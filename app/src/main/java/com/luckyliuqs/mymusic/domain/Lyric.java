package com.luckyliuqs.mymusic.domain;

import java.io.Serializable;

/**
 * 歌词类
 */
public class Lyric implements Serializable {
    /**
     * 歌词ID
     */
    private String id;

    /**
     * 歌词类型：0-LRC，10-KSC
     */
    private int style;

    /**
     * 歌词内容
     */
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

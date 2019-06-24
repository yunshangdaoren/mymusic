package com.luckyliuqs.mymusic.domain.event;

/**
 * 发布搜索key Changed Event
 */
public class OnSearchKeyChangedEvent {
    private String content;

    public OnSearchKeyChangedEvent(String content){
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

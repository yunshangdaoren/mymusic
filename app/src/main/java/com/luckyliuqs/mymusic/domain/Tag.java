package com.luckyliuqs.mymusic.domain;

public class Tag extends Base{
    private String content;
    private int start;

    public Tag(String content, int start){
        this.content = content;
        this.start = start;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
}

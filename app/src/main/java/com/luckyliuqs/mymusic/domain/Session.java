package com.luckyliuqs.mymusic.domain;

public class Session extends Base{
    /**
     * 当前用户的ID
     */
    private String id;
    private String token;
    /**
     * 聊天用的token
     */
    private String im_token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIm_token() {
        return im_token;
    }

    public void setIm_token(String im_token) {
        this.im_token = im_token;
    }
}

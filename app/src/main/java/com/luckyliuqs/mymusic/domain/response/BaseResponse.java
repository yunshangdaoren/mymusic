package com.luckyliuqs.mymusic.domain.response;

public class BaseResponse {
    /**
     * 状态码：有状态码表示出错
     */
    private Integer status;
    /**
     * 出错的提示信息，有可能没有值
     */
    private String message;

    public Integer getStatus() {
        return status;
    }

    public BaseResponse setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

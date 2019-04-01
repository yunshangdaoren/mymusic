package com.luckyliuqs.mymusic.domain.response;

public class DetailResponse<T> extends BaseResponse{

    private T data;

    public T getData() {
        return data;
    }

    public DetailResponse setData(T data) {
        this.data = data;
        return this;
    }
}

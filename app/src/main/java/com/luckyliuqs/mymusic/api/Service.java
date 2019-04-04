package com.luckyliuqs.mymusic.api;

import com.luckyliuqs.mymusic.domain.Session;
import com.luckyliuqs.mymusic.domain.User;
import com.luckyliuqs.mymusic.domain.response.DetailResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Service {
    /**
     * 登陆
     * @param user
     * @return
     */
    @POST("sessions.json")
    Observable<DetailResponse<Session>> login(@Body User user);


    /**
     * 退出
     * @param id
     * @return
     */
    @DELETE("sessions/{id}.json")
    Observable<DetailResponse<Session>> logout(@Path("id") String id);

    /**
     * 注册，注册完成后返回是登陆的信息
     * @param user
     * @return
     */
    @POST("users.json")
    Observable<DetailResponse<Session>> register(@Body User user);


    /**
     * 获取用户详情
     * @param id
     * @return
     */
    @GET("users/{id}.json")
    Observable<DetailResponse<User>> userDetail(@Path("id") String id);
}

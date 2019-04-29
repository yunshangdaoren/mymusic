package com.luckyliuqs.mymusic.api;

import android.icu.lang.UScript;

import com.luckyliuqs.mymusic.domain.Session;
import com.luckyliuqs.mymusic.domain.User;
import com.luckyliuqs.mymusic.domain.response.DetailResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

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
     * 通过用户ID获取用户详情
     * @param id
     * @return
     */
    @GET("users/{id}.json")
    Observable<DetailResponse<User>> userDetail(@Path("id") String id);

    /**
     * 通过用户昵称获取用户详情
     * @param data
     * @return
     */
    @GET("users/-1.json")
    Observable<DetailResponse<User>> userDetailByNickName(@QueryMap Map<String, String> data);
}

package com.luckyliuqs.mymusic.api;

import android.icu.lang.UScript;

import com.luckyliuqs.mymusic.domain.Advertisement;
import com.luckyliuqs.mymusic.domain.Session;
import com.luckyliuqs.mymusic.domain.Song;
import com.luckyliuqs.mymusic.domain.SongList;
import com.luckyliuqs.mymusic.domain.User;
import com.luckyliuqs.mymusic.domain.response.DetailResponse;
import com.luckyliuqs.mymusic.domain.response.ListResponse;

import java.util.HashMap;
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
     * @param id
     * @return 通过用户ID获取用户详情
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

    /**
     * @param data
     * @return 返回歌单列表
     */
    @GET("sheets.json")
    Observable<ListResponse<SongList>> songList(@QueryMap Map<String, String> data);

    /**
     * @return 返回单曲列表
     */
    @GET("songs.jason")
    Observable<ListResponse<Song>> songs();

    /**
     * @return 返回广告列表
     */
    @GET("advertisements.json")
    Observable<ListResponse<Advertisement>> advertisements();




}

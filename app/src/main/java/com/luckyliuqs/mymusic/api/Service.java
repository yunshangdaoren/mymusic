package com.luckyliuqs.mymusic.api;

import com.luckyliuqs.mymusic.domain.Advertisement;
import com.luckyliuqs.mymusic.domain.Comment;
import com.luckyliuqs.mymusic.domain.Session;
import com.luckyliuqs.mymusic.domain.Song;
import com.luckyliuqs.mymusic.domain.SongList;
import com.luckyliuqs.mymusic.domain.Topic;
import com.luckyliuqs.mymusic.domain.User;
import com.luckyliuqs.mymusic.domain.response.DetailResponse;
import com.luckyliuqs.mymusic.domain.response.ListResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 *
 */
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
     * @param data
     * @return 通过用户昵称获取用户详情
     */
    @GET("users/-1.json")
    Observable<DetailResponse<User>> userDetailByNickName(@QueryMap Map<String, String> data);

    /**
     * @return 返回单曲列表
     */
    @GET("songs.jason")
    Observable<ListResponse<Song>> songs();

    /**
     * @param id
     * @return 歌曲详情
     */
    @GET("songs/{id}.json")
    Observable<DetailResponse<Song>> songsDetail(@Path("id") String id);

    /**
     * 收藏指定歌曲到指定歌单中
     * @param songId
     * @param songListId
     * @return
     */
    @FormUrlEncoded
    @POST("contacts.json")
    Observable<DetailResponse<SongList>> addSongInSongList(@Field("song_id") String songId, @Field("sheet_id") String songListId);

    /**
     * 从指定歌单中删除指定歌曲
     * @param songId
     * @param songListId
     * @return
     */
    @DELETE("contacts/{song_id}.json")
    Observable<DetailResponse<SongList>> deleteSongInSongList(@Path("song_id") String songId, @Query("sheet_id") String songListId);

    /**
     * @param data
     * @return 返回歌单列表
     */
    @GET("sheets.json")
    Observable<ListResponse<SongList>> songList(@QueryMap Map<String, String> data);

    /**
     * @param id
     * @return 返回歌单详情
     */
    @GET("sheets/{id}.json")
    Observable<DetailResponse<SongList>> songListDetail(@Path("id") String id);

    /**
     * @return 返回用户自己创建的歌单列表
     */
    @GET("sheets/create.json")
    Observable<ListResponse<SongList>> songListsMyCreate();

    /**
     * @return 返回用户收藏的歌单
     */
    @GET("sheets/collect.json")
    Observable<ListResponse<SongList>> songListsMyCollection();

    /**
     * 创建歌单
     * @param songList
     * @return
     */
    @POST("sheets.json")
    Observable<DetailResponse<SongList>> createSongList(@Body SongList songList);

    /**
     * 传入歌单ID，收藏该歌单
     * @param songListId
     * @return
     */
    @FormUrlEncoded
    @POST("collections.json")
    Observable<DetailResponse<SongList>> collectionSongList(@Field("sheet_id") String songListId);

    /**
     * 传入歌单ID，取消收藏该歌单
     * @param songListId
     * @return
     */
    @DELETE("collections/{id}.json")
    Observable<DetailResponse<SongList>> cancelCollectionSongList(@Path("id") String songListId);

    /**
     * @return 返回广告列表
     */
    @GET("advertisements.json")
    Observable<ListResponse<Advertisement>> advertisements();

    /**
     * 评论列表
     * @param data
     * @return 返回评论列表
     */
    @GET("comments.json")
    Observable<ListResponse<Comment>> comments(@QueryMap Map<String, String> data);

    /**
     * 创建评论
     * @return
     */
    @POST("comments.json")
    Observable<DetailResponse<Comment>> createComment(@Body Comment data);

    /**
     * @param data
     * @return 返回话题列表
     */
    @GET("topics.json")
    Observable<ListResponse<Topic>> topics(@QueryMap Map<String, String> data);

    /**
     * @param id
     * @param data
     * @return 返回话题详情
     */
    @GET("topics/{id}.json")
    Observable<DetailResponse<Topic>> topicDetail(@Path("id") String id, @QueryMap Map<String, String> data);

    /**
     * 评论点赞
     * @param comment_id
     * @return
     */
    @FormUrlEncoded
    @POST("likes.json")
    Observable<DetailResponse<Comment>> like(@Field("comment_id") String comment_id);

    /**
     * 取消评论点赞
     * @param id
     * @return
     */
    @DELETE("likes/{id}.json")
    Observable<DetailResponse<Comment>> unlike(@Path("id") String id);

    /**
     * @param id
     * @param data
     * @return 返回用户的好友列表：用户关注的人
     */
    @GET("users/{id}/following.json")
    Observable<ListResponse<User>> following(@Path("id") String id, @QueryMap Map<String, String> data);

    /**
     * @param id
     * @param data
     * @return 返回用户的粉丝列表：关注用户的人
     */
    @GET("users/{id}/followers.json")
    Observable<ListResponse<User>> followers(@Path("id") String id, @QueryMap Map<String, String> data);




}






























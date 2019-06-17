package com.luckyliuqs.mymusic.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.luckyliuqs.mymusic.AppContext;
import com.luckyliuqs.mymusic.Util.Consts;
import com.luckyliuqs.mymusic.Util.LogUtil;
import com.luckyliuqs.mymusic.Util.SharedPreferencesUtil;
import com.luckyliuqs.mymusic.domain.Advertisement;
import com.luckyliuqs.mymusic.domain.Comment;
import com.luckyliuqs.mymusic.domain.Session;
import com.luckyliuqs.mymusic.domain.Song;
import com.luckyliuqs.mymusic.domain.SongList;
import com.luckyliuqs.mymusic.domain.Topic;
import com.luckyliuqs.mymusic.domain.User;
import com.luckyliuqs.mymusic.domain.response.DetailResponse;
import com.luckyliuqs.mymusic.domain.response.ListResponse;
import com.readystatesoftware.chuck.ChuckInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import com.luckyliuqs.mymusic.interceptor.HttpLoggingInterceptor;

import org.apache.commons.lang3.StringUtils;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private static Api instance;
    private static Service service;

    Api() {
        // 创建 OKHttpClient
        //File file = new File(AppContext.getContext().getCacheDir(), "http");
        //创建缓存
        //Cache cache = new Cache(file, 1024 * 1024 * 1000);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //builder.cache(cache);
        builder.connectTimeout(Consts.TIME_OUT, TimeUnit.SECONDS);//连接超时时间
        builder.writeTimeout(Consts.TIME_OUT, TimeUnit.SECONDS);//写操作 超时时间
        builder.readTimeout(Consts.TIME_OUT, TimeUnit.SECONDS);//读操作超时时间

        if (LogUtil.isDebug) {
            //添加日志拦截器
            builder.addInterceptor(new HttpLoggingInterceptor());
            //添加网络拦截器
            builder.addNetworkInterceptor(new StethoInterceptor());
            //添加通知栏抓包拦截器
            builder.addInterceptor(new ChuckInterceptor(AppContext.getContext()));
        }



        //用对网络请求缓存，详细的查看《详解OKHttp》课程
        //builder.addInterceptor(FORCE_CACHE_NETWORK_DATA_INTERCEPTOR);
        //builder.addNetworkInterceptor(FORCE_CACHE_NETWORK_DATA_INTERCEPTOR);

        //公共请求参数
        builder.addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                SharedPreferencesUtil sp = SharedPreferencesUtil.getCurrentInstance();
                Request request = chain.request();
                if (!sp.isLoginInfoEmpty()) {
                    String userId = sp.getUserId();
                    String token = sp.getToken();

                    if (LogUtil.isDebug) {
                        LogUtil.d("token:" + token + "," + userId);
                    }

                    request = chain.request().newBuilder()
                            .addHeader("User", userId)
                            .addHeader("Authorization", token)
                            .build();
                }
                return chain.proceed(request);
            }
        });

        // 添加公共参数拦截器


        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(Consts.ENDPOINT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(Service.class);
    }

    /**
     * 获取API唯一实例
     * @return
     */
    public static Api getInstance() {
        if (instance == null) {
            instance = new Api();
        }
        return instance;
    }

    /**
     * 登录
     * @param user
     * @return
     */
    public Observable<DetailResponse<Session>> login(User user) {
        return service.login(user);
    }

    /**
     * 注册
     * @param user
     * @return
     */
    public Observable<DetailResponse<Session>> register(User user) {
        return service.register(user);
    }

    /**
     * 注销
     * @param id
     * @return
     */
    public Observable<DetailResponse<Session>> logout(String id) {
        return service.logout(id);
    }

    /**
     * @param id
     * @return 通过用户ID获取用户详情
     */
    public Observable<DetailResponse<User>> userDetail(String id) {
        return service.userDetail(id);
    }

    /**
     * @param nickName
     * @return 通过用户昵称获取用户详情
     */
    public Observable<DetailResponse<User>> userDetailByNickName(String nickName){
        HashMap<String, String> data = new HashMap<>();
        data.put(Consts.NICKNAME, nickName);
        return service.userDetailByNickName(data);
    }

    /**
     * @return 歌单
     */
    public Observable<ListResponse<SongList>> songList(){
        HashMap<String, String> query = new HashMap<>();
        return service.songList(query);
    }

    /**
     * @return 单曲列表
     */
    public Observable<ListResponse<Song>> songs(){
        return service.songs();
    }

    /**
     * @param id
     * @return 歌曲详情
     */
    public Observable<DetailResponse<Song>> songsDetail(String id){
        return service.songsDetail(id);
    }

    /**
     * 添加指定歌曲到指定歌单中
     * @param songId
     * @param songListId
     * @return
     */
    public Observable<DetailResponse<SongList>> addSongInSongList(String songId, String songListId){
        return service.addSongInSongList(songId, songListId);
    }

    /**
     * 从指定歌单中删除指定歌曲
     * @param songId
     * @param songListId
     * @return
     */
    public Observable<DetailResponse<SongList>> deleteSongInSongList(String songId, String songListId){
        return service.deleteSongInSongList(songId, songListId);
    }

    /**
     * @param id
     * @return 歌单详情
     */
    public Observable<DetailResponse<SongList>> songListDetail(String id){
        return service.songListDetail(id);
    }

    /**
     * @return 返回用户自己创建的歌单列表
     */
    public Observable<ListResponse<SongList>> songListsMyCreate(){
        return service.songListsMyCreate();
    }

    /**
     * @return 返回用户收藏的歌单
     */
    public Observable<ListResponse<SongList>> songListsMyCollection(){
        return service.songListsMyCollection();
    }

    /**
     * 创建歌单
     * @param songList
     * @return
     */
    public Observable<DetailResponse<SongList>> createSongList(SongList songList){
        return service.createSongList(songList);
    }

    /**
     * 传入歌单ID，收藏该歌单
     * @param songListId
     * @return
     */
    public Observable<DetailResponse<SongList>> collectionSongList(String songListId){
        return service.collectionSongList(songListId);
    }

    /**
     * 传入歌单ID，取消收藏该歌单
     * @param songListId
     * @return
     */
    public Observable<DetailResponse<SongList>> cancelCollectionSongList(String songListId){
        return service.cancelCollectionSongList(songListId);
    }

    /**
     * @return 广告列表
     */
    public Observable<ListResponse<Advertisement>> advertisements(){
        return service.advertisements();
    }

    /**
     * @param data
     * @return 评论列表
     */
    public Observable<ListResponse<Comment>> comments(Map<String, String> data){
        return service.comments(data);
    }

    /**
     * 创建评论
     * @param comment
     * @return
     */
    public Observable<DetailResponse<Comment>> createComment(Comment comment){
        return service.createComment(comment);
    }

    /**
     * @param title
     * @return 话题列表
     */
    public Observable<ListResponse<Topic>> topics(String title){
        HashMap<String, String> data = new HashMap<>();
        if (StringUtils.isNotEmpty(title)){
            data.put(Consts.FILTER, title);
        }

        return service.topics(data);
    }

    /**
     * @param id
     * @return 话题详情
     */
    public Observable<DetailResponse<Topic>> topicsDetail(String id){
        HashMap<String, String> data = new HashMap<>();
        return service.topicDetail(id, data);
    }

    /**
     * 评论点赞
     * @param comment_id
     * @return
     */
    public Observable<DetailResponse<Comment>> like(String comment_id){
        return service.like(comment_id);
    }

    /**
     * 取消评论点赞
     * @param id
     * @return
     */
    public Observable<DetailResponse<Comment>> unlike(String id){
        return service.unlike(id);
    }

    /**
     * @param id
     * @param nickname
     * @return 好友列表数据：用户关注的人
     */
    public Observable<ListResponse<User>> myFriends(String id, String nickname){
        HashMap<String, String> data = new HashMap<>();

        //根据nickname查找
        if (StringUtils.isNotEmpty(nickname)){
            data.put(Consts.FILTER, nickname);
        }

        return service.following(id, data);
    }

    /**
     * @param id
     * @param nickname
     * @return 粉丝列表数据：关注用户的人
     */
    public Observable<ListResponse<User>> myFans(String id, String nickname){
        HashMap<String, String> data = new HashMap<>();

        //根据nickname查找
        if (StringUtils.isNotEmpty(nickname)){
            data.put(Consts.FILTER, nickname);
        }

        return service.following(id, data);
    }


}

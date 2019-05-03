package com.luckyliuqs.mymusic.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.luckyliuqs.mymusic.AppContext;
import com.luckyliuqs.mymusic.Util.Consts;
import com.luckyliuqs.mymusic.Util.LogUtil;
import com.luckyliuqs.mymusic.Util.SharedPreferencesUtil;
import com.luckyliuqs.mymusic.domain.Advertisement;
import com.luckyliuqs.mymusic.domain.Session;
import com.luckyliuqs.mymusic.domain.Song;
import com.luckyliuqs.mymusic.domain.SongList;
import com.luckyliuqs.mymusic.domain.User;
import com.luckyliuqs.mymusic.domain.response.DetailResponse;
import com.luckyliuqs.mymusic.domain.response.ListResponse;
import com.readystatesoftware.chuck.ChuckInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import com.luckyliuqs.mymusic.interceptor.HttpLoggingInterceptor;

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
            builder.addInterceptor(new HttpLoggingInterceptor());
            builder.addNetworkInterceptor(new StethoInterceptor());
        }

        builder.addInterceptor(new ChuckInterceptor(AppContext.getContext()));

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
     * 通过用户ID获取用户详情
     * @param id
     * @return
     */
    public Observable<DetailResponse<User>> userDetail(String id) {
        return service.userDetail(id);
    }

    /**
     * 通过用户昵称获取用户详情
     * @param nickName
     * @return
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
     * @return 广告列表
     */
    public Observable<ListResponse<Advertisement>> advertisements(){
        return service.advertisements();
    }


}

package com.luckyliuqs.mymusic.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


/**
 * SharedPreferences工具类，储存配置信息
 */
public class SharedPreferencesUtil {
    private static final String TAG = "SharedPreferencesUtil";
    //储存用户信息的key
    private static final String USER_TOKEN = "USER_TOKEN";
    private static final String USER_IM_TOKEN = "USER_IM_TOKEN";
    private static final String USER_ID = "USER_ID";
    //储存FIRST信息的key，判断是否进行登录操作
    private static final String FIRST = "FIRST";
    private static final String KEY_LYRIC_LOCK = "KEY_LYRIC_LOCK";
    private static final String KEY_LYRIC_Y = "KEY_LYRIC_Y";
    private static final String KEY_LYRIC_FONT_SIZE = "KEY_LYRIC_FONT_SIZE";
    private static final String KEY_LYRIC_TEXT_COLOR = "KEY_LYRIC_TEXT_COLOR";
    private static final String KEY_SHOW_LYRIC = "KEY_SHOW_LYRIC";
    private static final String KEY_LOCAL_MUSIC_SORT_KEY = "KEY_LOCAL_MUSIC_SORT_KEY";
    private static final String CURRENT_PLAY_SONG_ID = "CURRENT_PLAY_SONG_ID";
    private static final String LAST_PLAY_SONG_PROGRESS = "LAST_PLAY_SONG_PROGRESS";
    private static final String DEFAULT_LOCAL_MUSIC_SORT_KEY = "id";

    private final Context context;
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor mEditor;
    private static SharedPreferencesUtil mSharedPreferencesUtil;

    /**
     * 构造方法，传入Context
     * @param context
     */
    private SharedPreferencesUtil(Context context){
        this.context = context.getApplicationContext();
        //通过传入的Context对象获取SharedPreferences实例对象，操作模式为私有方式储存
        mSharedPreferences = this.context.getSharedPreferences(TAG,Context.MODE_PRIVATE);
        //获取到可编辑的Editor对象
        mEditor = mSharedPreferences.edit();
    }

    /**
     * 获取唯一SharedPreferencesUtil实例对象
     * @param context
     * @return SharedPreferencesUtil
     */
    public static SharedPreferencesUtil getInstance(Context context){
        if(mSharedPreferencesUtil == null){
            //实例化SharedPreferencesUtil对象
            mSharedPreferencesUtil = new SharedPreferencesUtil(context);
        }
        return mSharedPreferencesUtil;
    }

    /**
     * 获取到当前的SharedPreferencesUtil实例对象
     * @return
     */
    public static SharedPreferencesUtil getCurrentInstance(){
        return mSharedPreferencesUtil;
    }

    /**
     * 储存字符串，传入的key与value值
     * @param key
     * @param value
     */
    public void putString(String key, String value){
        mEditor.putString(key,value);
        //提交修改
        mEditor.commit();
    }

    /**
     * 储存boolean类型数据
     * @param key
     * @param value
     */
    public void putBoolean(String key,boolean value){
        mEditor.putBoolean(key,value);
        mEditor.commit();
    }

    /**
     * 获取传入指定key值所对应的value值
     * @param key
     * @return 如果有对应的value，则返回value值；
     *         如果没有value值则返回默认的""值
     */
    public String getString(String key){
        return mSharedPreferences.getString(key,"");
    }

    /**
     * 获取boolean类型的value值
     * @param key
     * @param defaultValue
     * @return 如果有返回值则返回对应的value值，如果没有返回值；
     *         如果没有value值则返回defalutValue
     */
    public boolean getBoolean(String key,boolean defaultValue){
        return mSharedPreferences.getBoolean(key,defaultValue);
    }

    /**
     * 移除指定key值
     * @param key
     */
    public void removeSP(String key){
        mEditor.remove(key);
        mEditor.commit();
    }

    /**
     * 储存用户Token信息
     * @param token
     */
    public void setToken(String token){
        putString(USER_TOKEN,token);
    }

    /**
     * 返回用户Token信息
     * @return Token
     */
    public String getToken(){
        return getString(USER_TOKEN);
    }

    /**
     * 储存用户IMToken信息
     * @param token
     */
    public void setIMToken(String token){
        putString(USER_IM_TOKEN,token);
    }

    /**
     * 返回用户IMToken信息
     * @return IMToken
     */
    public String getIMToken(){
        return getString(USER_IM_TOKEN);
    }

    /**
     * 判断获取储存的USER_TOKEN信息是否为空
     * @return 如果USER_TOKEN信息不为空，则返回false，表示不登录
     *         如果USER_TOKEN信息为空，则返回true，表示登录
     */
    public boolean isLoginInfoEmpty(){
        return TextUtils.isEmpty(getString(USER_TOKEN));
    }

    /**
     * 储存FISRT信息
     * @param value
     */
    public void setFirst(boolean value){
        putBoolean(FIRST,value);
    }

    /**
     * 获取FIRST信息
     * @return 有FIRST信息则返回对应的value值；
     *         没有FIRST信息则返回true
     */
    public boolean isFirst(){
        return getBoolean(FIRST,true);
    }

    public String getUserId() {
        return getString(USER_ID);
    }

    public void setUserId(String userId) {
        putString(USER_ID,userId);
    }

    public void logout(){
        putString(USER_TOKEN,"");
        putString(USER_IM_TOKEN,"");
        putString(USER_ID,"");
    }

    /**
     * 设置最后一次音乐播放进度值
     * @param progress
     */
    public void setLastSongProgress(int progress){
        putInt(LAST_PLAY_SONG_PROGRESS, progress);
    }

    /**
     * 储存int类型数据
     * @param key
     * @param value
     */
    public void putInt(String key, int value){
        mEditor.putInt(key, value);
        mEditor.apply();
    }

    /**
     * 得到最后一次音乐播放进度值
     * @return
     */
    public int getLastSongProgress(){
        return getInt(LAST_PLAY_SONG_PROGRESS, 0);
    }

    /**
     * 获取指定key对应的int类型数据，并设置默认返回值
     * @param key
     * @param defaultValue
     * @return
     */
    public int getInt(String key, int defaultValue){
        return mSharedPreferences.getInt(key, defaultValue);
    }



}

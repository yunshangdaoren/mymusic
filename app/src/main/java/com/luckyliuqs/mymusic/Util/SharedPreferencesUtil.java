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
    //储存FIRST信息的key，判断是否进行登录操作
    private static final String FIRST = "FIRST";


    private final Context context;
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor mEditor;
    private static SharedPreferencesUtil mSharedPreferencesUtil;

    /**
     * 构造方法，传入Context
     * @param context
     */
    public SharedPreferencesUtil(Context context){
        this.context = context.getApplicationContext();
        //获取实例对象，操作模式为私有方式储存
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
     * 储存传入的key与value值
     * @param key
     * @param value
     */
    public void put(String key, String value){
        mEditor.putString(key,value);
        //提交修改
        mEditor.commit();
    }

    /**
     * 储存博boolean类型数据
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
    public String get(String key){
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
        put(USER_TOKEN,token);
    }

    /**
     * 返回用户Token信息
     * @return Token
     */
    public String getToken(){
        return get(USER_TOKEN);
    }

    /**
     * 储存用户IMToken信息
     * @param token
     */
    public void setIMToken(String token){
        put(USER_IM_TOKEN,token);
    }

    /**
     * 返回用户IMToken信息
     * @return IMToken
     */
    public String getIMToken(){
        return get(USER_IM_TOKEN);
    }

    /**
     * 判断获取储存的USER_TOKEN信息是否为空
     * @return 如果USER_TOKEN信息不为空，则返回false，表示不登录
     *         如果USER_TOKEN信息为空，则返回true，表示登录
     */
    public boolean isLoginInfoEmpty(){
        return TextUtils.isEmpty(get(USER_TOKEN));
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




}

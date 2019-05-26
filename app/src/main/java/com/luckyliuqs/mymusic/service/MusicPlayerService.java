package com.luckyliuqs.mymusic.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.luckyliuqs.mymusic.Util.ServiceUtil;
import com.luckyliuqs.mymusic.manager.MusicPlayerManager;
import com.luckyliuqs.mymusic.manager.PlayListManager;
import com.luckyliuqs.mymusic.manager.impl.MusicPlayerManagerImpl;
import com.luckyliuqs.mymusic.manager.impl.PlayListManagerImpl;

/**
 * 音乐播放Service类
 */
public class MusicPlayerService extends Service {
    private static MusicPlayerManager manager;
    private static PlayListManager playListManager;

    /**
     * 提供一个静态方法获获取MusicPlayerManager实例对象
     * 为什么不支持将逻辑写到Service呢？
     * 是因为操作service要么通过bindService，那么startService会很麻烦
     * @param context
     * @return MusicPlayerManager
     */
    public static MusicPlayerManager getMusicPlayerManager(Context context){
        //开启服务
        startService(context);
        if (MusicPlayerService.manager == null){
            //初始化音乐播放管理器
            MusicPlayerService.manager = MusicPlayerManagerImpl.getInstance(context);
        }
        return manager;
    }

    /**
     * @param context
     * @return 返回一个PlayListManager实例对象
     */
    public static PlayListManager getPlayListManager(Context context){
        startService(context);
        if (MusicPlayerService.playListManager == null){
            //初始化歌曲播放列表管理器
            MusicPlayerService.playListManager = PlayListManagerImpl.getInstance(context);
        }
        return playListManager;
    }

    /**
     * 开启服务
     * @param context
     */
    private static void startService(Context context){
        if (!ServiceUtil.isServiceRunning(context)){
            //如果当前Service没有引用则要启动它
            Intent downloadSvr = new Intent(context, MusicPlayerService.class);
            context.startService(downloadSvr);
        }
    }

    public MusicPlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}

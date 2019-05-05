package com.luckyliuqs.mymusic.manager.impl;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.luckyliuqs.mymusic.domain.Song;
import com.luckyliuqs.mymusic.listener.OnMusicPlayerListener;
import com.luckyliuqs.mymusic.manager.MusicPlayerManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 音乐播放器管理接口实现类
 */
public class MusicPlayerManagerImpl implements MusicPlayerManager{
    /**
     * Handler通知事件类型常量
     */
    public static final int MSG_PROGRESS = 0;
    public static final int MSG_PLAYING = 10;
    public static final int MSG_PAUSE = 20;
    public static final int MSG_PREPARE = 30;
    public static final int MSG_COMPLETION = 40;
    public static final int MSG_ERROR = 50;
    public static final long DEFAULT_TIME = 16;

    private static MusicPlayerManagerImpl manager;
    private final Context context;
    /**
     * 媒体播发器
     */
    private MediaPlayer mediaPlayer;

    /**
     * 音乐播发器状态监听器列表集合
     */
    private List<OnMusicPlayerListener> listeners = new ArrayList<OnMusicPlayerListener>();

    /**
     * 时间任务，用于发布播放器进度
     */
    private TimerTask timerTask;
    private Timer timer;
    /**
     * 正在播放的歌曲
     */
    private Song data;

    private MusicPlayerManagerImpl(Context context){
        this.context = context;
        mediaPlayer = new MediaPlayer();
        initListener();
    }

    /**
     * 单例模式：获取MusicPlayerManagerImpl唯一实例对象
     * @param context
     * @return MusicPlayerManager
     */
    public static synchronized MusicPlayerManager getInstance(Context context){
        if (manager == null){
            manager = new MusicPlayerManagerImpl(context);
        }
        return manager;
    }

    /**
     * 初始化监听器
     */
    private void initListener(){
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                handler.obtainMessage(MSG_PREPARE).sendToTarget();
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                handler.obtainMessage(MSG_ERROR, what, extra).sendToTarget();
                return false;
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                handler.obtainMessage(MSG_COMPLETION).sendToTarget();
            }
        });
    }

    /**
     * 创建Handler，用来将事件转换到主线程
     */
    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_PROGRESS:
                    //回调播放播放进度
                    publishProgress();
                    break;
                case MSG_PLAYING:
                    //回调音乐正在播放
                    for (OnMusicPlayerListener listener : listeners){
                        listener.onPlaying(data);
                    }
                    break;
                case MSG_PAUSE:
                    //回调音乐暂停
                    for (OnMusicPlayerListener listener : listeners){
                        listener.onPaused(data);
                    }
                    break;
                case MSG_PREPARE:
                    //回调音乐播放器初始化完成
                    for (OnMusicPlayerListener listener : listeners){
                        listener.onPrepared(mediaPlayer, data);
                    }
                    break;
                case MSG_COMPLETION:
                    //回调音乐播放完成
                    for (OnMusicPlayerListener listener : listeners){
                        listener.onCompletion(mediaPlayer);
                    }
                    break;
                case MSG_ERROR:
                    //回调音乐播放出错
                    for (OnMusicPlayerListener listener : listeners){
                        listener.onError(mediaPlayer, msg.arg1, msg.arg2);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 回调播放进度通知
     */
    public void publishProgress(){
        //获取当前播放进度
        int currentPosition = mediaPlayer.getCurrentPosition();
        //获取总的播放进度
        int duration = mediaPlayer.getDuration();
        for (OnMusicPlayerListener listener : listeners){
            listener.onProgress(currentPosition, duration);
        }
    }

    /**
     * 开启时间任务，定时回调进度通知
     */
    private void startPublishProgressService(){
        cancelTask();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.obtainMessage(MSG_PROGRESS).sendToTarget();
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, DEFAULT_TIME);
    }

    /**
     * 停止时间任务
     */
    private void stopPublishProgressService(){
        cancelTask();
    }

    /**
     * 停止时间任务
     */
    private void cancelTask(){
        if (timerTask != null){
            timerTask.cancel();
            timerTask = null;
        }

        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }

    //音乐播放
    @Override
    public void play(String uri, Song data) {
        try {
            this.data = data;
            mediaPlayer.reset();
            mediaPlayer.setDataSource(uri);
            mediaPlayer.prepare();
            mediaPlayer.start();

            handler.obtainMessage(MSG_PLAYING).sendToTarget();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //判读音乐是否正在播放
    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    //播放暂停
    @Override
    public void pause() {
        if (isPlaying()){
            mediaPlayer.pause();
            handler.obtainMessage(MSG_PAUSE).sendToTarget();
            stopPublishProgressService();
        }
    }

    //音乐继续播放
    @Override
    public void resume() {
        if (!isPlaying()){
            mediaPlayer.start();
            handler.obtainMessage(MSG_PLAYING).sendToTarget();
            startPublishProgressService();
        }
    }

    //指定位置播放
    @Override
    public void seekTo(int progress) {
        mediaPlayer.seekTo(progress);
    }

    @Override
    public void addOnMusicPlayerListener(OnMusicPlayerListener listener) {
        if (!listeners.contains(listener)){
            //如果未包含才添加进来
            listeners.add(listener);
        }

        //发布一次当前的播放转态，目的是让新添加的播放监听器能够获取上一次的播放状态
        if (mediaPlayer != null){

            if (isPlaying()){
                //音乐正在播放，则发布正在播放事件
                handler.obtainMessage(MSG_PLAYING).sendToTarget();
            }else{
                //音乐暂停，则发布音乐暂停事件
                handler.obtainMessage(MSG_PAUSE).sendToTarget();
            }
        }
    }

    @Override
    public void removeOnMusicPlayerListener(OnMusicPlayerListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void setLooping(boolean looping) {
        mediaPlayer.setLooping(looping);
    }

    @Override
    public void destroy() {
        if (mediaPlayer != null){
            if (isPlaying()){
                //如果正在播放，则先停止播放
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
        }
    }

}

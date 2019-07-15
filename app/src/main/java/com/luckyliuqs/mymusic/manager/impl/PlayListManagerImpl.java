package com.luckyliuqs.mymusic.manager.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luckyliuqs.mymusic.Util.Consts;
import com.luckyliuqs.mymusic.Util.DataUtil;
import com.luckyliuqs.mymusic.Util.ImageUtil;
import com.luckyliuqs.mymusic.Util.NotificationUtil;
import com.luckyliuqs.mymusic.Util.OrmUtil;
import com.luckyliuqs.mymusic.Util.SharedPreferencesUtil;
import com.luckyliuqs.mymusic.api.Api;
import com.luckyliuqs.mymusic.domain.Song;
import com.luckyliuqs.mymusic.domain.response.DetailResponse;
import com.luckyliuqs.mymusic.listener.OnMusicPlayerListener;
import com.luckyliuqs.mymusic.listener.PlayListListener;
import com.luckyliuqs.mymusic.manager.MusicPlayerManager;
import com.luckyliuqs.mymusic.manager.PlayListManager;
import com.luckyliuqs.mymusic.reactivex.AbsObserver;
import com.luckyliuqs.mymusic.service.MusicPlayerService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import org.apache.commons.lang3.StringUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 歌曲播放列表manager接口实现类
 */
public class PlayListManagerImpl implements PlayListManager, OnMusicPlayerListener {
    /**
     * 列表循环
     */
    public static final int MODEL_LOOP_LIST = 0;

    /**
     * 单曲循环
     */
    public static final int MODEL_LOOP_ONE = 1;

    /**
     * 随机循环
     */
    public static final int MODEL_LOOP_RANDOM = 2;

    private static final String TAG = "PlayListManagerImpl";

    /**
     * 设置MediaSessionCompat回调监听动作事件
     */
    private static final long MEDIA_SESSION_ACTIONS = PlaybackStateCompat.ACTION_PLAY
            | PlaybackStateCompat.ACTION_PAUSE
            | PlaybackStateCompat.ACTION_PLAY_PAUSE
            | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            | PlaybackStateCompat.ACTION_STOP
            | PlaybackStateCompat.ACTION_SEEK_TO;


    private static final int MSG_DATA_READY = 0;

    /**
     * 默认保存进度值的间隔事件
     */
    private static final long DEFAULT_SAVE_PROGRESS_TIME = 1000;

    private final MusicPlayerManager musicPlayerManager;

    private List<Song> songList = new LinkedList<>();

    private static PlayListManager playListManager;
    private final Context context;
    private Song currentSong;

    /**
     * 歌曲播放模式
     */
    private int model = MODEL_LOOP_LIST;

    private List<PlayListListener> playListListenerList = new ArrayList<>();
    private final OrmUtil ormUtil;
    private final SharedPreferencesUtil sp;
    private boolean isPlay;
    private long lastTime;
    private MediaSessionCompat mediaSessionCompat;
    private PlaybackStateCompat.Builder stateBuilder;
    public Bitmap albumBitmap;

    /**
     * 创建通知栏音乐播放器广播接收器
     */
    private BroadcastReceiver notificationMusicReceiver;


    public PlayListManagerImpl(Context context){
        this.context = context;
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(context);
        musicPlayerManager.addOnMusicPlayerListener(this);
        ormUtil = OrmUtil.getInstance(context);
        sp = SharedPreferencesUtil.getInstance(context);

        init();
        //初始化媒体播放控制中心
        initMediaSessionCompat();
        //初始化通知栏音乐播放器广播接收器
        initNotificationReceiver();

    }

    public static synchronized PlayListManager getInstance(Context context){
        if (playListManager == null){
            playListManager = new PlayListManagerImpl(context);
        }

        return playListManager;
    }

    private void init(){
        //初始化的时，从数据库中获取歌曲播放列表
        List<Song> songs = ormUtil.queryPlayList(sp.getUserId());
        this.songList.clear();
        this.songList.addAll(songs);

        if (songList.size() > 0){
            //获取最后一次播放的歌曲id
            String id = sp.getLastPlaySongId();
            if (StringUtils.isNotBlank(id)){
                //id不为空，在播放列表中查看该歌曲
                Song song = null;
                for (Song s : songList){
                    if (s.getId().endsWith(id)){
                        song = s;
                        break;
                    }
                }

                if (song == null){
                    //表示在播放列表中没有找到该歌曲，则默认播放播放列表中第一首歌曲
                    defaultPlaySong();
                }else{
                    currentSong = song;
                }
            }else{
                //id为空，则默认播放播放列表中第一首歌曲
                defaultPlaySong();
            }
        }
    }

    private void defaultPlaySong(){
        currentSong = songList.get(0);
    }

    /**
     * 初始化媒体播放控制中心
     */
    private void initMediaSessionCompat(){
        mediaSessionCompat = new MediaSessionCompat(context, TAG);
        //设置哪些事件回调
        //FLAG_HANDLES_MEDIA_BUTTONS：媒体控制按钮
        //FLAG_HANDLES_TRANSPORT_CONTROLS：传输控制命令，耳机，蓝牙等控制
        mediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        //设置那些按钮可用
        stateBuilder = new PlaybackStateCompat.Builder().setActions(MEDIA_SESSION_ACTIONS);
        mediaSessionCompat.setPlaybackState(stateBuilder.build());

        //设置回调
        mediaSessionCompat.setCallback(new MediaSessionCallback());

        //激活控制器
        mediaSessionCompat.setActive(true);

    }

    /**
     * 初始化通知栏音乐播放器广播接收器
     */
    private void initNotificationReceiver(){
        notificationMusicReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Consts.ACTION_LIKE.equals(intent.getAction())){
                    //收藏
                }else if (Consts.ACTION_PREVIOUS.equals(intent.getAction())){
                    //上一首
                    play(previous());
                }else if (Consts.ACTION_PLAY.equals(intent.getAction())){
                    //暂停或播放
                    playOrPause();
                }else if (Consts.ACTION_NEXT.equals(intent.getAction())){
                    //下一首
                    play(next());
                }else if (Consts.ACTION_LYRIC.equals(intent.getAction())){
                    //显示或隐藏桌面歌词
                    showOrHideGlobalLyric();
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Consts.ACTION_LIKE);
        intentFilter.addAction(Consts.ACTION_PREVIOUS);
        intentFilter.addAction(Consts.ACTION_PLAY);
        intentFilter.addAction(Consts.ACTION_NEXT);
        intentFilter.addAction(Consts.ACTION_LYRIC);

        context.registerReceiver(notificationMusicReceiver, intentFilter);
    }


    private void playOrPause(){
        if (musicPlayerManager.isPlaying()){
            pause();
        }else{
            resume();
        }
    }


    /**
     * 显示或隐藏桌面歌词
     */
    private void showOrHideGlobalLyric(){

    }

    @Override
    public List<Song> getPlayList() {
        return songList;
    }

    @Override
    public void setPlayList(List<Song> songList) {
        //将原来的数据在PlayList的标志去掉，并保持到数据库中
        DataUtil.changePlayListFlag(this.songList, false);
        saveAll(this.songList);
        this.songList.clear();
        //将当前传递进来的数据更改为在PlayList标志，并添加到集合
        this.songList.addAll(DataUtil.changePlayListFlag(songList, true));
        //保存数据
        saveAll(this.songList);
    }

    private void saveAll(List<Song> songList){
        //然后在保存当前的所有数据
        for (Song song : songList){
            ormUtil.saveSong(song, sp.getUserId());
        }
    }

    @Override
    public void play(Song song) {
        isPlay = true;
        this.currentSong = song;
        if (song.isLocal()){
            //歌曲为本地音乐，则不需要拼接地址
            musicPlayerManager.play(currentSong.getUri(), song);
        }else{
            //歌曲为在线音乐，需要拼接地址
            musicPlayerManager.play(ImageUtil.getImageURI(currentSong.getUri()), song);
        }

        sp.setLastPlaySongId(currentSong.getId());

    }

    @Override
    public void pause() {
        musicPlayerManager.pause();
    }

    @Override
    public void resume() {
        if (isPlay){
            musicPlayerManager.resume();
        }else{
            //到这里，是应用开启后，第一次点继续播放
            //而这时内部其实还没有准备播放，所以应该调用播放
            play(currentSong);
            if (sp.getLastSongProgress() > 0){
                musicPlayerManager.seekTo(sp.getLastSongProgress());
            }
        }
    }

    @Override
    public void delete(Song song) {
        if (song.equals(currentSong)){
            //如果删除的是当前播放播放的歌曲，则应该停止当前播放，并播放下一首歌曲
            pause();

            Song nextSong = next();
            //只有获取的下一首歌曲不是自己才进行播放
            if (nextSong.equals(currentSong)){
                //没有歌曲可以进行播放了
                currentSong = null;
            }else{
                //播放下一首歌曲
                play(nextSong);
            }

            songList.remove(song);
        }else{
            //如果删除的不是正在播放的歌曲，则直接删除
            songList.remove(song);
        }

        ormUtil.deleteSong(song);
    }

    @Override
    public Song getPlayData() {
        return currentSong;
    }

    @Override
    public Song next() {
        if (songList.size() == 0){
            return null;
        }

        switch (model){
            case MODEL_LOOP_RANDOM:   //随机播放
                //在0 - songList.size()-1中
                int i = new Random().nextInt(songList.size());
                Song song = songList.get(i);
                return song;
            default:                  //列表循环
                int index = songList.indexOf(currentSong);
                if (index != -1){
                    //如果当前播放的是播放列表最后一个，那么就从下标0对应的歌曲开始播放
                    if (index == songList.size() - 1){
                        index = 0;
                    }else{
                        index ++;
                    }
                }else{
                    throw new IllegalArgumentException("Can't find current song!");
                }
                return songList.get(index);
        }
    }

    @Override
    public Song previous() {
        if (songList.size() == 0){
            return null;
        }

        switch (model){
            case MODEL_LOOP_RANDOM:     //随机播放
                //在0 - songList.size()-1中
                int i = new Random().nextInt(songList.size());
                Song song = songList.get(i);
                return song;
            default:                    //列表循环
                int index = songList.indexOf(currentSong);
                if (index != -1){
                    //如果当前播放的是播放列表最后一个，那么就从下标0对应的歌曲开始播放
                    if (index == 0){
                        index = songList.size() - 1;
                    }else{
                        index --;
                    }
                }else{
                    throw new IllegalArgumentException("Can't find current song!");
                }
                return songList.get(index);
        }
    }

    @Override
    public int getLoopModel() {
        return model;
    }

    @Override
    public int changeLoopModel() {
        int newModel = getLoopModel();
        newModel++;
        if (model > MODEL_LOOP_RANDOM){
            this.model = 0;
        }else{
            this.model = newModel;
        }
        if (MODEL_LOOP_ONE == this.model){
            //单曲循环，设置到musicPlayerManager
            musicPlayerManager.setLooping(true);
        }else{
            //非单曲循环
            musicPlayerManager.setLooping(false);
        }
        return this.model;
    }

    @Override
    public void addPlayListListener(PlayListListener playListListener) {
        playListListenerList.add(playListListener);

        //发布一次当前的状态，目的是让新添加的监听器能够获取上一次的状态
        if (currentSong != null && currentSong.getLyric() != null){
            sendDataReadyMessage();
        }

    }

    private void sendDataReadyMessage(){
        handler.obtainMessage(MSG_DATA_READY).sendToTarget();
    }

    @Override
    public void removePlayListListener(PlayListListener playListListener) {
        playListListenerList.remove(playListListener);
    }

    @Override
    public void destroy() {
        if (notificationMusicReceiver != null){
            context.unregisterReceiver(notificationMusicReceiver);
            notificationMusicReceiver = null;
        }
    }

    /**
     * 将指定歌曲设置为下一首播放
     * @param song
     */
    @Override
    public void nextPlay(Song song) {
        int index = songList.indexOf(currentSong);
        if (index != -1){
            //先移除原来的歌曲，因为可能已经存在于播放列表中
            songList.remove(song);
            songList.add(index + 1, song);
        }else{
            throw new IllegalArgumentException("Can't not find current song");
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer, final Song data) {
        data.setDuration(mediaPlayer.getDuration());
        ormUtil.saveSong(data, sp.getUserId());

        //获取歌词
        Api.getInstance().songsDetail(data.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsObserver<DetailResponse<Song>>() {
                    @Override
                    public void onNext(DetailResponse<Song> songDetailResponse) {
                        super.onNext(songDetailResponse);
                        if (songDetailResponse != null && songDetailResponse.getData() != null && songDetailResponse.getData().getLyric() != null) {
                            //将数据设置到歌曲上
                            data.setLyric(songDetailResponse.getData().getLyric());
                            ormUtil.saveSong(data,sp.getUserId());
                            //updateFloatingLayoutInfo();
                            sendDataReadyMessage();
                        }
                    }
                });

        //更新Android系统媒体控制中心的信息
        updateAndroidMediaInfo();


    }

    /**
     * 更新Android系统媒体控制中心的信息
     */
    private void updateAndroidMediaInfo(){
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(context).asBitmap().load(ImageUtil.getImageURI(currentSong.getBanner())).apply(options).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                PlayListManagerImpl.this.albumBitmap = resource;
                updateMediaInfo();
            }
        });
    }

    private void updateMediaInfo() {
        MediaMetadataCompat.Builder metaData = new MediaMetadataCompat.Builder()
                //歌曲名称
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE,currentSong.getTitle())

                //歌手
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, currentSong.getArtist_name())

                //专辑名
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, currentSong.getTitle())

                //专辑歌手
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, currentSong.getAlbum_title())

                //当前歌曲时长
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, currentSong.getDuration())

                //当前歌曲的封面
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumBitmap);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //当前列表总共有多少首音乐
            metaData.putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, getPlayList().size());
        }

        mediaSessionCompat.setMetadata(metaData.build());
    }


    @Override
    public void onProgress(long progress, long total) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastTime > DEFAULT_SAVE_PROGRESS_TIME){
            //间隔时间大于1s才保存，避免频繁操作数据库
            sp.setLastSongProgress((int)progress);
            lastTime = currentTimeMillis;
        }


    }

    @Override
    public void onPaused(Song data) {
        //设置状态，当前播放位置，播放速度
        if (currentSong != null){
            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, getPlayList().indexOf(currentSong), 1.0f);
            mediaSessionCompat.setPlaybackState(stateBuilder.build());
           // NotificationUtil.showMusicNotification(context, currentSong, false);

        }
    }

    @Override
    public void onPlaying(Song data) {
        stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, getPlayList().indexOf(currentSong), 1.0f);
        mediaSessionCompat.setPlaybackState(stateBuilder.build());
        //NotificationUtil.showMusicNotification(context, currentSong, true);


    }

    /**
     * 当前播放的歌曲播放完成
     * @param mediaPlayer
     */
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (getLoopModel() == MODEL_LOOP_ONE){
            //如果是单曲循环，则不进行处理

        }else{
            //非单曲循环，则播放下一首
            Song nextSong = next();
            if (nextSong != null){
                play(nextSong);
            }
        }
    }

    @Override
    public void onError(MediaPlayer mediaPlayer, int waht, int extra) {

    }

    /**
     * 用来将事件切换到主线程
     */
    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_DATA_READY:
                    for (PlayListListener playListListener : playListListenerList){
                        playListListener.onDataReady(currentSong);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 回调
     */
    public class MediaSessionCallback extends MediaSessionCompat.Callback{
        public MediaSessionCallback(){

        }

        /**
         * 播放
         */
        @Override
        public void onPlay() {
            super.onPlay();
            PlayListManagerImpl.this.resume();
        }

        /**
         * 暂停
         */
        @Override
        public void onPause() {
            super.onPause();
            PlayListManagerImpl.this.pause();
        }

        /**
         * 上一首
         */
        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            play(PlayListManagerImpl.this.previous());
        }

        /**
         * 下一首
         */
        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
            play(PlayListManagerImpl.this.next());
        }

    }
}

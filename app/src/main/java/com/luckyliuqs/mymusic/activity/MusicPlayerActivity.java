package com.luckyliuqs.mymusic.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.AlbumDrawableUtil;
import com.luckyliuqs.mymusic.Util.ImageUtil;
import com.luckyliuqs.mymusic.Util.TimeUtil;
import com.luckyliuqs.mymusic.domain.Song;
import com.luckyliuqs.mymusic.listener.OnMusicPlayerListener;
import com.luckyliuqs.mymusic.manager.MusicPlayerManager;
import com.luckyliuqs.mymusic.service.MusicPlayerService;
import com.luckyliuqs.mymusic.view.ListLyricView;
import com.luckyliuqs.mymusic.view.RecordThumbView;
import com.luckyliuqs.mymusic.view.RecordView;

import org.apache.commons.lang3.StringUtils;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * 音乐播放页面Activity类
 */
public class MusicPlayerActivity extends BaseTitleActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, OnMusicPlayerListener {
    //播放页面背景
    private ImageView iv_album_bg;
    //收藏
    private ImageView iv_like;
    //下载
    private ImageView iv_download;
    //评论
    private ImageView iv_comment;
    //更多
    private ImageView iv_more;
    //音乐播放模式
    private ImageView iv_loop_model;
    //上一首
    private ImageView iv_previous;
    //播放或暂停
    private ImageView iv_play_control;
    //下一首
    private ImageView iv_next;
    //播放列表
    private ImageView iv_play_list;
    //播放开始时间
    private TextView tv_start_time;
    //播放结束时间
    private TextView tv_end_time;
    //音乐播放进度条
    private SeekBar sb_progress;
    //音量进度条
    private SeekBar sb_volume;
    //黑胶唱片指针
    private RecordThumbView rt;
    //黑胶唱片CD
    private RecordView rv;

    //歌词页面
    private LinearLayout lyric_container;
    //黑胶唱片页面
    RelativeLayout rl_player_container;
    //歌词
    private ListLyricView lv;

    //音乐播放管理类
    private MusicPlayerManager musicPlayerManager;
    //音量管理类
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
    }

    @Override
    protected void initViews() {
        super.initViews();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            //状态栏颜色设置为透明色
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            //去除半透明的状态栏（如果有的话）
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            //SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：让内容显示到状态栏
            //SYSTEM_UI_FLAG_LAYOUT_STABLE：状态栏文字显示白色
            //SYSTEM_UI_FLAG_LIGHT_STATUS_BAR：状态栏文字显示黑色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        enableBackMenu();

        iv_album_bg = findViewById(R.id.iv_album_bg);
        iv_like = findViewById(R.id.iv_like);
        iv_download = findViewById(R.id.iv_download);
        iv_comment = findViewById(R.id.iv_comment);
        iv_more = findViewById(R.id.iv_more);
        iv_loop_model = findViewById(R.id.iv_loop_model);
        iv_previous = findViewById(R.id.iv_previous);
        iv_next = findViewById(R.id.iv_next);
        iv_play_control = findViewById(R.id.iv_play_control);
        iv_play_list = findViewById(R.id.iv_play_list);
        tv_start_time = findViewById(R.id.tv_start_time);
        tv_end_time = findViewById(R.id.tv_end_time);
        sb_progress = findViewById(R.id.sb_progress);
        sb_volume = findViewById(R.id.sb_volume);

        rt = findViewById(R.id.rt);
        rv = findViewById(R.id.rv);

    }


    @Override
    protected void initDatas() {
        super.initDatas();

        //初始化音乐播放管理器
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(getApplicationContext());
        //初始化音量管理器
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        setVolume();

        //musicPlayerManager.play("http://dev-courses-misuc.ixuea.com/assets/s1.mp3", new Song());

    }

    /**
     * 获取手机音频音量值，用于设置音量进度条
     */
    private void setVolume(){
        //获取最大音量值
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //获取当前音量值
        int current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        sb_volume.setMax(max);
        sb_volume.setProgress(current);
    }

    @Override
    protected void initListener() {
        super.initListener();
        iv_download.setOnClickListener(this);
        iv_play_control.setOnClickListener(this);
        iv_play_list.setOnClickListener(this);
        iv_loop_model.setOnClickListener(this);
        iv_previous.setOnClickListener(this);
        iv_next.setOnClickListener(this);
        //音乐播放进度条监听事件
        sb_progress.setOnSeekBarChangeListener(this);
        //音量进度条监听事件
        sb_volume.setOnSeekBarChangeListener(this);


    }

    /**
     * Activity onResume
     */
    @Override
    protected void onResume() {
        super.onResume();
        musicPlayerManager.addOnMusicPlayerListener(this);
    }

    /**
     * Activity onPause
     */
    @Override
    protected void onPause() {
        super.onPause();
        musicPlayerManager.removeOnMusicPlayerListener(this);
    }

    /**
     * 音乐暂停调用指针动画
     */
    private void stopRecordRotate(){
        rt.stopThumbAnimation();
        rv.stopAlbumRotate();
    }
    /**
    * 音乐播放时调用指针动画
    */
    private void startRecordRotate(){
        rt.startThumbAnimation();
        rv.startAlbumRotate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_play_control:
                playOrPause();
                break;
        }
    }

    private void playOrPause(){
        if (musicPlayerManager.isPlaying()){
            pause();
        }else{
            play();
        }
    }

    private void play(){
        musicPlayerManager.resume();
    }

    private void pause(){
        musicPlayerManager.pause();
    }

    /**
     * 用于监听进度条进度值改变，并实时处理当前进度条对应的事件
     * @param seekBar
     * @param progress
     * @param fromUser
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser){
            if (seekBar.getId() == R.id.sb_volume){
                //如果拖动的是音量进度条
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }else{
                //拖动的是音乐播放进度条
                musicPlayerManager.seekTo(progress);
                if (!musicPlayerManager.isPlaying()){
                    //如果音乐处于暂停状态，则让音乐开始播放
                    musicPlayerManager.resume();
                }
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /**
     * 音乐播放状态监听器回调方法:进度通知
     * @param progress
     * @param total
     */
    @Override
    public void onProgress(long progress, long total) {
        tv_start_time.setText(TimeUtil.formatMSTime((int) progress));
        sb_progress.setProgress((int)progress);
    }

    /**
     * 音乐播放状态监听器回调方法:音乐暂停了
     * @param data
     */
    @Override
    public void onPaused(Song data) {
        iv_play_control.setImageResource(R.drawable.selector_music_play);
        stopRecordRotate();
    }

    /**
     * 音乐播放状态监听器回调方法:音乐正在播放
     * @param data
     */
    @Override
    public void onPlaying(Song data) {
        iv_play_control.setImageResource(R.drawable.selector_music_pause);


        startRecordRotate();
    }

    /**
     * 音乐播放状态监听器回调方法:音乐播放完毕了
     * @param mediaPlayer
     */
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    /**
     * 音乐播放状态监听器回调方法:音乐播放发生错误了
     * @param mediaPlayer
     * @param what
     * @param extra
     */
    @Override
    public void onError(MediaPlayer mediaPlayer, int what, int extra) {

    }

    /**
     * 音乐播放状态监听器回调方法:音乐播放器初始化完成
     * @param mediaPlayer
     * @param data
     */
    @Override
    public void onPrepared(MediaPlayer mediaPlayer, Song data) {
        setInitData(data);
    }

    public void setInitData(Song data){
        //设置音乐播放进度条最大播放时间进度值
        sb_progress.setMax((int) data.getDuration());
        //设置最后一次播放时间进度值
        sb_progress.setProgress(sp.getLastSongProgress());
        //设置音乐播放进度条开始时间
        tv_start_time.setText(TimeUtil.formatMSTime((int)sp.getLastSongProgress()));
        //设置音乐播放进度条结束时间
        tv_end_time.setText(TimeUtil.formatMSTime((int)data.getDuration()));

        //设置页面标题为歌曲名称
        getActivity().setTitle(data.getTitle());

        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(data.getArtist_name());

        if (StringUtils.isNotBlank(data.getBanner())){

            final RequestOptions requestOptions = RequestOptions.bitmapTransform(new BlurTransformation(50, 5));

            requestOptions.error(R.drawable.default_album);

            Glide.with(getActivity()).asDrawable().load(ImageUtil.getImageURI(data.getBanner())).apply(requestOptions).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    AlbumDrawableUtil albumDrawableUtil = new AlbumDrawableUtil(iv_album_bg.getDrawable(), resource);
                    iv_album_bg.setImageDrawable(albumDrawableUtil.getDrawable());
                    albumDrawableUtil.start();
                }
            });

        }


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean result = super.onKeyDown(keyCode, event);
        if (KeyEvent.KEYCODE_VOLUME_UP == keyCode || KeyEvent.KEYCODE_VOLUME_DOWN == keyCode){
            setVolume();
        }
        return result;
    }
}
























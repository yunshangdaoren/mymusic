package com.luckyliuqs.mymusic.activity;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.AlbumDrawableUtil;
import com.luckyliuqs.mymusic.Util.ImageUtil;
import com.luckyliuqs.mymusic.Util.TimeUtil;
import com.luckyliuqs.mymusic.adapter.BaseRecyclerViewAdapter;
import com.luckyliuqs.mymusic.adapter.PlayListAdapter;
import com.luckyliuqs.mymusic.domain.Lyric;
import com.luckyliuqs.mymusic.domain.Song;
import com.luckyliuqs.mymusic.fragment.PlayListDialogFragment;
import com.luckyliuqs.mymusic.listener.OnLyricClickListener;
import com.luckyliuqs.mymusic.listener.OnMusicPlayerListener;
import com.luckyliuqs.mymusic.listener.PlayListListener;
import com.luckyliuqs.mymusic.manager.MusicPlayerManager;
import com.luckyliuqs.mymusic.manager.PlayListManager;
import com.luckyliuqs.mymusic.manager.impl.PlayListManagerImpl;
import com.luckyliuqs.mymusic.parser.LyricsParser;
import com.luckyliuqs.mymusic.parser.domain.ConvertedLyric;
import com.luckyliuqs.mymusic.parser.domain.Line;
import com.luckyliuqs.mymusic.service.MusicPlayerService;
import com.luckyliuqs.mymusic.view.ListLyricView;
import com.luckyliuqs.mymusic.view.LyricView;
import com.luckyliuqs.mymusic.view.RecordThumbView;
import com.luckyliuqs.mymusic.view.RecordView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * 音乐播放页面Activity类
 */
public class MusicPlayerActivity extends BaseTitleActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, OnMusicPlayerListener, OnLyricClickListener, PlayListListener {
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
    //歌词列表View
    private LyricView lv;

    //歌曲播放管理类
    private MusicPlayerManager musicPlayerManager;
    //歌曲播放列表管理类
    private PlayListManager playListManager;


    //音量管理类
    private AudioManager audioManager;

    private ArrayList<Line> currentLyricLines;

    //歌词解析基类
    private LyricsParser parser;

    //当前播放的歌曲
    private Song currentSong;

    private PlayListDialogFragment playListDialogFragment;

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

        //歌词页面
        lyric_container = findViewById(R.id.lyric_container);
        //歌词列表View
        lv = findViewById(R.id.lv);
        rl_player_container = findViewById(R.id.rl_player_container);

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
        rv = findViewById(R.id.recordView);

    }


    @Override
    protected void initDatas() {
        super.initDatas();

        //初始化歌曲播放管理器
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(getApplicationContext());
        //初始化歌曲播放列表管理器
        playListManager = MusicPlayerService.getPlayListManager(getApplicationContext());
        //初始化音量管理器
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        setVolume();

        //得到当前播放的歌曲
        currentSong = this.playListManager.getPlayData();
        //设置播放模式图标：单曲循环、列表循环、随机播放
        showLoopModel(playListManager.getLoopModel());
        //初始化歌曲播放页面信息
        setInitData(currentSong);


        //if (!musicPlayerManager.isPlaying()){
            //musicPlayerManager.play("http://dev-courses-misuc.ixuea.com/assets/s1.mp3", new Song());
            //http://localhost:8080/1.mp3
//            musicPlayerManager.play(playUrl+playIndex+".mp3", new Song());
//            if (playIndex > 7){
//                playIndex = 0;
//            }else{
//                playIndex ++;
//            }
        //}


//        Lyric lyric = new Lyric();
//        lyric.setStyle(10);
//        lyric.setContent("karaoke := CreateKaraokeObject;\nkaraoke.rows := 2;\nkaraoke.TimeAfterAnimate := 2000;\nkaraoke.TimeBeforeAnimate := 4000;\nkaraoke.clear;\nkaraoke.add('00:20.699', '00:27.055', '[●●●●●●]', '7356',RGB(255,0,0));\n\nkaraoke.add('00:27.487', '00:32.068', '一时失志不免怨叹', '347,373,1077,320,344,386,638,1096');\nkaraoke.add('00:33.221', '00:38.068', '一时落魄不免胆寒', '282,362,1118,296,317,395,718,1359');\nkaraoke.add('00:38.914', '00:42.164', '那通失去希望', '290,373,348,403,689,1147');\nkaraoke.add('00:42.485', '00:44.530', '每日醉茫茫', '298,346,366,352,683');\nkaraoke.add('00:45.273', '00:49.029', '无魂有体亲像稻草人', '317,364,380,351,326,351,356,389,922');\nkaraoke.add('00:50.281', '00:55.585', '人生可比是海上的波浪', '628,1081,376,326,406,371,375,1045,378,318');\nkaraoke.add('00:56.007', '01:00.934', '有时起有时落', '303,362,1416,658,750,1438');\nkaraoke.add('01:02.020', '01:04.581', '好运歹运', '360,1081,360,760');\nkaraoke.add('01:05.283', '01:09.453', '总嘛要照起来行', '303,338,354,373,710,706,1386');\nkaraoke.add('01:10.979', '01:13.029', '三分天注定', '304,365,353,338,690');\nkaraoke.add('01:13.790', '01:15.950', '七分靠打拼', '356,337,338,421,708');\nkaraoke.add('01:16.339', '01:20.870', '爱拼才会赢', '325,1407,709,660,1430');\nkaraoke.add('01:33.068', '01:37.580', '一时失志不免怨叹', '307,384,1021,363,357,374,677,1029');\nkaraoke.add('01:38.660', '01:43.656', '一时落魄不免胆寒', '381,411,1067,344,375,381,648,1389');\nkaraoke.add('01:44.473', '01:47.471', '那通失去希望', '315,365,340,369,684,925');\nkaraoke.add('01:48.000', '01:50.128', '每日醉茫茫', '338,361,370,370,689');\nkaraoke.add('01:50.862', '01:54.593', '无魂有体亲像稻草人', '330,359,368,376,325,334,352,389,898');\nkaraoke.add('01:55.830', '02:01.185', '人生可比是海上的波浪', '654,1056,416,318,385,416,373,1032,342,363');\nkaraoke.add('02:01.604', '02:06.716', '有时起有时落', '303,330,1432,649,704,1694');\nkaraoke.add('02:07.624', '02:10.165', '好运歹运', '329,1090,369,753');\nkaraoke.add('02:10.829', '02:15.121', '总嘛要照起来行', '313,355,362,389,705,683,1485');\nkaraoke.add('02:16.609', '02:18.621', '三分天注定', '296,363,306,389,658');\nkaraoke.add('02:19.426', '02:21.428', '七分靠打拼', '330,359,336,389,588');\nkaraoke.add('02:21.957', '02:26.457', '爱拼才会赢', '315,1364,664,767,1390');\nkaraoke.add('02:50.072', '02:55.341', '人生可比是海上的波浪', '656,1086,349,326,359,356,364,1095,338,340');\nkaraoke.add('02:55.774', '03:01.248', '有时起有时落', '312,357,1400,670,729,2006');\nkaraoke.add('03:01.787', '03:04.369', '好运歹运', '341,1084,376,781');\nkaraoke.add('03:05.041', '03:09.865', '总嘛要起工来行', '305,332,331,406,751,615,2084');\nkaraoke.add('03:10.754', '03:12.813', '三分天注定', '309,359,361,366,664');\nkaraoke.add('03:13.571', '03:15.596', '七分靠打拼', '320,362,349,352,642');\nkaraoke.add('03:16.106', '03:20.688', '爱拼才会赢', '304,1421,661,706,1490');\n");
//
//        setLyric(lyric);

//        Lyric lyric = new Lyric();
//        lyric.setStyle(0);
//        lyric.setContent("[ti:爱的代价]\n[ar:李宗盛]\n[al:滚石香港黄金十年 李宗盛精选]\n[ly:李宗盛]\n[mu:李宗盛]\n[ma:]\n[pu:]\n[by:ttpod]\n[total:272073]\n[offset:0]\n[00:00.300]爱的代价 - 李宗盛\n[00:01.979]作词：李宗盛\n[00:03.312]作曲：李宗盛\n[00:06.429]\n[00:16.282]还记得年少时的梦吗\n[00:20.575]像朵永远不调零的花\n[00:24.115]陪我经过那风吹雨打\n[00:27.921]看世事无常\n[00:29.653]看沧桑变化\n[00:32.576]那些为爱所付出的代价\n[00:36.279]是永远都难忘的啊\n[00:40.485]所有真心的痴心的话\n[00:43.779]永在我心中虽然已没有他\n[00:50.073]走吧 走吧\n[00:54.868]人总要学着自己长大\n[00:58.829]走吧 走吧\n[01:02.616]人生难免经历苦痛挣扎\n[01:06.316]走吧 走吧\n[01:10.795]为自己的心找一个家\n[01:14.399]也曾伤心流泪\n[01:16.742]也曾黯然心碎\n[01:18.845]这是爱的代价\n[01:21.579]\n[01:40.358]也许我偶尔还是会想他\n[01:44.553]偶尔难免会惦记着他\n[01:48.378]就当他是个老朋友啊\n[01:51.891]也让我心疼也让我牵挂\n[01:56.617]只是我心中不再有火花\n[02:00.507]让往事都随风去吧\n[02:04.660]所有真心的痴心的话\n[02:07.625]仍在我心中\n[02:09.563]虽然已没有他\n[02:14.454]走吧 走吧\n[02:18.580]人总要学着自己长大\n[02:24.499]走吧 走吧\n[02:26.586]人生难免经历苦痛挣扎\n[02:30.293]走吧 走吧\n[02:34.828]为自己的心找一个家\n[02:38.482]也曾伤心流泪\n[02:40.767]也曾黯然心碎\n[02:42.742]这是爱的代价\n[02:45.509]\n[03:22.502]走吧 走吧\n[03:26.581]人总要学着自己长大\n[03:32.414]走吧 走吧\n[03:34.496]人生难免经历苦痛挣扎\n[03:40.425]走吧 走吧\n[03:42.616]为自己的心找一个家\n[03:46.398]也曾伤心流泪\n[03:48.852]也曾黯然心碎\n[03:50.645]这是爱的代价\n");
//        setLyric(lyric);

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

    /**
     * 设置歌词
     * @param lyric
     */
    private void setLyric(Lyric lyric){
        //创建歌词解析类
        parser = LyricsParser.parse(lyric.getStyle(), lyric.getContent());
        //解析歌词
        parser.parse();
        if (parser.getConvertedLyric() != null){
            //如果解析的歌词不为null,则为LyricView歌词界面设置解析后的歌词数据
            lv.setData(parser.getConvertedLyric());
        }
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

        lv.setOnClickListener(this);
        rv.setOnClickListener(this);

        lv.setOnLyricClickListener(this);


        //音乐播放进度条监听事件
        sb_progress.setOnSeekBarChangeListener(this);
        //音量进度条监听事件
        sb_volume.setOnSeekBarChangeListener(this);

        playListManager.addPlayListListener(this);

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
            case R.id.lv:             //歌词
                showRecordView();
                break;
            case R.id.recordView:     //唱片
                showLyricView();
                break;
            case R.id.iv_play_control:  //播放或暂停
                playOrPause();
                break;
            case R.id.iv_previous:      //上一首
                Song previousSong = playListManager.previous();
                playListManager.play(previousSong);
                break;
            case R.id.iv_next:          //下一首
                Song nextSong = playListManager.next();
                playListManager.play(nextSong);
                break;
            case R.id.iv_loop_model:    //播放模式
                int loopModel = playListManager.changeLoopModel();
                showLoopModelByClick(loopModel);
                break;
            case R.id.iv_download:      //下载

                break;
            case R.id.iv_play_list:     //播放列表
                showPlayListDialog();
                break;

        }
    }

    /**
     * 从歌词页面切换到黑胶唱片页面
     */
    private void showRecordView(){
        lyric_container.setVisibility(View.GONE);
        rl_player_container.setVisibility(View.VISIBLE);
    }

    /**
     * 从黑胶唱片页面切换到歌词页面
     */
    private void showLyricView(){
        lyric_container.setVisibility(View.VISIBLE);
        rl_player_container.setVisibility(View.GONE);
    }

    /**
     * 暂停或播放
     */
    private void playOrPause(){
        if (musicPlayerManager.isPlaying()){
            //Toast.makeText(this, "暂停", Toast.LENGTH_SHORT).show();
            pause();
        }else{
            //Toast.makeText(this, "播放", Toast.LENGTH_SHORT).show();
            play();
        }
    }

    /**
     * 播放
     */
    private void play(){
        musicPlayerManager.resume();
        //musicPlayerManager.play("http://dev-courses-misuc.ixuea.com/assets/s1.mp3", new Song());
    }

    /**
     * 暂停
     */
    private void pause(){
        musicPlayerManager.pause();
    }

    /**
     * 设置播放模式图标
     * @param model
     */
    private void showLoopModel(int model){
        switch (model){
            case PlayListManagerImpl.MODEL_LOOP_RANDOM:  //随机播放
                //将图标设置为随机播放
                iv_loop_model.setImageResource(R.drawable.ic_music_play_random);
                break;
            case PlayListManagerImpl.MODEL_LOOP_LIST:    //列表循环
                //将图标设置为列表循环
                iv_loop_model.setImageResource(R.drawable.ic_music_play_repleat_list);
                break;
            case PlayListManagerImpl.MODEL_LOOP_ONE:      //单曲循环
                //将图标设置为单曲循环
                iv_loop_model.setImageResource(R.drawable.ic_music_play_repleat_one);
                break;
        }
    }

    /**
     * 设置播放模式图标
     * @param model
     */
    private void showLoopModelByClick(int model){
        switch (model){
            case PlayListManagerImpl.MODEL_LOOP_RANDOM:  //随机播放
                //将图标设置为随机播放
                iv_loop_model.setImageResource(R.drawable.ic_music_play_random);
                Toast.makeText(this, "随机播放", Toast.LENGTH_SHORT).show();
                break;
            case PlayListManagerImpl.MODEL_LOOP_LIST:    //列表循环
                //将图标设置为列表循环
                iv_loop_model.setImageResource(R.drawable.ic_music_play_repleat_list);
                Toast.makeText(this, "列表循环", Toast.LENGTH_SHORT).show();
                break;
            case PlayListManagerImpl.MODEL_LOOP_ONE:      //单曲循环
                //将图标设置为单曲循环
                iv_loop_model.setImageResource(R.drawable.ic_music_play_repleat_one);
                Toast.makeText(this, "单曲循环", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 展示歌曲播放列表Dialog
     */
    private void showPlayListDialog(){
        playListDialogFragment = new PlayListDialogFragment();
        //设置当前正在播放的歌曲
        playListDialogFragment.setCurrentSong(playListManager.getPlayData());
        //设置歌曲播放列表数据
        playListDialogFragment.setData(playListManager.getPlayList());

        //歌曲播放列表歌曲点击播放事件
        playListDialogFragment.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerViewAdapter.ViewHolder holder, int position) {
                playListDialogFragment.dismiss();
                playListManager.play(playListManager.getPlayList().get(position));
                playListDialogFragment.setCurrentSong(playListManager.getPlayData());
                playListDialogFragment.notifyDataSetChanged();
            }
        });

        //歌曲播放列表歌曲点击删除事件
        playListDialogFragment.setOnRemoveClickListener(new PlayListAdapter.OnRemoveClickListener() {
            @Override
            public void onRemoveClick(int position) {
                Song currentSong = playListManager.getPlayList().get(position);
                playListManager.delete(currentSong);
                playListDialogFragment.removeData(position);
                //获取到接下来的正在播放的歌曲
                currentSong = playListManager.getPlayData();
                if (currentSong == null){
                    //如果当前播放歌曲为null
                    playListManager.destroy();
                    finish();
                }else{
                    //如果当前播放歌曲不为null
                    playListDialogFragment.setCurrentSong(currentSong);
                }
            }
        });

        playListDialogFragment.show(getSupportFragmentManager(), "dialog");
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
                //Toast.makeText(this, "拖动进度为："+progress, Toast.LENGTH_SHORT).show();
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
        lv.show(progress);
    }

    /**
     * 音乐播放状态监听器回调方法:音乐暂停了
     * @param data
     */
    @Override
    public void onPaused(Song data) {
        iv_play_control.setImageResource(R.drawable.selector_music_play);
        //黑胶唱片停止转动
        stopRecordRotate();
    }

    /**
     * 音乐播放状态监听器回调方法:音乐正在播放
     * @param data
     */
    @Override
    public void onPlaying(Song data) {
        iv_play_control.setImageResource(R.drawable.selector_music_pause);

        currentSong = data;
        //黑胶唱片停止转动
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

    /**
     * 初始化歌曲播放页面信息
     * @param data
     */
    public void setInitData(Song data){
        //设置音乐播放进度条最大播放时间进度值
        sb_progress.setMax((int) data.getDuration());
        //设置最后一次播放时间进度值
        sb_progress.setProgress(sp.getLastSongProgress());
        //设置音乐播放进度条开始时间
        tv_start_time.setText(TimeUtil.formatMSTime((int)sp.getLastSongProgress()));
        //设置音乐播放进度条结束时间
        tv_end_time.setText(TimeUtil.formatMSTime((int)data.getDuration()));

        //设置歌曲播放页面唱片封面信息
        rv.setAlbumUri(data.getBanner());
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

    /**
     * 音量按键监听
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean result = super.onKeyDown(keyCode, event);
        if (KeyEvent.KEYCODE_VOLUME_UP == keyCode || KeyEvent.KEYCODE_VOLUME_DOWN == keyCode){
            setVolume();
        }
        return result;
    }

    /**
     * 播放文字监听事件：滚动到指定行歌词进行播放
     * @param time
     */
    @Override
    public void onLyricClick(long time) {
        musicPlayerManager.seekTo((int) time);
        if (!musicPlayerManager.isPlaying()){
            musicPlayerManager.resume();
        }
    }

    /**
     * 数据准备好了（歌词）
     * @param song
     */
    @Override
    public void onDataReady(Song song) {
        //设置歌词
        setLyric(song.getLyric());
    }


}
























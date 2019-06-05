package com.luckyliuqs.mymusic.activity;

import android.animation.ValueAnimator;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.MusicUtil;
import com.luckyliuqs.mymusic.domain.Song;
import com.luckyliuqs.mymusic.domain.event.ScanMusicCompleteEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 扫描本地音乐Activity
 */
public class ScanLocalMusicActivity extends BaseTitleActivity implements View.OnClickListener {
    /**
     * 扫描歌曲筛选条件：为歌曲，且歌曲大小大于指定值，歌曲时长大于指定值
     */
    private static final String SELECTION = MediaStore.Audio.AudioColumns.IS_MUSIC + " !=0 AND " +
            MediaStore.Audio.AudioColumns.SIZE + " >= ? AND " + MediaStore.Audio.AudioColumns.DURATION + " >= ?";
    private static final double DEFAULT_RADIUS = 30;

    /**
     * 1M
     */
    private static final int DEFAULT_FILTER_MUSIC_SIZE = 1*1024*1024;

    /**
     * 60秒
     */
    private static final int DEFAULT_FILTER_MUSIC_TIME = 60*1000;

    /**
     * 扫描放大镜图标
     */
    private ImageView iv_scan_local_music_zoom;

    /**
     * 扫描line
     */
    private ImageView iv_scan_local_music_line;

    /**
     * 扫描进度
     */
    private TextView tv_scan_local_music_progress;

    /**
     * 扫描button
     */
    private Button bt_scan_local_music;

    /**
     * 是否正在扫描本地歌曲
     */
    private boolean isScanning;

    /**
     * 扫描是否完成
     */
    private boolean isScanComplete;

    /**
     * 扫描放大镜图标扫描动画
     */
    private ValueAnimator zoomValueAnimator;

    /**
     * 扫描line动画
     */
    private TranslateAnimation lineTranslateAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_local_musci);
    }

    @Override
    protected void initViews() {
        super.initViews();
        enableBackMenu();

        iv_scan_local_music_zoom = findViewById(R.id.iv_scan_local_music_zoom);
        iv_scan_local_music_line = findViewById(R.id.iv_scan_local_music_line);
        tv_scan_local_music_progress = findViewById(R.id.tv_scan_local_music_progress);
        bt_scan_local_music = findViewById(R.id.bt_scan_local_music);
    }

    @Override
    protected void initListener() {
        super.initListener();
        bt_scan_local_music.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_scan_local_music:
                bt_scan_local_music();
                break;
        }

    }

    /**
     * 按钮点击事件：开始扫描或停止扫描本地歌曲
     */
    public void bt_scan_local_music(){
        if (isScanComplete){
            //如果搜索完成，则发送event
            EventBus.getDefault().post(new ScanMusicCompleteEvent());
            finish();
            return;
        }

        if (isScanning){
            //如果正在扫描中，按钮从停止扫描变为开始扫描
            isScanning = false;
            //设置按钮文字为开始扫描
            bt_scan_local_music.setText(R.string.start_scan);
            bt_scan_local_music.setBackgroundResource(R.drawable.selector_button_reverse);
            bt_scan_local_music.setTextColor(getResources().getColorStateList(R.color.selector_text_reverse));

            //停止扫描本地歌曲
            stopScan();
        }else{
            //如果未在扫描中，按钮从开始扫描变为停止扫描
            isScanning = false;
            //设置按钮文字为开始扫描
            bt_scan_local_music.setText(R.string.stop_scan);
            bt_scan_local_music.setBackgroundResource(R.drawable.selector_button);
            bt_scan_local_music.setTextColor(getResources().getColorStateList(R.color.selector_text));

            //开始扫描本地歌曲
            startScan();
        }
    }

    /**
     * 停止扫描本地歌曲
     */
    private void stopScan(){
        //清除动画
        iv_scan_local_music_line.clearAnimation();
        //隐藏扫描line
        iv_scan_local_music_line.setVisibility(View.GONE);
        if (zoomValueAnimator != null){
            zoomValueAnimator.cancel();
            zoomValueAnimator = null;
        }
    }

    /**
     * 开始扫描和扫描动画
     */
    private void startScan(){
        //创建扫描放大镜图标扫描动画
        zoomValueAnimator = ValueAnimator.ofFloat(0.0F, 360.0F);
        zoomValueAnimator.setInterpolator(new LinearInterpolator());
        //动画时长：3秒
        zoomValueAnimator.setDuration(2000);
        //动画次数：循环
        zoomValueAnimator.setRepeatCount(-1);
        zoomValueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        zoomValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float angle = (float) animation.getAnimatedValue();
                float translateX = (float) (DEFAULT_RADIUS * Math.cos(angle));
                float translateY = (float) (DEFAULT_RADIUS * Math.sin(angle));
                iv_scan_local_music_zoom.setTranslationX(translateX);
                iv_scan_local_music_zoom.setTranslationY(translateY);
            }
        });
        zoomValueAnimator.start();

        //扫描line动画，不使用属性动画是因为属性动画需要获取坐标
        lineTranslateAnimation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                                                        TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                                                        TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                                                        TranslateAnimation.RELATIVE_TO_PARENT, 0.7f);
        lineTranslateAnimation.setInterpolator(new DecelerateInterpolator());
        //动画时长：3秒
        lineTranslateAnimation.setDuration(3000);
        //动画次数：循环
        lineTranslateAnimation.setRepeatCount(-1);
        lineTranslateAnimation.setRepeatMode(ValueAnimator.RESTART);
        lineTranslateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                iv_scan_local_music_line.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv_scan_local_music_line.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        iv_scan_local_music_line.clearAnimation();
        iv_scan_local_music_line.startAnimation(lineTranslateAnimation);

        //开始扫描本地歌曲
        startScanMusic();
    }

    /**
     * 扫描本地歌曲，这里只扫描媒体库，不全盘扫描
     */
    private void startScanMusic(){
        new AsyncTask<Void, String, List<Song>>(){
            @Override
            protected List<Song> doInBackground(Void... voids) {
                List<Song> songList = null;
                try{
                   // SystemClock.sleep(6000);

                    songList = new ArrayList<>();

                    /**
                     * 使用内容提供者进行查询本地歌曲
                     * 歌曲筛选条件为：大于1M，时长大于60秒
                     *
                     * @param uri:资源标识符
                     * @param projection 选择那些字段
                     * @param selection 条件
                     * @param selectionArgs 条件参数
                     * @param sortOrder 排序
                     */
                    Cursor cursor = getContentResolver().query(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            new String[]{
                                    BaseColumns._ID,
                                    MediaStore.Audio.AudioColumns.TITLE,
                                    MediaStore.Audio.AudioColumns.ARTIST,
                                    MediaStore.Audio.AudioColumns.ALBUM,
                                    MediaStore.Audio.AudioColumns.ALBUM_ID,
                                    MediaStore.Audio.AudioColumns.DATA,
                                    MediaStore.Audio.AudioColumns.DISPLAY_NAME,
                                    MediaStore.Audio.AudioColumns.SIZE,
                                    MediaStore.Audio.AudioColumns.DURATION
                            },
                            SELECTION,
                            new String[]{
                                    String.valueOf(DEFAULT_FILTER_MUSIC_SIZE),
                                    String.valueOf(DEFAULT_FILTER_MUSIC_TIME)
                            },
                            MediaStore.Audio.Media.DEFAULT_SORT_ORDER
                    );

                    while (cursor != null && cursor.moveToNext()){
                        //如果有值，则遍历每一行数据
                        //ID
                        long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
                        //歌曲名称
                        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE));
                        //歌手
                        String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));
                        //专辑
                        String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM));
                        //专辑ID
                        long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID));
                        //播放时长
                        long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION));
                        //文件路径
                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));

                        //String fileName = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DISPLAY_NAME)));
                        //long fileSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));

                        Song song = new Song();
                        song.setId(String.valueOf(id));
                        song.setTitle(title);
                        song.setArtist_name(artist);
                        song.setAlbum_id(String.valueOf(albumId));
                        song.setAlbum_title(album);
                        song.setAlbum_banner(MusicUtil.getAlbumBanners(getActivity(), String.valueOf(albumId)));
                        song.setBanner(MusicUtil.getAlbumBanners(getActivity(), String.valueOf(albumId)));
                        song.setDuration(duration);
                        song.setUri(path);
                        song.setSource(Song.SOURCE_LOCAL);

                        songList.add(song);

                        //保存到数据库
                        ormUtil.saveSong(song, sp.getUserId());

                        //发布扫描进度
                        publishProgress(path);
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }finally {

                }
                return songList;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                //设置搜索进度
                tv_scan_local_music_progress.setText(values[0]);
            }

            @Override
            protected void onPostExecute(List<Song> songs) {
                super.onPostExecute(songs);
                isScanComplete = true;
                stopScan();
                tv_scan_local_music_progress.setText(getResources().getString(R.string.find_music_count, songs.size()));
                bt_scan_local_music.setBackgroundResource(R.drawable.selector_button_reverse);
                bt_scan_local_music.setTextColor(getResources().getColorStateList(R.color.selector_text_reverse));
                bt_scan_local_music.setText(R.string.go_my_music);
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        stopScan();
        super.onBackPressed();
    }
}


































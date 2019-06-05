package com.luckyliuqs.mymusic.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.adapter.BaseRecyclerViewAdapter;
import com.luckyliuqs.mymusic.adapter.SongAdapter;
import com.luckyliuqs.mymusic.domain.Song;
import com.luckyliuqs.mymusic.domain.event.ScanMusicCompleteEvent;
import com.luckyliuqs.mymusic.fragment.MusicSortDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 本地音乐Activity
 */
public class LocalMusicActivity extends BaseMusicPlayerActivity {
    private static final String TAG = "TAG";

    private LRecyclerView lRecyclerView;
    private View ll_play_all_container;

    /**
     * 本地音乐数量
     */
    private TextView tv_local_music_count;

    /**
     * 本地音乐List集合
     */
    private List<Song> songList;

    private SongAdapter songAdapter;
    private LRecyclerViewAdapter lRecyclerViewAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);
    }

    @Override
    protected void initViews() {
        super.initViews();
        enableBackMenu();

        lRecyclerView = findViewById(R.id.lrv_local_music);
        lRecyclerView.setHasFixedSize(true);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        lRecyclerView.setLayoutManager(linearLayoutManager);

        //分割线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL);
        lRecyclerView.addItemDecoration(dividerItemDecoration);

    }

    @Override
    protected void initDatas() {
        super.initDatas();
        EventBus.getDefault().register(this);

        songAdapter = new SongAdapter(getActivity(), R.layout.item_song_detail, getSupportFragmentManager(), playListManager);
        songAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerViewAdapter.ViewHolder holder, int position) {
                //播放本地音乐中指定位置position的歌曲
                play(position);
            }
        });

        lRecyclerViewAdapter = new LRecyclerViewAdapter(songAdapter);
        lRecyclerViewAdapter.addHeaderView(createHeaderView());

        lRecyclerView.setAdapter(lRecyclerViewAdapter);

        fetchData();
    }

    /**
     * 播放本地音乐中指定位置position的歌曲
     * @param position
     */
    private void play(int position){
        if (songAdapter.getDatas().size() > 0){
            Song song = songAdapter.getData(position);
            //设置歌曲播放列表
            playListManager.setPlayList(songAdapter.getDatas());
            //播放指定歌曲
            playListManager.play(song);
            songAdapter.notifyDataSetChanged();
            //跳转到音乐播放页面
            startActivity(MusicPlayerActivity.class);
        }
    }

    /**
     * create header view
     * @return
     */
    private View createHeaderView(){
        View top = getLayoutInflater().inflate(R.layout.header_local_music, (ViewGroup) lRecyclerView.getParent(), false);
        ll_play_all_container = top.findViewById(R.id.ll_local_music_play_all_container);
        tv_local_music_count = top.findViewById(R.id.tv_local_music_count);

        return top;
    }

    private void fetchData(){
        //获取到本地数据库保存的本地歌曲列表数据
        List<Song> songs = getData();

        if (songs != null && songs.size() > 0){
            //如果本地歌曲列表数据不为空，则设置本地歌曲列表数据
            setData(songs);
        }else{
            //如果本地歌曲列表为空，则跳转到扫描本地歌曲页面
            toScanLocalMusic();
        }
    }

    /**
     * @return 返回本地数据库保存的本地歌曲列表数据
     */
    private List<Song> getData(){
        //根据排序规则查找
        return ormUtil.queryLocalMusic(sp.getUserId(), Song.SORT_KEYS[sp.getLocalMusciSortKey()]);
    }

    /**
     * 设置本地歌曲列表数据
     * @param songList
     */
    private void setData(List<Song> songList){
        this.songList = songList;
        songAdapter.setData(songList);

        //显示本地歌曲数量
        tv_local_music_count.setText(getResources().getString(R.string.music_count, songList.size()));
    }

    /**
     * 转到扫描本地歌曲页面
     */
    private void toScanLocalMusic(){
        startActivity(ScanLocalMusicActivity.class);
    }

    @Override
    protected void initListener() {
        super.initListener();
        ll_play_all_container.setOnClickListener(this);
    }

    /**
     * 扫描本地音乐完成Event
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void scanMusicCompleteEvent(ScanMusicCompleteEvent event){
        //得到本地数据库保存的本地歌曲列表数据
        List<Song> songs = getData();
        if (songs != null && songs.size() > 0){
            //设置本地歌曲列表数据
            setData(songList);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_local_music_play_all_container:   //从本地歌曲列表中的第一首歌曲开始播放
                play(0);
                break;
            default:
                super.onClick(view);   //调用super的onCLick方法
                break;
        }
    }

    /**
     * 创建弹出option menu菜单
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.local_music, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_scan_local_music:   //转到搜索本地歌曲页面
                toScanLocalMusic();
                break;
            case R.id.action_select_music_sort:        //显示歌曲排序方式dialog对话框
                showMusicSortDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 显示本地歌曲排序方式dialog对话框
     */
    private void showMusicSortDialog(){
        MusicSortDialogFragment musicSortDialogFragment = new MusicSortDialogFragment();
        musicSortDialogFragment.show(getSupportFragmentManager(), sp.getLocalMusciSortKey(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                    dialogInterface.dismiss();
                    //保存歌曲排序方式到本地数据库
                    sp.setKeyLocalMusicSortKey(which);
                    fetchData();
            }
        });
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
















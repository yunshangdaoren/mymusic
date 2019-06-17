package com.luckyliuqs.mymusic.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.ToastUtil;
import com.luckyliuqs.mymusic.activity.DownloadManagerActivity;
import com.luckyliuqs.mymusic.activity.LocalMusicActivity;
import com.luckyliuqs.mymusic.activity.RecentPlayActivity;
import com.luckyliuqs.mymusic.activity.SongListDetailActivity;
import com.luckyliuqs.mymusic.adapter.DownloadManagerAdapter;
import com.luckyliuqs.mymusic.adapter.MeFragmentAdapter;
import com.luckyliuqs.mymusic.api.Api;
import com.luckyliuqs.mymusic.domain.MeFragmentUI;
import com.luckyliuqs.mymusic.domain.SongList;
import com.luckyliuqs.mymusic.domain.event.SongListCollectionStatusChangedEvent;
import com.luckyliuqs.mymusic.domain.response.DetailResponse;
import com.luckyliuqs.mymusic.domain.response.ListResponse;
import com.luckyliuqs.mymusic.reactivex.HttpListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 我的音乐主页
 */
public class MeFragment extends BaseCommonFragment implements MeFragmentAdapter.OnMeFragmentListener, View.OnClickListener, ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener, SongListGroupMoreDialogFragment.OnSongListGroupMoreListener{
    /**
     * 二级列表
     */
   private ExpandableListView elv;

    /**
     * 本地音乐
     */
    private LinearLayout ll_local_music;

    /**
     * 最近播放
     */
    private LinearLayout ll_me_recent_play;

    /**
     * 下载管理
     */
    private LinearLayout ll_me_download_manager;

    /**
     * 音乐数量
     */
    private TextView tv_music_count;

    /**
     * 下载音乐数量
     */
    private TextView tv_download_count;

    private MeFragmentAdapter meFragmentAdapter;

    public static MeFragment newInstance() {
        
        Bundle args = new Bundle();

        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me,null);
    }

    @Override
    protected void initViews() {
        super.initViews();
        elv = findViewById(R.id.elv_me);

        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.header_fragment_me, elv, false);
        elv.addHeaderView(headerView);

        ll_local_music = findViewById(R.id.ll_local_music);
        ll_me_recent_play = findViewById(R.id.ll_me_recent_play);
        ll_me_download_manager = findViewById(R.id.ll_me_download_manager);
        tv_music_count = findViewById(R.id.tv_music_count);
        tv_download_count = findViewById(R.id.tv_download);


    }
    @Override
    protected void initDatas() {
        super.initDatas();
        //注册EventBus
        EventBus.getDefault().register(this);

        //设置本地音乐数量
        tv_music_count.setText(getResources().getString(R.string.music_count1,orm.countOfLocalMusic(sp.getUserId()) ));

        meFragmentAdapter = new MeFragmentAdapter(getActivity());
        meFragmentAdapter.setOnMeFragmentListener(this);
        elv.setAdapter(meFragmentAdapter);

        fetchData();


    }

    private void fetchData(){
        final ArrayList<MeFragmentUI> meFragmentUIList = new ArrayList<>();

        final Observable<ListResponse<SongList>> songListsMyCreate = Api.getInstance().songListsMyCreate();
        final Observable<ListResponse<SongList>> songListsMyCollection =  Api.getInstance().songListsMyCollection();

        //用户创建的歌单
        songListsMyCreate.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<ListResponse<SongList>>(getMainActivity()) {
                    @Override
                    public void onSucceeded(ListResponse<SongList> data) {
                        super.onSucceeded(data);
                        meFragmentUIList.add(new MeFragmentUI("我创建的歌单", data.getData()));

                        //用户收藏的歌单
                        songListsMyCollection.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new HttpListener<ListResponse<SongList>>(getMainActivity()) {
                                    @Override
                                    public void onSucceeded(final ListResponse<SongList> data) {
                                        super.onSucceeded(data);

                                        meFragmentUIList.add(new MeFragmentUI("我收藏的歌单",data.getData()));
                                        meFragmentAdapter.setData(meFragmentUIList);

                                        //默认展开ExpandableListView每项的内容
                                        int groupCount = elv.getCount();
//                                        //这里groupCount要-1，因为添加了headerView
//                                        for(int i = 0; i < groupCount - 1; i++){
//                                            elv.expandGroup(i);
//                                        }
                                    }
                                });
                    }
                });

    }

    @Override
    public void initListener() {
        super.initListener();
        //本地音乐点击事件
        ll_local_music.setOnClickListener(this);

        //最近播放点击事件
        ll_me_recent_play.setOnClickListener(this);

        //下载管理点击事件
        ll_me_download_manager.setOnClickListener(this);

        elv.setOnGroupClickListener(this);

        //歌单列表下,子歌单点击事件
        elv.setOnChildClickListener(this);
    }

    /**
     * 歌单列表改变事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void songListCollectionChangedEvent(SongListCollectionStatusChangedEvent event){
        fetchData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_local_music:    //本地音乐
                startActivity(LocalMusicActivity.class);
                break;
            case R.id.ll_me_recent_play:    //最近播放
                startActivity(RecentPlayActivity.class);
                break;
            case R.id.ll_me_download_manager:    //下载管理
                startActivity(DownloadManagerActivity.class);
                break;

        }

    }

    @Override
    public void onSongListGroupSettingsClick() {
        SongListGroupMoreDialogFragment.show(getChildFragmentManager(), this);
    }

    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
        //子歌单
        SongList songList = meFragmentAdapter.getChildData(groupPosition, childPosition);
        startActivityExtraId(SongListDetailActivity.class, songList.getId());
        return true;
    }

    /**
     * 创建歌单
     */
    @Override
    public void onCreateSongList() {
        CreateSongListDialogFragment.show(getChildFragmentManager(), new CreateSongListDialogFragment.OnConfirmCreateSongListListener() {
            @Override
            public void onConfirmCreateSongListClick(String text) {
                createDialog(text);
            }
        });
    }

    private void createDialog(String text){
        SongList songList = new SongList();

        //这里不要用户id，不然这就是一个漏洞，就可以给任何人创建歌单
        //而是服务端根据token获取用户信息
        songList.setTitle(text);

        Api.getInstance().createSongList(songList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<DetailResponse<SongList>>(getMainActivity()) {
                    @Override
                    public void onSucceeded(DetailResponse<SongList> data) {
                        super.onSucceeded(data);
                        ToastUtil.showSortToast(getActivity(),getString(R.string.list_create_susscess));
                        fetchData();
                    }
                });

    }

    /**
     * 管理歌单
     */
    @Override
    public void onManagerSongList() {

    }

    @Override
    public void onDestroy() {
        //取消注册EventBus
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


}
























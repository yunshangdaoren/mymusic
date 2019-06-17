package com.luckyliuqs.mymusic.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.Consts;
import com.luckyliuqs.mymusic.Util.DataUtil;
import com.luckyliuqs.mymusic.Util.ImageUtil;
import com.luckyliuqs.mymusic.Util.StorageUtil;
import com.luckyliuqs.mymusic.Util.ToastUtil;
import com.luckyliuqs.mymusic.adapter.BaseRecyclerViewAdapter;
import com.luckyliuqs.mymusic.adapter.SongAdapter;
import com.luckyliuqs.mymusic.api.Api;
import com.luckyliuqs.mymusic.domain.Song;
import com.luckyliuqs.mymusic.domain.SongList;
import com.luckyliuqs.mymusic.domain.event.SongListCollectionStatusChangedEvent;
import com.luckyliuqs.mymusic.domain.response.DetailResponse;
import com.luckyliuqs.mymusic.domain.response.ListResponse;
import com.luckyliuqs.mymusic.fragment.SelectSongListDialogFragment;
import com.luckyliuqs.mymusic.manager.MusicPlayerManager;
import com.luckyliuqs.mymusic.manager.PlayListManager;
import com.luckyliuqs.mymusic.reactivex.HttpListener;
import com.luckyliuqs.mymusic.service.MusicPlayerService;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import cn.woblog.android.downloader.DownloadService;
import cn.woblog.android.downloader.callback.DownloadManager;
import cn.woblog.android.downloader.domain.DownloadInfo;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 歌单详情主页Activity
 */
public class SongListDetailActivity extends BaseMusicPlayerActivity implements View.OnClickListener, SongAdapter.OnSongListener {
    private LRecyclerView rv;

    /**
     * 歌单封面
     */
    private ImageView iv_song_list_icon;

    /**
     * 歌单名称
     */
    private TextView tv_song_list_title;

    /**
     * 歌单创建者
     */
    private TextView tv_song_list_nickname;

    /**
     * 歌单评论数量
     */
    private TextView tv_song_list_comment_count;
    private LinearLayout header_song_list_container;
    /**
     * 歌单评论模块
     */
    private LinearLayout ll_song_list_comment_container;

    /**
     * 歌单分享模块
     */
    private LinearLayout ll_song_list_share_container;

    /**
     * 歌单下载模块
     */
    private LinearLayout ll_song_list_download_container;

    /**
     * 歌单多选模块
     */
    private LinearLayout ll_song_list_multiple_selection_container;

    /**
     * 歌单内歌曲全部播放
     */
    private LinearLayout ll_song_list_play_all_container;

    private RelativeLayout rl_player_container;

    private TextView tv_play_all;

    /**
     * 歌单内歌曲数量
     */
    private TextView tv_song_list_count;

    /**
     * 收藏歌单全部歌曲按钮
     */
    private Button bt_song_list_collection;

    /**
     * 歌单ID
     */
    private String songListId;

    /**
     * 歌单
     */
    private SongList data;

    private LRecyclerViewAdapter adapterWrapper;
    private SongAdapter songAdapter;

    /**
     * 下载manager
     */
    private DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list_detail);
    }

    @Override
    protected void initViews() {
        super.initViews();
        enableBackMenu();

        rv = findViewById(R.id.rv_song_list);
        rv.setHasFixedSize(true);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void initDatas() {
        super.initDatas();
        //获取传入的歌单ID
        songListId = getIntent().getStringExtra(Consts.ID);
        downloadManager = DownloadService.getDownloadManager(getApplicationContext());

        songAdapter = new SongAdapter(getActivity(), R.layout.item_song_detail, getSupportFragmentManager(), playListManager, downloadManager);
        //歌单内歌曲点击事件
        songAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerViewAdapter.ViewHolder holder, int position) {
                //点击歌单内某个歌曲后开始播放该歌曲
                play(position);
            }
        });

        songAdapter.setOnSongListener(this);

        adapterWrapper = new LRecyclerViewAdapter(songAdapter);
        adapterWrapper.addHeaderView(createHeaderView());

        rv.setAdapter(adapterWrapper);
        rv.setPullRefreshEnabled(false);
        rv.setLoadMoreEnabled(false);

        fetchData();
    }

    /**
     * create header view
     * @return View
     */
    private View createHeaderView(){
        View top = getLayoutInflater().inflate(R.layout.header_song_list_detail, (ViewGroup) rv.getParent(), false);
        header_song_list_container = top.findViewById(R.id.header_song_list_container);
        ll_song_list_comment_container =  top.findViewById(R.id.ll_song_list_comment_container);
        ll_song_list_share_container =  top.findViewById(R.id.ll_song_list_share_container);
        ll_song_list_download_container =  top.findViewById(R.id.ll_song_list_download_container);
        ll_song_list_multiple_selection_container =  top.findViewById(R.id.ll_song_list_multiple_selection_container);
        ll_song_list_play_all_container =  top.findViewById(R.id.ll_song_list_play_all_container);
        bt_song_list_collection =  top.findViewById(R.id.bt_song_list_collection);

        iv_song_list_icon =  top.findViewById(R.id.iv_song_list_icon);
        tv_song_list_title =  top.findViewById(R.id.tv_song_list_title);
        tv_song_list_nickname =  top.findViewById(R.id.tv_song_list_nickname);
        tv_song_list_count =  top.findViewById(R.id.tv_song_list_count);
        tv_song_list_comment_count =  top.findViewById(R.id.tv_song_list_comment_count);

        return top;
    }

    private void fetchData() {
        Api.getInstance().songListDetail(songListId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<DetailResponse<SongList>>(getActivity()) {
                    @Override
                    public void onSucceeded(DetailResponse<SongList> response) {
                        super.onSucceeded(response);
                        next(response.getData());
                    }
                });
    }

    private void next(SongList data){
        this.data = data;
        RequestBuilder<Bitmap> bitmapRequestBuilder = null;
        if (StringUtils.isBlank(data.getBanner())){
            //如果歌单图片为null，则使用默认图片
            bitmapRequestBuilder = Glide.with(this).asBitmap().load(R.drawable.cd_bg);
        }else{
            //歌单图片不为null，则使用歌单图片
            bitmapRequestBuilder = Glide.with(this).asBitmap().load(ImageUtil.getImageURI(data.getBanner()));
        }

        bitmapRequestBuilder.into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull final Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                //用Palette从图片中获取颜色
                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(@Nullable Palette palette) {
                        iv_song_list_icon.setImageBitmap(resource);
                        Palette.Swatch swatch = palette.getVibrantSwatch();
                        if (swatch != null){
                            //获取到主色调
                            int rgb = swatch.getRgb();
                            //为toolbar设置背景颜色
                            toolbar.setBackgroundColor(rgb);
                            //为歌单详情页头部页面设置背景颜色
                            header_song_list_container.setBackgroundColor(rgb);

                            //设置状态栏颜色
                            if (Build.VERSION.SDK_INT >= 21){
                                Window window = getWindow();
                                window.setStatusBarColor(rgb);
                                window.setNavigationBarColor(rgb);
                            }

                        }
                    }
                });

            }
        });

        //设置歌单名称
        tv_song_list_title.setText(data.getTitle());
        //设置歌单创建者
        tv_song_list_nickname.setText(data.getUser().getNickname());
        //设置歌单内歌曲数量
        tv_song_list_count.setText(getResources().getString(R.string.music_count, data.getSongs().size()));
        //设置歌单评论数量
        //tv_song_list_comment_count.setText(String.valueOf(data.getComments_count()));

        if (data.isCollection()){
            //如果歌单已经被收藏了，变成取消收藏按钮
            bt_song_list_collection.setText(R.string.cancel_collection_all);
            //弱化取消收藏按钮
            bt_song_list_collection.setBackground(null);
            bt_song_list_collection.setTextColor(getResources().getColor(R.color.text_grey));
        }else{
            bt_song_list_collection.setText(R.string.collection_all);
            bt_song_list_collection.setBackgroundResource(R.drawable.selector_button_reverse);
            bt_song_list_collection.setTextColor(getResources().getColorStateList(R.color.selector_text_reverse));
        }

        ArrayList<Song> songs = new ArrayList<>();
        songs.addAll(DataUtil.fill(data.getSongs()));

        boolean isMySheet = data.getUser().getId().equals(sp.getUserId());
        songAdapter.setIsMySheet(isMySheet);
        //设置歌曲data
        songAdapter.setData(songs);

        if (isMySheet){
            //如果是用户自己创建的歌单，则隐藏收藏按钮
            bt_song_list_collection.setVisibility(View.GONE);
        }

    }

    /**
     * 播放歌曲
     * @param position
     */
    private void play(int position){
        Song song = songAdapter.getData(position);
        //设置歌曲播放列表信息
        playListManager.setPlayList(songAdapter.getDatas());
        playListManager.play(song);
        //通知事件
        songAdapter.notifyDataSetChanged();
        //跳转至歌曲播放页面
        startActivity(MusicPlayerActivity.class);
    }

    @Override
    protected void initListener() {
        super.initListener();
        //播放全部
        ll_song_list_play_all_container.setOnClickListener(this);
        //评论
        ll_song_list_comment_container.setOnClickListener(this);
        //收藏
        bt_song_list_collection.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_song_list_play_all_container:   //播放歌单内全部歌曲
                play(0);
                break;
            case R.id.ll_song_list_comment_container:    //歌单的评论列表
                Intent intent = new Intent(this, CommentListActivity.class);
                //设置传入歌单id参数
                intent.putExtra(Consts.SONG_LIST_ID, songListId);
                //设置传入参数类型：歌单
                intent.putExtra(Consts.STYLE, Consts.STYLE_SONG_LIST);
                startActivity(intent);
                break;
            case R.id.bt_song_list_collection:           //收藏歌单
                collectionSongList();
                break;
            default:
                //调用父类方法
                super.onClick(v);
                break;
        }
    }

    /**
     * 收藏歌单
     */
    private void collectionSongList(){
        if (data.isCollection()){
            //如果该歌单已经被收藏了，则调用取消收藏该歌单接口
            Api.getInstance().cancelCollectionSongList(data.getCollection_id())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new HttpListener<DetailResponse<SongList>>(getActivity()) {
                        @Override
                        public void onSucceeded(DetailResponse<SongList> data) {
                            super.onSucceeded(data);
                            ToastUtil.showSortToast(getActivity(), getString(R.string.cancel_song_list_collection_success));
                            fetchData();
                            //发布歌单收藏状态信息
                            publishSongListCollectionStatusChanged();
                        }
                    });

        }else{
            //如果该歌单没有被收藏，则传入歌单id，调用收藏该歌单接口
            Api.getInstance().collectionSongList(songListId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new HttpListener<DetailResponse<SongList>>(getActivity()) {
                        @Override
                        public void onSucceeded(DetailResponse<SongList> data) {
                            super.onSucceeded(data);
                            ToastUtil.showSortToast(getActivity(), getString(R.string.song_list_collection_success));
                            fetchData();
                            //发布歌单收藏状态信息
                            publishSongListCollectionStatusChanged();
                        }
                    });

        }
    }

    /**
     * 发布歌单收藏状态信息
     */
    private void publishSongListCollectionStatusChanged(){
        EventBus.getDefault().post(new SongListCollectionStatusChangedEvent());
    }

    //SongAdapter.onSongListener
    /**
     * 收藏指定歌曲到指定歌单中
     * @param song
     */
    @Override
    public void onCollectionClick(final Song song) {
        //获取用户自己创建的歌单，然后显示选择歌单的Fragment，选择收藏完成后调用接口完成收藏
        Observable<ListResponse<SongList>> listResponseObservable = Api.getInstance().songListsMyCreate();
        listResponseObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<ListResponse<SongList>>((BaseActivity) getActivity()) {
                    @Override
                    public void onSucceeded(final ListResponse<SongList> data) {
                        super.onSucceeded(data);
                        SelectSongListDialogFragment.show(getSupportFragmentManager(), data.getData(), new SelectSongListDialogFragment.OnSelectSongListListener() {
                            @Override
                            public void onSelectSongListClick(SongList songList) {
                                //收藏指定歌曲大指定歌单中
                                collectionSong(song, songList);
                            }
                        });
                    }
                });
    }

    /**
     * 收藏指定歌曲到指定歌单中
     * @param song
     * @param songList
     */
    private void collectionSong(Song song, SongList songList){
        Api.getInstance().addSongInSongList(song.getId(), songList.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<DetailResponse<SongList>>(getActivity()) {
                    @Override
                    public void onSucceeded(DetailResponse<SongList> data) {
                        super.onSucceeded(data);
                        //收藏成功提示
                        ToastUtil.showSortToast(getActivity(),getString(R.string.song_like_success));
                    }
                });
    }

    /**
     * 下载歌曲
     * @param song
     */
    @Override
    public void onDownloadClick(Song song) {
        //将下载歌曲保存到手机外部储存：MyMusic/Music目录中
        DownloadInfo downloadInfo = downloadManager.getDownloadById(song.getId());
        if (downloadInfo == null){
            //如果该歌曲还没添加到下载任务中
            downloadInfo = new DownloadInfo.Builder().setUrl(ImageUtil.getImageURI(song.getUri()))
                                    //设置下载歌曲储存目录地址
                                    .setPath(StorageUtil.getExternalPath(song.getTitle(), StorageUtil.MP3))
                                    .build();
            downloadInfo.setId(song.getId());

            //开始下载，这里不需要进度，所以不设置回调
            downloadManager.download(downloadInfo);

            //保存业务数据
            //将该歌曲的来源设置位下载的音乐
            song.setSource(Song.SOURCE_DOWNLOAD);
            ormUtil.saveSong(song, sp.getUserId());

            //显示提示：下载任务添加成功
            ToastUtil.showSortToast(getActivity(), getString(R.string.download_add_complete));
        }else{
            //如果该歌曲已经添加到下载任务中
            if (downloadInfo.getStatus() == DownloadInfo.STATUS_COMPLETED){
                //如果该歌曲已经下载完成了，则显示提示：该歌曲已经下载了!
                ToastUtil.showSortToast(getActivity(), getString(R.string.already_downloaded));
            }else{
                //如果该歌曲正在下载中，则显示提示：已经在下载列表中!
                ToastUtil.showSortToast(getActivity(), getString(R.string.already_downloading));
            }
        }
    }

    /**
     * 从歌单中删除歌曲
     * @param song
     */
    @Override
    public void onDeleteClick(Song song) {
        Api.getInstance().deleteSongInSongList(song.getId(), songListId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<DetailResponse<SongList>>(getActivity()) {
                    @Override
                    public void onSucceeded(DetailResponse<SongList> data) {
                        super.onSucceeded(data);
                        //删除成功提示
                        ToastUtil.showSortToast(getActivity(),getString(R.string.delete_success));
                    }
                });

    }
    //SongAdapter.onSongListener

}

package com.luckyliuqs.mymusic.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.DataUtil;
import com.luckyliuqs.mymusic.Util.ImageUtil;
import com.luckyliuqs.mymusic.activity.BaseWebViewActivity;
import com.luckyliuqs.mymusic.activity.SongListDetailActivity;
import com.luckyliuqs.mymusic.activity.MusicPlayerActivity;
import com.luckyliuqs.mymusic.adapter.BaseRecyclerViewAdapter;
import com.luckyliuqs.mymusic.adapter.RecommendAdapter;
import com.luckyliuqs.mymusic.api.Api;
import com.luckyliuqs.mymusic.domain.Advertisement;
import com.luckyliuqs.mymusic.domain.Song;
import com.luckyliuqs.mymusic.domain.SongList;
import com.luckyliuqs.mymusic.domain.response.ListResponse;
import com.luckyliuqs.mymusic.manager.PlayListManager;
import com.luckyliuqs.mymusic.reactivex.HttpListener;
import com.luckyliuqs.mymusic.service.MusicPlayerService;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 我的推荐主页：推荐Fragment页面
 */
public class RecommendFragment extends BaseCommonFragment implements OnBannerListener {
    private LRecyclerView rv;
    private RecommendAdapter adapter;
    private LRecyclerViewAdapter adapterWrapper;
    private GridLayoutManager layoutManager;
    //广告轮播图
    private Banner banner;
    //每日推荐LinearLayout
    private LinearLayout ll_day_container;
    //每日推荐动态日期描述
    TextView tv_day;
    //广告轮播图data
    private ArrayList<Advertisement> bannerData;
    /**
     * 歌曲播放列表管理器
     */
    private PlayListManager playListManager;

    public static RecommendFragment newInstance() {
        
        Bundle args = new Bundle();

        RecommendFragment fragment = new RecommendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViews() {
        super.initViews();
        rv = findViewById(R.id.rv);

        //设置LayoutManager为网格布局GridLayout，以3列的形式
        layoutManager = new GridLayoutManager(getActivity(), 3);
        //为LRecyclerViewAdapter设置LayoutManager
        rv.setLayoutManager(layoutManager);
    }

    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        playListManager = MusicPlayerService.getPlayListManager(getActivity().getApplicationContext());

        adapter = new RecommendAdapter(getActivity());
        //子item项点击事件
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerViewAdapter.ViewHolder holder, int position) {
                //获取点击事件数据源
                Object data = adapter.getData(position);
                if (data instanceof Song){
                    //单曲点击事件：将该单曲添加到播放列表中并跳转音乐播放页面
                    List<Song> list = new ArrayList<>();
                    list.add((Song) data);
                    playListManager.setPlayList(list);
                    //播放该歌曲
                    playListManager.play((Song) data);
                    startActivity(MusicPlayerActivity.class);
                }else if(data instanceof SongList){
                    //歌单点击事件：跳转至点击的歌单详情页面，并传入歌单ID
                    startActivityExtraId(SongListDetailActivity.class, ((SongList) data).getId());
                }else if(data instanceof Advertisement){
                    //广告点击事件：跳转至点击的广告详情页面
                    BaseWebViewActivity.start(getMainActivity(), ((Advertisement)data).getTitle(),((Advertisement)data).getUri() );
                }
            }
        });

        adapterWrapper = new LRecyclerViewAdapter(adapter);
        adapterWrapper.setSpanSizeLookup(new LRecyclerViewAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                //获取ItemType
                int itemViewType = adapterWrapper.getItemViewType(position);
                if(position < adapterWrapper.getHeaderViewsCount() || position > (adapterWrapper.getHeaderViewsCount() + adapter.getItemCount())){
                    //当前位置的item是header，占用列数spanCount一样
                    return ((GridLayoutManager)layoutManager).getSpanCount();
                }
                return adapter.setSpanSizeLookup(position);
            }
        });

        adapterWrapper.addHeaderView(createHeaderView());

        rv.setAdapter(adapterWrapper);
        //关闭上拉刷新
        rv.setPullRefreshEnabled(false);
        //关闭下拉刷新
        rv.setLoadMoreEnabled(false);

        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //添加轮播图Banner数据
        fetchBannerData();

        //填充推荐歌单，推荐单曲，广告数据
        fetchData();
    }

    private View createHeaderView(){
        View top = getLayoutInflater().inflate(R.layout.header_music_recommend, (ViewGroup) rv.getParent(), false);
        banner = top.findViewById(R.id.bannber);
        banner.setOnBannerListener(this);

        //每日推荐
        ll_day_container = top.findViewById(R.id.ll_day_container);
        //每日推荐动态日期描述
        tv_day = top.findViewById(R.id.tv_day);

        //设置动态日期
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        tv_day.setText(String.valueOf(day));

        //3D翻转动画
        //ll_day_container.setOnClickListener(this);
        return top;
    }

    /**
     * 填充轮播图Banner数据
     */
    private void fetchBannerData(){
        Api.getInstance().advertisements().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<ListResponse<Advertisement>>(getMainActivity()){
                    @Override
                    public void onSucceeded(ListResponse<Advertisement> data) {
                        super.onSucceeded(data);
                        showBanner(data.getData());
                    }
                });
    }

    /**
     * 填充图片并展示轮播图Bannber
     * @param data
     */
    public void showBanner(ArrayList<Advertisement> data){
        //设置图片集合
        this.bannerData = data;
        banner.setImages(data);
        banner.start();
    }

    /**
     * 填充推荐歌单，推荐单曲，广告数据
     */
    public void fetchData(){
        //这里获取三种类型的数据，然后全部放入到一个ArrayList中
        Observable<ListResponse<SongList>> songList = Api.getInstance().songList();
        final Observable<ListResponse<Song>> songs = Api.getInstance().songs();
        final Observable<ListResponse<Advertisement>> advertisements = Api.getInstance().advertisements();

        final ArrayList<Object> arrayList = new ArrayList<Object>();
        arrayList.add("推荐歌单");

        songList.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<ListResponse<SongList>>(getMainActivity()) {
                    @Override
                    public void onSucceeded(ListResponse<SongList> data) {
                        super.onSucceeded(data);
                        arrayList.addAll(data.getData());

                        songs.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new HttpListener<ListResponse<Song>>(getMainActivity()) {
                                    @Override
                                    public void onSucceeded(ListResponse<Song> data) {
                                        super.onSucceeded(data);
                                        arrayList.add("推荐单曲");
                                        arrayList.addAll(DataUtil.fill(data.getData()));

                                        advertisements.subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new HttpListener<ListResponse<Advertisement>>(getMainActivity()) {
                                                               @Override
                                                               public void onSucceeded(ListResponse<Advertisement> data) {
                                                                   super.onSucceeded(data);
                                                                   arrayList.addAll(data.getData());
                                                                   adapter.setData(arrayList);

                                                               }
                                                }
                                        );
                                    }
                                }
                        );

                    }
                }
        );

    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recommend,null);
    }

    /**
     * 轮播图点击监听事件
     * @param position
     */
    @Override
    public void OnBannerClick(int position) {
        Advertisement advertisement = bannerData.get(position);
        //调用其静态start方法，跳转页面并加载指定URL
        BaseWebViewActivity.start(getMainActivity(), "活动详情","http://55648370lqs.top");
    }

    /**
     * 图片加载器
     */
    public class GlideImageLoader extends ImageLoader{

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //引入一个Banner控件，要使用全类名
            Advertisement banner = (Advertisement) path;
            ImageUtil.show(getActivity(), imageView, banner.getBanner());
        }
    }
}






















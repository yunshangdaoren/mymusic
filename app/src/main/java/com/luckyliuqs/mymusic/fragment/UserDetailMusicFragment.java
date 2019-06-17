package com.luckyliuqs.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.activity.SongListDetailActivity;
import com.luckyliuqs.mymusic.adapter.MeFragmentAdapter;
import com.luckyliuqs.mymusic.api.Api;
import com.luckyliuqs.mymusic.domain.MeFragmentUI;
import com.luckyliuqs.mymusic.domain.SongList;
import com.luckyliuqs.mymusic.domain.response.ListResponse;
import com.luckyliuqs.mymusic.reactivex.HttpListener;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 用户资料主页：音乐Fragment页面
 */
public class UserDetailMusicFragment extends BaseCommonFragment implements MeFragmentAdapter.OnMeFragmentListener, ExpandableListView.OnChildClickListener {
    private ExpandableListView expandableListView;
    private MeFragmentAdapter meFragmentAdapter;


    public static UserDetailMusicFragment newInstance(){
        Bundle args = new Bundle();
        UserDetailMusicFragment fragment = new UserDetailMusicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViews() {
        super.initViews();
        expandableListView = findViewById(R.id.elv_me);
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, null);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        meFragmentAdapter = new MeFragmentAdapter(getActivity());
        meFragmentAdapter.setOnMeFragmentListener(this);
        expandableListView.setAdapter(meFragmentAdapter);

        fetchData();
    }

    private void fetchData(){
        final ArrayList<MeFragmentUI> meFragmentUIList = new ArrayList<>();

        //获取创建的歌单
        final Observable<ListResponse<SongList>> listResponseObservable = Api.getInstance().songListsMyCreate();
        listResponseObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<ListResponse<SongList>>(getMainActivity()) {
                    @Override
                    public void onSucceeded(final ListResponse<SongList> data) {
                        super.onSucceeded(data);
                        meFragmentUIList.add(new MeFragmentUI("Ta创建的歌单",data.getData()));

                        //获取收藏的歌单
                        Api.getInstance().songListsMyCollection().subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new HttpListener<ListResponse<SongList>>(getMainActivity()) {
                                    @Override
                                    public void onSucceeded(final ListResponse<SongList> data) {
                                        super.onSucceeded(data);
                                        meFragmentUIList.add(new MeFragmentUI("Ta收藏的歌单",data.getData()));
                                        meFragmentAdapter.setData(meFragmentUIList);
                                    }
                                });
                    }
                });

    }

    @Override
    public void initListener() {
        super.initListener();
        expandableListView.setOnChildClickListener(this);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        //获取到点击的歌单
        SongList songList = meFragmentAdapter.getChildData(groupPosition, childPosition);
        //跳转到歌单详情页面，传入该歌单的id
        startActivityExtraId(SongListDetailActivity.class, songList.getId());
        return true;
    }

    @Override
    public void onSongListGroupSettingsClick() {

    }
}

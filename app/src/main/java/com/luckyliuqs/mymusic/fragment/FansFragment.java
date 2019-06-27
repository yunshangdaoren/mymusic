package com.luckyliuqs.mymusic.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.activity.UserDetailActivity;
import com.luckyliuqs.mymusic.adapter.BaseRecyclerViewAdapter;
import com.luckyliuqs.mymusic.adapter.FriendAdapter;
import com.luckyliuqs.mymusic.api.Api;
import com.luckyliuqs.mymusic.domain.User;
import com.luckyliuqs.mymusic.domain.response.ListResponse;
import com.luckyliuqs.mymusic.reactivex.HttpListener;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 粉丝列表Fragment页面
 */
public class FansFragment extends BaseCommonFragment{
    private static final long ANIMATION_DURATIOn = 500;
    RecyclerView recyclerView;
    private FriendAdapter friendAdapter;

    public static FansFragment newInstance(){
        Bundle args = new Bundle();
        FansFragment fansFragment = new FansFragment();
        fansFragment.setArguments(args);
        return fansFragment;
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fans, null);
    }

    @Override
    protected void initViews() {
        super.initViews();
        recyclerView = findViewById(R.id.rv_fans);
        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL);
        recyclerView.addItemDecoration(decoration);

    }

    @Override
    protected void initDatas() {
        super.initDatas();
        friendAdapter = new FriendAdapter(getActivity(), R.layout.item_friend);
        //好友item点击事件
        friendAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerViewAdapter.ViewHolder holder, int position) {
                //获取到点击的好友信息
                User user = friendAdapter.getData(position);
                //跳转到好友详情页面
                startActivityExtraId(UserDetailActivity.class, user.getId());
            }
        });

        recyclerView.setAdapter(friendAdapter);

        //获取和绑定好友消息数据
        fetchData();
    }

    /**
     * 获取和绑定好友消息数据
     */
    private void fetchData(){
        Api.getInstance().myFans(sp.getUserId(), null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<ListResponse<User>>(getMainActivity()) {
                    @Override
                    public void onSucceeded(ListResponse<User> data) {
                        super.onSucceeded(data);
                        next(data.getData());
                    }
                });
    }

    private void next(List<User> data){
        //Log.i("FansFragment", "好友数据"+data.size() + "  "+((User)(data.get(0))).getNickname());
        friendAdapter.setData(data);
    }

    @Override
    public void initListener() {
        super.initListener();
    }


}

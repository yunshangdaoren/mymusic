package com.luckyliuqs.mymusic.fragment;


import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
 * 关注列表Fragment页面
 */
public class FollowFragment extends BaseCommonFragment{
    private static long ANIMATION_DURATION = 500;
    private RecyclerView recyclerView;
    private FriendAdapter friendAdapter;

    public static FollowFragment newInstance(){
        Bundle args = new Bundle();
        FollowFragment followFragment =  new FollowFragment();
        followFragment.setArguments(args);
        return followFragment;
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_follow, null);
    }

    @Override
    protected void initViews() {
        super.initViews();
        recyclerView = findViewById(R.id.rv_follow);
        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected void initDatas() {
        super.initDatas();

        friendAdapter = new FriendAdapter(getActivity(), R.layout.item_friend);
        friendAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerViewAdapter.ViewHolder holder, int position) {
                //获取到点击的用户
                User user = friendAdapter.getData(position);
                //跳转到用户详情页面
                startActivityExtraId(UserDetailActivity.class, user.getId());
            }
        });

        //设置Adapter
        recyclerView.setAdapter(friendAdapter);

        fetchData();
    }

    private void fetchData(){
        Api.getInstance().myFriends(sp.getUserId(), null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<ListResponse<User>>(getMainActivity()) {
                    @Override
                    public void onSucceeded(ListResponse<User> data) {
                        super.onSucceeded(data);
                        //为FriendAdapter设置数据
                        next(data.getData());
                    }
                });
    }

    /**
     * 为FriendAdapter设置数据
     * @param data
     */
    private void next(List<User> data){
        friendAdapter.setData(data);
    }

    @Override
    public void initListener() {
        super.initListener();
    }
}

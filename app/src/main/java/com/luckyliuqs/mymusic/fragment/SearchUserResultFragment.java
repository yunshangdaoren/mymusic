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
import com.luckyliuqs.mymusic.domain.event.OnSearchKeyChangedEvent;
import com.luckyliuqs.mymusic.domain.response.DetailResponse;
import com.luckyliuqs.mymusic.domain.response.ListResponse;
import com.luckyliuqs.mymusic.reactivex.HttpListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 搜索结果Fragment页面-用户
 */
public class SearchUserResultFragment extends BaseCommonFragment {
    private RecyclerView recyclerView;
    private FriendAdapter friendAdapter;

    public static SearchUserResultFragment newInstance() {
        Bundle args = new Bundle();
        SearchUserResultFragment fragment = new SearchUserResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViews() {
        super.initViews();
        EventBus.getDefault().register(this);

        recyclerView = findViewById(R.id.rv_search_result_user);
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
                User user = friendAdapter.getData(position);
                //跳转到用户详情页面
                startActivityExtraId(UserDetailActivity.class, user.getId());
            }
        });

        recyclerView.setAdapter(friendAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchKeyChangedEvent(OnSearchKeyChangedEvent event) {
        fetchData(event.getContent());
    }

    /**
     * 从服务器查询用户，设置到FriendAdapter上
     * @param userName
     */
    private void fetchData(String userName){
        Api.getInstance().userDetailByNickName(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<DetailResponse<User>>(getMainActivity()){
                    @Override
                    public void onSucceeded(DetailResponse<User> data) {
                        super.onSucceeded(data);
                        next(data.getData());
                    }
                });

    }

    /**
     * 为friendAdapter绑定数据
     * @param user
     */
    private void next(User user) {
        List<User> userList = new ArrayList<>();
        userList.add(user);
        friendAdapter.setData(userList);
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_result_user, null);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
package com.luckyliuqs.mymusic.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.adapter.BaseRecyclerViewAdapter;
import com.luckyliuqs.mymusic.adapter.FriendAdapter;
import com.luckyliuqs.mymusic.api.Api;
import com.luckyliuqs.mymusic.domain.User;
import com.luckyliuqs.mymusic.domain.event.FriendSelectedEvent;
import com.luckyliuqs.mymusic.domain.response.ListResponse;
import com.luckyliuqs.mymusic.reactivex.HttpListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 选择好友列表Activity界面
 */
public class SelectFriendActivity extends BaseTitleActivity {
    private RecyclerView recyclerView;
    private FriendAdapter friendAdapter;

    /**
     * 用户昵称
     */
    private String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_friend);
    }

    @Override
    protected void initViews() {
        super.initViews();
        enableBackMenu();

        recyclerView = findViewById(R.id.rv_select_friend);
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
                //获取选择的好友
                User user = friendAdapter.getData(position);
                //发布好友选择事件
                EventBus.getDefault().post(new FriendSelectedEvent(user));
                //关闭选择好友页面
                finish();
            }
        });

        recyclerView.setAdapter(friendAdapter);

        fetchData();
    }

    private void fetchData(){
       Api.getInstance().myFriends(sp.getUserId(), nickname)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<ListResponse<User>>(getActivity()) {
                    @Override
                    public void onSucceeded(ListResponse<User> data) {
                        super.onSucceeded(data);
                        //friendAdapter
                        next(data.getData());
                    }
                });
    }

    /**
     * 为riendAdapter绑定数据
     * @param data
     */
    private void next(List<User> data) {
        friendAdapter.setData(data);
    }

    /**
     * create options menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search_view);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        //可以在这里配置SearchView

        //设置搜索监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onSearchTextChanged(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                onSearchTextChanged(newText);
                return true;
            }
        });

        //是否进入界面就打开搜索栏，false表示默认打开，默认位true
        searchView.setIconified(false);


        searchView.setQueryHint(getString(R.string.enter_nickname));
        return true;
    }

    /**
     * 当用户输入搜索信息时和提交搜索信息时都会调用这个方法，
     * 实时的去服务端查询跟搜索信息相关的好友列表信息，
     * 再刷新好友列表界面
     * @param query
     */
    private void onSearchTextChanged(String query){
        nickname = query;
        fetchData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search_view){
            return true;
        }

        //调用父类的方法。因为父类可以实现通过的按钮事件，比如：返回
        return super.onOptionsItemSelected(item);
    }
}

package com.luckyliuqs.mymusic.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.adapter.BaseRecyclerViewAdapter;
import com.luckyliuqs.mymusic.adapter.TopicAdapter;
import com.luckyliuqs.mymusic.api.Api;
import com.luckyliuqs.mymusic.domain.Topic;
import com.luckyliuqs.mymusic.domain.event.TopicSelectedEvent;
import com.luckyliuqs.mymusic.domain.response.ListResponse;
import com.luckyliuqs.mymusic.reactivex.HttpListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 选择话题列表Activity界面
 */
public class SelectTopicActivity extends BaseTitleActivity {
    private RecyclerView recyclerView;
    private TopicAdapter topicAdapter;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_topic);
    }

    @Override
    protected void initViews() {
        super.initViews();
        enableBackMenu();

        recyclerView = findViewById(R.id.rv_select_topic);
        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL);
        //recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected void initDatas() {
        super.initDatas();

        topicAdapter = new TopicAdapter(getActivity(), R.layout.item_topic);
        topicAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerViewAdapter.ViewHolder holder, int position) {
                //获取到话题
                Topic topic = topicAdapter.getData(position);
                //发布选择话题事件
                EventBus.getDefault().post(new TopicSelectedEvent(topic));
                //关闭当前页面
                finish();
            }
        });

        recyclerView.setAdapter(topicAdapter);

        fetchData();
    }

    private void fetchData(){
        //获取话题列表数据
        Api.getInstance().topics(title)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<ListResponse<Topic>>(getActivity()) {
                    @Override
                    public void onSucceeded(ListResponse<Topic> data) {
                        super.onSucceeded(data);
                        //为TopicAdapter设置数据
                        next(data.getData());
                    }
                });
    }

    /**
     * 为TopicAdapter设置数据
     * @param data
     */
    private void next(List<Topic> data){
        topicAdapter.setData(data);
    }

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

        //是否进入界面就打开搜索栏，false为默认打开，默认为true
        searchView.setIconified(false);

        searchView.setQueryHint(getString(R.string.enter_topic_title));

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 当用户输入搜索信息时和提交搜索信息时都会调用这个方法，
     * 实时的去服务端查询跟搜索信息相关的话题列表信息，
     * 再刷新话题列表界面
     * @param query
     */
    private void onSearchTextChanged(String query) {
        title =query;
        fetchData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search_view){
            return true;
        }

        //调用父类的方法，因为父类可以实现通过的按钮事件，比如：返回
        return super.onOptionsItemSelected(item);
    }
}

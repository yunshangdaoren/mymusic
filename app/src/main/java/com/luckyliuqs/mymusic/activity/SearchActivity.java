package com.luckyliuqs.mymusic.activity;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.KeyboardUtil;
import com.luckyliuqs.mymusic.adapter.ArrayAdapter;
import com.luckyliuqs.mymusic.adapter.BaseRecyclerViewAdapter;
import com.luckyliuqs.mymusic.adapter.SearchHistoryAdapter;
import com.luckyliuqs.mymusic.adapter.SearchResultPagerAdapter;
import com.luckyliuqs.mymusic.api.Api;
import com.luckyliuqs.mymusic.domain.SearchHistory;
import com.luckyliuqs.mymusic.domain.HotSearch;
import com.luckyliuqs.mymusic.domain.event.OnSearchKeyChangedEvent;
import com.luckyliuqs.mymusic.domain.response.ListResponse;
import com.luckyliuqs.mymusic.reactivex.HttpListener;
import com.nex3z.flowlayout.FlowLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 搜索页面Activity
 */
public class SearchActivity extends BaseMusicPlayerActivity {
    private static final String TAG = "TAG";
    private RelativeLayout rl_singer;
    private RecyclerView recyclerView;
    private SearchHistoryAdapter searchHistoryAdapter;
    private LRecyclerViewAdapter lRecyclerViewAdapter;

    /**
     * 流式布局
     */
    private FlowLayout flowLayout;

    private LayoutInflater layoutInflater;
    private SearchResultPagerAdapter searchResultPagerAdapter;
    private ViewPager viewPager;
    private LinearLayout ll_search_result_container;
    private TabLayout tabLayout;
    private SearchView searchView;

    /**
     * 搜索提示
     */
    private SearchView.SearchAutoComplete searchAutoComplete;

    private ArrayAdapter<String> stringArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void initViews() {
        super.initViews();
        enableBackMenu();

        layoutInflater = LayoutInflater.from(getActivity());

        tabLayout = findViewById(R.id.tabs_search);
        viewPager = findViewById(R.id.vp_search);
        ll_search_result_container = findViewById(R.id.ll_search_result_container);
        recyclerView = findViewById(R.id.rv_search);
        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

    }

    @Override
    protected void initDatas() {
        super.initDatas();
        //搜索历史
        searchHistoryAdapter = new SearchHistoryAdapter(getActivity(), R.layout.item_search_history);
        searchHistoryAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerViewAdapter.ViewHolder holder, int position) {
                SearchHistory data = searchHistoryAdapter.getData(position);
                onSearchClick(data.getContent());
            }
        });

        searchHistoryAdapter.setOnSearchHistoryRemoveListener(new SearchHistoryAdapter.OnSearchHistoryDeleteListener() {
            @Override
            public void onDeleteClick(int position, SearchHistory searchHistory) {
                searchHistoryAdapter.removeData(position);
                ormUtil.deleteSearchHistory(searchHistory);
            }
        });

        lRecyclerViewAdapter = new LRecyclerViewAdapter(searchHistoryAdapter);
        lRecyclerViewAdapter.addHeaderView(createHeaderView());

        recyclerView.setAdapter(lRecyclerViewAdapter);

        //显示搜索历史数据
        fetchSearchHistoryData();

        //显示热门搜索数据
        fetchHotSearch();

        searchResultPagerAdapter = new SearchResultPagerAdapter(getActivity(), getSupportFragmentManager());
        viewPager.setAdapter(searchResultPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(0);
        integers.add(1);
        integers.add(2);
        integers.add(3);
        integers.add(4);
        integers.add(5);
        integers.add(6);
        searchResultPagerAdapter.setDatas(integers);

    }

    private void onSearchClick(String content){
        searchView.setQuery(content, true);
        //是否进入界面就打开搜索栏，false为打开，true为关闭
        searchView.setIconified(false);

        KeyboardUtil.hideKeyboard(this);
    }

    private View createHeaderView(){
        View top = getLayoutInflater().inflate(R.layout.header_search, (ViewGroup)recyclerView.getParent(), false);
        rl_singer = top.findViewById(R.id.rl_singer_search);
        flowLayout = top.findViewById(R.id.fl_search);

        return top;
    }

    /**
     * 显示搜索历史数据
     */
    private void fetchSearchHistoryData(){
        List<SearchHistory> searchHistoryList = ormUtil.queryAllSearchHistory();
        searchHistoryAdapter.setData(searchHistoryList);
    }

    /**
     * 显示热门搜索数据
     */
    private void fetchHotSearch(){
        Api.getInstance().searchHot().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<ListResponse<HotSearch>>(getActivity()) {
                    @Override
                    public void onSucceeded(final ListResponse<HotSearch> data) {
                        super.onSucceeded(data);
                        setHotData(data.getData());
                    }
                });
    }

    /**
     * 设置热门搜索数据
     * @param data
     */
    public void setHotData(List<HotSearch> data){
        for (final HotSearch hotSearch : data){
            TextView tv = (TextView) layoutInflater.inflate(R.layout.item_hot_search,flowLayout, false );
            tv.setText(hotSearch.getContent());
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //搜索点击事件
                    onSearchClick(hotSearch.getContent());
                }
            });

            flowLayout.addView(tv);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search_view);
        searchView = (SearchView) searchItem.getActionView();
        //可以在这里配置SearchView

        //设置搜索监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //搜索提交，执行搜索
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //获取搜索建议
                getSuggestionsData(newText);
                return true;
            }
        });

        //关闭监听器
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //显示搜索页面，隐藏搜索结果页面
                Log.i(TAG, "onClose: 显示搜索页面，隐藏搜索结果页面");
                changeSearchViewShow();
                return false;
            }
        });

        //是否进入界面就打开搜索栏，false为打开，true为关闭
        searchView.setIconified(false);

        searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        //设置输入一个字符显示提示（默认要输入两个字符才显示提示）
        searchAutoComplete.setThreshold(1);

        SearchManager searchManager = (SearchManager) getSystemService(this.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        //搜索提示点击事件
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //执行搜索内容
                performSearch(stringArrayAdapter.getItem(position));
            }
        });

        return true;
    }

    /**
     * 执行搜索内容
     * @param data
     */
    private void performSearch(String data){
        //发布搜索key
        EventBus.getDefault().post(new OnSearchKeyChangedEvent(data));

        //保存搜索内容
        SearchHistory searchHistory = new SearchHistory();
        searchHistory.setContent(data);
        searchHistory.setCreated_at(System.currentTimeMillis());
        //保存搜索历史数据
        ormUtil.createOrUpdate(searchHistory);

        fetchSearchHistoryData();

        changeSearchResultViewShow();
    }

    /**
     * 隐藏搜索页面，显示搜索结果页面
     */
    private void changeSearchResultViewShow(){
        ll_search_result_container.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    /**
     * 显示搜索页面，隐藏搜索结果页面
     */
    private void changeSearchViewShow(){
        ll_search_result_container.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    /**
     * 获取搜索建议数据
     * @param newText
     */
    private void getSuggestionsData(String newText){
        if (newText.length() > 0){
            fetchPrompt(newText);
        }
    }

    /**
     * 设置搜索建议数据
     * @param content
     */
    private void fetchPrompt(String content){
        Api.getInstance().prompt(content).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<ListResponse<HotSearch>>(getActivity()) {
                    @Override
                    public void onSucceeded(final ListResponse<HotSearch> data) {
                        super.onSucceeded(data);
                        setPromptData(data.getData());
                    }
                });
    }

    /**
     * 设置搜索建议数据
     * @param data
     */
    private void setPromptData(List<HotSearch> data){
        if (data.size() > 0){
            ArrayList<String> strings = new ArrayList<>();
            for (HotSearch hotSearch : data){
                strings.add(hotSearch.getContent());
            }

            stringArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_suggestion, R.id.tv_suggestion_search, strings);
            searchAutoComplete.setAdapter(stringArrayAdapter);
        }
    }

    private void hideSuggestionWindow(){

    }
}




































package com.luckyliuqs.mymusic.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.EditText;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.Consts;
import com.luckyliuqs.mymusic.Util.KeyboardUtil;
import com.luckyliuqs.mymusic.Util.TagUtil;
import com.luckyliuqs.mymusic.Util.ToastUtil;
import com.luckyliuqs.mymusic.adapter.BaseRecyclerViewAdapter;
import com.luckyliuqs.mymusic.adapter.CommentAdapter;
import com.luckyliuqs.mymusic.api.Api;
import com.luckyliuqs.mymusic.domain.Comment;
import com.luckyliuqs.mymusic.domain.event.FriendSelectedEvent;
import com.luckyliuqs.mymusic.domain.event.TopicSelectedEvent;
import com.luckyliuqs.mymusic.domain.response.DetailResponse;
import com.luckyliuqs.mymusic.domain.response.ListResponse;
import com.luckyliuqs.mymusic.fragment.CommentMoreDialogFragment;
import com.luckyliuqs.mymusic.reactivex.HttpListener;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 评论列表Activity页面
 */
public class CommentListActivity extends BaseTitleActivity implements CommentAdapter.OnCommentAdapter, OnRefreshListener, OnLoadMoreListener, TextWatcher, View.OnClickListener {
    private static final String TAG = "CommentListActivityTo";
    private LRecyclerView lRecyclerView;
    private CommentAdapter commentAdapter;
    private LRecyclerViewAdapter lRecyclerViewAdapter;

    /**
     * 歌单id
     */
    private String songListId;


    private ListResponse.Meta pageMeta;

    /**
     * 发送评论按钮
     */
    private Button bt_comment_list_send;

    /**
     * 要发送的评论内容
     */
    private EditText et_comment_list_content;

    /**
     * 某个评论用户的id
     */
    private String parentId;
    private int touchSlop;
    private int lastContentLength;
    private int style;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
    }

    @Override
    protected void initViews() {
        super.initViews();
        enableBackMenu();

        EventBus.getDefault().register(this);

        bt_comment_list_send = findViewById(R.id.bt_comment_list_send);
        et_comment_list_content = findViewById(R.id.et_comment_list_content);
        lRecyclerView = findViewById(R.id.lrv_comment_list_);
        lRecyclerView.setHasFixedSize(true);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        lRecyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL);
        lRecyclerView.addItemDecoration(dividerItemDecoration);

        ViewConfiguration viewConfiguration = ViewConfiguration.get(getApplicationContext());
        touchSlop = viewConfiguration.getScaledTouchSlop();

    }

    @Override
    protected void initDatas() {
        super.initDatas();
        songListId = getIntent().getStringExtra(Consts.SONG_LIST_ID);
        style = getIntent().getIntExtra(Consts.STYLE, 0);

        commentAdapter = new CommentAdapter(getActivity());
        commentAdapter.setOnCommentAdapter(this);
        commentAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerViewAdapter.ViewHolder holder, int position) {
                Object data = commentAdapter.getData(position);
                if (data instanceof Comment){
                    //点击评论弹出更多选项对话框
                    showCommentMoreDialog((Comment) data);
                }
            }
        });

        lRecyclerViewAdapter = new LRecyclerViewAdapter(commentAdapter);
        lRecyclerView.setAdapter(lRecyclerViewAdapter);

        fetchData();

    }

    /**
     * 点击评论弹出更多选项对话框
     * @param comment
     */
    private void showCommentMoreDialog(final Comment comment){
        final CommentMoreDialogFragment commentMoreDialogFragment = new CommentMoreDialogFragment();
        commentMoreDialogFragment.show(getSupportFragmentManager(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //关闭对话框
                commentMoreDialogFragment.dismiss();
                //处理对话框选项点击事件
                processOnClick(which, comment);
            }
        });
    }

    /**
     * 处理对话框选项点击事件
     * @param which 对话框选项下标
     * @param comment 评论
     */
    private void processOnClick(int which, Comment comment){
        switch (which){
            case 0:
                //回复评论选项
                parentId = comment.getId();
                et_comment_list_content.setHint(getResources().getString(R.string.reply_comment, comment.getUser().getNickname()));
                //将光标移到最后
                //et_comment_list_content.setSelection(et_comment_list_content.getText().toString().length());
                break;
            case 1:
                //分享评论选项
                break;
            case 2:
                //复制评论选项
                break;
            case 3:
                //举报评论选项
                break;
        }
    }

    private void fetchData(){
        //Log.i(TAG, "begin fetchData: ");
        final ArrayList<Object> objects = new ArrayList<>();
        final HashMap<String, String> querys = getQuerys();
        querys.put(Consts.ORDER, String.valueOf(Consts.ORDER_HOT));

        if (StringUtils.isNotBlank(songListId)){
            querys.put(Consts.SONG_LIST_ID, songListId);
        }

        objects.add("精彩评论");

        //加载精彩评论数据
        Api.getInstance().comments(querys)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<ListResponse<Comment>>(getActivity()) {
                    @Override
                    public void onSucceeded(ListResponse<Comment> data) {
                        super.onSucceeded(data);

                        objects.addAll(data.getData());
                        commentAdapter.setData(objects);

                        commentAdapter.addData("最新评论");
                        //加载最新评论数据
                        loadMore();
                    }
                });

    }

    private HashMap<String, String> getQuerys(){
        final HashMap<String, String> querys = new HashMap<>();
        if (StringUtils.isNotBlank(songListId)){
            querys.put(Consts.SONG_LIST_ID, songListId);
        }
        return querys;
    }

    /**
     * 加载最新评论数据
     */
    private void loadMore(){
        final ArrayList<Object> objects = new ArrayList<>();
        final HashMap<String, String> querys = getQuerys();

        if (StringUtils.isNotBlank(songListId)){
            querys.put(Consts.SONG_LIST_ID, songListId);
        }

       querys.put(Consts.PAGE, String.valueOf(ListResponse.Meta.nextPage(pageMeta)));

        Api.getInstance().comments(querys)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<ListResponse<Comment>>(getActivity()) {
                    @Override
                    public void onSucceeded(ListResponse<Comment> data) {
                        super.onSucceeded(data);

                        objects.addAll(data.getData());
                        commentAdapter.addData(objects);

                        pageMeta = data.getMeta();
                        lRecyclerView.refreshComplete(Consts.DEFAULT_PAGE_SIZE);
                    }
                });
    }

    @Override
    protected void initListener() {
        super.initListener();
        //设置下拉刷新监听事件
        lRecyclerView.setOnRefreshListener(this);
        //设置上拉加载更多监听事件
        lRecyclerView.setOnLoadMoreListener(this);
        lRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > 100){
                    //如果Y轴滚动距离大于100
                    if (StringUtils.isEmpty(et_comment_list_content.getText().toString())){
                        //如果评论输入框没有输入内容，则清除回复
                        clearReplayComment();
                    }
                }
            }
        });

        et_comment_list_content.addTextChangedListener(this);
        bt_comment_list_send.setOnClickListener(this);
    }

    /**
     * 清除回复
     */
    private void clearReplayComment(){
        parentId = null;
        et_comment_list_content.setHint(R.string.hint_comment3);
    }

    /**
     * 点赞
     * @param comment
     */
    @Override
    public void onLikeClick(final Comment comment) {
        if (comment.isLiked()){
            //如果该评论已经点赞了，点击，则为取消点赞
            Api.getInstance().unlike(comment.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new HttpListener<DetailResponse<Comment>>(getActivity()) {
                        @Override
                        public void onSucceeded(DetailResponse<Comment> data) {
                            super.onSucceeded(data);
                            //可以调用接口，也可以在本地加减
                            comment.setLike_count(comment.getLike_count() - 1);
                            comment.setLike_id(null);
                            commentAdapter.notifyDataSetChanged();
                        }
                    });
        }else{
            //如果该评论还未点赞，点击，则为点赞
            Api.getInstance().like(comment.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new HttpListener<DetailResponse<Comment>>(getActivity()) {
                        @Override
                        public void onSucceeded(DetailResponse<Comment> data) {
                            super.onSucceeded(data);
                            comment.setLike_count(comment.getLike_count() + 1);
                            comment.setLike_id("1");
                            commentAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    @Override
    public void onRefresh() {
        pageMeta.setCurrent_page(0);
        fetchData();
    }

    @Override
    public void onLoadMore() {
        if (pageMeta == null || pageMeta.getCurrent_page() < pageMeta.getTotal_pages()){
            loadMore();
        }else{
            lRecyclerView.setNoMore(true);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int currentLength = s.length();
        if (currentLength > lastContentLength){
            //新增内容，如果不判断，用户删除到@,#符号就会跳转
            String string = s.toString();
            String lastChar = string.substring(s.length() - 1);
            if (Consts.MENTION.equals(lastChar)){
                Log.i(TAG, "========================afterTextChanged: " + lastChar);
                //输入了@符号，就跳转到选择用户的好友列表界面
                startActivity(SelectFriendActivity.class);
            }else if(Consts.HAST_TAG.equals(lastChar)){
                Log.i(TAG, "=======================afterTextChanged: " + lastChar);
                //输入了#符号，就跳转选择话题列表界面
                startActivity(SelectTopicActivity.class);
            }
        }

        lastContentLength = s.length();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_comment_list_send:
                //发送评论内容
                sendComment();
                break;

        }
    }

    /**
     * 发送评论内容
     */
    private void sendComment(){
        //获取将要发送的评论内容
        String content = et_comment_list_content.getText().toString().trim();
        if (StringUtils.isEmpty(content)){
            ToastUtil.showSortToast(getActivity(), getString(R.string.enter_comment_content));
            return;
        }

        Comment comment = new Comment();
        comment.setParent_id(parentId);
        comment.setContent(content);
        comment.setSheet_id(songListId);
        comment.setStyle(style);

        Api.getInstance().createComment(comment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<DetailResponse<Comment>>(getActivity()) {
                    @Override
                    public void onSucceeded(DetailResponse<Comment> data) {
                        super.onSucceeded(data);
                        ToastUtil.showSortToast(getActivity(), getString(R.string.comment_create_susscess));
                        onRefresh();

                        ////重新设置最热评论，然后在拉去第一页评论
                        ////当然也可以手动将发送的评论插入到adapter中
                        ////拉去第一页
                        //pageMate.setCurrent_page(1);
                        //loadMore();

                        //请空输入框
                        et_comment_list_content.setText("");
                        clearReplyComment();
                        //关闭键盘
                        KeyboardUtil.hideKeyboard(getActivity());
                    }
                });
    }

    /**
     * 清空评论输入框
     */
    private void clearReplyComment(){
        parentId = null;
        et_comment_list_content.setHint(R.string.hint_comment3);
    }

    /**
     * 选择好友页面中选择好友事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void friendSelectedEvent(FriendSelectedEvent event){
        //原来的内容加选择的输入
        //这里的@用户使用的昵称，如果有相同昵称的就会出问题
        //所以要实现好这个功能，最好附加上用户ID，但这会增加更多的代码
        //我们会在相应的课程提供整套解决方案
        //这里只是简单实现

        String source = et_comment_list_content.getText().toString();
        setText(source + event.getUser().getNickname() + " ");

        //将光标移到最后
        et_comment_list_content.setSelection(et_comment_list_content.getText().toString().length());
    }

    /**
     * 选择话题页面中选择话题事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void topicSelectedEvent(TopicSelectedEvent event){
        String source = et_comment_list_content.getText().toString();

        //这里也只是简单实现，没有保存话题Id
        //不过话题这样的，就不应该重复
        //为什么话题最好用#包裹，因为可能存在空格
        setText(source + event.getTopic().getTitle() + "# ");

        //将光标移到最后
        et_comment_list_content.setSelection(et_comment_list_content.getText().toString().length());
    }

    /**
     * 设置评论输入框文本内容
     * @param s
     */
    private void setText(String s){
        //将Tag高亮
        et_comment_list_content.setText(TagUtil.processHightLight(getActivity(), s));
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}


























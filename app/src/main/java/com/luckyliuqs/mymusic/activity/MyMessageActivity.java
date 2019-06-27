package com.luckyliuqs.mymusic.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.adapter.BaseRecyclerViewAdapter;
import com.luckyliuqs.mymusic.adapter.ConversationAdapter;
import com.luckyliuqs.mymusic.domain.event.OnMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 我的消息Activity页面
 */
public class MyMessageActivity extends BaseTitleActivity {
    private RecyclerView recyclerView;
    private ConversationAdapter conversationAdapter;
    private RongIMClient rongIMClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);
    }

    @Override
    protected void initViews() {
        super.initViews();
        enableBackMenu();

        recyclerView = findViewById(R.id.rv_my_message);
        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL);
        recyclerView.addItemDecoration(decoration);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        rongIMClient = RongIMClient.getInstance();

        conversationAdapter = new ConversationAdapter(getActivity(),R.layout.item_conversation);
        //未读消息item点击事件
        conversationAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerViewAdapter.ViewHolder holder, int position) {
                //获取到点击的未读消息item数据
                Conversation data = conversationAdapter.getData(position);

                String targetId = data.getTargetId();
                //跳转到聊天页面
                ConversationActivity.start(getActivity(), targetId);

                //int messageId = data.getLatestMessageId();
                //
                ////获取到消息，因为要获取对方的id
                //imClient.getMessage(messageId, new RongIMClient.ResultCallback<Message>() {
                //    @Override
                //    public void onSuccess(final Message message) {
                //        //由于消息上没有用户信息，只有id，所以还需要从用户管理器中获取用户信息
                //        //当然也可以每条消息上带上用户信息，但这肯定会更耗资源
                //        ConversationActivity.start(getActivity(), MessageUtil.getTargetId(message));
                //    }
                //
                //    @Override
                //    public void onError(RongIMClient.ErrorCode errorCode) {
                //
                //    }
                //});


            }
        });

        recyclerView.setAdapter(conversationAdapter);
        fetchData();
    }

    private void fetchData(){
        //获取用户的所有本地会话列表
        rongIMClient.getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (conversations != null && conversations.size() > 0){
                    //获取到会话数据，为conversationAdapter设置会话数据
                    conversationAdapter.setData(conversations);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        }, Conversation.ConversationType.PRIVATE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnMessageEvent event) {
        fetchData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        fetchData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

}

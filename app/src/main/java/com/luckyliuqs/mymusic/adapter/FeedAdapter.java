package com.luckyliuqs.mymusic.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.Consts;
import com.luckyliuqs.mymusic.Util.ImageUtil;
import com.luckyliuqs.mymusic.Util.TagUtil;
import com.luckyliuqs.mymusic.Util.TimeUtil;
import com.luckyliuqs.mymusic.activity.BaseActivity;
import com.luckyliuqs.mymusic.activity.TopicDetailActivity;
import com.luckyliuqs.mymusic.activity.UserDetailActivity;
import com.luckyliuqs.mymusic.domain.Feed;
import com.luckyliuqs.mymusic.listener.OnTagClickListener;


/**
 * 动态页面Adapter
 */
public class FeedAdapter extends BaseQuickRecyclerViewAdapter<Feed>{

    public FeedAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    protected void bindData(ViewHolder holder, int position, Feed feed) {
        //用户头像
        ImageView iv_avatar = holder.getView(R.id.iv_item_feed_avatar);
        ImageUtil.showCircle((Activity) context, iv_avatar, feed.getUser().getAvatar());

        //动态内容
        TextView tv_content = holder.getView(R.id.tv_item_feed_content);
        //设置后才能点击
        tv_content.setMovementMethod(LinkMovementMethod.getInstance());
        //设置链接颜色
        tv_content.setLinkTextColor(context.getResources().getColor(R.color.text_Highlight));
        tv_content.setText(TagUtil.process(context, feed.getContent(), new OnTagClickListener() {
            @Override
            public void onTagClick(String content) {
                processTagClick(content);
            }
        }));

        //用户昵称
        TextView tv_nickname = holder.getView(R.id.tv_item_feed_nickaname);
        tv_nickname.setText(feed.getUser().getNickname());

        //动态创建时间
        TextView tv_time = holder.getView(R.id.tv_item_feed_time);
        tv_time.setText(TimeUtil.dateTimeFormat(feed.getCreated_at()));

        RecyclerView recyclerView = (RecyclerView) holder.findViewById(R.id.rv_item_feed);
        if (feed.getImages() != null && feed.getImages().size() > 0){
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);

            int spanCount = 2;
            if (feed.getImages().size() > 4){
                //如果大于4张图片，就3列显示
                spanCount = 3;
            }

            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, spanCount);
            recyclerView.setLayoutManager(gridLayoutManager);

            ImageAdapter imageAdapter = new ImageAdapter(context, R.layout.item_image);
            recyclerView.setAdapter(imageAdapter);

            imageAdapter.setData(feed.getImages());
        }else{
            recyclerView.setVisibility(View.GONE);
            recyclerView.setAdapter(null);
        }

    }

    /**
     * Tag点击事件，这部分可以用监听器回调到Activity中在处理
     * @param content
     */
    private void processTagClick(String content){
        if (content.startsWith(Consts.MENTION)){
            //进入用户详情页面
            Intent intent = new Intent(context, UserDetailActivity.class);
            intent.putExtra(Consts.NICKNAME, TagUtil.removeTag(content));
            ((BaseActivity) context).startActivity(intent);
        }else{
            //跳转到话题详情页面
            Intent intent = new Intent(context, TopicDetailActivity.class);
            intent.putExtra(Consts.TITLE, TagUtil.removeTag(content));
            ((BaseActivity) context).startActivity(intent);
        }
    }

}





























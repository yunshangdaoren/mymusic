package com.luckyliuqs.mymusic.adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.ImageUtil;
import com.luckyliuqs.mymusic.domain.Topic;

/**
 * 话题详情页面Adapter
 */
public class TopicAdapter extends BaseQuickRecyclerViewAdapter<Topic> {

    public TopicAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    protected void bindData(ViewHolder holder, int position, Topic topic) {
        ImageUtil.show((Activity) context, (ImageView) holder.getView(R.id.iv_topic_icon), topic.getBanner());
        holder.setText(R.id.tv_topic_title, "#" + topic.getTitle() + "#");
        holder.setText(R.id.tv_topic_info, context.getResources().getString(R.string.join_count, topic.getJoins_count()));

    }
}

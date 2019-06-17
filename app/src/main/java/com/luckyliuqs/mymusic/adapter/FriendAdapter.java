package com.luckyliuqs.mymusic.adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.ImageUtil;
import com.luckyliuqs.mymusic.domain.User;

/**
 * 好友列表页面Adapter
 */
public class FriendAdapter extends BaseQuickRecyclerViewAdapter<User> {


    public FriendAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    protected void bindData(ViewHolder holder, int position, User user) {
        ImageUtil.showCircle((Activity) context, (ImageView) holder.getView(R.id.iv_avatar), user.getAvatar());
        holder.setText(R.id.tv_friend_nickname, user.getNickname());
        holder.setText(R.id.tv_friend_info, user.getDescription());
    }
}

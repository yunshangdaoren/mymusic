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
        //设置用户头像
        ImageUtil.showCircle((Activity) context, (ImageView) holder.getView(R.id.iv_friend_avatar), user.getAvatar());
        //设置用户昵称
        holder.setText(R.id.tv_friend_nickname, user.getNickname());
        //设置用户个性签名
        holder.setText(R.id.tv_friend_info, user.getDescription());
    }
}

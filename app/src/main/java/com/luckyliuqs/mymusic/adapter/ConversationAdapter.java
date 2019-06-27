package com.luckyliuqs.mymusic.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.ImageUtil;
import com.luckyliuqs.mymusic.Util.MessageUtil;
import com.luckyliuqs.mymusic.Util.TimeUtil;
import com.luckyliuqs.mymusic.domain.User;
import com.luckyliuqs.mymusic.manager.UserManager;
import com.luckyliuqs.mymusic.manager.impl.UserManagerImpl;

import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;

/**
 * 我的消息页面消息Adapter
 */
public class ConversationAdapter extends BaseQuickRecyclerViewAdapter<Conversation> {
    private final UserManager userManager;


    public ConversationAdapter(Context context, int layoutId) {
        super(context, layoutId);
        userManager = UserManagerImpl.getInstance(context);
    }

    @Override
    protected void bindData(final ViewHolder holder, int position, final Conversation data) {
        final MessageContent messageContent = data.getLatestMessage();

        String targetId = data.getTargetId();
        //由于消息上没有用户信息，只有id，所以还需要从用户管理器中获取用户信息
        //当然也可以每条消息上带上用户信息，但这肯定会更耗资源
        userManager.getUser(targetId, new UserManager.OnUserListener() {
            @Override
            public void onUser(User user) {
                //获取用户的头像并绑定图片
                ImageView iv_avatar = (ImageView) holder.findViewById(R.id.iv_avatar_conversation);
                ImageUtil.showCircle(context, iv_avatar, user.getAvatar());
                //设置用户名称
                holder.setText(R.id.tv_nickname_conversation,user.getNickname());
                //设置未读消息内容
                holder.setText(R.id.tv_info_conversation, MessageUtil.getContent(messageContent));
                //设置消息时间
                holder.setText(R.id.tv_time_conversation, TimeUtil.toLocalDate(data.getReceivedTime()));

                //获取到未读消息数量
                int unreadMessageCount = data.getUnreadMessageCount();
                if (unreadMessageCount > 0) {
                    if (unreadMessageCount > 99) {
                        //如果未读消息数量大于99条，则显示99+
                        holder.setText(R.id.tv_count_conversation, R.string.message_count_99);
                    } else {
                        //显示未读消息数量
                        holder.setText(R.id.tv_count_conversation, String.valueOf(unreadMessageCount));
                    }
                    //显示未读消息提示
                    holder.setVisibility(R.id.tv_count_conversation, View.VISIBLE);
                } else {
                    //隐藏未读消息提示
                    holder.setVisibility(R.id.tv_count_conversation, View.GONE);
                }

            }
        });

        ////获取到消息，因为要根据消息方向判断显示发送方的昵称，头像
        //AppContext.getImClient().getMessage(messageId, new RongIMClient.ResultCallback<Message>() {
        //    @Override
        //    public void onSuccess(final Message message) {
        //        //由于消息上没有用户信息，只有id，所以还需要从用户管理器中获取用户信息
        //        //当然也可以每条消息上带上用户信息，但这肯定会更耗资源
        //        userManager.getUser(MessageUtil.getTargetId(message), new UserManager.OnUserListener() {
        //            @Override
        //            public void onUser(User user) {
        //                ImageView iv_avatar= (ImageView) holder.findViewById(R.id.iv_avatar);
        //                ImageUtil.showCircle(context, iv_avatar, user.getAvatar());
        //                holder.setText(R.id.tv_nickname,user.getNickname());
        //                holder.setText(R.id.tv_info, MessageUtil.getContent(message));
        //                holder.setText(R.id.tv_time, TimeUtil.toLocalDate(data.getReceivedTime()));
        //                int unreadMessageCount = data.getUnreadMessageCount();
        //                if (unreadMessageCount > 0) {
        //                    if (unreadMessageCount > 99) {
        //                        //显示99+
        //                        holder.setText(R.id.tv_count, R.string.message_count_99);
        //                    } else {
        //                        holder.setText(R.id.tv_count, String.valueOf(unreadMessageCount));
        //                    }
        //
        //                    holder.setVisibility(R.id.tv_count, View.VISIBLE);
        //                } else {
        //                    holder.setVisibility(R.id.tv_count, View.GONE);
        //                }
        //
        //            }
        //        });
        //
        //    }
        //
        //    @Override
        //    public void onError(RongIMClient.ErrorCode errorCode) {
        //
        //    }
        //});


    }
}

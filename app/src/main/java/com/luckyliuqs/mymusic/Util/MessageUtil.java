package com.luckyliuqs.mymusic.Util;

import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;

/**
 * 消息Message工具类
 */
public class MessageUtil {

    /**
     * 获取对方的Id
     * @param message
     * @return
     */
    public static String getTargetId(Message message) {
        if (message.getMessageDirection() == io.rong.imlib.model.Message.MessageDirection.SEND) {
            //发送，显示targetId
            return message.getTargetId();
        } else {
            return message.getSenderUserId();
        }
    }

    /**
     * 将不同的消息转为可用文字描述的消息
     * <p>用在通知栏，会话列表</p>
     *
     * @param messageContent
     * @return
     */
    public static String getContent(MessageContent messageContent) {
        if (messageContent instanceof TextMessage) {
            //如果是文本消息，返回文本内容
            return ((TextMessage) messageContent).getContent();
        } else if (messageContent instanceof ImageMessage) {
            //如果是图片消息，则返回"[图片]"
            return "[图片]";
        }
        return "";
    }


}

package com.luckyliuqs.mymusic.domain.event;

import io.rong.imlib.model.Message;

/**
 * 消息Event
 */
public class OnMessageEvent {
    private Message message;

    public OnMessageEvent(Message message){
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}



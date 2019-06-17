package com.luckyliuqs.mymusic.domain.event;

import com.luckyliuqs.mymusic.domain.Topic;

/**
 * 选择话题页面中选择话题事件
 */
public class TopicSelectedEvent {
    private Topic topic;

    public TopicSelectedEvent(Topic topic){
        this.topic = topic;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}

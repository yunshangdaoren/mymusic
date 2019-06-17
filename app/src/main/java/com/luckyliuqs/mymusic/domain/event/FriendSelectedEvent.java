package com.luckyliuqs.mymusic.domain.event;

import com.luckyliuqs.mymusic.domain.User;

/**
 * 选择好友页面中选择好友事件
 */
public class FriendSelectedEvent {
    private User user;

    public FriendSelectedEvent(User user){
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

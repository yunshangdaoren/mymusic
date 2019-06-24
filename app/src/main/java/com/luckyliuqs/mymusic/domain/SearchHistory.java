package com.luckyliuqs.mymusic.domain;

import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

/**
 * 搜索历史
 */
public class SearchHistory extends Base{
    /**
     * id,主键
     */
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;

    /**
     * 标题内容
     */
    @NotNull
    private String content;

    /**
     * 创建时间
     */
    private long created_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }
}

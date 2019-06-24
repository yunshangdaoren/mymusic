package com.luckyliuqs.mymusic.Util;


import android.content.Context;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.luckyliuqs.mymusic.domain.SearchHistory;
import com.luckyliuqs.mymusic.domain.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库连接工具类
 */
public class OrmUtil {
    static LiteOrm orm;
    private static OrmUtil instance;

    public OrmUtil(Context context){
        //创建LiteOrm
        orm = LiteOrm.newSingleInstance(context, "my_music.db");
    }

    public static OrmUtil getInstance(Context context){
        if (instance == null){
            instance = new OrmUtil(context);
        }
        return instance;
    }

    /**
     * 储存指定歌曲和对应的用户id
     * @param song
     * @param userId
     */
    public void saveSong(Song song, String userId){
        song.setUserId(userId);
        orm.save(song);
    }

    /**
     * 删除指定用户id对应的歌曲
     * @param userId
     */
    public void deleteSongs(String userId) {
        orm.delete(new WhereBuilder(Song.class).where("userId=?", new Object[]{userId}));
    }

    public void deleteSong(Song song){
        orm.delete(song);
    }

    public List<Song> queryPlayList(String userId){
        ArrayList<Song> songs = orm.query(new QueryBuilder<Song>(Song.class)
                                            .whereEquals("userId", userId)
                                            .whereAppendAnd()
                                            .whereEquals("playList", true)
                                            .appendOrderAscBy("id"));
        return songs;
    }

    /**
     * @param userId
     * @param orderBy
     * @return 查询的本地歌曲
     */
    public List<Song> queryLocalMusic(String userId, String orderBy){
        ArrayList<Song> songs = orm.query(new QueryBuilder<Song>(Song.class)
                                            .whereEquals("userId", userId)
                                            .whereAppendAnd()
                                            .whereEquals("source", Song.SOURCE_LOCAL)
                                            .appendOrderAscBy(orderBy));

        return songs;
    }

    public int countOfLocalMusic(String userId){
        return (int)orm.queryCount(new QueryBuilder<Song>(Song.class)
                .whereEquals("userId", userId)
                .whereAppendAnd()
                .whereEquals("source", Song.SOURCE_LOCAL));
    }

    public Song findSongById(String id){
        return orm.queryById(id, Song.class);
    }

    /**
     * @return 搜索历史数据
     */
    public List<SearchHistory> queryAllSearchHistory(){
        return orm.query(new QueryBuilder<SearchHistory>(SearchHistory.class)
                            .appendOrderDescBy("create_at"));
    }
    /**
     * 删除搜索历史
     * @param searchHistory
     */
    public void deleteSearchHistory(SearchHistory searchHistory){
        orm.delete(searchHistory);
    }

    /**
     * 保存搜索历史数据
     * @param searchHistory
     */
    public void createOrUpdate(SearchHistory searchHistory){
        orm.save(searchHistory);
    }

}

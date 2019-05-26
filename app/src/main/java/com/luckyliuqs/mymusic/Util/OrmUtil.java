package com.luckyliuqs.mymusic.Util;


import android.content.Context;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
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
        orm = LiteOrm.newSingleInstance(context, "my-music.db");
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





}

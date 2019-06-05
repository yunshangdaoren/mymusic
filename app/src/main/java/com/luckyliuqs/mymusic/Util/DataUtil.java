package com.luckyliuqs.mymusic.Util;

import com.luckyliuqs.mymusic.domain.Song;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataUtil {

    /**
     * 将歌曲中对象的字段展开
     * @param songs
     * @return
     */
    public static List<Song> fill(List<Song> songs){
        for (Song s:songs) {
            //填充专辑和歌曲信息
            s.fill();
        }
        return songs;
    }

    /**
     * 更改歌曲是否在播放列表表示
     * @param songs
     * @param isPlayList
     * @return
     */
    public static Collection<? extends Song> changePlayListFlag(List<Song> songs, boolean isPlayList){
        for (Song s:songs) {
            //设置歌曲是否在播放列表
            s.setPlayList(isPlayList);
        }
        return songs;
    }

}

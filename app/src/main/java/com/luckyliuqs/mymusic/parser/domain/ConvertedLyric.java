package com.luckyliuqs.mymusic.parser.domain;

import java.util.Map;
import java.util.TreeMap;

/**
 * 解析要播放的歌词实体类
 */
public class ConvertedLyric {
    /**
     * LRC类型歌词
     */
    public static final int TYPE_LRC = 0;

    /**
     * 卡拉OK类型歌词
     */
    public static final int TYPE_KSC = 10;

    /**
     * 储存歌词头部元数据tag：包括歌曲名，歌手等信息
     */
    protected Map<String, Object> tags;

    /**
     * 保存所有行歌词
     */
    protected TreeMap<Integer, Line> lyrics;


    public Map<String, Object> getTags() {
        return tags;
    }

    public void setTags(Map<String, Object> tags) {
        this.tags = tags;
    }

    public TreeMap<Integer, Line> getLyrics() {
        return lyrics;
    }

    public void setLyrics(TreeMap<Integer, Line> lyrics) {
        this.lyrics = lyrics;
    }

    /**
     * 根据当前播放进度获取所对应的该行歌词的行号
     * @param time
     * @return 歌词的行号
     */
    public int getLineNumber(long time){
        for (int i = lyrics.size() - 1; i >= 0; i--){
            //如果播放的时间在该行歌词的开始时间内，即当前播放的歌词行就是该行
            Line line = lyrics.get(i);
            if (time >= line.getStartTime()){
                return i;
            }
        }
        return 0;
    }

    /**
     * @param lineNumber
     * @param position
     * @return 根据行号和时间，获取该行歌词正在播放的字的下标
     */
    public int getWordIndex(int lineNumber, long position){
        return -1;
    }

    /**
     * @param lineNumber
     * @param position
     * @return 当前行歌词已经播放的时间
     */
    public float getWordPlayedTime(int lineNumber, long position){
        return -1;
    }

    /**
     * 歌词是否精确到字
     * @return
     */
    public boolean isAccurate(){
        return true;
    }


}

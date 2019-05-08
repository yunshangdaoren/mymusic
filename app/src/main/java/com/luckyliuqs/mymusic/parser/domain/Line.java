package com.luckyliuqs.mymusic.parser.domain;

import com.luckyliuqs.mymusic.domain.Base;

/**
 * 代表一行歌词实体类
 */
public class Line extends Base {
    /**
     * 整行歌词
     */
    private String lineLyrics = null;

    /**
     * 该行歌词的开始播放的时间
     */
    private long startTime = 0;

    /**
     * 一行歌词所有文字
     */
    private String[] lyricsWord = null;

    /**
     * 该行歌词中每个字所需要播放的时间
     */
    private int[] wordDuration = null;

    /**
     * 该行歌词结束播放的时间
     */
    private long endTime = 0;


    public String getLineLyrics() {
        return lineLyrics;
    }

    public void setLineLyrics(String lineLyrics) {
        this.lineLyrics = lineLyrics;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String[] getLyricsWord() {
        return lyricsWord;
    }

    public void setLyricsWord(String[] lyricsWord) {
        this.lyricsWord = lyricsWord;
    }

    public int[] getWordDuration() {
        return wordDuration;
    }

    public void setWordDuration(int[] wordDuration) {
        this.wordDuration = wordDuration;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

}

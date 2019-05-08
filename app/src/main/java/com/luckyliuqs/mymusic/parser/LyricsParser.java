package com.luckyliuqs.mymusic.parser;


import com.luckyliuqs.mymusic.parser.domain.ConvertedLyric;

/**
 * 歌词解析基类
 */
public abstract class LyricsParser {
    /**
     * 歌词类型
     */
    protected final int type;
    /**
     * 原始歌词内容
     */
    protected final String content;
    /**
     * 解析后的歌词
     */
    protected ConvertedLyric convertedLyric;


    protected LyricsParser(int type, String content){
        this.type = type;
        this.content = content;
    }

    /**
     * 根据传入的歌词类型，进行不同歌词类型解析
     * @param type
     * @param content
     * @return 解析后的歌词
     */
    public static LyricsParser parse(int type, String content){
        switch (type){
            case ConvertedLyric.TYPE_LRC:
                //返回解析的LRC类型歌词
                return new LRCLyricsParser(type, content);
            case ConvertedLyric.TYPE_KSC:
                //返回解析的KSC类型歌词
                return new KSCLyricsParser(type, content);
            default:
                //其他类型不支持，则返回null
                return null;
        }
    }

    /**
     * 歌词抽象解析方法
     */
    public abstract void parse();

    /**
     * 返回解析转换后的歌词
     * @return
     */
    public ConvertedLyric getConvertedLyric(){
        return convertedLyric;
    }


}

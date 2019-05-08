package com.luckyliuqs.mymusic.parser;

import com.luckyliuqs.mymusic.Util.TimeUtil;
import com.luckyliuqs.mymusic.parser.domain.ConvertedLyric;
import com.luckyliuqs.mymusic.parser.domain.Line;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * LRC类型歌词解析类
 */
public class LRCLyricsParser extends LyricsParser{

    protected LRCLyricsParser(int type, String content) {
        super(type, content);
    }

    /**
     * 对LRC类型歌词进行解析
     */
    @Override
    public void parse() {
        //以换号符"\n"对歌词进行分割
        String[] strings = content.split("\n");
        //实例化ConvertedLyric
        convertedLyric = new ConvertedLyric();

        //储存所有行歌词
        TreeMap<Integer, Line> lyrics = new TreeMap<Integer, Line>();
        //储存歌词头部元数据tag
        Map<String, Object> tags = new HashMap<String, Object>();

        //每一行还未解析的歌词内容
        String lineInfo = null;
        //对应的歌词行号
        int lineNumber = 0;
        for(int i = 0; i < strings.length; i++){
            try {
                lineInfo = strings[i];
                //解析后的每一行歌词
                Line line = parseLine(tags, lineInfo);
                if (line != null){
                    lyrics.put(lineNumber, line);
                    lineNumber ++;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        convertedLyric.setLyrics(lyrics);
        convertedLyric.setTags(tags);
    }

    /**
     * @param tags
     * @param lineInfo
     * @return 解析后的一行歌词
     */
    private Line parseLine(Map<String, Object> tags, String lineInfo){
        if (lineInfo.startsWith("[0")){
            //一行歌词
            Line line = new Line();

            int leftIndex = 1;
            int rightIndex = lineInfo.length();
            String[] lineComments = lineInfo.substring(leftIndex, rightIndex).split("]",-1);

            //开始时间
            String startTimeStr = lineComments[0];
            int startTime = TimeUtil.parseInteger(startTimeStr);
            line.setStartTime(startTime);

            //歌词
            String lineLyricsStr = lineComments[1];
            line.setLineLyrics(lineLyricsStr);

            return line;
        }
        return null;
    }



}

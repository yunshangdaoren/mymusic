package com.luckyliuqs.mymusic.parser;

import com.luckyliuqs.mymusic.Util.CharUtil;
import com.luckyliuqs.mymusic.Util.StringUtil;
import com.luckyliuqs.mymusic.Util.TimeUtil;
import com.luckyliuqs.mymusic.parser.domain.KSCLyric;
import com.luckyliuqs.mymusic.parser.domain.Line;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * KSC类型歌词解析类
 * KSC歌词如下所示:
 *
 * karaoke := CreateKaraokeObject;
 * karaoke.rows := 2;
 * karaoke.clear;
 *
 * karaoke.add('00:19.662', '00:22.609', '有没有一扇窗', '313,419,732,299,348,836');
 * karaoke.add('00:22.609', '00:25.995', '能让你不绝望', '388,721,440,370,387,1080');
 */
public class KSCLyricsParser extends LyricsParser{

    protected KSCLyricsParser(int type, String content) {
        super(type, content);
    }

    /**
     * 对KSC类型歌词进行解析
     */
    @Override
    public void parse() {
        //以换号符"\n"对歌词进行分割成一行行歌词数组
        String[] strings = content.split("\n");

        convertedLyric = new KSCLyric();

        //储存歌词中的每一行歌词
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
        String[] left;

        if (lineInfo.startsWith("karaoke.songname")){
            left = lineInfo.split("\'");
            tags.put(LyricTag.TAG_TITLE, left[1]);
        }else if(lineInfo.startsWith("karaoke.singer")){
            left = lineInfo.split("\'");
            tags.put(LyricTag.TAG_ARTIST, left[1]);
        }else if(lineInfo.startsWith("karaoke.offset")){
            left = lineInfo.split("\'");
            tags.put(LyricTag.TAG_OFFSET, left[1]);
        }else if(lineInfo.startsWith("karaoke.tag")) {
            left = lineInfo.split("\'")[1].split(":");
            tags.put(left[0], left[1]);
        }else{  //代表真正的歌词
            //一行歌词
            Line line = new Line();
            int left1 = "karaoke.add".length() + 1;
            int right = lineInfo.length();
            //karaoke.add('00:22.609', '00:25.995', '能让你不绝望', '388,721,440,370,387,1080');

            //对该行歌词进行分割
            String[] lineComments = lineInfo.substring(left1 + 1, right - 3).split("\'\\s*,\\s*\'", -1);

            //该行歌词开始时间
            String startTimeStr = lineComments[0];
            int startTime = TimeUtil.parseInteger(startTimeStr);
            line.setStartTime(startTime);

            //该行歌词结束时间
            String endTimeStr = lineComments[1];
            int endTime = TimeUtil.parseInteger(endTimeStr);
            line.setEndTime(endTime);

            //该行歌词的内容
            String lineLyricStr = lineComments[2];
            List<String> lineLyricList = getLyricsWord(lineLyricStr);
            //该行歌词文字数组
            String[] lyricsWord = (String[]) lineLyricList.toArray(new String[lineLyricList.size()]);
            line.setLyricsWord(lyricsWord);
            //该行歌词文字字符串
            String lineLyrics = getLineLyric(lineLyricStr);
            line.setLineLyrics(lineLyrics);

            //该行歌词每个文字播放时间列表
            List<String> wordDurationList = getWordDurationList(lineComments[3]);
            int[] wordDuration = this.getWordDurationList(wordDurationList);
            line.setWordDuration(wordDuration);

            return line;
        }

        return null;
    }

    /**
     * 将一行歌词中每个文字取出来，处理后储存在ArrayList中
     * @param line
     * @return ArrayList
     */
    private List<String> getLyricsWord(String line){
        List<String> lineLyricsList = new ArrayList<String>();
        String temp = "";
        //是否为空格
        boolean isEnter = false;

        for (int i = 0; i < line.length(); ++i){
            char c = line.charAt(i);
            if (CharUtil.isChinese(c) || !CharUtil.isWord(c) && c != 91 && c != 93){
                //中文
                if (isEnter){
                    temp = temp + String.valueOf(line.charAt(i));
                }else{
                    lineLyricsList.add(String.valueOf(line.charAt(i)));
                }
            }else if(c == 91){
                isEnter = true;
            }else if(c == 93){
                isEnter = false;
                lineLyricsList.add(temp);
                temp = "";
            }else{
                temp = temp + String.valueOf(line.charAt(i));
            }
        }
        return lineLyricsList;
    }


    private String getLineLyric(String lineLyricsStr){
        String temp = "";
        int i = 0;

        while (i < lineLyricsStr.length()){
            switch (lineLyricsStr.charAt(i)){
                case '\\':
                default:
                    temp = temp + String.valueOf(lineLyricsStr.charAt(i));
                case '[':
                case ']':
                    ++i;
            }
        }
        return temp;
    }

    private int[] getWordDurationList(List<String> wordDurationList){
        int[] wordDuration = new int[wordDurationList.size()];

        for (int i = 0; i < wordDurationList.size(); i++){
            String wordDurationStr = wordDurationList.get(i);
            if (StringUtil.isNumber(wordDurationStr)){
                wordDuration[i] = Integer.parseInt(wordDurationStr);
            }
        }

        return wordDuration;
    }

    private List<String> getWordDurationList(String wordDurationString){
        ArrayList wordDurationList = new ArrayList();
        String temp = "";

        for (int i = 0; i < wordDurationString.length(); i++){
            char c = wordDurationString.charAt(i);
            switch (c){
                case ',':
                    wordDurationList.add(temp);
                    temp = "";
                    break;
                default:
                    if (Character.isDigit(c)){
                        temp = temp + String.valueOf(wordDurationString.charAt(i));
                    }
            }
        }

        if (!temp.equals("")){
            wordDurationList.add(temp);
        }

        return wordDurationList;
    }


    public static class LyricTag{
        //歌曲名称
        public static final String TAG_TITLE = "lyrics.tag.title";
        //歌手
        public static final String TAG_ARTIST = "lyrics.tag.artist";
        //
        public static final String TAG_OFFSET = "lyrics.tag.offset";
        //
        public static final String TAG_BY = "lyrics.tag.by";
        //
        public static final String TAG_TOTAL = "lyrics.tag.total";
    }

}


























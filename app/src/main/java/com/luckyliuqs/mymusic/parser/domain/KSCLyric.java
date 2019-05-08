package com.luckyliuqs.mymusic.parser.domain;

/**
 * KSC类型解析后的歌词类
 */
public class KSCLyric extends ConvertedLyric{

    /**
     * 获取当前播放时间对应的歌词行号
     * @param time
     * @return
     */
    @Override
    public int getLineNumber(long time) {
        for (int i = 0; i < lyrics.size(); i++){
            //下标i对应的一行歌词
            Line line = (Line) lyrics.get(i);

            //当前播放时间正好在该行歌词的开始和结束时间内，则播放的就是该行歌词
            if (time >= lyrics.get(i).getStartTime() && time <= lyrics.get(i).getEndTime()){
                return i;
            }

            //当前播放时间在该行歌词结束时间后，但是在下一行歌词开始时间前，则播放的还是该行歌词
            if (time >= line.getEndTime() && i + 1 < lyrics.size() && time <= lyrics.get(i + 1).getStartTime()){
                return i;
            }
        }

        //如果当前播放时间在最后一行歌词结束时间之外，则就是最后一行歌词
        Line line = (Line) lyrics.get(lyrics.size() - 1);
        if (time >= line.getEndTime()){
            return lyrics.size() - 1;
        }

        return 0;
    }


    /**
     * @param lineNumber
     * @param position
     * @return 根据行号和时间，获取该行歌词正在播放的字的下标
     */
    @Override
    public int getWordIndex(int lineNumber, long position) {
        if (lineNumber == -1){
            return -1;
        }

        //获取该行歌词
        Line line = (Line)lyrics.get(lineNumber);
        //改行歌词播放的开始时间
        long startTime = line.getStartTime();
        //对改行歌词所有字进行遍历
        for (int i = 0; i < line.getLyricsWord().length; i++){
            startTime += line.getWordDuration()[i];
            if (position < startTime){
                return i;
            }
        }
        return -1;
    }

    /**
     * @param lineNumber
     * @param position
     * @return 当前行歌词已经播放的时间
     */
    @Override
    public float getWordPlayedTime(int lineNumber, long position) {
        if (lineNumber == -1){
            return -1;
        }

        Line line = (Line)lyrics.get(lineNumber);
        long startTime = line.getStartTime();
        for (int i = 0; i < line.getLyricsWord().length; i++){
            startTime += line.getWordDuration()[i];
            if (position < startTime){
                return line.getWordDuration()[i] - (startTime - position);
            }
        }
        return -1;
    }

    @Override
    public boolean isAccurate() {
        return true;
    }
}














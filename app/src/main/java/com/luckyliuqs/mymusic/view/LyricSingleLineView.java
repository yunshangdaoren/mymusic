package com.luckyliuqs.mymusic.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.luckyliuqs.mymusic.Util.DensityUtil;
import com.luckyliuqs.mymusic.parser.domain.ConvertedLyric;
import com.luckyliuqs.mymusic.parser.domain.Line;

import java.util.TreeMap;

/**
 * 单行歌词View，用在迷你播放控制器上
 */
public class LyricSingleLineView extends View {
    private static final String TAG = "TAG";

    /**
     * 默认歌词字体大小
     */
    private static final float DEFAULT_LYRIC_FONT_SIZE = 13;

    /**
     * 默认显示的歌词内容
     */
    private static final String DEFAULT_TIP_TEXT = "我的云音乐，听你想听";

    /**
     * 解析后的歌词
     */
    private ConvertedLyric convertedLyric;

    /**
     * 储存每一行歌词
     */
    private TreeMap<Integer, Line> lyricsLines;

    /**
     * 歌词画笔
     */
    private Paint backgroundTextPaint;

    /**
     * 当前歌词所在行号
     */
    private int lineNumber = 0;

    /**
     * 歌曲当前播放的位置
     */
    private long position;


    public LyricSingleLineView(Context context) {
        super(context);
        init();
    }

    public LyricSingleLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LyricSingleLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LyricSingleLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init(){
        //初始化画笔
        backgroundTextPaint = new Paint();
        backgroundTextPaint.setDither(true);
        backgroundTextPaint.setAntiAlias(true);
        backgroundTextPaint.setTextSize(DensityUtil.dipToPx(getContext(), DEFAULT_LYRIC_FONT_SIZE));
        backgroundTextPaint.setColor(Color.parseColor("#aaaaaa"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        if (isEmptyLyric()) {
            //如果没有歌词，则绘制默认的歌词
            drawDefaultLyricText(canvas);
        }else{
            //有歌词，则绘制歌词
            drawLyricText(canvas);
        }
    }

    /**
     * 如果没有歌词，则绘制默认的歌词
     * @param canvas
     */
    private void drawDefaultLyricText(Canvas canvas){
        float textWidth = getTextWidth(backgroundTextPaint, DEFAULT_TIP_TEXT);
        float textHeight = getTextHeight(backgroundTextPaint);

        Paint.FontMetrics fontMetrics = backgroundTextPaint.getFontMetrics();

        float centerX = 0;
        float centerY = (getHeight() - textHeight) / 2 + Math.abs(fontMetrics.top);

        canvas.drawText(DEFAULT_TIP_TEXT, centerX, centerY, backgroundTextPaint);
    }

    /**
     * 有歌词，则绘制歌词
     * @param canvas
     */
    private void drawLyricText(Canvas canvas){
        //在当前位置绘制正在播放的歌词
        Line line = lyricsLines.get(lineNumber);

        //当前歌词的宽高
        float textHeight = getTextHeight(backgroundTextPaint);

        Paint.FontMetrics fontMetrics = backgroundTextPaint.getFontMetrics();
        //TextView绘制是从baseLine开始，而不是左上角
        float centerY = (getMeasuredHeight() - textHeight) / 2 + Math.abs(fontMetrics.top);
        float x = 0;
        float y = centerY;

        //精确到行
        canvas.drawText(line.getLineLyrics(), x, y, backgroundTextPaint);
    }

    private float getTextHeight(Paint paint){
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (float)Math.ceil(fontMetrics.descent - fontMetrics.ascent);
    }

    private float getTextWidth(Paint paint, String text){
        return paint.measureText(text);
    }

    public void setData(ConvertedLyric convertedLyric){
        this.convertedLyric = convertedLyric;
        this.lyricsLines = convertedLyric.getLyrics();
    }

    public void show(long position){
        if (isEmptyLyric()) {
            //如果没有歌词，则返回
            return;
        }

        this.position = position;
        lineNumber = convertedLyric.getLineNumber(position);
        invalidate();
    }

    private boolean isEmptyLyric(){
        return convertedLyric == null;
    }

}























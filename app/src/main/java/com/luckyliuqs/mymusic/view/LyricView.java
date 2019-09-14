package com.luckyliuqs.mymusic.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;

import com.luckyliuqs.mymusic.Util.DensityUtil;
import com.luckyliuqs.mymusic.Util.TimeUtil;
import com.luckyliuqs.mymusic.activity.MusicPlayerActivity;
import com.luckyliuqs.mymusic.listener.OnLyricClickListener;
import com.luckyliuqs.mymusic.parser.domain.ConvertedLyric;
import com.luckyliuqs.mymusic.parser.domain.Line;

import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

/**
 * 自定义View：歌词播放页面
 */
public class LyricView extends View {
    private static final String TAG = "TAG";

    /**
     * 默认歌词字体大小
     */
    private static final float DEFAULT_LYRIC_FONT_SIZE = 15;

    /**
     * 默认时间线的字体大小
     */
    private static final float DEFAULT_TIME_FONT_SIZE = 15;

    /**
     * 默认时间线的文字到屏幕两边的距离
     */
    private static final float DEFAULT_LYRIC_MARGIN = 16;

    /**
     * 默认时间线的播放按钮增大区域
     */
    private static final float DEFAULT_PLAY_TEXT_ARE = 10;

    /**
     * 默认分割线到两边文字的距离
     */
    private static final float DEFAULT_LYRIC_TIME_LINE_MARGIN_LEFT = 5;

    /**
     * 默认歌词显示的内容
     */
    private static final String DEFAULT_TIP_TEXT = "我的云音乐，听你想听";

    /**
     * 歌词拖拽的时候，播放按钮，可以换成图片
     */
    private static final String PLAY_TEXT = "播放";

    /**
     * 歌词拖拽后，多少秒继续滚蛋歌词
     */
    private static final long DEFAULT_HIDE_DRAG_TIME = 2000;

    /**
     * 事件类型
     */
    private static final int MSG_HIDE_TIME_LINE = 0;

    /**
     * 点击了歌词旁边的播放文字
     */
    private OnLyricClickListener onLyricClickListener;

    /**
     * 转换后要进行播放的歌词
     */
    private ConvertedLyric convertedLyric;

    /**
     * 储存歌曲中每一行歌词
     */
    private TreeMap<Integer, Line> lyricsLines;

    /**
     * 未播放状态下的歌词的画笔
     */
    private Paint backgroundTextPaint;

    /**
     * 播放状态下高亮的歌词的画笔
     */
    private Paint foregroundTextPaint;

    /**
     * 时间线画笔
     */
    private Paint timeLinePant;

    /**
     * 时间线左侧播放文字画笔
     */
    private Paint playPaint;

    /**
     * 时间线右时间文字画笔
     */
    private Paint timePaint;

    /**
     * 每行歌词之间的间隔高度
     */
    private float lineSpaceHeight = 35;

    /**
     * 当前所在行歌词的行号
     */
    private int lineNumber = 0;

    /**
     * 当前歌词行已经播放的高度
     */
    private float foregroundWidth = 0;

    /**
     * 当前播放时间点，即在当前歌词行播放的第几个字
     */
    private int lyricCurrentWordIndex = -1;

    /**
     * 对于当前歌词中正在播放的字，已经播放的时间
     */
    private float wordPlayedTime = 0;

    /**
     * 得到当前行歌词已经播放的宽度，即要绘制的高亮宽度 = 当前正在播放的文字前面所有已经播放过的文字宽度 + 当前文字已经播放过的宽度
     */
    private float lineLyricPlayedWidth;

    /**
     * 当前播放位置的歌词的偏移
     */
    private float offsetY = 0;

    /**
     * 歌词滚动的属性动画
     */
    private ValueAnimator valueAnimator;

    /**
     * 音乐当前播放的位置
     */
    private long position;

    /**
     * 上一次触摸点
     */
    private float lastY;

    /**
     * 是否是拖拽模式
     */
    private boolean isDrag;

    /**
     * 歌词旁边播放文字的矩形，用来判断是否点击到了
     */
    private Rect playRect = new Rect();

    /**
     * 歌词左右间距
     */
    private int lyricMargin;

    /**
     * 时间线距离左右的间距
     */
    private int lyricTimeLineMarginLeft;

    /**
     * 用于多少秒后隐藏时间线
     */
    private TimerTask timerTask;
    private Timer timer;

    /**
     * 最小滑动值
     */
    private int touchSlop;

    /**
     * 播放文字增大区域
     */
    private int playTextArea;

    /**
     * 用来检测是否是长按事件的Runnable
     */
    private CheckForLongPress mPendingCheckForLongPress;

    /**
     * 长按事件是否已经返回了true
     */
    private boolean mHasPerformedLongPress;


    public LyricView(Context context) {
        super(context);
        init();
    }

    public LyricView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LyricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public LyricView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        //将一些值转为px
        lyricMargin = DensityUtil.dipToPx(getContext(), DEFAULT_LYRIC_MARGIN);
        lyricTimeLineMarginLeft = DensityUtil.dipToPx(getContext(), DEFAULT_LYRIC_TIME_LINE_MARGIN_LEFT);
        playTextArea = DensityUtil.dipToPx(getContext(), DEFAULT_PLAY_TEXT_ARE);

        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        touchSlop = viewConfiguration.getScaledTouchSlop();

        //初始化画笔
        backgroundTextPaint = new Paint();
        backgroundTextPaint.setDither(true);
        backgroundTextPaint.setAntiAlias(true);
        backgroundTextPaint.setTextSize(DensityUtil.dipToPx(getContext(), DEFAULT_LYRIC_FONT_SIZE));
        backgroundTextPaint.setColor(Color.WHITE);

        foregroundTextPaint = new Paint();
        foregroundTextPaint.setDither(true);
        foregroundTextPaint.setAntiAlias(true);
        foregroundTextPaint.setTextSize(DensityUtil.dipToPx(getContext(), DEFAULT_TIME_FONT_SIZE));
        foregroundTextPaint.setColor(Color.RED);

        timeLinePant = new Paint();
        timeLinePant.setDither(true);
        timeLinePant.setAntiAlias(true);
        timeLinePant.setTextSize(DensityUtil.dipToPx(getContext(), DEFAULT_TIME_FONT_SIZE));
        timeLinePant.setColor(Color.RED);

        timePaint = new Paint();
        timePaint.setDither(true);
        timePaint.setAntiAlias(true);
        timePaint.setTextSize(DensityUtil.dipToPx(getContext(), DEFAULT_TIME_FONT_SIZE));
        timePaint.setColor(Color.RED);

        playPaint = new Paint();
        playPaint.setDither(true);
        playPaint.setAntiAlias(true);
        playPaint.setTextSize(DensityUtil.dipToPx(getContext(), DEFAULT_TIME_FONT_SIZE));
        playPaint.setColor(Color.RED);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        if (isEmptyLyric()){
            //如果没有歌词，那么就绘制默认的歌词
            drawDefaultText(canvas);
        }else{
            //有歌词就绘制歌词
            drawLyricText(canvas);
        }

        //如果歌词处于拖拽状态，则绘制时间线
        if (isDrag){
            drawIndicator(canvas);
        }

        canvas.restore();
    }

    /**
     * 判断歌词是否为null
     * @return boolean
     */
    private boolean isEmptyLyric(){
        return convertedLyric == null;
    }

    /**
     * 歌词Lyric为null，绘制默认的要显示的歌词文字
     * @param canvas
     */
    private void drawDefaultText(Canvas canvas){
        //返回要显示歌词文字的测量宽度值
        float textWidth = getTextWidth(backgroundTextPaint, DEFAULT_TIP_TEXT);
        //返回要显示歌词文字的测量高度值
        float textHeight = getTextHeight(backgroundTextPaint);

        //绘制的文字在屏幕中心的X坐标
        float centerX = (getWidth() - textWidth) / 2;
        //绘制的文字在屏幕中心的Y坐标
        float centerY = (getHeight() - textHeight) / 2;

        //绘制歌词文字
        canvas.drawText(DEFAULT_TIP_TEXT, centerX, centerY, foregroundTextPaint);
    }

    /**
     * @param paint
     * @param text
     * @return 返回画笔测量的文本宽度值
     */
    private float getTextWidth(Paint paint, String text){
        return paint.measureText(text);
    }

    /**
     * @param paint
     * @return 画笔测量文本后的高度值
     */
    private float getTextHeight(Paint paint){
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        //返回一个大于等于（fontMetrics.descent - fontMetrics.ascent）的最小整数，ascent是一个负值
        return (float) Math.ceil(fontMetrics.descent - fontMetrics.ascent);
    }

    /**
     * 歌词Lyric不为null，绘制要显示的当前位置正在播放的歌词行
     * @param canvas
     */
    private void drawLyricText(Canvas canvas){
        //得到当前正在播放歌词行号对应的行歌词
        Line line = lyricsLines.get(lineNumber);

        //当前行歌词文本的宽
        float textWidth = getTextWidth(backgroundTextPaint, line.getLineLyrics());
        //当前行歌词文本的高
        float textHeight = getTextHeight(backgroundTextPaint);

        //屏幕中心Y坐标
        float centerY = (getMeasuredHeight() - textHeight) / 2 + lineNumber * getLineHeight(backgroundTextPaint) - offsetY;
        //当前行歌词在屏幕中心的X坐标
        float x = (getMeasuredWidth() - textWidth) / 2;
        //当前行歌词在屏幕中心的Y坐标
        float y = centerY;

        //判断当前行歌词绘制是否精确到字
        if (convertedLyric.isAccurate()){
            //歌词绘制精确到字
            // 绘制行当前歌词
            canvas.drawText(line.getLineLyrics(), x, y, backgroundTextPaint);

            if(lyricCurrentWordIndex == -1){
                //代表该行歌词已经播放完了
                lineLyricPlayedWidth = textWidth;
            }else{
                //获取当前行歌词中的所有文字
                String[] lyricsWord = line.getLyricsWord();
                //获取该行歌词行中每个字所需要播放的时间
                int[] wordDuration = line.getWordDuration();

                //获取当前正在播放的文字前面所有已经播放过的文字宽度
                String beforeText = line.getLineLyrics().substring(0, lyricCurrentWordIndex);
                //文本的宽度
                float beforeTextWidth = getTextWidth(foregroundTextPaint,beforeText);

                //获取当前正在播放的文字
                String currentWord = lyricsWord[lyricCurrentWordIndex];
                //文字的宽度
                float currentWordTextWidth = getTextWidth(foregroundTextPaint, currentWord);

                //当前文字已经播放的宽度:（当前文字已经播放的时间/文字总的播放时间) * 文字总的宽度
                float currentWordWidth = (wordPlayedTime / wordDuration[lyricCurrentWordIndex]) * currentWordTextWidth;

                //得到当前行歌词已经播放的宽度，即要绘制的高亮宽度 = 当前正在播放的文字前面所有已经播放过的文字宽度 + 当前文字已经播放过的宽度
                lineLyricPlayedWidth = beforeTextWidth + currentWordWidth;
            }

            canvas.save();
            //裁剪一个矩形用来绘制已经播放过的歌词
            canvas.clipRect(x, y-textHeight, x+lineLyricPlayedWidth, y+textHeight);

            //canvas.drawRect(x, y - textHeight, x + lineLyricPlayedWidth, y + textHeight, foregroundTextPaint);

            //覆盖绘制已经播放过的歌词文字：以高亮形式
            canvas.drawText(line.getLineLyrics(), x, y ,foregroundTextPaint);

            canvas.restore();
        }else{
            //不精确到字，只精确到行
            canvas.drawText(line.getLineLyrics(), x, y, foregroundTextPaint);
        }

        //遍历绘制当前歌词行前面的歌词行，从对应第i行歌词的上一行开始绘制
        for(int i = lineNumber - 1; i > 0; i--){
            //获取当前行歌词的上一行歌词
            line = lyricsLines.get(i);

            //当前行歌词的宽
            textWidth = getTextWidth(backgroundTextPaint, line.getLineLyrics());
            //当前行歌词的高
            textHeight = getTextHeight(backgroundTextPaint);

            //当前行歌词X轴坐标
            x = (getMeasuredWidth() - textWidth) / 2;
            //当前行歌词Y轴坐标：centerY - 每行歌词的高度
            y = centerY - (lineNumber - i) * getLineHeight(backgroundTextPaint);

            if (y < getLineHeight(backgroundTextPaint)){
                //如果超出View的顶部，则不再绘制
                break;
            }

            canvas.drawText(line.getLineLyrics(), x, y, backgroundTextPaint);
        }

        //遍历绘制当前歌词行后面的歌词行，从对应第i行歌词的下一行开始绘制
        for(int i = lineNumber + 1; i < lyricsLines.size(); i++){
            //获取当前行歌词的上一行歌词
            line = lyricsLines.get(i);

            //当前行歌词的宽
            textWidth = getTextWidth(backgroundTextPaint, line.getLineLyrics());
            //当前行歌词的高
            textHeight = getTextHeight(backgroundTextPaint);

            //当前行歌词X轴坐标
            x = (getMeasuredWidth() - textWidth) / 2;
            //当前行歌词Y轴坐标：centerY + 每行歌词的高度
            y = centerY + (i - lineNumber) * getLineHeight(backgroundTextPaint);

            if (y + getLineHeight(backgroundTextPaint) > getHeight()){
                //如果超出View的顶部，则不再绘制
                break;
            }

            canvas.drawText(line.getLineLyrics(), x, y, backgroundTextPaint);
        }

    }

    /**
     * 获取一行歌词的高度
     * @param paint
     * @return 歌词文本的高度 + 歌词行与行之间的间隔高度
     */
    private float getLineHeight(Paint paint){
        return Math.abs(getTextHeight(backgroundTextPaint) + lineSpaceHeight);
    }

    /**
     * 歌词处于拖拽状态，绘制时间线
     * @param canvas
     */
    private void drawIndicator(Canvas canvas){
        //获取当前滑动到歌词播放行
        int lineNumber = getCurrentLineNumber();

        //获取这一行歌词的开始时间
        int startTime = (int)lyricsLines.get(lineNumber).getStartTime();
        String startTimeString = TimeUtil.parseString(startTime);

        //绘制左侧，播放文本
        //文本宽度
        float playTextWidth = playPaint.measureText(PLAY_TEXT);
        //文本高度
        int playTextHeight = (int)getTextHeight(playPaint);
        //文本X坐标
        int playTextX = lyricMargin;
        //文本Y坐标
        int playTextY = (getMeasuredHeight() - playTextHeight) / 2;
        Paint.FontMetricsInt fmi = playPaint.getFontMetricsInt();
        canvas.drawText(PLAY_TEXT, playTextX, playTextY + Math.abs(fmi.top), playPaint);

        //增大文字的矩形区域，这样更任意被点击
        playRect.left = playTextX - playTextArea;
        playRect.right = (int)(playTextX + playTextWidth + playTextArea);
        playRect.top = playTextY - playTextArea;
        playRect.bottom = playTextY + playTextHeight + playTextArea;

        //绘制右侧的时间文本
        //文本宽度
        float timeTextWidth = timeLinePant.measureText(startTimeString);
        //文本高度
        float timeTextHeight = getTextHeight(timePaint);

        fmi = timePaint.getFontMetricsInt();
        //文本X坐标
        float timeTextX = (int)(getMeasuredWidth() - lyricMargin - timeTextWidth);
        //文本Y坐标：绘制文字时，他不是从位置顶部绘制，而是从baseline位置开始绘制
        float timeTextY = (getMeasuredHeight() - timeTextHeight) / 2 + Math.abs(fmi.top);
        canvas.drawText(startTimeString, timeTextX, timeTextY, timePaint);

        //绘制中间的时间线
        //时间线X坐标
        float timeLineX = lyricMargin + playTextWidth + lyricTimeLineMarginLeft;
        //时间线Y坐标
        float timeLineY = getMeasuredHeight() / 2;
        canvas.drawLine(timeLineX, timeLineY, getWidth() - timeTextWidth - lyricTimeLineMarginLeft - lyricMargin,timeLineY,timePaint);
    }

    /**
     * @return 当前滚动所在的行号
     */
    private int getCurrentLineNumber(){
        float scrollY = offsetY + getLineHeight(backgroundTextPaint) / 2;
        int lineNumber = (int)(scrollY / getLineHeight(backgroundTextPaint));

        if (lineNumber >= lyricsLines.size()){
            lineNumber = lyricsLines.size() - 1;
        }else if(lineNumber < 0){
            lineNumber = 0;
        }
        return lineNumber;
    }

    /**
     * 设置歌词
     * @param convertedLyric
     */
    public void setData(ConvertedLyric convertedLyric){
        this.convertedLyric = convertedLyric;
        this.lyricsLines = convertedLyric.getLyrics();
    }

    /**
     * 根据传递进来的时间显示对应行的歌词
     * @param position
     */
    public void show(long position){
        if (isEmptyLyric()){
            //如果没有歌词，则返回
            return;
        }

        if (isDrag){
            //如果在拖拽，则返回
            return;
        }
        this.position = position;

        int newlineNumber = convertedLyric.getLineNumber(position);
        if (newlineNumber != lineNumber){
            //重置变量
            lineLyricPlayedWidth = 0;
            lyricCurrentWordIndex = 0;

            lineNumber = newlineNumber;

            //要滚动的距离
            float distanceY = lineNumber * getLineHeight(backgroundTextPaint);
            //以动画的形式滚动到当前行歌词
            smoothScrollTo(distanceY);
        }

        if(convertedLyric.isAccurate()){
            //如果精确到字
            //则获取当前时间是该行歌词中的第几个字
            lyricCurrentWordIndex = convertedLyric.getWordIndex(lineNumber, position);

            //获取当前时间的该文字已经播放的时间
            wordPlayedTime = convertedLyric.getWordPlayedTime(lineNumber, position);
        }

        invalidate();
    }

    /**
     * 以动画的形式滚动到指定行歌词
     * @param distanceY:滚动距离
     */
    private void smoothScrollTo(float distanceY){
        if (valueAnimator != null && valueAnimator.isRunning()){
            valueAnimator.cancel();
        }

        valueAnimator = ValueAnimator.ofFloat(offsetY, distanceY);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offsetY = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

        //设置滚动时间
        valueAnimator.setDuration(200);
        //减速DecelerateInterpolator
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.start();
    }

    private void cancelTask(){
        if (timerTask != null){
            timerTask.cancel();
            timerTask = null;
        }

        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 触摸事件
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isEmptyLyric()){
            if (MotionEvent.ACTION_UP == event.getActionMasked()){
                performClick();
            }
            return true;
        }

        final float x = event.getX();
        final float y = event.getY();

        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:    //按下
                lastY = event.getY();
                //清除上一次长按事件的标志
                mHasPerformedLongPress = false;
                //设置为按下状态
                setPressed(true);
                //发送延时消息
                checkForLongClick(0, x, y);
                break;
            case MotionEvent.ACTION_MOVE:   //移动
                float distance = y - lastY;
                //如果滚动距离大于最小识别的距离，表示进入拖拽模式
                if (Math.abs(distance) > touchSlop){
                    isDrag = true;
                    //如果移动就取消按下
                    setPressed(false, x, y);
                }
                offsetY -= distance;
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:   //
                //设置为抬起
                setPressed(false, x, y);
                onActionUp(event);
                break;
            default:
                break;

        }

        invalidate();
        return true;
    }

    /**
     * 创建一个长按事件Runnable，并发送一个延时消息
     * @param delayOffset
     * @param x
     * @param y
     */
    private void checkForLongClick(int delayOffset, float x, float y){
        mHasPerformedLongPress = false;

        if (mPendingCheckForLongPress == null){
            mPendingCheckForLongPress = new CheckForLongPress();
        }
        mPendingCheckForLongPress.setAnchor(x, y);
        mPendingCheckForLongPress.rememberPressedState();
        postDelayed(mPendingCheckForLongPress, ViewConfiguration.getLongPressTimeout() - delayOffset);
    }

    /**
     * 处理点击事件
     * @param pressed
     * @param x
     * @param y
     */
    private void setPressed(boolean pressed, float x, float y){
        if (pressed){
            drawableHotspotChanged(x, y);
        }
        setPressed(pressed);
    }

    private void onActionUp(MotionEvent event){
        //判断是否为拖拽模式
        if (isDrag){
            //判断是否点击了播放文字
            if (isPlayClick(event)){
                isDrag = false;
                invalidate();

                if (onLyricClickListener != null){
                    //获取当前滑动到的歌词播放行
                    int scrollLyricLineNumber = getCurrentLineNumber();
                    long startTime = lyricsLines.get(scrollLyricLineNumber).getStartTime();
                    onLyricClickListener.onLyricClick(startTime);
                }
            }else{
                startHideTimeLine();
            }
        }else{
            if (!mHasPerformedLongPress){
                //如果长按事件没有处理，则在执行点击事件
                removeLongPressCallback();
                performClick();
            }
        }
    }

    /**
     * 判断是否点击的是播放文字
     * @param event
     * @return boolean
     */
    private boolean isPlayClick(MotionEvent event){
        int x = (int) event.getX();
        int y = (int) event.getY();
        //判断x,y坐标是否在plyRect内
        return playRect.contains(x, y);
    }

    private void removeLongPressCallback(){
        if (mPendingCheckForLongPress != null){
            removeCallbacks(mPendingCheckForLongPress);
        }
    }

    /**
     * 隐藏时间线
     */
    private void startHideTimeLine(){
        cancelTask();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.obtainMessage(MSG_HIDE_TIME_LINE).sendToTarget();
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, DEFAULT_HIDE_DRAG_TIME);
    }

    public void setOnLyricClickListener(OnLyricClickListener onLyricClickListener) {
        this.onLyricClickListener = onLyricClickListener;
    }

    /**
     * 用于检测长按事件类
     */
    private final class CheckForLongPress implements Runnable{
        private float mX;
        private float mY;
        private boolean mOriginalPressedState;

        @Override
        public void run() {
            if (mOriginalPressedState == isPressed()){
                if (performLongClick(mX, mY)){
                    mHasPerformedLongPress = true;
                }
            }
        }

        public void setAnchor(float x, float y){
            this.mX = x;
            this.mY = y;
        }

        public void rememberPressedState(){
            mOriginalPressedState = isPressed();
        }
    }

    /**
     * 有内存泄漏
     */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_HIDE_TIME_LINE:
                    isDrag = false;
                    invalidate();
                    break;
            }
        }
    };

}

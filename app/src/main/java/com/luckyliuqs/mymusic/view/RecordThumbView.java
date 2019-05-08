package com.luckyliuqs.mymusic.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.DensityUtil;
import com.luckyliuqs.mymusic.Util.ImageUtil;

import javax.annotation.Nullable;

/**
 * 自定义View：黑胶唱片指针
 */
public class RecordThumbView extends View implements ValueAnimator.AnimatorUpdateListener {
    /**
     * 包裹黑胶唱片CD的白圈背景的比例
     */
    public static final float CD_BG_SCALE = 1.333F;
    /**
     * 包裹黑胶唱片CD的白圈背景到页面顶部的比例
     */
    public static final float CD_BG_TOP_SCALE = 21F;
    /**
     * 指针头部的那条线的高度
     */
    private static final int CD_THUMB_LINE_HEIGHT = 2;
    /**
     * 指针在停止的时候，旋转的角度
     */
    private static final float THUMB_ROTATION_PAUSE = -25F;
    /**
     * 指针在播放的时候，旋转的角度
     */
    private static final float THUMB_ROTATION_PLAY = 0F;
    /**
     * 指针旋转的角度，默认是未播放的状态
     */
    private float thumbRotation = THUMB_ROTATION_PAUSE;
    /**
     * 指针旋转动画播放的时间
     */
    private static final long THUMB_DURATION = 300;
    /**
     * 指针宽度和屏幕1080宽度的比值
     */
    private static final float THUMB_WIDTH_SCALE = 2F;

    private static final float THUMB_HEIGHT_SCALE = 16F;
    /**
     * 绘制时使用的画笔
     */
    private Paint paint;
    /**
     * 指针上面的那条线
     */
    private Drawable cdThumbLine;
    /**
     * 音乐播放时指针移动的动画
     */
    private ValueAnimator playThumbAnimator;
    /**
     * 音乐暂停时指针移动的动画
     */
    private ValueAnimator pauseThumbAnimator;
    /**
     * 指针头部白色小圆点的宽度，dp形式
     */
    private static final int THUMB_CIRCLE_WIDTH = 33;
    /**
     * 指针的高度，px形式
     */
    private static final int THUMB_HEIGHT = 200;
    /**
     * 指针的宽度，px形式
     */
    private static final int THUMB_WIDTH = 230;
    /**
     * 绘制指针的坐标
     */
    private Point thumbPoint;
    /**
     * 绘制指针旋转的坐标
     */
    private Point thumbRotationPoint;
    /**
     * 指针的Bitmap
     */
    private Bitmap cdThumb;
    /**
     * 指针旋转使用的矩阵
     */
    private Matrix thumbMatrix = new Matrix();
    /**
     * 包裹黑胶唱片的白圈
     */
    private Drawable cdBg;


    public RecordThumbView(Context context) {
        super(context);
        init();
    }

    public RecordThumbView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecordThumbView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RecordThumbView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }



    public void init(){
        //画笔
        paint = new Paint();
        paint.setAntiAlias(true);

        //初始化指针上面线
        cdThumbLine = getResources().getDrawable(R.drawable.shape_cd_thumb_line);
        //初始化包裹黑胶唱片CD的白圈
        cdBg = getResources().getDrawable(R.drawable.shape_cd_bg);

        //创建音乐播放指针属性动画
        playThumbAnimator = ValueAnimator.ofFloat(THUMB_ROTATION_PAUSE, THUMB_ROTATION_PLAY);
        //设置指针移动动画时长
        playThumbAnimator.setDuration(THUMB_DURATION);
        //设置监听器
        playThumbAnimator.addUpdateListener(this);

        //创建音乐暂停指针属性动画
        pauseThumbAnimator = ValueAnimator.ofFloat(THUMB_ROTATION_PLAY, THUMB_ROTATION_PAUSE);
        //设置指针移动动画时长
        pauseThumbAnimator.setDuration(THUMB_DURATION);
        //设置监听器
        pauseThumbAnimator.addUpdateListener(this);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取测量的页面宽度
        int measureWidth = getMeasuredWidth();
        //页面宽度的一半
        int widthHalf = measureWidth / 2;

        //白圈的宽度
        int cdBgWidth = (int)(measureWidth / CD_BG_SCALE);
        //白圈的半径
        int cdBgWidthHalf = cdBgWidth / 2;
        //白圈左边到页面左边的距离
        int cdBgLeft = widthHalf - cdBgWidthHalf;
        //白圈顶部到页面顶部的距离
        int cdBgTop = DensityUtil.dipToPx(getContext(), measureWidth / CD_BG_TOP_SCALE);
        //设置白圈的坐标位置
        cdBg.setBounds(cdBgLeft, cdBgTop, cdBgLeft + cdBgWidth, cdBgTop + cdBgWidth);


        //为指针上面线设置位置，将dp转换为px
        cdThumbLine.setBounds(0, 0, measureWidth, DensityUtil.dipToPx(getContext(), CD_THUMB_LINE_HEIGHT));

        //指针头部白色小圆点的宽度，px形式
        int topCircleWidth = DensityUtil.dipToPx(getContext(), THUMB_CIRCLE_WIDTH);

        //绘制指针的坐标，x:屏幕宽度一半 - 小圆点半径， y:-小圆点半径
        thumbPoint = new Point(measureWidth/2 - topCircleWidth/2, -topCircleWidth/2);
        //绘制指针旋转的坐标，x:屏幕宽度一半， y:0
        thumbRotationPoint = new Point(measureWidth/2, 0);

        if (cdThumb == null){
            initBitMap();
        }
    }

    public void initBitMap(){
        //获取BitMap，需要用到View宽度，所以要在onMeasure方法中调用
        int measureWidth = getMeasuredWidth();
        //指针高度
        int imageHeight = (int) (measureWidth / THUMB_WIDTH_SCALE);
        double scale = imageHeight * 1.0 / DensityUtil.dipToPx(getContext(), THUMB_HEIGHT);
        //指针宽度
        int imageWidth = (int)(scale * DensityUtil.dipToPx(getContext(), THUMB_WIDTH));

        //获取到的Bitmap可能比需要的大，要进行调整
        cdThumb = ImageUtil.scaleBitmap(getResources(), R.drawable.cd_thumb, imageWidth, imageHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        //可以通过SurfaceView来实现局部绘制
        //因为旋转指针时，背景和上面那条线不用在重新绘制了
        //但View不行，因为每次View都是一个全新的Canvas
        //绘制线
        cdThumbLine.draw(canvas);

        //绘制白圈背景
        cdBg.draw(canvas);

        //绘制指针
        thumbMatrix.setRotate(thumbRotation, thumbRotationPoint.x, thumbRotationPoint.y);
        thumbMatrix.preTranslate(thumbPoint.x, thumbPoint.y);
        canvas.drawBitmap(cdThumb, thumbMatrix, paint);

        canvas.restore();
    }

    /**
     * 指针动画监听器
     * @param animation
     */
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        thumbRotation = (float) animation.getAnimatedValue();
        invalidate();
    }

    /**
     * 音乐暂停指针动画
     */
    public void stopThumbAnimation(){
        pauseThumbAnimator.start();
    }

    /**
     * 音乐播放指针动画
     */
    public void startThumbAnimation(){
        playThumbAnimator.start();
    }


}

























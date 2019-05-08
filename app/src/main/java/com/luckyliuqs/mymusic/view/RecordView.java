package com.luckyliuqs.mymusic.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.DensityUtil;
import com.luckyliuqs.mymusic.Util.ImageUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义View:黑胶唱片CD页面
 */
public class RecordView extends View {
    /**
     * 黑胶唱片CD宽高比例
     */
    private static final float CD_SCALE = 1.367F;
    /**
     * 歌曲封面比例
     */
    private static final float ALBUM_Scale = 2.1F;
    /**
     * 每16毫秒旋转的角度：大约25秒旋转一圈
     */
    private static final float ROTATION_PER = 0.2304F;
    /**
     * 画笔
     */
    private Paint paint;
    /**
     * 黑胶唱片CD的bitmap
     */
    private Bitmap cd;
    /**
     * 绘制黑胶唱片CD坐标
     */
    private Point cdPoint =  new Point();
    /**
     * 绘制黑胶唱片旋转点坐标，都是在中点，一个就即可
     */
    private Point cdRotationPoint = new Point();
    /**
     * 歌曲封面的宽度
     */
    private int albumWidth;
    /**
     * 歌曲封面绘制坐标
     */
    private Point albumPoint = new Point();
    /**
     * 歌曲封面图片uri
     */
    private String albumUri;
    /**
     * 歌曲封面Bitmap
     */
    private Bitmap album;
    /**
     * 黑胶唱片CD矩阵
     */
    private Matrix cdMatrix = new Matrix();
    /**
     * 歌曲封面矩阵
     */
    private Matrix albumMatrinx = new Matrix();
    /**
     * 旋转的角度
     */
    private float cdRotation = 0;
    /**
     * 计时器任务
     */
    private TimerTask timerTask;
    /**
     * 计算器，用于调度唱片CD，专辑封面转动
     */
    private Timer timer;


    public RecordView(Context context) {
        super(context);
    }

    public RecordView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RecordView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(){
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //页面宽度
        int measureWidth = getMeasuredWidth();
        //页面宽度的一半
        int widthHalf = measureWidth / 2;
        //初始化黑胶唱片bitmap
        initResource();

        //黑胶唱片外面的包裹的白圈
        //白圈宽度
        int cdBgWidth = (int)(measureWidth / RecordThumbView.CD_BG_SCALE);
        //白圈半径
        int cdBgWidthHalf = cdBgWidth / 2;
        //白圈顶部圈边距离页面顶部距离
        int cdBgTop = DensityUtil.dipToPx(getContext(), measureWidth / RecordThumbView.CD_BG_TOP_SCALE);
        //白圈中心点距离页面顶部的距离
        int cdBgCenterY = cdBgTop + cdBgWidthHalf;

        //黑胶唱片CD的半径
        int cdWidthHalf = cd.getWidth() / 2;
        //黑胶唱片CD距离页面左边距离
        int cdLeft = widthHalf - cdWidthHalf;
        //黑胶唱片顶部到页面顶部距离
        int cdTop = cdBgCenterY - cdWidthHalf;
        //绘制黑胶唱片CD坐标
        cdPoint.set(cdLeft, cdTop);
        //设置黑胶唱片CD旋转点
        cdRotationPoint.set(widthHalf, cdWidthHalf + cdTop);

        //歌曲封面宽度
        albumWidth = (int)(measureWidth / ALBUM_Scale);
        //歌曲封面半径
        int albumWidthHalf = albumWidth / 2;
        //歌曲封面左边距离页面左边的距离
        int albumLeft = widthHalf - albumWidthHalf;
        //歌曲封面顶部距离页面顶部的距离
        int albumTop = cdBgCenterY - albumWidthHalf;
        //绘制歌曲封面坐标
        albumPoint.set(albumLeft, albumTop);


        //展示歌曲封面
        showAlbum();
    }

    /**
     * 设置封面uri
     * @param uri
     */
    public void setAlbumUri(String uri){
        this.albumUri = uri;
        showAlbum();
    }

    /**
     * 展示封面
     */
    private void showAlbum(){
        if (albumWidth != 0){
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            options.circleCrop();
            //options.diskCacheStrategy(DiskCacheStrategy.NONE);
            options.override(albumWidth, albumWidth);

            Glide.with(this).asBitmap().load(ImageUtil.getImageURI(this.albumUri)).apply(options).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    album = ImageUtil.resizeImage(resource, albumWidth, albumWidth);
                    invalidate();
                }
            });

        }
    }

    /**
     * 初始化黑胶唱片bitmap
     */
    private void initResource() {
        if (cd == null){
            int cdWidth = (int)(getMeasuredWidth() / CD_SCALE);
            cd = ImageUtil.scaleBitmap(getResources(), R.drawable.cd_bg, cdWidth, cdWidth);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        //绘制黑胶唱片CD
        cdMatrix.setRotate(cdRotation, cdRotationPoint.x, cdRotationPoint.y);
        cdMatrix.preTranslate(cdPoint.x, cdPoint.y);
        canvas.drawBitmap(cd, cdMatrix, paint);

        //绘制歌曲封面
        if (album != null){
            albumMatrinx.setRotate(cdRotation, cdRotationPoint.x, cdRotationPoint.y);
            albumMatrinx.preTranslate(albumPoint.x, albumPoint.y);
            canvas.drawBitmap(album, albumMatrinx, paint);
        }

        canvas.restore();
    }

    /**
     * 封面开始旋转
     */
    public void startAlbumRotate(){
        cancleTask();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if(cdRotation >= 360){
                    cdRotation = 0;
                }
                cdRotation += ROTATION_PER;
                //
                postInvalidate();
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 16);
    }

    /**
     * 封面停止旋转
     */
    public void stopAlbumRotate(){
        cancleTask();
    }

    private void cancleTask(){
        if (timerTask != null){
            timerTask.cancel();
            timerTask = null;
        }

        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }

}

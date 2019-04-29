package com.luckyliuqs.mymusic.Util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.luckyliuqs.mymusic.R;

import org.apache.commons.lang3.StringUtils;

public class ImageUtil {
    /**
     *通过图片名称获取到网络图片，将其展示到ImageView控件上
     * @param activity
     * @param view
     * @param name
     */
    public static void show(Activity activity, ImageView view, String name) {
        if (StringUtils.isBlank(name)) {
            //如果name为空,则默认显示指定图片
            view.setImageResource(R.drawable.cd_bg);
        } else {
            ////如果name不为空,则从网上获取图片将其展示到ImageView控件上
            RequestOptions options = getCommentRequestOptions();
            Glide.with(activity).load(getImageURI(name)).apply(options).into(view);
        }

    }

    /**
     * 通过路径获取到本地图片，将其圆角化展示到ImageView控件上
     * @param activity
     * @param view
     * @param imagePath
     */
    public static void showCircleUri(Activity activity, ImageView view, String imagePath) {
        showCircle(activity,view,getImageURI(imagePath));
    }

    /**
     * 通过路径获取到本地图片，将其圆角化展示到ImageView控件上
     * @param activity
     * @param view
     * @param imagePath
     */
    public static void showCircle(Activity activity, ImageView view, String imagePath) {
        RequestOptions options = getCommentRequestOptions();
        options.circleCrop();
        Glide.with(activity).load(imagePath).apply(options).into(view);
    }

    /**
     * 通过路径获取到本地图片，将其圆角化展示到ImageView控件上
     * @param context
     * @param view
     * @param imagePath
     */
    public static void showCircle(Context context, ImageView view, String imagePath) {
        RequestOptions options = getCommentRequestOptions();
        options.circleCrop();
        Glide.with(context).load(imagePath).apply(options).into(view);
    }

    /**
     * 通过ID获取到本地图片，将其圆角化展示到ImageView控件上
     * @param activity
     * @param view
     * @param imageId
     */
    public static void showCircle(Activity activity, ImageView view, int imageId) {
        RequestOptions options = getCommentRequestOptions();
        options.circleCrop();
        Glide.with(activity).load(imageId).apply(options).into(view);
    }

    /**
     * 通过ID获取到本地图片，展示到ImageView控件上
     * @param activity
     * @param view
     * @param imageId
     */
    public static void showLocalImage(Activity activity, ImageView view, int imageId) {
        RequestOptions options = getCommentRequestOptions();
        Glide.with(activity).load(imageId).apply(options).into(view);
    }

    /**
     * 通过路径获取到本地图片，展示到ImageView控件上
     * @param activity
     * @param view
     * @param imagePath
     */
    public static void showLocalImage(Activity activity, ImageView view, String imagePath) {
        RequestOptions options = getCommentRequestOptions();
        Glide.with(activity).load(imagePath).apply(options).into(view);
    }

    /**
     * 通过路径获取到本地图片，展示到ImageView控件上
     * @param context
     * @param view
     * @param imagePath
     */
    public static void showLocalImage(Context context, ImageView view, String imagePath) {
        RequestOptions options = getCommentRequestOptions();
        Glide.with(context).load(imagePath).apply(options).into(view);
    }

    /**
     * 聊天界面用
     * @param context
     * @param view
     * @param imagePath
     */
    public static void showImage(Context context, ImageView view, String imagePath) {
        RequestOptions options = new RequestOptions();
        //options.placeholder(R.drawable.cd_bg);
        //options.error(R.drawable.cd_bg);

        Glide.with(context).load(imagePath).apply(options).into(view);
    }

    public static RequestOptions getCommentRequestOptions() {
        RequestOptions options = new RequestOptions();
        //设置占位图片
        options.placeholder(R.drawable.cd_bg);
        options.error(R.drawable.cd_bg);
        options.centerCrop();

        //测试，禁用所有缓存
        //options.diskCacheStrategy(DiskCacheStrategy.NONE);
        return options;
    }

    public static String getImageURI(String name) {
        return String.format(Consts.RESOURCE_PREFIX,name);
    }

    //public static void showImageBlur(Activity activity, ImageView view, String name) {
    //    RequestOptions requestOptions = bitmapTransform(new BlurTransformation(50, 5));
    //    requestOptions.placeholder(R.drawable.default_album);
    //    requestOptions.error(R.drawable.default_album);
    //    Glide.with(activity).load(getImageURI(name)).apply(requestOptions).into(view);
    //}
    //
    //public static void showImageBlur(Activity activity, ImageView view, int imageId) {
    //    RequestOptions requestOptions = bitmapTransform(new BlurTransformation(50, 5));
    //    Glide.with(activity).load(imageId).apply(requestOptions).into(view);
    //}


    public static Bitmap scaleBitmap(Resources resource, int resourceId, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds=true;

        BitmapFactory.decodeResource(resource,resourceId,options);

        options.inSampleSize=computerSampleSize(options,width,height);

        options.inJustDecodeBounds=false;
        Bitmap bitmap = BitmapFactory.decodeResource(resource, resourceId, options);

        //使用inSampleSize可以一次不能缩放到指定大小，所以可以还需要再次缩放
        int currentWidth = bitmap.getWidth();
        if (currentWidth > width) {
            //继续缩放
            Bitmap newBitmap = ImageUtil.resizeImage(bitmap, width, height);

            bitmap.recycle();
            return newBitmap;
        }

        return bitmap;
    }

    private static int computerSampleSize(BitmapFactory.Options options, int width, int height) {
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;

        int inSampleSize=1;

        if (outWidth>width||outHeight>height) {
            int currentWidth = width / 2;
            int currentHeight = height / 2;

            while ((currentWidth/inSampleSize)>width && (currentHeight/inSampleSize)>height) {
                inSampleSize*=2;
            }
        }

        return inSampleSize;
    }

    /**
     * 将图片放大或缩小到指定尺寸
     */
    public static Bitmap resizeImage(Bitmap source, int w, int h) {
        int width = source.getWidth();
        int height = source.getHeight();
        float scaleWidth = ((float) w) / width;
        float scaleHeight = ((float) h) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(source, 0, 0, width, height, matrix, true);
    }
}

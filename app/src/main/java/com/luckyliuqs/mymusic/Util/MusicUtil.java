package com.luckyliuqs.mymusic.Util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

/**
 * music工具类
 */
public class MusicUtil {

    /**
     * 根据传入的歌曲专辑id，获取封面
     * @param context
     * @param album_id
     * @return
     */
    public static String getAlbumBanners(Context context, String album_id){
        Cursor cursor = null;
        try{
            //String mUriAlbums = "content://media/external/audio/albums";
            String[] projection = new String[]{"album_art"};
            cursor = context.getContentResolver().query(
                    Uri.parse(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI + "/" + album_id),
                    projection,
                    null,
                    null,
                    null
            );
            if (cursor.getCount() > 0 && cursor.getColumnCount() > 0){
                cursor.moveToNext();
                //Log.i("MusicUtil", "banner:"+cursor.getString(0));
                return cursor.getString(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
                cursor = null;
            }
        }

        return null;
    }

    /**
     * 根据专辑ID获取专辑封面图
     * @param album_id 专辑ID
     * @return
     */
    public static String getAlbumBanner(Context context,String album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cur = context.getContentResolver().query(Uri.parse(mUriAlbums + "/" + album_id), projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        String path = null;
        if (album_art != null) {
            path = album_art;
        } else {
            //path = "drawable/music_no_icon.png";
            //bm = BitmapFactory.decodeResource(getResources(), R.drawable.default_cover);
        }
        return path;
    }

    public static Bitmap getMusicBanner(String url){
        MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(url);
        if(mediaMetadataRetriever.getEmbeddedPicture() == null){
            return null;
        }
        byte[] picture = mediaMetadataRetriever.getEmbeddedPicture();
        return BitmapFactory.decodeByteArray(picture,0,picture.length);
    }

}

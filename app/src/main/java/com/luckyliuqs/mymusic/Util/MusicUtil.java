package com.luckyliuqs.mymusic.Util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * music工具类
 */
public class MusicUtil {

    /**
     * 根据传入的歌曲封面id，获取封面
     * @param context
     * @param album_id
     * @return
     */
    public static String getAlbumBanner(Context context, String album_id){
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
}

package com.luckyliuqs.mymusic.Util;

import android.os.Environment;

import java.io.File;

public class StorageUtil {
    public static final String MP3 = ".mp3";

    /**
     * 返回下载歌曲储存目录地址
     * @param title  文件名
     * @param suffix 文件后缀
     * @return  文件储存目的地址
     */
    public static String getExternalPath(String title, String suffix){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), String.format("/MyMusic/%s%s", title, suffix));
        if (!file.getParentFile().exists()){
            //如果ParentFile不存在，则创建
            file.getParentFile().mkdir();
        }

        return file.getAbsolutePath();
    }
}

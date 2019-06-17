package com.luckyliuqs.mymusic.Util;

public class FIleUtil {

    /**
     * 文件大小格式化输出：以byte, k, m, G, T为单位
     * @param size
     * @return
     */
    public static String formatFileSize(long size){
        String sFileSize = "";
        if (size > 0){
            double dFileSize = (double)size;
            double kiloByte = dFileSize / 1024;

            if (kiloByte < 1 && kiloByte > 0){
                //文件小于1k，返回以byte为单位
                return size + "Byte";
            }

            double megaByte = kiloByte / 1024;
            if (megaByte < 1){
                //如果小于1M，返回以k为单位
                sFileSize = String.format("%.2f", kiloByte);
                return sFileSize + "K";
            }

            double gigaByte = megaByte / 1024;
            if (gigaByte < 1){
                //如果小于1G，返回以M为单位
                sFileSize = String.format("%.2f", megaByte);
                return sFileSize + "M";
            }

            double teraByte = megaByte / 1024;
            if (teraByte < 1){
                //如果小于1T，返回以G为单位
                sFileSize = String.format("%.2f", gigaByte);
                return sFileSize + "G";
            }

            //返回以为单位
            sFileSize = String.format("%.2f", teraByte);
            return sFileSize + "T";
        }

        return "OK";
    }
}

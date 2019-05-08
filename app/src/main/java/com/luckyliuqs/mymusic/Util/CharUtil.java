package com.luckyliuqs.mymusic.Util;

public class CharUtil {

    /**
     * 判断字符是否为中文，中文字符标点都可以判断
     * @param c
     * @return boolean
     */
    public static boolean isChinese(char c){
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                        || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                        || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                        || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                        || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                        || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                        || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS){
            return true;
        }
        return false;
    }

    /**
     * 判断字符是否为字母
     * @param c
     * @return boolean
     */
    public static boolean isWord(char c){
        if (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')){
            return true;
        }
        return false;
    }
}

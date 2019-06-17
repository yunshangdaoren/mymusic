package com.luckyliuqs.mymusic.Util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.domain.Tag;
import com.luckyliuqs.mymusic.listener.OnTagClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagUtil {
    /**
     * 匹配以@开头，以\s或者:结尾的正则表达式，？表示禁用贪婪模式
     * 用于评论@用户
     */
    private static final String REG_MENTION = "(@.*?)[\\s|:]";

    /**
     * 用于表示匹配一个话题：#话题#
     */
    private static final String REG_HASH_TAG = "(#.*?#)\\s?";

    /**
     * 从text中匹配出Tag
     * @param text
     * @return
     */
    public static List<Tag> findMentionAndHashTag(String text){
        ArrayList<Tag> strings = new ArrayList<>();
        //匹配@
        Pattern mention = Pattern.compile(REG_MENTION);
        Matcher mentionMatcher = mention.matcher(text);
        while (mentionMatcher.find()){
            strings.add(new Tag(mentionMatcher.group(0).trim(), mentionMatcher.start()));
        }

        //匹配#话题
        Pattern hashTag = Pattern.compile(REG_HASH_TAG);
        Matcher hashTagMatcher = hashTag.matcher(text);
        while (hashTagMatcher.find()){
            strings.add(new Tag(hashTagMatcher.group(0).trim(), hashTagMatcher.start()));
        }

        return strings;
    }

    /**
     * 对Tag添加点击事件
     * @param context
     * @param text
     * @param onTagClickListener
     * @return
     */
    public static CharSequence process(Context context, String text, final OnTagClickListener onTagClickListener){
        List<Tag> mentionAndHashTag = TagUtil.findMentionAndHashTag(text);
        SpannableString spanString = new SpannableString(text);

        for (Tag tag : mentionAndHashTag) {
            //高亮文本
            //ForegroundColorSpan span = new ForegroundColorSpan(context.getResources().getColor(R.color.text_Highlight));
            int start = tag.getStart();
            final String content = tag.getContent();
            int end = tag.getStart() + content.length();
            //spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            //点击事件
            spanString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    onTagClickListener.onTagClick(content);
                }
            }, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }

        return spanString;
    }

    /**
     * 对Tag高亮
     * @param context
     * @param text
     * @return
     */
    public static CharSequence processHightLight(Context context, String text){
        List<Tag> mentionAndHashTag = TagUtil.findMentionAndHashTag(text);
        SpannableString spanString = new SpannableString(text);

        for (Tag tag : mentionAndHashTag) {
            //高亮文本
            ForegroundColorSpan span = new ForegroundColorSpan(context.getResources().getColor(R.color.text_Highlight));
            int start = tag.getStart();
            final String content = tag.getContent();
            int end = tag.getStart() + content.length();
            spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        return spanString;
    }

    /**
     * 删除Tag
     * @param text
     * @return
     */
    public static String removeTag(String text){
        return text.replace("#", "").replace("@", "");
    }

}





























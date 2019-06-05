package com.luckyliuqs.mymusic.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import com.luckyliuqs.mymusic.R;

/**
 * 本地歌曲排序方式dialog对话框
 * 注意引入的是v4和v7包
 */
public class MusicSortDialogFragment extends DialogFragment {
    /**
     * 排序方式
     */
    private static final String[] items = {"默认", "单曲名", "专辑名"};

    private DialogInterface.OnClickListener onClickListener;

    /**
     * 选择排序方式的下标
     */
    private int selectIndex;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //填充标题，数据和监听器
        builder.setTitle(R.string.select_song_sort).setSingleChoiceItems(items, selectIndex, onClickListener);
        return builder.create();
    }

    public void show(FragmentManager fragmentManager, int selectIndex, DialogInterface.OnClickListener onClickListener){
        this.selectIndex = selectIndex;
        this.onClickListener = onClickListener;

        //显示对话框
        show(fragmentManager, "SortDialogFragment");

    }
}

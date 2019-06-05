package com.luckyliuqs.mymusic.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.widget.EditText;
import android.widget.Toast;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.ToastUtil;

import org.apache.commons.lang3.StringUtils;

/**
 * 创建歌单Dialog Fragment
 */
public class CreateSongListDialogFragment extends DialogFragment {
    private OnConfirmCreateSongListListener onConfirmCreateSongListListener;
    private EditText et;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        et = new EditText(getActivity());
        et.setHint(R.string.enter_song_list_name);
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(getActivity());

        //设置标题
        inputDialog.setTitle(R.string.new_song_list).setView(et);
        inputDialog.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String text = et.getText().toString();
                if (StringUtils.isNotBlank(text)){
                    dialogInterface.dismiss();
                    onConfirmCreateSongListListener.onConfirmCreateSongListClick(text);
                }else{
                    ToastUtil.showSortToast(getActivity(), R.string.enter_song_list_name);
                }
            }
        });

        return inputDialog.create();
    }

    private void initData(){

    }

    private void initListener(){

    }

    public static void show(FragmentManager fragmentManager, OnConfirmCreateSongListListener onConfirmCreateSongListListener){
        CreateSongListDialogFragment createSongListDialogFragment = new CreateSongListDialogFragment();
        createSongListDialogFragment.setListener(onConfirmCreateSongListListener);
        createSongListDialogFragment.show(fragmentManager, "CreateSongListDialogFragment");
    }

    private void setListener(OnConfirmCreateSongListListener onConfirmCreateSongListListener){
        this.onConfirmCreateSongListListener = onConfirmCreateSongListListener;
    }

    public interface OnConfirmCreateSongListListener{
        void onConfirmCreateSongListClick(String text);
    }
}

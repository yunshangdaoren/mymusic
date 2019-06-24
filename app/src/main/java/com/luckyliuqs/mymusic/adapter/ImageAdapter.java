package com.luckyliuqs.mymusic.adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.ImageUtil;
import com.luckyliuqs.mymusic.domain.Image;

public class ImageAdapter extends BaseQuickRecyclerViewAdapter<Image> {

    public ImageAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    protected void bindData(ViewHolder holder, int position, Image image) {
        ImageView iv_icon = holder.getView(R.id.iv_item_image_icon);
        ImageUtil.show((Activity) context, iv_icon, image.getUri());
    }
}

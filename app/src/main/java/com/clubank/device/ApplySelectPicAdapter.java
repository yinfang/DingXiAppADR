package com.clubank.device;

import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.clubank.dingxi.R;
import com.clubank.util.GlideUtil;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

/**
 * 活动申请 上传图片资料
 */

public class ApplySelectPicAdapter extends BaseAdapter {

    public ApplySelectPicAdapter(BaseActivity a, MyData data) {
        super(a, R.layout.item_add_pic, data);
    }

    @Override
    protected void display(int position, View v, MyRow row) {
        AbsListView.LayoutParams params = (AbsListView.LayoutParams) v.getLayoutParams();
        params.height = a.screen.getWidth() / 4;
        v.setLayoutParams(params);
        if (row.isEmpty()) {
            show(v, R.id.add_ll);
            hide(v, R.id.pic_ll);
            LinearLayout add = v.findViewById(R.id.add_ll);
            add.setTag(position);
        } else {
            hide(v, R.id.add_ll);
            show(v, R.id.pic_ll);
            ImageView delete = v.findViewById(R.id.delete_pic);
            delete.setTag(position);
            setImage(R.id.show_pic, row.getString("Path"));
        }
    }
}

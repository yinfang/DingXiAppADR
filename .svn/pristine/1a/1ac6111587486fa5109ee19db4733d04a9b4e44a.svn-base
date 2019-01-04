package com.clubank.device;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.clubank.dingxi.R;
import com.clubank.util.GlideUtil;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

/**
 * 活动详情 图片资料
 */

public class ApplyDetailPicAdapter extends BaseAdapter {

    public ApplyDetailPicAdapter(BaseActivity a, MyData data) {
        super(a, R.layout.item_show_pic, data);
    }

    @Override
    protected void display(int position, View v, MyRow row) {
        AbsListView.LayoutParams params = (AbsListView.LayoutParams) v.getLayoutParams();
        params.height = a.screen.getWidth() / 4;
        v.setLayoutParams(params);
        setImage(R.id.show_pic, row.getString("Path"));
    }
}

package com.clubank.device;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.clubank.dingxi.R;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

/**
 * 图片选择
 */

public class PicMultiSelectAdapter extends BaseAdapter {

    private boolean single = false;

    public PicMultiSelectAdapter(BaseActivity a, MyData data) {
        super(a, R.layout.pic_multi_select_item, data);
    }

    public void setSingle(boolean single) {
        this.single = single;
    }

    public boolean isSingle() {
        return single;
    }

    @Override
    protected void display(int position, View v, MyRow row) {
        FrameLayout frameItem = (FrameLayout) v.findViewById(R.id.frame_item);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) frameItem.getLayoutParams();
        params.height = a.screen.getWidth() / 4;
        frameItem.setLayoutParams(params);
        setImage(R.id.iv_item, row.getString("path"));
        ImageView ivShow = (ImageView) v.findViewById(R.id.iv_show);
        if (single) {
            ivShow.setVisibility(View.GONE);
        } else {
            if (row.getBoolean("check")) {
                ivShow.setImageDrawable(a.getResources().getDrawable(R.mipmap.icon2));
            } else {
                ivShow.setImageDrawable(a.getResources().getDrawable(R.mipmap.icon1));
            }
        }
    }


    public int getSelectedCount() {
        int selected = 0;
        for (int i = 0; i < getCount(); i++) {
            MyRow row = getItem(i);
            if (row != null && row.getBoolean("check")) {
                selected += 1;
            }
        }
        return selected;
    }
}

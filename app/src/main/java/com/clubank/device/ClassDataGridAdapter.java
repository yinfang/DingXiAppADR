package com.clubank.device;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.LinearLayout;

import com.clubank.dingxi.R;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

/**
 * 排课 Grid选中adapter
 * <p>
 * 状态 0.未排 1.可约 2.约课待确认 3.约课已确认 4.签课待确认 5.签课已确认 10.已禁用 99.已过期
 */
public class ClassDataGridAdapter extends BaseAdapter {
    private int height = a.screen.getWidth() / 8;

    public ClassDataGridAdapter(BaseActivity a, MyData data) {
        super(a, R.layout.item_class_data, data);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void display(int position, View v, MyRow row) {
        super.display(position, v, row);
        LinearLayout ll = v.findViewById(R.id.null_grid_bg);
        LinearLayout ll_new = v.findViewById(R.id.booked_grid_bg);
        ll.setLayoutParams(new LinearLayout.LayoutParams(height, height));
        ll_new.setLayoutParams(new LinearLayout.LayoutParams(height, height));
        int status = row.getInt("Status");
        switch (status) {
            case 0://未选
                show(v, R.id.null_grid_bg);
                hide(v, R.id.booked_grid_bg);
                if (row.getBoolean("checked")) {
                    ll.setBackground(a.getResources().getDrawable(R.drawable.solid_bule_rectangle1));
                } else {
                    ll.setBackground(a.getResources().getDrawable(R.drawable.solid_white_rectangle1));
                }
                break;
            case 1://可约
                show(v, R.id.null_grid_bg);
                hide(v, R.id.booked_grid_bg);
                if (row.getBoolean("checked")) {
                    ll.setBackground(a.getResources().getDrawable(R.drawable.solid_bule_rectangle1));
                } else {
                    ll.setBackground(a.getResources().getDrawable(R.drawable.solid_green_rectangle1));
                }
                break;
            case 4://签课待确认
                show(v, R.id.null_grid_bg);
                hide(v, R.id.booked_grid_bg);
                ll.setBackgroundColor(a.getResources().getColor(R.color.class_puple));
                break;
            case 2://2.约课待确认 3.约课已确认
            case 3:
                hide(v, R.id.null_grid_bg);
                show(v, R.id.booked_grid_bg);
                setEText(R.id.name_new, row.getString("CustomerName"));
                break;
            case 99://已过期
                show(v, R.id.null_grid_bg);
                hide(v, R.id.booked_grid_bg);
                ll.setBackground(a.getResources().getDrawable(R.drawable.solid_gray_rectangle1));
                setEText(R.id.name, row.getString("CustomerName"));
                break;
        }
    }

}

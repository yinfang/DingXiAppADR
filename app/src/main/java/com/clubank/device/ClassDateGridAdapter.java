package com.clubank.device;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.LinearLayout;

import com.clubank.dingxi.R;
import com.clubank.domain.C;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

import java.text.ParseException;

/**
 * 排课 日期adapter
 */
public class ClassDateGridAdapter extends BaseAdapter {
    private int height = a.screen.getWidth() / 8;

    public ClassDateGridAdapter(BaseActivity a, MyData data) {
        super(a, R.layout.item_class_date, data);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void display(int position, View v, MyRow row) {
        super.display(position, v, row);
        LinearLayout ll = v.findViewById(R.id.ll);
         ll.setLayoutParams(new LinearLayout.LayoutParams(height, height));
        setEText(R.id.week, row.getString("WeekName"));
        String date = row.getString("Date").substring(5, 10);
        String[] ss = date.split("-");
        setEText(R.id.date, ss[0] + "月" + ss[1] + "日");
    }

}

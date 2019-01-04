package com.clubank.device;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;

import com.clubank.dingxi.R;
import com.clubank.domain.C;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

/**
 * 课程订单Adapter
 */
public class ClassOrderListAdapter extends BaseAdapter {
    public ClassOrderListAdapter(BaseActivity a, MyData data) {
        super(a, R.layout.item_class_order_list, data);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void display(int position, View v, MyRow row) {
        super.display(position, v, row);
        setEText(R.id.tv_order_no, row.getInt("OrderID") + "");
        setEText(R.id.tv_title, row.getString("LessonName"));
        setEText(R.id.tv_amount, getString(R.string.yuan) + row.getInt("Price"));
        setEText(R.id.tv_student, row.getString("ContactsName"));
        setEText(R.id.tv_has_class, row.getString("ConsumeHour") + "节/");
        setEText(R.id.tv_total, row.getString("TotalHour") + "节");

        setEText(R.id.tv_over_time, row.getString("DeadlineTime").replace("T", " ").substring(0, 10));
        setEText(R.id.tv_order_time, row.getString("CreateTime").replace("T", " ").substring(0, 16));
        Button class_record = v.findViewById(R.id.class_record);
        class_record.setTag(position);
    }

}

package com.clubank.device;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.clubank.dingxi.R;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

/**
 * 上课记录Adapter
 */
public class ClassOrderRecordListAdapter extends BaseAdapter {
    public ClassOrderRecordListAdapter(BaseActivity a, MyData data) {
        super(a, R.layout.item_class_order_record_list, data);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void display(int position, View v, MyRow row) {
        super.display(position, v, row);
//        setEText(R.id.tv_title, row.getString("LessonName"));
//        setEText(R.id.tv_status, row.getString("StatusStr"));
        setEText(R.id.tv_class_start, row.getString("SignInTime").replace("T", " ").substring(0, 16));
        if (!a.checkNull(row.getString("Estimation"))) {
            try {
                setEText(R.id.tv_teacher_comment, new String(android.util.Base64.decode(row.getString("Estimation"), android.util.Base64.NO_WRAP)));
            } catch (Exception e) {

            }
        }
    }

}

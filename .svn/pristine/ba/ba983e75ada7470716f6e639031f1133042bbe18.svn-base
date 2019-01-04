package com.clubank.device;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;

import com.clubank.dingxi.R;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

import java.io.UnsupportedEncodingException;

/**
 * 预约记录Adapter
 */
public class BookingRecordListAdapter extends BaseAdapter {
    public BookingRecordListAdapter(BaseActivity a, MyData data) {
        super(a, R.layout.item_booking_record_list, data);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void display(int position, View v, MyRow row) {
        super.display(position, v, row);
        Button cancel_class = v.findViewById(R.id.cancel_class);
        Button sure_class = v.findViewById(R.id.sure_class);

        setEText(R.id.tv_title, row.getString("LessonName"));
        setEText(R.id.tv_status, row.getString("StatusStr"));
        int status = Integer.parseInt(row.getString("Status"));
        switch (status) {//状态0全部 1.未确认 2.已确认 3.已取消 4.已签课
            case 0:
                show(v, R.id.cancel_class);
                show(v, R.id.sure_class);
                break;
            case 1:
                show(v, R.id.cancel_class);
                show(v, R.id.sure_class);
                break;
            case 2:
                show(v, R.id.cancel_class);
                hide(v, R.id.sure_class);
                break;
            case 3:
                hide(v, R.id.cancel_class);
                hide(v, R.id.sure_class);
                break;
            case 4:
                hide(v, R.id.cancel_class);
                hide(v, R.id.sure_class);
                break;

        }
        setEText(R.id.tv_student, row.getString("Name"));
        setEText(R.id.tv_has_class, row.getInt("ConsumeHour") + "节/");
        setEText(R.id.tv_total, row.getInt("TotalHour") + "节");
        setEText(R.id.tv_booking_time, row.getString("AppointTime").replace("T", " ").substring(0, 16));
        if (!a.checkNull(row.getString("Remark"))) {
            try {
                setEText(R.id.tv_remark, new String(android.util.Base64.decode(row.getString("Remark"), android.util.Base64.NO_WRAP)));
            } catch (Exception e) {

            }
        }
        sure_class.setTag(position);
        cancel_class.setTag(position);
    }

}

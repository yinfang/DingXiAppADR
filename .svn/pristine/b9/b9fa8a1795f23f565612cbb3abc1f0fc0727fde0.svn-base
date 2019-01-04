package com.clubank.device;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import com.clubank.dingxi.R;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

public class ApplyRecordListAdapter extends BaseAdapter {
    public ApplyRecordListAdapter(BaseActivity a, MyData data) {
        super(a, R.layout.item_apply_record, data);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void display(int position, View v, MyRow row) {
        super.display(position, v, row);
        setEText(R.id.tv_title, row.getString("ProcessName"));
        if (row.getInt("Remind") == 0) {
            hide(v, R.id.red_msg);
        } else {
            show(v, R.id.red_msg);
        }
        int status = Integer.parseInt(row.getString("Status"));
        TextView statusTv = v.findViewById(R.id.tv_status);
        if (status == 0 || status == 1) {//  0  审批中（新）  1 审批中   2 已批准  3  已否决  4 已取消
            setEText(R.id.tv_status, row.getString("StatusText"));
            statusTv.setTextColor(a.getResources().getColor(R.color.status01));
        } else if (status == 2) {
            setEText(R.id.tv_status, row.getString("StatusText"));
            statusTv.setTextColor(a.getResources().getColor(R.color.status2));
        } else if (status == 3) {
            setEText(R.id.tv_status, row.getString("StatusText"));
            statusTv.setTextColor(a.getResources().getColor(R.color.status3));
        } else if (status == 4) {
            setEText(R.id.tv_status, row.getString("StatusText"));
            statusTv.setTextColor(a.getResources().getColor(R.color.class_puple));
        }
        setEText(R.id.tv_apply_people, row.getString("CreateName"));
        setEText(R.id.tv_apply_time, row.getString("CreateTime").replace("T", " ").substring(0, 16));
        setEText(R.id.tv_apply_place, row.getString("Address"));
    }

}

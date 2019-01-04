package com.clubank.device;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.clubank.dingxi.R;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

/**
 * 活动申请详情 审批流程
 */
public class ApprovesDetailAdapter extends com.clubank.device.BaseAdapter {
    private MyData data;
//    private int[] images = new int[]{R.mipmap.step1, R.mipmap.step2, R.mipmap.step3};

    public ApprovesDetailAdapter(BaseActivity a, MyData da) {
        super(a, R.layout.item_detail_approves, da);
        this.data = da;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void display(int position, View v, MyRow row) {
        setImage(R.id.pic, row.getString("Pic"), R.mipmap.icon_member_input);
        setEText(R.id.name, row.getString("UserName"));
        if (!a.checkNull(row.getString("StepRemarks"))) {
            try {
                setEText(R.id.apply_advice, new String(android.util.Base64.decode(row.getString("StepRemarks"), android.util.Base64.NO_WRAP)));
            } catch (Exception e) {

            }
        }
        int StepStatus = row.getInt("StepStatus");
        TextView statusTv = v.findViewById(R.id.apply_result);
        //StepStatus 0待审批 1 已同意、2打回修改、3 已否决
        //Step	0 申请 1 馆长审批 2 体育局领导审批
        switch (StepStatus) {
            case 0:
                setEText(R.id.apply_result, "待审批");
                statusTv.setTextColor(a.getResources().getColor(R.color.status01));
                break;
            case 1:
                setEText(R.id.apply_result, "已同意");
                statusTv.setTextColor(a.getResources().getColor(R.color.status2));
                break;
            case 2:
                setEText(R.id.apply_result, "打回修改");
                statusTv.setTextColor(a.getResources().getColor(R.color.class_puple));
                break;
            case 3:
                setEText(R.id.apply_result, "已否决");
                statusTv.setTextColor(a.getResources().getColor(R.color.status3));
                break;
        }
        if (position == data.size() - 1) {
            hide(v, R.id.next);
        } else {
            show(v, R.id.next);
        }
    }
}
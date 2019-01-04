package com.clubank.device;

import android.view.View;

import com.clubank.dingxi.R;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;

/**
 * 首页权限列表
 */
public class MainListAdapter extends BaseAdapter {
    public MainListAdapter(BaseActivity a, MyData data) {
        super(a, R.layout.item_main_list, data);
    }

    @Override
    protected void display(int position, View v, MyRow row) {
        super.display(position, v, row);
        int reminds = row.getInt("reminds");
        if (reminds > 0) {
            show(v, R.id.read_msg_ll);
//            setEText(R.id.msg_num,reminds+"");
        } else {
            hide(v, R.id.read_msg_ll);
        }
        setImage(R.id.icon, row.getString("ImgUrl"), true);
        setEText(R.id.title, row.getString("Name"));
        setEText(R.id.desc, row.getString("Remarks"));
    }
}

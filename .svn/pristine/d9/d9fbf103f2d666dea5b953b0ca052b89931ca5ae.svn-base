package com.clubank.device;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.clubank.device.op.ProcessList;
import com.clubank.dingxi.R;
import com.clubank.domain.BC;
import com.clubank.domain.BRT;
import com.clubank.domain.Result;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.UI;

/**
 * 活动申请-----申请记录 列表
 */
public class ApplyRecordListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ApplyRecordListAdapter adapter;
    private MyData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_record_list);
        data = new MyData();
        ListView list = findViewById(R.id.list);
        adapter = new ApplyRecordListAdapter(this, data);
        addEmptyView(list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        refreshData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData(true, ProcessList.class);
    }

    @Override
    public void refreshData() {
        super.refreshData();
        new MyAsyncTask(this, ProcessList.class).run();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MyRow row = (MyRow) parent.getItemAtPosition(position);
        Bundle b = new Bundle();
        b.putString("processid", row.getString("ProcessID"));
        b.putInt("from", 1);
        openIntent(ApplyDetailActivity.class, R.string.apply_detail, b);
    }

    @Override
    public void onPostExecute(Class<?> op, Result result) {
        super.onPostExecute(op, result);
        if (op == ProcessList.class) {
            if (result.code == BC.SUCCESS) {
                MyData data = (MyData) result.obj;
                if (null != adapter) {
                    adapter.clear();
                }
                if (null != data && data.size() > 0) {
                    for (MyRow ro : data) {
                        adapter.add(ro);
                    }
                    adapter.notifyDataSetChanged();
                    return;
                }
                show(R.id.emptyview);
            } else {
                UI.showToast(this, result.msg);
            }
        }
    }

}

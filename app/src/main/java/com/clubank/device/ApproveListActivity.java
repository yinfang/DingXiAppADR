package com.clubank.device;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.clubank.device.op.ProcessApplyList;
import com.clubank.dingxi.R;
import com.clubank.domain.BC;
import com.clubank.domain.Result;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.UI;

/**
 * 活动审批 列表
 */
public class ApproveListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ApproveListAdapter adapter;
    private MyData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_record_list);
        data = new MyData();
        ListView list = findViewById(R.id.list);
        adapter = new ApproveListAdapter(this, data);
        addEmptyView(list);
        list.setOnItemClickListener(this);
        list.setAdapter(adapter);
        refreshData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public void refreshData() {
        super.refreshData();
        new MyAsyncTask(this, ProcessApplyList.class).run();
    }

    @Override
    public void onPostExecute(Class<?> op, Result result) {
        super.onPostExecute(op, result);
        if (op == ProcessApplyList.class) {
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

    public void doWork(View view) {
        switch (view.getId()) {
            case R.id.see_detail://查看详情
                int pos = (int) view.getTag();
                MyRow row = data.get(pos);
                Bundle b = new Bundle();
                b.putString("processid", row.getString("ProcessID"));
                b.putInt("from", 2);
                openIntent(ApplyDetailActivity.class, R.string.apply_detail, b);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MyRow row = (MyRow) adapterView.getItemAtPosition(i);
        Bundle b = new Bundle();
        b.putString("processid", row.getString("ProcessID"));
        b.putInt("from", 2);
        openIntent(ApplyDetailActivity.class, R.string.apply_detail, b);
    }
}

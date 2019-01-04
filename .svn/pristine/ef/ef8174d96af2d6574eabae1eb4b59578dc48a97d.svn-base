package com.clubank.device;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.clubank.device.op.LessonOrders;
import com.clubank.device.op.ProcessList;
import com.clubank.dingxi.R;
import com.clubank.domain.BC;
import com.clubank.domain.Result;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.UI;

/**
 * 选择学员课程
 */
public class ChooseClassListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView list;
    private ChooseClassListAdapter adapter;
    private MyData data;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_record_list);
        Bundle b = getIntent().getExtras();
        if (null != b) {
            date = b.getString("date");
        }
        data = new MyData();
        list = findViewById(R.id.list);
        adapter = new ChooseClassListAdapter(this, data);
        addEmptyView(list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        refreshData();
    }

    @Override
    public void refreshData() {
        super.refreshData();
        new MyAsyncTask(this, LessonOrders.class).run(date);
    }

    @Override
    public void onPostExecute(Class<?> op, Result result) {
        super.onPostExecute(op, result);
        if (op == LessonOrders.class) {
            if (result.code == BC.SUCCESS) {
                data = (MyData) result.obj;
                if (null != data && data.size() > 0) {
                    for (MyRow ro : data) {
                        ro.put("checked", false);
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MyRow row = (MyRow) adapterView.getItemAtPosition(i);
        row.put("checked", true);
        adapter.notifyDataSetChanged();

        Intent in = new Intent();
        Bundle b = new Bundle();
        b.putSerializable("row", row);
        in.putExtras(b);
        setResult(RESULT_OK, in);
        finish();
    }
}

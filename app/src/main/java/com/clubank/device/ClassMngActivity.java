package com.clubank.device;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.clubank.view.AlertDialog;
import com.clubank.View.MyGridList;
import com.clubank.device.op.LessonStatus;
import com.clubank.device.op.LessonTime;
import com.clubank.dingxi.R;
import com.clubank.domain.BC;
import com.clubank.domain.MainTabEntity;
import com.clubank.domain.Result;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.UI;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

/**
 * 排课管理
 */
public class ClassMngActivity extends BaseActivity implements AdapterView.OnItemClickListener, OnTabSelectListener {
    private String from;
    private Bundle b;
    private Button btn_yue, btn_pai, btn_sure;
    private CommonTabLayout commonTabLayout;
    private ArrayList<CustomTabEntity> tabs = new ArrayList<>();
    private GridView dateGrid;
    private MyGridList timeGrid, dataGrid;
    private ClassDateGridAdapter dateAdapter;
    private ClassTimeGridAdapter timeAdapter;
    private ClassDataGridAdapter gridAdapter;
    private MyData dateData, timeData, gridData, selectedDates;
    private int week = 0, height;//-1代表上一周，0 本周，1代表下一周
    private boolean isCopy = false, isBeforeWeek = false, isNextWeek = false;//是否点击了复制上周，切换上周,是否点击了下一周

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_manage);
        tabs.add(new MainTabEntity("〈 上周", R.mipmap.ic_launcher, R.mipmap.ic_launcher));
        tabs.add(new MainTabEntity("本周", R.mipmap.ic_launcher, R.mipmap.ic_launcher));
        tabs.add(new MainTabEntity("下周 〉", R.mipmap.ic_launcher, R.mipmap.ic_launcher));
        commonTabLayout = findViewById(R.id.tablayout);
        commonTabLayout.setTabData(tabs);
        commonTabLayout.setOnTabSelectListener(this);
        commonTabLayout.setCurrentTab(1);
        btn_yue = findViewById(R.id.btn_yue);//可约
        btn_pai = findViewById(R.id.btn_pai);//未排
        btn_sure = findViewById(R.id.btn_sure);//确认时间

        b = getIntent().getExtras();
        from = b.getString("from");
        if (from.equals("AppPrivielge_05")) {//排课管理
            show(R.id.manage_ll);
        } else {//课程预约
            show(R.id.btn_sure);
        }
//        setEText(R.id.year, C.df_y.format(new Date()));
        dateData = new MyData();
        timeData = new MyData();
        gridData = new MyData();
        selectedDates = new MyData();
        height = screen.getWidth() / 8;
        TextView year = findViewById(R.id.year);
        TextPaint textPaint = year.getPaint();
        //单个文字的高度
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        int textheight = (int) (fontMetrics.bottom - fontMetrics.top) / 2;

        LinearLayout time_ll = findViewById(R.id.time_ll);
        time_ll.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height + textheight));
        LinearLayout yearll = findViewById(R.id.left_top_ll);
        yearll.setLayoutParams(new LinearLayout.LayoutParams(height, height - textheight));
        ScrollView scroll = findViewById(R.id.scroll);
        scroll.setPadding(0, height - textheight, 0, 0);
        dateGrid = findViewById(R.id.date_grid);
        timeGrid = findViewById(R.id.time_grid);
        timeGrid.setPadding(0, 0, 0, textheight);
        timeGrid.setLayoutParams(new LinearLayout.LayoutParams(height, ViewGroup.LayoutParams.WRAP_CONTENT));
        dataGrid = findViewById(R.id.data_grid);
        dataGrid.setOnItemClickListener(this);
        ViewGroup.MarginLayoutParams mag = (ViewGroup.MarginLayoutParams) dataGrid.getLayoutParams();
        mag.topMargin = textheight;
        refreshData();
    }

    @Override
    public void refreshData() {
        super.refreshData();
        new MyAsyncTask(this, LessonTime.class).run(week);
    }

    public void setAdapters() {
        if (from.equals("bookingSure")) {//确认预约
            String date = b.getString("date");
            String time = b.getString("time");
            for (MyRow ro : gridData) {
                if (ro.getString("Date").equals(date) && ro.getString("ClassRange").equals(time)) {
                    ro.put("checked", true);
                    MyRow dateRo = new MyRow();
                    dateRo.put("date", date);
                    dateRo.put("time", time);
                    selectedDates.add(dateRo);
                }
            }
            btn_sure.setEnabled(true);
            btn_pai.setTextColor(getResources().getColor(R.color.white));
            btn_sure.setBackground(getResources().getDrawable(R.drawable.btn_bg_normal_blue));
        }
        dateAdapter = new ClassDateGridAdapter(this, dateData);
        timeAdapter = new ClassTimeGridAdapter(this, timeData);
        gridAdapter = new ClassDataGridAdapter(this, gridData);
        dateGrid.setAdapter(dateAdapter);
        timeGrid.setAdapter(timeAdapter);
        dataGrid.setAdapter(gridAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MyRow row = (MyRow) parent.getItemAtPosition(position);
        int Status = row.getInt("Status");
        if (from.equals("AppPrivielge_05")) {//排课管理  可选择多个时间段排课或取消排课
            MyRow ro = new MyRow();
            ro.put("ClassTimeDetailId", row.getInt("ID"));
            ro.put("AppointmentDate", row.getString("Date"));
            switch (Status) {
                case 0://未选状态可排课
                case 1://已选状态可取消排课
//                    if (!isBeforeWeek) {//上周数据不可排课
                    if (row.getBoolean("checked")) {//取消选中
                        row.put("checked", false);
                        for (int i = 0; i < selectedDates.size(); i++) {
                            if (selectedDates.get(i).getString("AppointmentDate").equals(row.getString("Date")) &&
                                    selectedDates.get(i).getString("ClassTimeDetailId").equals(row.getString("ID"))) {
                                selectedDates.remove(i);
                            }
                        }
                    } else {//选中
                        row.put("checked", true);
                        selectedDates.add(ro);
                    }
                    //按钮点击状态更改
                    if (selectedDates.size() > 0) {
                        if (Status == 0) {//有已选可排课项
                            btn_yue.setEnabled(true);
                            btn_yue.setTextColor(getResources().getColor(R.color.white));
                            btn_yue.setBackground(getResources().getDrawable(R.drawable.btn_bg_normal_blue));
                        } else {//有已选可取消排课项
                            btn_pai.setEnabled(true);
                            btn_pai.setTextColor(getResources().getColor(R.color.white));
                            btn_pai.setBackground(getResources().getDrawable(R.drawable.btn_bg_normal_blue));
                        }
                    } else {
                        setBtnDisable();
                    }
//                    }
                    break;
                case 2://已约、过期已约学员课程点击弹框查看详情
                case 3:
                case 99:
                    if (!TextUtils.isEmpty(row.getString("CustomerName"))) {
                        showDetailAlert(row, Status);
                    }
                    break;
            }
        } else {//课程预约  一次只可预约一个时间段
            if (!isBeforeWeek) {//上周数据不可操作
                switch (Status) {
                    case 0://未排课
                        UI.showToast(this, "该时间段未排课");
                        break;
                    case 1://可约
                        for (MyRow r : gridData) {
                            r.put("checked", false);
                        }
                        selectedDates = new MyData();
                        row.put("checked", true);
                        MyRow date = new MyRow();
                        date.put("date", row.getString("Date"));
                        date.put("time", row.getString("ClassRange"));
                        date.put("ID", row.getInt("ID"));
                        selectedDates.add(date);

                        //按钮点击状态更改
                        if (selectedDates.size() > 0) {
                            btn_sure.setEnabled(true);
                            btn_sure.setTextColor(getResources().getColor(R.color.white));
                            btn_sure.setBackground(getResources().getDrawable(R.drawable.btn_bg_normal_blue));
                        } else {
                            btn_sure.setEnabled(false);
                            btn_sure.setTextColor(getResources().getColor(R.color.main_gray));
                            btn_sure.setBackground(getResources().getDrawable(R.drawable.btn_bg_normal_gray));
                        }
                        break;
                    case 2://已约
                    case 3:
                        UI.showToast(this, "该时间段已占用");
                        break;
                }
            }
        }
        gridAdapter.notifyDataSetChanged();
    }

    /**
     * 排课和取消排课按钮置灰
     */
    public void setBtnDisable() {
        btn_yue.setEnabled(false);
        btn_pai.setEnabled(false);
        btn_yue.setTextColor(getResources().getColor(R.color.main_gray));
        btn_pai.setTextColor(getResources().getColor(R.color.main_gray));
        btn_yue.setBackground(getResources().getDrawable(R.drawable.btn_bg_normal_gray));
        btn_pai.setBackground(getResources().getDrawable(R.drawable.btn_bg_normal_gray));
    }

    public void doWork(View v) {
        switch (v.getId()) {
            case R.id.btn_all://全选
//                if (!isBeforeWeek) {//上周数据 不可操作
                int size = gridData.size();
                for (int i = 0; i < size; i++) {//gridView 适配数据
                    int status = gridData.get(i).getInt("Status");
                    if (status != 2 && status != 3 & status != 9) {//除已过期已预约 全部选中
                        if (!gridData.get(i).getBoolean("checked")) {//有未选中项则可全选
                            gridData.get(i).put("checked", true);
                            MyRow ro = new MyRow();
                            ro.put("ClassTimeDetailId", gridData.get(i).getInt("ID"));
                            ro.put("AppointmentDate", gridData.get(i).getString("Date"));
                            selectedDates.add(ro);
                        } else {
                            gridData.get(i).put("checked", false);
                            selectedDates = new MyData();
                        }
                    }
                }
                if (selectedDates.size() > 0) {
                    btn_yue.setEnabled(true);
                    btn_pai.setEnabled(true);
                    btn_yue.setTextColor(getResources().getColor(R.color.white));
                    btn_pai.setTextColor(getResources().getColor(R.color.white));
                    btn_yue.setBackground(getResources().getDrawable(R.drawable.btn_bg_normal_blue));
                    btn_pai.setBackground(getResources().getDrawable(R.drawable.btn_bg_normal_blue));
                } else {
                    btn_yue.setEnabled(false);
                    btn_pai.setEnabled(false);
                    btn_yue.setTextColor(getResources().getColor(R.color.main_gray));
                    btn_pai.setTextColor(getResources().getColor(R.color.main_gray));
                    btn_yue.setBackground(getResources().getDrawable(R.drawable.btn_bg_normal_gray));
                    btn_pai.setBackground(getResources().getDrawable(R.drawable.btn_bg_normal_gray));
                }
                gridAdapter.notifyDataSetChanged();
//                }
                break;
            case R.id.btn_copy://复制上周
                if (isBeforeWeek) {//上周数据 不可操作
                    return;
                }
                isCopy = true;
                if (isNextWeek) {//下周的复制上一周
                    new MyAsyncTask(this, LessonTime.class).run(0);
                } else {//本周的复制上一周
                    new MyAsyncTask(this, LessonTime.class).run(-1);
                }
                break;
            case R.id.btn_pai://设为未安排
                showSetAlert(0);
                break;
            case R.id.btn_yue://设为可预约
                showSetAlert(9);
                break;
            case R.id.btn_sure://确定时间
                MyRow ro = selectedDates.get(0);
                if (from.equals("bookingSure")) {//确认预约
                    Intent in = new Intent();
                    in.putExtra("date", ro.getString("date"));
                    in.putExtra("time", ro.getString("time"));
                    in.putExtra("ID", ro.getInt("ID"));
                    setResult(RESULT_OK, in);
                } else if (from.equals("AppPrivielge_07")) {//新建预约
                    Bundle b = new Bundle();
                    b.putString("date", ro.getString("date"));
                    b.putString("time", ro.getString("time"));
                    b.putInt("ID", ro.getInt("ID"));
                    openIntent(BookingSureActivity.class, R.string.sure_booking, b);
                }
                finish();
                break;
        }
    }

    @Override
    public void onPostExecute(Class<?> op, Result result) {
        super.onPostExecute(op, result);
        if (op == LessonTime.class) {
            if (result.code == BC.SUCCESS) {
                MyData data;
                if (!TextUtils.isEmpty(result.obj.toString()) && !result.obj.toString().equals("null")) {
                    data = (MyData) result.obj;
                } else {
                    data = new MyData();
                }
                if (isCopy) {//复制上周
                    MyData copyData = new MyData();
                    for (MyRow row : data) {//时间数据
                        MyData data1 = (MyData) row.get("ClassDetail");
                        for (MyRow rr : data1) {//gridView 适配数据
                            if (rr.getInt("Status") == 1) {//可选状态
                                MyRow ro = new MyRow();
                                ro.put("WeekName", rr.getString("WeekName"));
                                ro.put("ClassRange", row.getString("ClassRange"));
                                copyData.add(ro);
                            }
                        }
                    }
                    for (int i = 0; i < gridData.size(); i++) {
                        for (int j = 0; j < copyData.size(); j++) {
                            if ((copyData.get(j).getString("ClassRange").equals(gridData.get(i).getString("ClassRange"))) &&
                                    (copyData.get(j).getString("WeekName").equals(gridData.get(i).getString("WeekName"))) &&
                                    (gridData.get(i).getInt("Status") == 0 ||
                                            gridData.get(i).getInt("Status") == 1)) {//除了过期已预约（即未选、可选状态）的数据全选
                                gridData.get(i).put("checked", true);
                                MyRow ro = new MyRow();
                                ro.put("ClassTimeDetailId", gridData.get(i).getInt("ID"));
                                ro.put("AppointmentDate", gridData.get(i).getString("Date"));
                                selectedDates.add(ro);
                            }
                        }
                    }
                    gridAdapter.notifyDataSetChanged();
                    if (selectedDates.size() > 0) {//更新按钮状态
                        btn_yue.setEnabled(true);
                        btn_yue.setTextColor(getResources().getColor(R.color.white));
                        btn_yue.setBackground(getResources().getDrawable(R.drawable.btn_bg_normal_blue));
                    }
                    isCopy = false;
                } else {
                    if (data.size() > 0) {
                        for (MyRow row : data) {//时间数据
                            timeData.add(row);
                            MyData data1 = (MyData) row.get("ClassDetail");
                            for (MyRow rr : data1) {//gridView 适配数据
                                rr.put("checked", false);
                                rr.put("ID", row.getInt("ID"));
                                rr.put("ClassRange", row.getString("ClassRange"));
                                gridData.add(rr);
                            }
                        }
                        MyData dates = (MyData) data.get(0).get("ClassDetail");//周和日期
                        for (MyRow row : dates) {
                            dateData.add(row);
                        }
                    }
                    setAdapters();
                }
            } else {
                UI.showToast(this, result.msg);
            }
        } else if (op == LessonStatus.class) {
            if (result.code == BC.SUCCESS) {
                UI.showToast(this, result.msg);
                dateData = new MyData();
                timeData = new MyData();
                gridData = new MyData();
                refreshData();
                setBtnDisable();
                selectedDates = new MyData();
            } else {
                UI.showToast(this, result.msg);
            }
        }
    }

    public void showSetAlert(final int type) {//0 设为未安排 9设为可预约
        final AlertDialog ad = new AlertDialog(this);
        if (type == 0) {
            ad.setTitleAndMsg(getString(R.string.set_pai_tip), getString(R.string.set_pai_tip1));
        } else if (type == 9) {
            ad.setTitleAndMsg(getString(R.string.set_yue_tip), getString(R.string.set_yue_tip1));
        }
        ad.setNegativeButton("取消", 0, 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
        ad.setPositiveButton("确定", 0, 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (MyRow ro : selectedDates) {
                    ro.put("Status", type);
                }
                new MyAsyncTask(ClassMngActivity.this, LessonStatus.class).run(selectedDates);
                ad.dismiss();
            }
        });
    }

    public void showDetailAlert(MyRow row, int type) {// 已约(2、3)、过期已约（99）学员课程点击弹框查看详情
        final AlertDialog ad = new AlertDialog(this);
        if (type == 2 || type == 3 ||
                (type == 99 && !TextUtils.isEmpty(row.getString("CustomerName")))) {//已过期且已约
            String[] times = row.getString("ClassRange").split("-");
            String tips = getString(R.string.detail_student) + row.getString("CustomerName") + "\n" +
                    getString(R.string.detail_time) + row.getString("Date") + " " + times[0] + "～" + times[1] + "\n" +
                    getString(R.string.detail_class) + row.getString("LessonName") + "\n" +
                    getString(R.string.detail_num) + "第" + row.getInt("ConsumeHour") + "节，共" + row.getInt("TotalHour") + "节";
            ad.setTitleAndMsg(getString(R.string.detail_title), tips);
        }
        ad.setMsgGravityLeft();
        ad.setOKDialog();
        ad.setPositiveButton("确定", 0, 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
    }

    @Override
    public void onTabSelect(int position) {
        switch (position) {
            case 0:
                week = -1;
                isBeforeWeek = true;
                isNextWeek = false;
                break;
            case 1:
                isBeforeWeek = false;
                isNextWeek = false;
                week = 0;
                break;
            case 2:
                isBeforeWeek = false;
                isNextWeek = true;
                week = 1;
                break;
        }
        dateData = new MyData();
        timeData = new MyData();
        gridData = new MyData();
        refreshData();
        setBtnDisable();
        selectedDates = new MyData();
    }

    @Override
    public void onTabReselect(int position) {

    }

    class ClassTimeGridAdapter extends BaseAdapter {

        public ClassTimeGridAdapter(BaseActivity a, MyData data) {
            super(a, R.layout.pop_item, data);
        }

        @Override
        protected void display(int position, View v, MyRow row) {
            super.display(position, v, row);
            LinearLayout ll = v.findViewById(R.id.ll);
            ll.setLayoutParams(new LinearLayout.LayoutParams(height, height));
            setEText(R.id.title, row.getString("StartTime").substring(11, 16));
        }
    }
}

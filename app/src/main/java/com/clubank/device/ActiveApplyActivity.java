package com.clubank.device;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.clubank.device.op.AppPrivilege;
import com.clubank.device.op.ProcessArena;
import com.clubank.device.op.ProcessDoc;
import com.clubank.device.op.ProcessSave;
import com.clubank.device.op.ProcessStep;
import com.clubank.device.op.ProcessTips;
import com.clubank.device.op.ProcessView;
import com.clubank.dingxi.R;
import com.clubank.domain.BC;
import com.clubank.domain.C;
import com.clubank.domain.PlaceModel;
import com.clubank.domain.Result;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.U;
import com.clubank.util.UI;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 活动申请
 */
public class ActiveApplyActivity extends CamareAndCropActivity implements AdapterView.OnItemClickListener, TextWatcher {
    private MyData picData, approvesData;//图片资料 审批人
    private ApplySelectPicAdapter picAdapter;
    private ApprovesAdapter approveAdapter;
    private RecyclerView approveList;
    private String currentTime, starTime, endTime, processId = "", SeleDate;
    private int dateSeleStartOrEnd;
    private Calendar selectedDate, compareTempDate, rangeEnd;
    private TimePickerView pvCustomTime;
    private ArrayList<String> localPicUrl;
    private final int multiSelectPic = 15421;
    private int uploadPos = 0, deletePicPos;
    private OptionsPickerView pvOptions;
    private ArrayList<PlaceModel> options1Items = new ArrayList<>();
    private ArrayList<List<String>> options2Items = new ArrayList<>();
    private int mCurrentPlacePos = 0, mCurrentSelePos = 0;
    private StringBuffer receivePicData;
    private EditText active_detail;
    private TextView text_num;
    private int operatePosition, limit = 500, cursor = 0, before_length;//限制数  用来记录输入字符的时候光标的位置   用来标注输入某一内容之前的编辑框中的内容的长度
    private View view;
    private MyRow operateRow, readMsgRow;
    private Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_apply);
        initView();
    }

    private void initView() {
        hideSoftKeyboard();
        compareTempDate = Calendar.getInstance();
        selectedDate = Calendar.getInstance();
        selectedDate.add(Calendar.DAY_OF_YEAR, 0);
        picData = new MyData();
        b = getIntent().getExtras();
        if (null != b) {
            if (b.getBoolean("modify")) {
                processId = b.getString("processid");
                new MyAsyncTask(this, ProcessView.class).run(processId);
            } else {
                picData.add(new MyRow());
            }
        }
        approvesData = new MyData();
        GridView gridView = findViewById(R.id.pics_gridView);
        picAdapter = new ApplySelectPicAdapter(this, picData);
        gridView.setOnItemClickListener(this);
        gridView.setAdapter(picAdapter);

        approveList = findViewById(R.id.approves_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        approveList.setLayoutManager(linearLayoutManager);
        approveAdapter = new ApprovesAdapter(this, approvesData);
        text_num = findViewById(R.id.text_num);
        active_detail = findViewById(R.id.active_detail);
        active_detail.addTextChangedListener(this);
        new MyAsyncTask(this, ProcessArena.class).run();
        new MyAsyncTask(this, ProcessStep.class).run();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new MyAsyncTask(this, ProcessTips.class).run();
    }

    private void setDetails(MyRow row) {
        MyRow details = ((MyData) row.get("Process")).get(0);
        MyData pics = (MyData) row.get("Docs");
//        MyData steps = (MyData) row.get("Step");

        setEText(R.id.active_title, details.getString("ProcessName"));
        setEText(R.id.start_time, details.getString("StartTime").substring(0, 16));
        starTime = details.getString("StartTime").substring(0, 16);
        endTime = details.getString("EndTime").substring(0, 16);
        setEText(R.id.end_time, details.getString("EndTime").substring(0, 16));
        setEText(R.id.active_place, details.getString("Address"));
        if (!checkNull(details.getString("ProcessDetail"))) {
            try {
                setEText(R.id.active_detail, new String(android.util.Base64.decode(details.getString("ProcessDetail"), android.util.Base64.NO_WRAP)));
            } catch (Exception e) {

            }
        }
        for (MyRow r : pics) {
            r.put("isNew", false);
            picData.add(r);
        }
        notifyPicChange();
    }

    private void notifyPicChange() {
        if (picAdapter != null) {
            if (picData.size() < 9) {
                MyRow row = picData.get(picData.size() - 1);
                if (!row.isEmpty()) {
                    picData.add(new MyRow());
                }
            }
            picAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPostExecute(Class<?> op, Result result) {
        super.onPostExecute(op, result);
        if (op == ProcessView.class) {
            if (result.code == BC.SUCCESS) {
                MyRow row = (MyRow) result.obj;
                show(R.id.read_msg_ll);
//                setEText(R.id.msg_num,reminds+"");
                setDetails(row);
            } else {
                UI.showToast(this, result.msg);
            }
        } else if (op == ProcessDoc.class) {
            if (result.code == BC.SUCCESS) {
                String docId = result.obj.toString();
                receivePicData.append(docId).append(",");
                uploadImage(localPicUrl, ++uploadPos);
            } else {
                UI.showToast(this, result.msg);
            }
        } else if (op == ProcessSave.class) {
            if (result.code == BC.SUCCESS) {
                processId = result.obj.toString();
                if (b.getBoolean("modify")) {//修改申请
                    UI.showToast(this, "修改成功");
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Bundle b = new Bundle();
                            b.putInt("from", 1);
                            b.putString("processid", processId);
                            Intent intent = new Intent(ActiveApplyActivity.this, ApplyDetailActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("title", getString(R.string.apply_detail));
                            intent.putExtras(b);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.forward_enter,R.anim.forward_exit);
                        }
                    }, 1500);
                } else {
                    Intent intent = new Intent(this, ApplySuccessActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("title", getString(R.string.apply_success));
                    startActivity(intent);
                    finish();
                    overridePendingTransition( R.anim.forward_enter,R.anim.forward_exit);
                }
            } else {
                UI.showToast(this, result.msg);
            }
        } else if (op == ProcessArena.class) {
            if (result.code == BC.SUCCESS) {
                try {
                    JSONArray data = new JSONArray(result.obj.toString());
                    Gson gson = new Gson();
                    for (int i = 0; i < data.length(); i++) {
                        PlaceModel entity = gson.fromJson(data.optJSONObject(i).toString(), PlaceModel.class);
                        JSONArray seles = data.optJSONObject(i).getJSONArray("SeatInfo");
                        ArrayList<PlaceModel.SeatInfo> entitys = new ArrayList<>();
                        ArrayList<String> seleList = new ArrayList<>();//分场
                        for (int o = 0; o < seles.length(); o++) {
                            PlaceModel.SeatInfo entity1 = gson.fromJson(seles.optJSONObject(o).toString(), PlaceModel.SeatInfo.class);
                            entitys.add(entity1);
                            seleList.add(entity1.getName());
                        }
                        entity.setSeleList(entitys);
                        options1Items.add(entity);
                        options2Items.add(seleList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                UI.showToast(this, result.msg);
            }
        } else if (op == ProcessTips.class) {//小红点提示
            if (result.code == BC.SUCCESS) {
                readMsgRow = ((MyData) result.obj).get(0);
                int remind = readMsgRow.getInt("actApplyCount");
                if (remind > 0) {
                    show(R.id.read_msg_ll);
                }
            } else {
                UI.showToast(this, result.msg);
            }
            new MyAsyncTask(this, AppPrivilege.class).run();
        } else if (op == ProcessStep.class) {
            if (result.code == BC.SUCCESS) {
                MyData datas = (MyData) result.obj;
                if (null != datas) {
                    for (MyRow r : datas) {
                        approvesData.add(r);
                    }
                }
                approveList.setAdapter(approveAdapter);
                approveAdapter.notifyDataSetChanged();
            } else {
                UI.showToast(this, result.msg);
            }
        }
    }

    public void doWork(View view) {
        switch (view.getId()) {
            case R.id.apply_record://申请记录
                openIntent(ApplyRecordListActivity.class, R.string.active_record);
                break;
            case R.id.start_time_ll://开始时间
                dateSeleStartOrEnd = 1;
                showDatePicker();
                break;
            case R.id.end_time_ll://结束时间
                dateSeleStartOrEnd = 2;
                showDatePicker();
                break;
            case R.id.active_place_ll://活动地点
                showPlacePiker();
                break;
            case R.id.add_pic://添加图片
                MyRow ro = picData.get(picData.size() - 1);
                if (ro != null && ro.isEmpty()) {
                    this.view = view;
                    checkAllNeedPermissions();
                } else {
                    UI.showToast(this, "您最多只能上传9张图片！");
                    return;
                }
                break;
            case R.id.delete_pic://删除图片
                deletePicPos = (int) view.getTag();
                MyRow row = picData.get(deletePicPos);
                if (row != null) {
                    if (!row.isEmpty()) {
                        picData.remove(deletePicPos);
                        notifyPicChange();
                    }
                }
                break;
            case R.id.submit://提交
                String title = getEText(R.id.active_title);
                String start_time = getEText(R.id.start_time);
                String end_time = getEText(R.id.end_time);
                String active_place = getEText(R.id.active_place);
                String active_detail = getEText(R.id.active_detail);
                if (TextUtils.isEmpty(title)) {
                    UI.showToast(this, "请输入活动名称");
                    return;
                }
                if (TextUtils.isEmpty(start_time)) {
                    UI.showToast(this, "请选择开始时间");
                    return;
                }
                if (TextUtils.isEmpty(end_time)) {
                    UI.showToast(this, "请选择结束时间");
                    return;
                }
                if (TextUtils.isEmpty(active_place)) {
                    UI.showToast(this, "请选择活动举办地点");
                    return;
                }
                if (TextUtils.isEmpty(active_detail)) {
                    UI.showToast(this, "请输入活动详情");
                    return;
                }
                //判断完成其他后在上传图片 图片是选填
                receivePicData = new StringBuffer();
                localPicUrl = new ArrayList<>();
                for (int i = 0; i < picData.size(); i++) {
                    MyRow roo = picData.get(i);
                    if (!roo.isEmpty()) {
                        String picUrl = roo.getString("Path");
                        if (!TextUtils.isEmpty(picUrl) && roo.getBoolean("isNew")) {
                            localPicUrl.add(picUrl);
                        }
                        if (!TextUtils.isEmpty(picUrl) && !roo.getBoolean("isNew")) {
                            receivePicData.append(roo.getString("DocID")).append(",");
                        }
                    }
                }
                if (localPicUrl.size() > 0) {
                    uploadImage(localPicUrl, uploadPos);
                } else {
                    SaveApply();
                }
                break;
        }
    }

    /**
     * 活动地点选择
     */
    private void showPlacePiker() {
        pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText()
                        + "  " + options2Items.get(options1).get(option2);
                setEText(R.id.active_place, tx);
            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("")//标题
                .setLineSpacingMultiplier(2.0F)
                .setSubmitColor(Color.DKGRAY)//确定按钮文字颜色
                .setCancelColor(Color.DKGRAY)//取消按钮文字颜色
                .setLabels("", "", "")//设置选择的三级单位
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(mCurrentPlacePos, mCurrentSelePos)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .isDialog(true)//是否显示为对话框样式
                .build();
        pvOptions.setPicker(options1Items, options2Items);//添加数据源
        pvOptions.show();
    }

    public void uploadImage(ArrayList<String> picUrls, int pos) {
        uploadPos = pos;
        if (picUrls.size() > pos) {
//            UI.showToast(this, "正在上传第" + (pos + 1) + "张图片");
            new MyAsyncTask(this, ProcessDoc.class).run(processId, picUrls.get(pos));
        } else {
            SaveApply();
        }
    }

    public void SaveApply() {
        MyRow row = new MyRow();
        row.put("processid", processId);
        row.put("processname", getEText(R.id.active_title));
        row.put("starttime", starTime);
        row.put("endtime", endTime);
        row.put("address", getEText(R.id.active_place));
        row.put("processdetail", android.util.Base64.encodeToString(getEText(R.id.active_detail).getBytes(), android.util.Base64.NO_WRAP));
        if (receivePicData.length() > 1) {
            receivePicData.substring(0, receivePicData.length() - 1);
        }
        row.put("docs", receivePicData);
        new MyAsyncTask(this, ProcessSave.class).run(row);
    }


    @Override
    protected void doUploadImage(String tmpfile, Bitmap small) {
        super.doUploadImage(tmpfile, small);
//        if (operatePosition < picData.size()) {
        MyRow row = picData.get(operatePosition);
        row.put("isNew", true);
        row.put("Path", tmpfile);
        notifyPicChange();
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == multiSelectPic && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    MyData d = U.getData(bundle, "data");
                    if (d != null && d.size() > 0) {
                        MyRow row = picData.get(operatePosition);
                        if (row.isEmpty()) {
                            for (int i = 0; i < d.size(); i++) {
                                MyRow r = d.get(i);
                                if (i == 0) {
                                    row.put("isNew", true);
                                    row.put("Path", r.getString("path"));
                                } else {
                                    MyRow r1 = new MyRow();
                                    r1.put("isNew", true);
                                    r1.put("Path", r.getString("path"));
                                    picData.add(r1);
                                }
                            }
                        } else {
                            row.put("isNew", true);
                            row.put("Path", d.get(0).getString("path"));
                        }
                        notifyPicChange();
                    }
                }
            }
        }
    }

    private void showDatePicker() {
        Calendar rangeStart = Calendar.getInstance();
        if (dateSeleStartOrEnd == 1) { //开始日期
            currentTime = starTime;
        } else if (dateSeleStartOrEnd == 2) { //结束日期
            currentTime = endTime;
        }
        if (TextUtils.isEmpty(currentTime)) {
            currentTime = C.df_yMdHms.format(new Date());
        }
        String ymdT = currentTime;
        Date str1Time = null;
        try {
            if (ymdT.contains("T")) {
                str1Time = C.df_yMdHm.parse(currentTime.replaceAll("T", " ").substring(0, 16));
            } else {
                str1Time = C.df_yMdHm.parse(currentTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        selectedDate = dataToCalendar(str1Time);
        rangeEnd = Calendar.getInstance();
//        int year = rangeEnd.get(Calendar.YEAR);
        rangeEnd.set(2099, 12, 31);
        pvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                int[] ints = getDateInt(C.dff_yMdHm.format(date));
                compareTempDate.set(ints[0], ints[1], ints[2], ints[3], ints[4]);
                Log.d("pvCustomTime", "onTimeSelect");
                selectedDate.set(ints[0], ints[1], ints[2], ints[3], ints[4]);
                SeleDate = C.df_yMdHm.format(((Calendar) compareTempDate.clone()).getTime());

                if (dateSeleStartOrEnd == 1) {  //开始时间  要小于结束时间  大于当前时间
                    //如果compareTo返回0，表示两个日期相等，返回小于0的值，表示d1在d2之前，大于0表示d1在d2之后
                    boolean crp = false;
                    if (!TextUtils.isEmpty(endTime)) {
                        try {
                            crp = greater(SeleDate, endTime);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (crp) {   //startDate 大于 endDate
                                pvCustomTime.setDate(dataToCalendar(C.df_yMdHm.parse(endTime)));
                                starTime = endTime;
                                setEText(R.id.start_time, getEText(R.id.end_time));
                            } else {
                                setEText(R.id.start_time, C.dfs_yMd.format(selectedDate.getTime()) + " " + getWeekday(C.df_yMd.format(selectedDate.getTime())) + " " + C.df_Hm.format(selectedDate.getTime()));
                                starTime = SeleDate;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        starTime = C.df_yMdHm.format(selectedDate.getTime());
                        setEText(R.id.start_time, C.dfs_yMd.format(selectedDate.getTime()) + " " + getWeekday(C.df_yMd.format(selectedDate.getTime())) + " " + C.df_Hm.format(selectedDate.getTime()));
                    }
                } else if (dateSeleStartOrEnd == 2) {//结束时间  要小于结束时间  大于当前时间
                    boolean crp = false;
                    if (!TextUtils.isEmpty(starTime)) {
                        try {
                            crp = greater(SeleDate, starTime);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (!crp) {   //endDate 小于 startDate
                                pvCustomTime.setDate(dataToCalendar(C.df_yMdHm.parse(starTime)));
                                endTime = starTime;
                                setEText(R.id.end_time, getEText(R.id.start_time));
                            } else {
                                setEText(R.id.end_time, C.dfs_yMd.format(selectedDate.getTime()) + " " + getWeekday(C.df_yMd.format(selectedDate.getTime())) + " " + C.df_Hm.format(selectedDate.getTime()));
                                endTime = SeleDate;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        endTime = C.df_yMdHm.format(selectedDate.getTime());
                        setEText(R.id.end_time, C.dfs_yMd.format(selectedDate.getTime()) + " " + getWeekday(C.df_yMd.format(selectedDate.getTime())) + " " + C.df_Hm.format(selectedDate.getTime()));
                    }
                }
            }
        }).setType(new boolean[]{true, true, true, true, true, false})
                .setLabel("", "", "", "", "", "")
//                .setContentSize(20)
                .setDecorView(null)
                .setOutSideCancelable(false)
                .setLineSpacingMultiplier(2.0F)
                .setDate(selectedDate)
                .setRangDate(rangeStart, rangeEnd)
                .setSubmitColor(Color.DKGRAY)//确定按钮文字颜色
                .setCancelColor(Color.DKGRAY)//取消按钮文字颜色
                .isDialog(true)
                .setOutSideCancelable(true)
                .build();
        pvCustomTime.show();
    }

    //	实现给定某日期，判断是星期几
    public static String getWeekday(String date) {//必须yyyy-MM-dd
        SimpleDateFormat sdw = new SimpleDateFormat("E");
        Date d = null;
        try {
            d = C.df_yMd.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sdw.format(d);
    }

    public static boolean greater(String d1, String d2) throws Exception {
        //如果compareTo返回0，表示两个日期相等，返回小于0的值，表示d1在d2之前，大于0表示d1在d2之后
        return C.df_yMdHm.parse(d1).compareTo(C.df_yMdHm.parse(d2)) >= 0;
    }

    private int[] getDateInt(String date) {
        String[] sdate = date.split("-");
        int year = Integer.parseInt(sdate[0]);
        int month = Integer.parseInt(sdate[1]) - 1;
        int day;
        if (sdate.length > 2) {
            day = Integer.parseInt(sdate[2]);
        } else {
            day = 0;
        }
        int hh = Integer.parseInt(sdate[3]);
        int mm = Integer.parseInt(sdate[4]);
        int ss = Integer.parseInt(sdate[5]);
        int[] ints = new int[]{year, month, day, hh, mm, ss};
        return ints;
    }

    public static Calendar dataToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        operatePosition = i;
        operateRow = (MyRow) adapterView.getItemAtPosition(i);
        if (operateRow != null && operateRow.isEmpty()) {
            this.view = view;
            checkAllNeedPermissions();
        } else {
            Intent in = new Intent(this, ImageLargeActivity.class);
            Bundle b = new Bundle();
            b.putSerializable("data", picData);
            in.putExtras(b);
            in.putExtra("pos", i);
            startActivity(in);
        }
    }

    @Override
    protected String[] getNeedPermissions() {
        return new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
    }

    @Override
    protected void permissionGrantedSuccess() {
        if (operateRow != null) {
            if (operateRow.isEmpty()) {
                int[] images = new int[]{com.clubank.common.R.drawable.take_picture,
                        com.clubank.common.R.drawable.choose_picture};
                getPicture(view, getResources().getStringArray(R.array.takePic), images);
            } else {
                int[] images = new int[]{com.clubank.common.R.drawable.take_picture,
                        com.clubank.common.R.drawable.choose_picture};
                getPicture(view, getResources().getStringArray(R.array.takePic), images);
            }
        }
    }

    protected void getPicture(View view, String[] pic_options, int[] images) {
        showListDialog(view, pic_options, images);
    }

    @Override
    public void listSelected(View view, int index) {
        if (index == 0) {// 拍照
            takePicture(this, C.REQUEST_FROM_CAMERA);
        } else if (index == 1) {// 选择图片
            int maxSelect;
            MyRow row = picAdapter.getItem(operatePosition);
            if (row != null && row.isEmpty()) {
                maxSelect = 9 - picData.size() + 1;
            } else {
                maxSelect = 1;
            }
            Bundle bundle = new Bundle();
            bundle.putInt("maxSelect", maxSelect);
            openIntent(PicMultiSelectActivity.class, "选择图片", bundle, multiSelectPic);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        before_length = charSequence.length();
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        cursor = i;
        text_num.setText(String.format(getString(R.string.active_detail_hintt), charSequence.length() + "/500"));
    }

    @Override
    public void afterTextChanged(Editable editable) {
        int after_length = editable.length();
        if (after_length > limit) {
            UI.showToast(this, "您最多只能输入500字");
            active_detail.setText(editable.toString().substring(0, 500));
            // 设置光标最后显示的位置为超出部分的开始位置，优化体验
            active_detail.setSelection(500);
        }
    }
}




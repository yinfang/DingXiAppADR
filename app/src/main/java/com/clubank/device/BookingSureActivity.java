package com.clubank.device;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.clubank.device.op.LessonAppointment;
import com.clubank.dingxi.R;
import com.clubank.domain.BC;
import com.clubank.domain.Result;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.U;
import com.clubank.util.UI;

/**
 * 确认预约
 */
public class BookingSureActivity extends BaseActivity implements TextWatcher {
    private int lessonorderId, classTimedetailId;
    private String appointmentdate;
    private String temp, date = "", time = "";
    private TextView text_num;
    private EditText tv_mark;
    private int limit = 200, cursor = 0, before_length;//限制数  用来记录输入字符的时候光标的位置   用来标注输入某一内容之前的编辑框中的内容的长度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_sure);
        text_num = findViewById(R.id.text_num);
        tv_mark = findViewById(R.id.tv_mark);
        tv_mark.addTextChangedListener(this);
        Bundle b = getIntent().getExtras();
        if (null != b) {
            date = b.getString("date");
            time = b.getString("time");
            classTimedetailId = b.getInt("ID");
            String[] times = time.split("-");
            setEText(R.id.tv_time, date + " " + times[0] + "～" + times[1]);
            appointmentdate = date;
        }
    }

    @Override
    public void onPostExecute(Class<?> op, Result result) {
        super.onPostExecute(op, result);
        if (op == LessonAppointment.class) {
            if (result.code == BC.SUCCESS) {
                MyRow data = ((MyData) result.obj).get(0);
                Bundle b = new Bundle();
                b.putSerializable("row", data);
                openIntent(BookingClassSuccessActivity.class, R.string.booking_success, b);
                finish();
            } else {
                UI.showToast(this, result.msg);
            }
        }
    }

    public void doWork(View view) {
        Bundle b = new Bundle();
        switch (view.getId()) {
            case R.id.class_name_ll://选择学员课程
                b.putString("date", date);
                openIntent(ChooseClassListActivity.class, getString(R.string.choose_class), b, 1005);
                break;
            case R.id.class_time_ll://选择上课时间
                b.putString("date", date);
                b.putString("time", time);
                b.putString("from", "bookingSure");
                openIntent(ClassMngActivity.class, getString(R.string.choose_time), b, 1006);
                break;
            case R.id.sure_booking://确认预约
                String class_name = getEText(R.id.tv_class);
                String tv_time = getEText(R.id.tv_time);
                String tv_mark = getEText(R.id.tv_mark);
                if (TextUtils.isEmpty(class_name)) {
                    UI.showToast(this, getString(R.string.choose_class_hint));
                    return;
                }
                if (TextUtils.isEmpty(tv_time)) {
                    UI.showToast(this, getString(R.string.choose_time_hint));
                    return;
                }
                MyRow ro = new MyRow();
                ro.put("lessonorderId", lessonorderId);
                ro.put("classTimedetailId", classTimedetailId);
                ro.put("appointmentdate", appointmentdate);
                ro.put("remarks", android.util.Base64.encodeToString(getEText(R.id.tv_mark).getBytes(), android.util.Base64.NO_WRAP));
                new MyAsyncTask(this, LessonAppointment.class).run(ro);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data && resultCode == RESULT_OK) {
            if (requestCode == 1005) {//选择学员课程
                Bundle b = data.getExtras();
                MyRow ro = U.getRow(b, "row");
                lessonorderId = ro.getInt("OrderID");
                setEText(R.id.tv_class, ro.getString("ContactsName") + " " + ro.getString("LessonName"));
            } else if (requestCode == 1006) {//选择上课时间
                date = data.getStringExtra("date");
                time = data.getStringExtra("time");
                String[] times = time.split("-");
                setEText(R.id.tv_time, date + " " + times[0] + "～" + times[1]);
                appointmentdate = date;
                classTimedetailId = data.getIntExtra("ID", 0);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        before_length = charSequence.length();
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        cursor = i;
        text_num.setText(String.format(getString(R.string.class_mark_hint1), charSequence.length() + "/200"));
    }

    @Override
    public void afterTextChanged(Editable editable) {
        int after_length = editable.length();
        if (after_length > limit) {
            UI.showToast(this, "您最多只能输入200字");
            tv_mark.setText(editable.toString().substring(0, 200));
            // 设置光标最后显示的位置为超出部分的开始位置，优化体验
            tv_mark.setSelection(200);
        }
    }
}




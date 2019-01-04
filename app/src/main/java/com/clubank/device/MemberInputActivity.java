package com.clubank.device;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.clubank.device.op.SaveCustomer;
import com.clubank.dingxi.R;
import com.clubank.domain.BC;
import com.clubank.domain.BRT;
import com.clubank.domain.Result;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.UI;

/**
 * 会员录入
 */
public class MemberInputActivity extends BaseActivity {
    private String sex = "M";//性别 M=男 ，F=女

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_input);
    }

    @Override
    public void onPostExecute(Class<?> op, Result result) {
        super.onPostExecute(op, result);
        if (op == SaveCustomer.class) {
            if (result.code == BC.SUCCESS) {
                MyData data = (MyData) result.obj;
                MyRow r = data.get(0);
                Bundle b = new Bundle();
                b.putSerializable("row", r);
                openIntent(MemberInputSuccessActivity.class, R.string.input_success, b);
                finish();
            } else {
                UI.showToast(this, result.msg);
            }
        }
    }

    public void doWork(View view) {
        RadioButton boy = findViewById(R.id.boy);
        RadioButton girl = findViewById(R.id.girl);
        switch (view.getId()) {
            case R.id.boy://男
                sex = "M";
                boy.setChecked(true);
                girl.setChecked(false);
                break;
            case R.id.girl://女
                sex = "F";
                boy.setChecked(false);
                girl.setChecked(true);
                break;
            case R.id.btn_input://录入
                String member_name = getEText(R.id.member_name);
                String member_no = getEText(R.id.member_no);
                String member_phone = getEText(R.id.member_phone);
                String member_phone1 = getEText(R.id.member_phone1);
                String car_number = getEText(R.id.car_number);
                if (TextUtils.isEmpty(member_name)) {
                    UI.showToast(this, getString(R.string.member_name_hint));
                    return;
                }
                if (TextUtils.isEmpty(member_no)) {
                    UI.showToast(this, getString(R.string.member_no_hint));
                    return;
                }

                if (!TextUtils.isEmpty(member_no) && !UI.checkIdentityCard(member_no)) {
                    UI.showToast(this, "无效身份证号！");
                    findViewById(R.id.member_no).requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(member_phone)) {
                    UI.showToast(this, getString(R.string.member_phone_hint));
                    return;
                }
                if (!UI.checkMobile(member_phone)) {
                    UI.showToast(this, "无效手机号！");
                    findViewById(R.id.member_phone).requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(member_phone1)) {
                    UI.showToast(this, getString(R.string.member_phone1_hint));
                    return;
                }

                if (!TextUtils.isEmpty(member_phone1) && !UI.checkMobile(member_phone)) {
                    UI.showToast(this, "无效手机号！");
                    findViewById(R.id.member_phone1).requestFocus();
                    return;
                }
                if (!TextUtils.isEmpty(car_number) && !UI.isCarNum(car_number)) {
                    UI.showToast(this, "无效车牌号！");
                    findViewById(R.id.car_number).requestFocus();
                    return;
                }

                MyRow ro = new MyRow();
                ro.put("name", member_name);
                ro.put("gender", sex);
                ro.put("idnumber", member_no);
                ro.put("mobileno", member_phone);
                ro.put("urgentmobileno", member_phone1);
                ro.put("carnumber", car_number);
                new MyAsyncTask(this, SaveCustomer.class).run(ro);
                break;
        }
    }
}




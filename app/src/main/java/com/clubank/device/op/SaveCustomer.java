package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.domain.User;
import com.clubank.util.MyRow;

/**
 * •	会员录入
 * token	是	string	签名
 * name	是	string	会员姓名
 * gender	是	string	性别 M=男 ，F=女
 * idnumber	否	string	身份证号
 * mobileno	是	string	会员手机号
 * urgentmobileno	否	string	紧急联系电话
 * carnumber	否	string	车牌号
 */
public class SaveCustomer extends OPBase {
    public SaveCustomer(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {
        MyRow row = (MyRow) args[0];
        row.put("token", User.getIn().getToken());
        Result result = operateHttp(C.HTTP_POST, "SaveCustomer", row);
        return result;
    }
}

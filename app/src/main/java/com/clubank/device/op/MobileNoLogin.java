package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.util.MyRow;

/**
 * 手机号验证码登录
 */
public class MobileNoLogin extends OPBase {
    public MobileNoLogin(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {

        MyRow row = new MyRow();
        row.put("mobileno", args[0]);
        row.put("checkcode", args[1]);
        Result result = operateHttp(C.HTTP_POST, "MobileNoLogin", row);
        return result;
    }
}

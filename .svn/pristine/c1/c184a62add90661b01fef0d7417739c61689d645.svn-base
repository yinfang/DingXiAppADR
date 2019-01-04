package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.util.MyRow;

/**
 * 登录
 */
public class UserLogin extends OPBase {
    public UserLogin(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {

        MyRow row = new MyRow();
        row.put("usercode", args[0]);
        row.put("pwd", args[1]);
        row.put("source", "android");
        Result result = operateHttp(C.HTTP_POST, "UserLogin", row);
        return result;
    }
}

package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.domain.User;
import com.clubank.util.MyRow;

/**
 * 获取验证码
 */
public class GetLoginCheckCode extends OPBase {
    public GetLoginCheckCode(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {

        MyRow row = new MyRow();
        row.put("mobileno", args[0]);
        Result result = operateHttp(C.HTTP_POST, "GetLoginCheckCode", row);
        return result;
    }
}

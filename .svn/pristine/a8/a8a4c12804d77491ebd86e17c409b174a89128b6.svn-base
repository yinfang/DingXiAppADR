package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.domain.User;
import com.clubank.util.MyRow;

/**
 * 首页个人信息
 */
public class GetUserInfo extends OPBase {
    public GetUserInfo(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {

        MyRow row = new MyRow();
        row.put("token",args[0]);
        Result result = operateHttp(C.HTTP_POST, "GetUserInfo", row);
        return result;
    }
}

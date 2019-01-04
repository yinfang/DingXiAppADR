package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.domain.User;
import com.clubank.util.MyRow;

/**
 * 活动申请，获取活动举办地点
 */
public class ProcessArena extends OPBase {
    public ProcessArena(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {

        MyRow row = new MyRow();
        row.put("token", User.getIn().getToken());
        Result result = operateHttp(C.HTTP_POST, "ProcessArena", row);
        return result;
    }
}

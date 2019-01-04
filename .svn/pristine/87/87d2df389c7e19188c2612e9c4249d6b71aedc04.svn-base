package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.domain.User;
import com.clubank.util.MyRow;

/**
 * 审批记录
 */
public class ProcessRecords extends OPBase {
    public ProcessRecords(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {
        MyRow row = new MyRow();
        row.put("token", User.getIn().getToken());
        Result result = operateHttp(C.HTTP_POST, "ProcessRecords", row);
        return result;
    }
}

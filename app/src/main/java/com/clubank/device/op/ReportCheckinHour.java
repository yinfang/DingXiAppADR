package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.domain.User;
import com.clubank.util.MyRow;

/**
 * 各时段登记数
 */
public class ReportCheckinHour extends OPBase {
    public ReportCheckinHour(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {
        MyRow row = new MyRow();
        row.put("datefrom", args[0]);
        row.put("dateto", args[1]);
        row.put("arenaid", args[2]);
        row.put("token", User.getIn().getToken());
        Result result = operateHttp(C.HTTP_POST, "ReportCheckinHour", row);
        return result;
    }
}

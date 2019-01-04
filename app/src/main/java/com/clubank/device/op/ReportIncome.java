package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.domain.User;
import com.clubank.util.MyRow;

/**
 * 营业统计
 */
public class ReportIncome extends OPBase {
    public ReportIncome(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {
        MyRow row = new MyRow();
        row.put("datefrom", args[0]);
        row.put("dateto", args[1]);
        if ((int) args[2] != 100) {
            row.put("arenaid", args[2]);
        }
        row.put("token", User.getIn().getToken());
        Result result = operateHttp(C.HTTP_POST, "ReportIncome", row);
        return result;
    }
}

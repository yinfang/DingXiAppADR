package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.C;
import com.clubank.domain.Criteria;
import com.clubank.domain.Result;
import com.clubank.domain.User;
import com.clubank.util.MyRow;

/**
 * 确认预约
 */
public class LessonConfirm extends OPBase {
    public LessonConfirm(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {
        MyRow row = new MyRow();
        row.put("aid", args[0]);
        row.put("token", User.getIn().getToken());
        Result result = operateHttp(C.HTTP_POST, "LessonConfirm", row);
        return result;
    }
}

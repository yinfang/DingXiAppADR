package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.C;
import com.clubank.domain.Criteria;
import com.clubank.domain.Result;
import com.clubank.domain.User;
import com.clubank.util.MyRow;

/**
 * 课程时间列表
 * -1代表上一周，0 本周，1代表下一周
 */
public class LessonTime extends OPBase {
    public LessonTime(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {
        MyRow row = new MyRow();
        row.put("week", args[0]);
        row.put("token", User.getIn().getToken());
        Result result = operateHttp(C.HTTP_POST, "LessonTime", row);
        return result;
    }
}

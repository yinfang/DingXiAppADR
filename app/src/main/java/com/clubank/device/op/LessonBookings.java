package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.C;
import com.clubank.domain.Criteria;
import com.clubank.domain.Result;
import com.clubank.domain.User;
import com.clubank.util.MyRow;

/**
 * 课程预约记录
 * 状态 1.未确认 2.已确认 3.已取消 4.已拒绝
 */
public class LessonBookings extends OPBase {
    public LessonBookings(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {
        MyRow row = new MyRow();
        if ((int)args[0] != 0) {
            row.put("status", args[0]);
        }
        Criteria c = (Criteria) args[1];
        row.put("rows", c.PageSize);
        row.put("page", c.PageIndex);
        row.put("token", User.getIn().getToken());
        Result result = operateHttp(C.HTTP_POST, "LessonBookings", row);
        return result;
    }
}

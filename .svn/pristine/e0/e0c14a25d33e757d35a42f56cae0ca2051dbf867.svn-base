package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.domain.User;
import com.clubank.util.MyRow;

/**
 * 教练约课
 * lessonorderId 	是 	int 	课程订单ID
 classTimedetailId 	是 	int 	课程方案ID
 appointmentdate 	是 	int 	日期（所选格子的日期）
 */
public class LessonAppointment extends OPBase {
    public LessonAppointment(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {
        MyRow row = (MyRow) args[0];
        row.put("token", User.getIn().getToken());
        Result result = operateHttp(C.HTTP_POST, "LessonAppointment", row);
        return result;
    }
}

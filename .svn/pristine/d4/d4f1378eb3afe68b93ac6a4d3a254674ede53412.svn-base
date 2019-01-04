package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.domain.User;
import com.clubank.util.MyRow;

/**
 * 课程订单列表
 * 1上课中 2.已毕业 3.已过期
 */
public class LessonOrderByCoach extends OPBase {
    public LessonOrderByCoach(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {
        MyRow row = new MyRow();
        if((int)args[0]!=0){
            row.put("status",args[0]);
        }
        row.put("token", User.getIn().getToken());
        Result result = operateHttp(C.HTTP_POST, "LessonOrderByCoach", row);
        return result;
    }
}

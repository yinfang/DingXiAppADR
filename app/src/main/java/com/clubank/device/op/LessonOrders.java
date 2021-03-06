package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.C;
import com.clubank.domain.Criteria;
import com.clubank.domain.Result;
import com.clubank.domain.User;
import com.clubank.util.MyRow;

/**
 * 学员课程订单
 */
public class LessonOrders extends OPBase {
    public LessonOrders(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {
        MyRow row = new MyRow();
        row.put("appointmentDate", args[0]);
     /*   Criteria c = (Criteria) args[1];
        row.put("rows", c.PageSize);
        row.put("page", c.PageIndex);*/
        row.put("token", User.getIn().getToken());
        Result result = operateHttp(C.HTTP_POST, "LessonOrders", row);
        return result;
    }
}

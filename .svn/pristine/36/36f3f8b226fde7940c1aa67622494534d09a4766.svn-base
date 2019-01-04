package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.domain.User;
import com.clubank.util.MyRow;

/**
 * 上课记录列表
 */
public class LessonClassList extends OPBase {
    public LessonClassList(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {
        MyRow row = new MyRow();
        row.put("orderid", args[0]);
        row.put("token", User.getIn().getToken());
        Result result = operateHttp(C.HTTP_POST, "LessonClassList", row);
        return result;
    }
}

package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.C;
import com.clubank.domain.Criteria;
import com.clubank.domain.Result;
import com.clubank.domain.User;
import com.clubank.util.MyRow;
import com.google.gson.Gson;

/**
 * 修改排课预约状态
 * 状态 9.可约 0.未排
 */
public class LessonStatus extends OPBase {
    public LessonStatus(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {
        MyRow row = new MyRow();
        String json = new Gson().toJson(args[0]);
        row.put("data", json);
        row.put("token", User.getIn().getToken());
        Result result = operateHttp(C.HTTP_POST, "LessonStatus", row);
        return result;
    }
}

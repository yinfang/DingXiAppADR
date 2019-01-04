package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.domain.User;
import com.clubank.util.MyRow;

/**
 * 扫码签课后  教练评价学员
 */
public class LessonSign extends OPBase {
    public LessonSign(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {
        MyRow row = new MyRow();
        row.put("aid",args[0]);
        row.put("estimation",args[1]);
        row.put("token", User.getIn().getToken());
        Result result = operateHttp(C.HTTP_POST, "LessonSign", row);
        return result;
    }
}

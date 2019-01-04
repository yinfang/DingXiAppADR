package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.domain.User;
import com.clubank.util.MyRow;

/**
 * 活动申请 删除图片资料
 */
public class PicDelete extends OPBase {
    public PicDelete(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {

        MyRow row = new MyRow();
        row.put("DocID", args[0]);
        row.put("token", User.getIn().getToken());
        Result result = operateHttp(C.HTTP_POST, "PicDelete", row);
        return result;
    }
}

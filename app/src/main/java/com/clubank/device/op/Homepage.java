package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.domain.User;
import com.clubank.util.MyRow;

/**
 * 首页轮播图
 */
public class Homepage extends OPBase {
    public Homepage(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {
        MyRow row = new MyRow();
        row.put("token", User.getIn().getToken());
        Result result = operateHttp(C.HTTP_POST, "Homepage", row);
        return result;
    }
}

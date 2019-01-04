package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.domain.User;
import com.clubank.util.MyRow;

/**
 * 场馆列表
 */
public class GetShopArena extends OPBase {
    public GetShopArena(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {
        MyRow row = new MyRow();
        row.put("token", User.getIn().getToken());
        Result result = operateHttp(C.HTTP_POST, "GetShopArena", row);
        return result;
    }
}

package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.domain.User;
import com.clubank.util.MyRow;

/**
 * 票务二维码核销
 */
public class TicketWriteOff extends OPBase {
    public TicketWriteOff(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {
        MyRow row = new MyRow();
        row.put("qrcode",args[0]);
        row.put("token", User.getIn().getToken());
        Result result = operateHttp(C.HTTP_POST, "TicketWriteOff", row);
        return result;
    }
}

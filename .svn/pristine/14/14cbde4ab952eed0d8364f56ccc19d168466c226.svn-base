package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.domain.User;
import com.clubank.util.MyRow;

/**
 * •	活动审批
 * token	是	string	签名
 * processid	是	int	活动ID
 * stepstatus	是	int 1 同意,2打回，3否决
 * remarks	否	string	备注
 */
public class ProcessResult extends OPBase {
    public ProcessResult(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {
        MyRow row = new MyRow();
        row.put("processid", args[0]);
        row.put("stepstatus", args[1]);
        row.put("remarks", args[2]);
        row.put("token", User.getIn().getToken());
        Result result = operateHttp(C.HTTP_POST, "ProcessResult", row);
        return result;
    }
}

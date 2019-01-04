package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.Result;
import com.clubank.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量压缩图片
 */

public class CompressImage extends OPBase {
    public CompressImage(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {
        Result result = new Result();
        List<String> paths = (List<String>) args[0];
        List<String> pics = new ArrayList<>();
        for (String path : paths) {
//            pics.add(ImageUtil.saveTempBitmap(path, "tmp", ".jpg"));
        }
        result.obj = pics;
        return result;
    }
}

package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.BC;
import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.domain.User;
import com.clubank.util.MyRow;
import com.google.gson.Gson;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 用户头像上传
 */
public class UploadUserHeader extends OPBase {
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");

    public UploadUserHeader(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().writeTimeout(60000, TimeUnit.SECONDS).build();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        File file = new File((String) args[0]);
        builder.addFormDataPart("file", file.getName(), RequestBody.create(MEDIA_TYPE_JPG,
                file));
        builder.addFormDataPart("token", User.getIn().getToken());

        MultipartBody body = builder.build();
        Request request = new Request.Builder()
                .url(BC.BASE_URL_INTERNET + "UploadUserHeader")
                .post(body)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        Result result = new Result();
        if (response.isSuccessful()) {
            String re = response.body().string();
            MyRow row = new Gson().fromJson(re, MyRow.class);
            result.code = (int) row.getFloat(C.apiState);
            result.msg = row.getString(C.apiMsg);
            result.obj = row.get(C.apiDataKey);
        } else {
            result.code = response.code();
        }
        return result;
    }
}

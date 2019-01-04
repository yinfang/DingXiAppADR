package com.clubank.device.op;

import android.content.Context;

import com.clubank.domain.BC;
import com.clubank.domain.C;
import com.clubank.domain.Result;
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

public class ImageUpload extends OPBase {

    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");

    public ImageUpload(Context context) {
        super(context);
    }

    @Override
    public Result execute(Object... args) throws Exception {

        OkHttpClient okHttpClient = new OkHttpClient.Builder().writeTimeout(60000, TimeUnit.SECONDS).build();
        String filePath = (String) args[0];
        String fileType = (String) args[1];

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        File file = new File(filePath);
        if (file != null) {
            builder.addFormDataPart("file", file.getName(), RequestBody.create(MEDIA_TYPE_JPG,
                    file));
        }
        builder.addFormDataPart("fileType", fileType);

        MultipartBody body = builder.build();
        Request request = new Request.Builder()
                .url(BC.BASE_URL_INTERNET + "ProcessDoc")
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
        if (null != args[2]) {
            result.obj4 = args[2] + "";
        }
        return result;
    }
}

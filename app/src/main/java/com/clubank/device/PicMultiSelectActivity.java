package com.clubank.device;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.clubank.dingxi.R;
import com.clubank.domain.Result;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.UI;

import java.util.ArrayList;
import java.util.List;

public class PicMultiSelectActivity extends BaseActivity implements AdapterView
        .OnItemClickListener {
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;
    GridView gridView;
    PicMultiSelectAdapter adapter;
    private int maxSelect = 8;
    private MyData pics = new MyData();
    private boolean checkVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_multi_select);
        gridView = findViewById(R.id.gridView);
        gridView.setOnItemClickListener(this);
        adapter = new PicMultiSelectAdapter(this, new MyData());
        gridView.setAdapter(adapter);
        checkVersion = settings.getBoolean("checkVersion", false);
        if (checkVersion) {
            saveSetting("checkVersion", false);//不检测版本更新
        }
        getLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
        maxSelect = getIntent().getIntExtra("maxSelect", 8);
        if (maxSelect == 1) {
            adapter.setSingle(true);
            findViewById(R.id.tv_sure).setVisibility(View.GONE);
        } else {
            adapter.setSingle(false);
            findViewById(R.id.tv_sure).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getLoaderManager().destroyLoader(LOADER_ALL);
    }


    @Override
    public void doWork(View view) {
        super.doWork(view);
        switch (view.getId()) {
            case R.id.tv_sure:
                if (adapter != null) {
                    MyData data = new MyData();
                    for (int i = 0; i < adapter.getCount(); i++) {
                        MyRow r = adapter.getItem(i);
                        if (r.getBoolean("check")) {
                            data.add(r);
                        }
                    }
                    if (data.size() > 0) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", data);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                        //上传时批量压缩图片（当前客户要求不压缩）toZoomImage(data);
                    } else {
                        UI.showToast(this, "请选择图片");
                    }
                }
                break;
        }
    }

    /**
     * 压缩图片
     */
    private void toZoomImage(MyData data) {
        List<String> paths = new ArrayList<>();
        for (final MyRow row : data) {
            pics.add(row);
            paths.add(row.getString("path"));
        }
//        new MyAsyncTask(this, CompressImage.class).run(paths);
    }

    @Override
    public void onPostExecute(Class<?> op, Result result) {
//        if (op == CompressImage.class) {
//            List<String> lists = (List<String>) result.obj;
//            if (lists.size() > 0) {
//                for (int i = 0; i < pics.size(); i++) {
//                    pics.get(i).put("path", lists.get(i));
//                }
//            }
//        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", pics);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager
            .LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ALL) {
                CursorLoader cursorLoader =
                        new CursorLoader(PicMultiSelectActivity.this,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                                null, null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            } else if (id == LOADER_CATEGORY) {
                CursorLoader cursorLoader = new CursorLoader(PicMultiSelectActivity.this,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null,
                        IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                int count = data.getCount();
                MyData paths = new MyData();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        MyRow row = new MyRow();
                        row.put("check", false);
                        String path = data.getString(data.getColumnIndexOrThrow
                                (IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow
                                (IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow
                                (IMAGE_PROJECTION[2]));
                        row.put("path", path);
                        row.put("name", name);
                        row.put("dateTime", dateTime);
                        paths.add(row);
                    } while (data.moveToNext());
                }
                adapter.clear();
                adapter.addAll(paths);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (adapter.isSingle()) {
            MyData data = new MyData();
            MyRow row = adapter.getItem(position);
            data.add(row);
          /*  String filePath = ImageUtil.saveTempBitmap(row.getString("path"), "tmp", ".jpg");
            row.put("path", filePath);
            pics.add(row);*/
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", data);
//            bundle.putSerializable("data", pics);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            saveSetting("checkVersion", checkVersion);//恢复检测版本状态
            finish();
        } else {
            if (maxSelect > adapter.getSelectedCount()) {
                MyRow row = adapter.getItem(position);
                if (row != null) {
                    row.put("check", !row.getBoolean("check"));
                    adapter.notifyDataSetChanged();
                }
            } else {
                MyRow row = adapter.getItem(position);
                if (row != null) {
                    if (row.getBoolean("check")) {
                        row.put("check", false);
                        adapter.notifyDataSetChanged();
                    } else {
                        UI.showToast(this, "您不能选择更多图片");
                    }
                }

            }
        }
    }

    @Override
    public void back() {
        super.back();
        saveSetting("checkVersion", checkVersion);//恢复检测版本状态
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveSetting("checkVersion", checkVersion);//恢复检测版本状态
        }
        return super.onKeyDown(keyCode, event);
    }
}

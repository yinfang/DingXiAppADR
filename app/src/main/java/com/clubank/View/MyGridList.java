package com.clubank.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * ScrollView嵌套GridView
 */
public class MyGridList extends GridView {
    public int type;
    private boolean oneLine = false;

    public MyGridList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridList(Context context) {
        super(context);
    }

    public MyGridList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyGridList(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (oneLine) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec
                    .AT_MOST);
            super.onMeasure(widthMeasureSpec, mExpandSpec);
        }

    }
}

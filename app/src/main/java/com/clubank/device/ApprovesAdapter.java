package com.clubank.device;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.RequestBuilder;
import com.clubank.dingxi.R;
import com.clubank.util.GlideUtil;
import com.clubank.util.MyData;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 活动申请 审批人
 */
public class ApprovesAdapter extends RecyclerView.Adapter<ApprovesAdapter.MyViewHolder> {
    private BaseActivity a;
    private MyData data;

    public ApprovesAdapter(BaseActivity a, MyData da) {
        this.a = a;
        this.data = da;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        MyViewHolder holder = new MyViewHolder(a.getLayoutInflater().inflate(R.layout.item_approves, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        myViewHolder.item_ll.setLayoutParams(new LinearLayout.LayoutParams(a.screen.getWidth() / 3, ViewGroup.LayoutParams.WRAP_CONTENT));
        if (i == data.size() - 1) {
            myViewHolder.item_ll.setGravity(Gravity.LEFT);
        }
        String url = data.get(i).getString("Pic");
        if (!a.checkNull(url)) {
            RequestBuilder<Bitmap> builder = GlideUtil.getInstance().getBitmapBuilder(a, url, R.mipmap.icon_member_input);
            builder.into(myViewHolder.img);
        } else {
            a.setImage(myViewHolder.img, R.mipmap.icon_member_input);
        }
        myViewHolder.title.setText(data.get(i).getString("UserName"));
        if (i == data.size() - 1) {
            myViewHolder.next.setVisibility(View.GONE);
        } else {
            myViewHolder.next.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item_ll;
        ImageView img, next;
        TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            item_ll = itemView.findViewById(R.id.item_ll);
            img = itemView.findViewById(R.id.pic);
            title = itemView.findViewById(R.id.name);
            next = itemView.findViewById(R.id.next);
        }
    }

}
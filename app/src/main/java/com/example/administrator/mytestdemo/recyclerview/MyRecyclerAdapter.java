package com.example.administrator.mytestdemo.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.mytestdemo.R;
import com.example.administrator.mytestdemo.util.KLog;
import com.example.administrator.mytestdemo.util.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 9/7/2016.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyHolder> {
    public List<String> mDataList;

    public MyRecyclerAdapter(List<String> mDataList) {
        this.mDataList = mDataList;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        String s = mDataList.get(position);
        KLog.i("------et_src:" + s);
        KLog.i("------et_src:" + s.length());
        int count = StringUtils.findCtrl(s);
        KLog.i("------et_src_count:" + count);


        // TODO: 9/7/2016
        if (count <= 6 && s.length() < 140) {
            //不需显示全文
        } else {
            //计算是否显示全文
        }


        holder.tv_text.setText(mDataList.get(position));
        final String s2 = holder.tv_text.getText().toString();
        KLog.i("------tv_dst:" + s2);
        KLog.i("------tv_dst:" + s2.length());

        holder.tv_text.post(new Runnable() {
            @Override
            public void run() {
                KLog.i("------tv_dst_count:" + StringUtils.findCtrl(s2));
                KLog.i("------tv_dst_getLineCount:" + holder.tv_text.getLineCount());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }


    static class MyHolder extends RecyclerView.ViewHolder {
        public TextView tv_text;

        public MyHolder(View itemView) {
            super(itemView);
            tv_text = (TextView) itemView.findViewById(R.id.tv_text);
        }
    }


}

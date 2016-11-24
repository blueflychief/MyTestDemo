package com.example.administrator.mytestdemo.recyclerview;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.mytestdemo.R;
import com.example.administrator.mytestdemo.util.KLog;
import com.example.administrator.mytestdemo.widge.recycler.CustomRefreshAdapter;
import com.example.administrator.mytestdemo.widge.recycler.CustomRefreshRecycleView;

import java.util.List;

/**
 * Created by Administrator on 9/7/2016.
 */
public class MyRecyclerAdapter extends CustomRefreshAdapter<MyRecyclerAdapter.MyHolder> {
    public List<String> mDataList;

    /**
     * 创建适配器
     *
     * @param context
     * @param list
     * @param refreshView
     */
    public MyRecyclerAdapter(Context context, List<String> list, CustomRefreshRecycleView refreshView) {
        super(context, list, refreshView);
        mDataList = list;
    }


    @Override
    public MyHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false));
    }

    @Override
    public void onBindHolder(MyHolder holder, int position) {
        holder.tv_text.setText(mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }


    public void addData(boolean isRefresh, final List<String> list) {
        if (isRefresh) {
            mDataList.clear();
            notifyDataSetChanged();
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                KLog.i("----------rangechange");
                if (list != null && list.size() > 0) {
                    mDataList.addAll(list);
                }
                notifyItemRangeChanged(mDataList.size(), list == null ? 0 : list.size());
            }
        }, 1000);

    }

    static class MyHolder extends RecyclerView.ViewHolder {
        public TextView tv_text;

        public MyHolder(View itemView) {
            super(itemView);
            tv_text = (TextView) itemView.findViewById(R.id.tv_text);
        }
    }


}

package com.example.administrator.mytestdemo.recyclerview;

/**
 * Created by Administrator on 10/19/2016.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.mytestdemo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleItemAdapter extends RecyclerView.Adapter<SimpleItemAdapter.ItemHolder> {

    /**
     * 单击处理程序的接口，与 AdapterView 不同，
     * RecyclerView 没有自己的内部接口
     */
    public interface OnItemClickListener {
        public void onItemClick(ItemHolder item, int position);
    }

    private static final String[] ITEMS = {
            "赵...", "钱...", "孙...", "李...",
            "周...", "吴...", "郑...", "王...",
            "冯...", "陈...", "楮...", "卫..."
    };

    private List<String> mItems;

    private OnItemClickListener mOnItemClickListener;
    private LayoutInflater mLayoutInflater;

    public SimpleItemAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);

        // 创建虚拟项的静态列表
        mItems = new ArrayList<>();
        mItems.addAll(Arrays.asList(ITEMS));
        mItems.addAll(Arrays.asList(ITEMS));
    }

    /**
     *  创建
     */
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mLayoutInflater.inflate(R.layout.collection_item, parent, false);
        // 创建新的视图
        return new ItemHolder(itemView, this);
    }

    /**
     *  绑定
     */
    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        // 把 position 位置处的数据附加到新的视图
        holder.setTitle("Item #" + (position + 1));
        holder.setSummary(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /* 管理数据集修改的方法 */
    public void insertItemAtIndex(String item, int position) {
        mItems.add(position, item);
        // 通知视图触发变化动画
        notifyItemInserted(position);
    }

    public void removeItemAtIndex(int position) {
        if (position >= mItems.size())
            return;
        mItems.remove(position);
        // 通知视图触发变化动画
        notifyItemRemoved(position);
    }

    /**
     *  用作与子项关联的元数据(例如当前位置和稳定的 ID)的存储位置，
     *  具体的实现通常还提供对视图内部字段的直接访问，从而尽量减少对 findViewById() 的重复调用
     */
    public static class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private SimpleItemAdapter mParent;
        private TextView mTitleView, mSummaryView;

        public ItemHolder(View itemView, SimpleItemAdapter parent) {
            super(itemView);
            itemView.setOnClickListener(this);
            mParent = parent;

            mTitleView = (TextView)itemView.findViewById(R.id.text_title);
            mSummaryView = (TextView)itemView.findViewById(R.id.text_summary);
        }

        public void setTitle(CharSequence title) {
            mTitleView.setText(title);
        }

        public void setSummary(CharSequence summary) {
            mSummaryView.setText(summary);
        }

        public CharSequence getSummary() {
            return mSummaryView.getText();
        }

        @Override
        public void onClick(View v) {
            // ViewHolder 处理子视图上的单击事件，并将其发送回在适配器上定义的公共监听接口
            // 这样 ViewHolder 就可以在最后的监听器回调中添加位置数据
            final OnItemClickListener listener = mParent.getOnItemClickListener();
            if (listener != null) {
                listener.onItemClick(this, getPosition());
            }
        }
    }
}

package com.example.administrator.mytestdemo.superrecycler;

import android.content.Context;

import com.example.administrator.mytestdemo.R;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;

/**
 * Created by super南仔 on 07/28/16.
 * blog: http://supercwn.github.io/
 * GitHub: https://github.com/supercwn
 */
public class RefreshAndLoadMoreAdapter extends SuperBaseAdapter<String> {

    public RefreshAndLoadMoreAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, String item, int position) {
        holder.setText(R.id.info_text,item);
    }

    @Override
    protected int getItemViewLayoutId(int position, String item) {
        return R.layout.adapte_refresh_load_layout;
    }
}

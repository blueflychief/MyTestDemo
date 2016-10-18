package com.example.administrator.mytestdemo.recyclerview;

/**
 * Created by Administrator on 10/18/2016.
 * http://blog.csdn.net/antimage08/article/details/50199841
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.administrator.mytestdemo.R;

public class MultiRecyclerViewActivity extends AppCompatActivity implements SimpleItemAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private SimpleItemAdapter mAdapter;

    /* 布局管理器 */
    private GridLayoutManager mVerticalGridManager;

    /* 修饰 */
    private ConnectorDecoration mConnectors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecyclerView = new RecyclerView(this);
        mVerticalGridManager = new GridLayoutManager(this, 2,
                LinearLayoutManager.VERTICAL, false);


        // 垂直网格的连接线修饰
        mConnectors = new ConnectorDecoration(this);

        // 交错垂直网格
        mVerticalGridManager.setSpanSizeLookup(new GridStaggerLookup());

        mAdapter = new SimpleItemAdapter(this);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        // 对所有连接应用边缘修饰
        mRecyclerView.addItemDecoration(new InsetDecoration(this));

        // 默认为垂直布局
        selectLayoutManager(R.id.action_grid_vertical);
        setContentView(mRecyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.layout_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return selectLayoutManager(item.getItemId());
    }

    private boolean selectLayoutManager(int id) {
        switch (id) {
            case R.id.action_grid_vertical:
                mRecyclerView.setLayoutManager(mVerticalGridManager);
                mRecyclerView.addItemDecoration(mConnectors);
                return true;
            case R.id.action_add_item:
                // 插入新的项
                mAdapter.insertItemAtIndex("百家姓：", 1);
                return true;
            case R.id.action_remove_item:
                // 删除第一项
                mAdapter.removeItemAtIndex(1);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onItemClick(SimpleItemAdapter.ItemHolder item, int position) {
        Toast.makeText(this, item.getSummary(), Toast.LENGTH_SHORT).show();
    }
}
package com.example.administrator.mytestdemo.superrecycler;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.example.administrator.mytestdemo.R;
import com.superrecycleview.superlibrary.recycleview.ProgressStyle;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SuperRecyclerActivity extends AppCompatActivity implements SuperRecyclerView.LoadingListener {
    private SuperRecyclerView mSuperRecyclerView;
    private RefreshAndLoadMoreAdapter mAdapter;
    private List<String> dataList = new ArrayList<>();
    private List<String> tempList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_recycler);
        initView();
        dataList = initData(20);
        initAdapter();
    }

    private void initView() {
        mSuperRecyclerView = (SuperRecyclerView) findViewById(R.id.super_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mSuperRecyclerView.setLayoutManager(layoutManager);
        mSuperRecyclerView.setRefreshEnabled(true);//可以定制是否开启下拉刷新
        mSuperRecyclerView.setLoadMoreEnabled(true);//可以定制是否开启加载更多
        mSuperRecyclerView.setLoadingListener(this);//下拉刷新，上拉加载的监听
        mSuperRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);//下拉刷新的样式
        mSuperRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);//上拉加载的样式
        mSuperRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);//设置下拉箭头
    }

    private List<String> initData(int size) {
        dataList.clear();
        for (int i = 1; i <= size; i++) {
            dataList.add("数据" + i);
        }
        return dataList;
    }

    //模拟加载更多的数据
    private List<String> getDataList(int size) {
        List<String> data = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            data.add("新加的数据" + i);
        }
        return data;
    }


    private void initAdapter() {
        mAdapter = new RefreshAndLoadMoreAdapter(this, dataList);
        mSuperRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dataList = initData(20);
                mSuperRecyclerView.completeRefresh();
                mAdapter.notifyDataSetChanged();
            }
        }, 3000);
    }

    @Override
    public void onLoadMore() {
        if (dataList.size() >= 50) {
            mSuperRecyclerView.setNoMore(true);
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tempList.clear();
                tempList = getDataList(20);
                dataList.addAll(tempList);
                mSuperRecyclerView.completeLoadMore();
            }
        }, 3000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_super_recycler, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int style = -1;
        switch (item.getItemId()) {
            case R.id.SysProgress:
                style = ProgressStyle.SysProgress;
                break;
            case R.id.BallPulse0:
                style = ProgressStyle.BallPulse;
                break;
            case R.id.BallGridPulse1:
                style = ProgressStyle.BallGridPulse;
                break;
            case R.id.BallClipRotate2:
                style = ProgressStyle.BallClipRotate;
                break;
            case R.id.BallClipRotatePulse3:
                style = ProgressStyle.BallClipRotatePulse;
                break;
            case R.id.SquareSpin4:
                style = ProgressStyle.SquareSpin;
                break;
            case R.id.BallClipRotateMultiple5:
                style = ProgressStyle.BallClipRotateMultiple;
                break;
            case R.id.BallPulseRise6:
                style = ProgressStyle.BallPulseRise;
                break;
            case R.id.BallRotate7:
                style = ProgressStyle.BallRotate;
                break;
            case R.id.CubeTransition8:
                style = ProgressStyle.CubeTransition;
                break;
            case R.id.BallZigZagDeflect10:
                style = ProgressStyle.BallZigZagDeflect;
                break;
            case R.id.BallTrianglePath11:
                style = ProgressStyle.BallTrianglePath;
                break;
            case R.id.BallScale12:
                style = ProgressStyle.BallScale;
                break;
            case R.id.LineScale13:
                style = ProgressStyle.LineScale;
                break;
            case R.id.LineScaleParty14:
                style = ProgressStyle.LineScaleParty;
                break;
            case R.id.BallScaleMultiple15:
                style = ProgressStyle.BallScaleMultiple;
                break;
            case R.id.BallPulseSync16:
                style = ProgressStyle.BallPulseSync;
                break;
            case R.id.BallBeat17:
                style = ProgressStyle.BallBeat;
                break;
            case R.id.LineScalePulseOut18:
                style = ProgressStyle.LineScalePulseOut;
                break;
            case R.id.LineScalePulseOutRapid19:
                style = ProgressStyle.LineScalePulseOutRapid;
                break;
            case R.id.BallScaleRipple20:
                style = ProgressStyle.BallScaleRipple;
                break;
            case R.id.BallScaleRippleMultiple21:
                style = ProgressStyle.BallScaleRippleMultiple;
                break;
            case R.id.BallSpinFadeLoader22:
                style = ProgressStyle.BallSpinFadeLoader;
                break;
            case R.id.LineSpinFadeLoader23:
                style = ProgressStyle.LineSpinFadeLoader;
                break;
            case R.id.TriangleSkewSpin24:
                style = ProgressStyle.TriangleSkewSpin;
                break;
            case R.id.Pacman25:
                style = ProgressStyle.Pacman;
                break;
            case R.id.BallGridBeat26:
                style = ProgressStyle.BallGridBeat;
                break;
            case R.id.SemiCircleSpin27:
                style = ProgressStyle.SemiCircleSpin;
                break;

        }
        mSuperRecyclerView.setRefreshProgressStyle(style);
        return super.onOptionsItemSelected(item);
    }
}

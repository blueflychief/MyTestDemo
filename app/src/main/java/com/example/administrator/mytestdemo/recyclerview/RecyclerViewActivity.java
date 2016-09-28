package com.example.administrator.mytestdemo.recyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.example.administrator.mytestdemo.widge.recycler.CustomRefreshRecycleView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        CustomRefreshRecycleView.OnLoadMoreListener {
    private static final String testText = "__?????2006年1月20日，发行EP《霍元甲》，主打歌《霍元甲》是李连杰主演的\n" +
            "杂志封面\n" +
            "杂志封面(21张)\n" +
            " 同名电影的主题曲[74]  ；2月5日至6日，在日本东京举行演唱会；9月，发行专辑《依然范特西》；该专辑延续了周杰伦以往的音乐风格，并融合了中国风、RAP等曲风，其中与费玉清合唱的中国风歌曲《千里之外》获得第13届全球华语音乐榜中榜年度最佳歌曲奖、第29届十大中文金曲全国最受欢迎中文歌曲奖等奖项[75-76] ";
    private CustomRefreshRecycleView mRefreshRecycleView;
    private MyRecyclerAdapter mMyRecyclerAdapter;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRefreshRecycleView = new CustomRefreshRecycleView(this);
        setContentView(mRefreshRecycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRefreshRecycleView.setLayoutManager(linearLayoutManager);
        mMyRecyclerAdapter = new MyRecyclerAdapter(this, initTestData(), mRefreshRecycleView);
        mRefreshRecycleView.setAdapter(mMyRecyclerAdapter);
        mRefreshRecycleView.setOnLoadMoreListener(this);
        mRefreshRecycleView.setOnRefreshListener(this);

    }

    private List<String> initTestData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            index += 1;
            list.add(index + testText);
        }
        return list;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                index = 0;
                mMyRecyclerAdapter.addData(true, initTestData());
                mRefreshRecycleView.onRefreshAndLoadComplete(true);
            }
        }, 3000);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mMyRecyclerAdapter.addData(false, initTestData());
                mRefreshRecycleView.onRefreshAndLoadComplete(true);
            }
        }, 3000);
    }
}

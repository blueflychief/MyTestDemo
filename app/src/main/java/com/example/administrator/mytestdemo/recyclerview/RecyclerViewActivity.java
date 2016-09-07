package com.example.administrator.mytestdemo.recyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {
    private static final String testText = "2006年1月20日，发行EP《霍元甲》，主打歌《霍元甲》是李连杰主演的\n" +
            "杂志封面\n" +
            "杂志封面(21张)\n" +
            " 同名电影的主题曲[74]  ；2月5日至6日，在日本东京举行演唱会；9月，发行专辑《依然范特西》；该专辑延续了周杰伦以往的音乐风格，并融合了中国风、RAP等曲风，其中与费玉清合唱的中国风歌曲《千里之外》获得第13届全球华语音乐榜中榜年度最佳歌曲奖、第29届十大中文金曲全国最受欢迎中文歌曲奖等奖项[75-76] ";
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_recycler_view);
        recycler=new RecyclerView(this);
        setContentView(recycler);
//        recycler = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(new MyRecyclerAdapter(initTestData()));

    }

    private List<String> initTestData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(testText);
        }
        return list;
    }
}

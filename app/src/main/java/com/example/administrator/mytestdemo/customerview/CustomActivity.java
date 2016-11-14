package com.example.administrator.mytestdemo.customerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.mytestdemo.R;
import com.example.administrator.mytestdemo.customerview.views.PieData;
import com.example.administrator.mytestdemo.customerview.views.PieView;

import java.util.ArrayList;

public class CustomActivity extends AppCompatActivity {

    private PieView pie_view;
    private ArrayList<PieData> mData = new ArrayList<PieData>() {{
        add(new PieData("张三", 10));
        add(new PieData("里三", 22));
        add(new PieData("王三", 44));
        add(new PieData("张三", 11));
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        pie_view = (PieView) findViewById(R.id.pie_view);
        pie_view.setData(mData);
    }
}

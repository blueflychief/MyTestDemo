package com.example.administrator.mytestdemo.popwindow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.example.administrator.mytestdemo.R;
import com.example.administrator.mytestdemo.popwindow.pop.PopupWindowUtils;

public class PopwindowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popwindow);
        findViewById(R.id.bt_pop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当前位置弹出
                View view = LayoutInflater.from(PopwindowActivity.this).inflate(R.layout.ppw_textview_layout, null);
                PopupWindowUtils.Builder builder = new PopupWindowUtils.Builder(v, view);
                builder.setShowAtCurrentPosition(true);
                builder.create();

//                View view = LayoutInflater.from(PopwindowActivity.this).inflate(R.layout.ppw_popwindow_layout, null);
//                PopupWindowUtils.createPopWindowFromBottom(findViewById(R.id.bt_pop),view);   //底部弹出
            }
        });

        findViewById(R.id.bt_pop1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                View view = LayoutInflater.from(PopwindowActivity.this).inflate(R.layout.ppw_textview_layout, null);
//                PopupWindowUtils.Builder builder = new PopupWindowUtils.Builder(v, view);
//                builder.setShowAtCurrentPosition(true);
//                builder.create();

                View view = LayoutInflater.from(PopwindowActivity.this).inflate(R.layout.ppw_popwindow_layout, null);
                PopupWindowUtils.createPopWindowFromBottom(findViewById(R.id.bt_pop), view);   //底部弹出
            }
        });
    }
}

package com.example.administrator.mytestdemo.tinker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.administrator.mytestdemo.R;
import com.example.administrator.mytestdemo.util.ToastUtils;

public class TinkerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinker);
        findViewById(R.id.btClick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast("这是补丁之前的");
            }
        });
    }
}

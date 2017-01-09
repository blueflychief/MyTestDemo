package com.example.administrator.mytestdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class ViewStubActivity extends AppCompatActivity {
    private ViewStub vs1;
    private ViewStub vs2;
    private LinearLayout view1;
    private ScrollView view2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stub);
        vs1 = (ViewStub) findViewById(R.id.vs1);
        vs2 = (ViewStub) findViewById(R.id.vs2);
        findViewById(R.id.btLoadView1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view1 == null) {
                    view1 = (LinearLayout) vs1.inflate();
                }
            }
        });

        findViewById(R.id.btLoadView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view2 == null) {
                    view2 = (ScrollView) vs2.inflate();
                }
            }
        });
    }
}

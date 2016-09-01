package com.example.administrator.mytestdemo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.mytestdemo.util.KLog;
import com.example.administrator.mytestdemo.widge.DotIndicatorView;
import com.example.administrator.mytestdemo.widge.ExpandTextView;
import com.example.administrator.mytestdemo.widge.TextViewExpandableAnimation;

public class LeakTestActivity extends AppCompatActivity {
    private static Drawable sBackground;
    private TextView label;
    private ExpandTextView expand_view;
    private TextView tv_switch;
    private boolean mExpanded = false;
    private DotIndicatorView mDotView;
    private int current = 1;
    private TextViewExpandableAnimation mTextViewExpandableAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KLog.i("-----onCreate");
//        label = new TextView(this);
//        label.setText("Leaks are bad");
//        if (sBackground == null) {
//            sBackground = this.getResources().getDrawable(R.mipmap.ic_launcher);
//        }
//        label.setBackgroundDrawable(sBackground);//drawable attached to a view

        setContentView(R.layout.activity_leak_test);
        expand_view = (ExpandTextView) findViewById(R.id.expand_view);
        mDotView = (DotIndicatorView) findViewById(R.id.dv);
        expand_view.setOnExpandStateChangeListener(new ExpandTextView.OnExpandStateChangeListener() {
            @Override
            public void onExpandStateChanged(TextView textView, boolean isExpanded) {
                mExpanded = isExpanded;
            }
        });

        tv_switch = (TextView) findViewById(R.id.tv_switch);
        tv_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                expand_view.changeTextView();
                mDotView.setCurrentDot(current > 4 ? current-- : current++);
            }
        });


        mTextViewExpandableAnimation= (TextViewExpandableAnimation) findViewById(R.id.tv_expand_text);
        mTextViewExpandableAnimation.setText(getString(R.string.tips));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KLog.i("-----onDestroy");
    }
}

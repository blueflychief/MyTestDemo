package com.example.administrator.mytestdemo.stringbuilder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.mytestdemo.R;

public class SpannableStringBuilderActivity extends AppCompatActivity {

    private TextView tvLabel1;
    private BadgeView bvText;
    int index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spannable_string_builder);
        tvLabel1 = (TextView) findViewById(R.id.tvLabel1);
        bvText = (BadgeView) findViewById(R.id.bvText);
        initViews();
        findViewById(R.id.btChange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index++;
                bvText.setText(Integer.toString(index));
            }
        });
    }

    private void initViews() {
        String text1 = "这是一个简单的示例";
        tvLabel1.setText(text1);
    }
}

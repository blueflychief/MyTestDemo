package com.example.administrator.mytestdemo.textlength;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.mytestdemo.R;
import com.example.administrator.mytestdemo.util.KLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextLengthActivity extends AppCompatActivity {
    private static final String CRLF_STR = "(\r\n|\r|\n|\n\r)";
    private EditText et_src;
    private Button bt_add;
    private TextView tv_dst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_length);
        et_src = (EditText) findViewById(R.id.et_src);
        bt_add = (Button) findViewById(R.id.bt_add);
        tv_dst = (TextView) findViewById(R.id.tv_dst);
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = et_src.getText().toString();
                KLog.i("------et_src:" + s);
                KLog.i("------et_src:" + s.length());
                KLog.i("------et_src_count:" + findCtrl(s));


                tv_dst.setText(s);

                final String s2 = tv_dst.getText().toString();
                KLog.i("------tv_dst:" + s2);
                KLog.i("------tv_dst:" + s2.length());

                tv_dst.post(new Runnable() {
                    @Override
                    public void run() {
                        KLog.i("------tv_dst_count:" + findCtrl(s2));
                        KLog.i("------tv_dst_getLineCount:" + tv_dst.getLineCount());
                    }
                });

            }
        });
    }

    private int findCtrl(String s) {
        int c = 0;
        Pattern pattern = Pattern.compile(CRLF_STR);
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            c++;
        }
        return c;
    }
}

package com.example.administrator.mytestdemo.textlength;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.mytestdemo.R;
import com.example.administrator.mytestdemo.util.KLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextLengthActivity extends AppCompatActivity {
    private static final String CRLF_STR = "(\r\n|\r|\n|\n\r)";
    private EditText et_src;
    private Button bt_add;
    private TextView tv_dst;
    private TextView tv;

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

        tv = (TextView) findViewById(R.id.tv_span);
        StringBuilder sbBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sbBuilder.append("username-" + i + "、");
        }
        String likeUsers = sbBuilder.substring(0, sbBuilder.lastIndexOf("、")).toString();
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setText(addClickablePart(likeUsers), TextView.BufferType.SPANNABLE);
        //去掉超链接背景
        tv.setHighlightColor(getResources().getColor(android.R.color.transparent));

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


    private SpannableStringBuilder addClickablePart(String str) {
        // 第一个赞图标
        ImageSpan span = new ImageSpan(this, R.drawable.icon_green_arrow_down);
        SpannableString spanStr = new SpannableString("p.");
        spanStr.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder ssb = new SpannableStringBuilder(spanStr);
        ssb.append(str);
        String[] likeUsers = str.split("、");
        if (likeUsers.length > 0) {
            // 最后一个
            for (int i = 0; i < likeUsers.length; i++) {
                final String name = likeUsers[i];
                final int start = str.indexOf(name) + spanStr.length();
                ssb.setSpan(new ClickableSpan() {

                    @Override
                    public void onClick(View widget) {
                        Toast.makeText(TextLengthActivity.this, name,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        if (name.equals("username-1")) {
                            ds.setColor(Color.RED); // 设置文本颜色
                        }
                        // 去掉下划线
                        ds.setUnderlineText(false);
                    }

                }, start, start + name.length(), 0);
            }
        }
        return ssb.append("等"
                + likeUsers.length + "个人赞了您.");
    } // end of addClickablePart
}

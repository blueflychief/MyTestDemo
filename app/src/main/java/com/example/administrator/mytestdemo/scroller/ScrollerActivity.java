package com.example.administrator.mytestdemo.scroller;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.mytestdemo.R;

public class ScrollerActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout activity_scroller;
    ScrollerLayout ll_root;
    Button bt_scroll_to;
    Button bt_scroll_by;
    Button bt_hide_toolbar;
    Button bt_hide_toolbar_statusbar;
    Button bt_hide_toolbar_to_statusbar;
    Button bt_show_toolbar_to_statusbar;

    Toolbar mToolbar;
    private Button bt_normal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroller);
        activity_scroller = (LinearLayout) findViewById(R.id.activity_scroller);
        ll_root = (ScrollerLayout) findViewById(R.id.ll_root);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Title");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        bt_scroll_to = (Button) findViewById(R.id.bt_scroll_to);
        bt_scroll_by = (Button) findViewById(R.id.bt_scroll_by);
        bt_hide_toolbar = (Button) findViewById(R.id.bt_hide_toolbar);
        bt_hide_toolbar_statusbar = (Button) findViewById(R.id.bt_hide_toolbar_statusbar);
        bt_hide_toolbar_to_statusbar = (Button) findViewById(R.id.bt_hide_toolbar_to_statusbar);
        bt_show_toolbar_to_statusbar = (Button) findViewById(R.id.bt_show_toolbar_to_statusbar);
        bt_normal = (Button) findViewById(R.id.bt_normal);

        View view = LayoutInflater.from(this).inflate(R.layout.view_inflate_test, activity_scroller, false);
        activity_scroller.addView(view);
        bt_scroll_to.setOnClickListener(this);
        bt_scroll_by.setOnClickListener(this);
        bt_hide_toolbar.setOnClickListener(this);
        bt_hide_toolbar_statusbar.setOnClickListener(this);
        bt_show_toolbar_to_statusbar.setOnClickListener(this);
        bt_hide_toolbar_to_statusbar.setOnClickListener(this);
        bt_normal.setOnClickListener(this);

        ImageView imageView = new ImageView(this);
        Drawable icon = getResources().getDrawable(R.drawable.ic_test_1);
        Drawable tintIcon = DrawableCompat.wrap(icon);
        DrawableCompat.setTintList(tintIcon, getResources().getColorStateList(R.color.white));
        imageView.setImageDrawable(tintIcon);
    }

    /**
     * 0：正常显示toolbar和statubar
     * 1：隐藏toolbar，显示statubar
     * 2：隐藏toolbar和statubar
     * 3：显示toolbar，内容沉浸到statubar（5.0以上有效）
     * 4：隐藏toolbar，内容沉浸到statubar（5.0以上有效）
     *
     * @param status
     */
    private void changeDecorView(int status) {
        int option = 0;
        switch (status) {
            case 0:
                option = View.SYSTEM_UI_FLAG_VISIBLE;
                break;
            case 1:
                option = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                break;
            case 2:
                if (Build.VERSION.SDK_INT >= 16) {
                    option = View.SYSTEM_UI_FLAG_FULLSCREEN;
                }
                break;
            case 3:
            case 4:
                if (Build.VERSION.SDK_INT >= 21) {
                    option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                }
                break;
        }
        getWindow().getDecorView().setSystemUiVisibility(option);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(status == 3 || status == 4 ? Color.TRANSPARENT : getResources().getColor(R.color.colorPrimaryDark));
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (status == 4 || status == 2 || status == 1) {
                actionBar.hide();
            } else {
                actionBar.show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_scroll_by:
                ll_root.scrollTo(-20, 20);
                break;
            case R.id.bt_scroll_to:
                ll_root.scrollBy(-20, 20);
                break;
            case R.id.bt_normal:
                changeDecorView(0);
                break;
            case R.id.bt_hide_toolbar:
                changeDecorView(1);
                break;
            case R.id.bt_hide_toolbar_statusbar:
                changeDecorView(2);
                break;
            case R.id.bt_show_toolbar_to_statusbar:
                changeDecorView(3);
                break;
            case R.id.bt_hide_toolbar_to_statusbar:
                changeDecorView(4);
                break;

        }
    }
}

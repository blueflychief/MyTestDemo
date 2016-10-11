package com.example.administrator.mytestdemo.premission;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.administrator.mytestdemo.R;
import com.example.administrator.mytestdemo.premission.yzjpermission.AndPermission;
import com.example.administrator.mytestdemo.premission.yzjpermission.PermissionListener;
import com.example.administrator.mytestdemo.util.KLog;
import com.example.administrator.mytestdemo.util.ToastUtils;

public class YzjPermissionActivity extends AppCompatActivity implements PermissionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yzj_permission);

        findViewById(R.id.bt_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndPermission.with(YzjPermissionActivity.this).requestCode(100).permission(Manifest.permission.CAMERA).send();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, this);
    }

    @Override
    public void onSucceed(int requestCode) {
        if (requestCode==100) {
            ToastUtils.showToast("请求成功");
        }
    }

    @Override
    public void onFailed(int requestCode) {
        if (requestCode==100) {
            ToastUtils.showToast("请求失败");
        }
    }
}

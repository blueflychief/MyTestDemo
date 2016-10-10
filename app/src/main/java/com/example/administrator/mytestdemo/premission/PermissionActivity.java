package com.example.administrator.mytestdemo.premission;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.administrator.mytestdemo.R;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;
import com.zhy.m.permission.ShowRequestPermissionRationale;

public class PermissionActivity extends AppCompatActivity {

    private Button mBtnSdcard, mBtnCallPhone,id_btn_camera;
    private static final int REQUECT_CODE_SDCARD = 2;
    private static final int REQUECT_CODE_CALL_PHONE = 3;
    private static final int REQUECT_CODE_CAMERA = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        mBtnSdcard = (Button) findViewById(R.id.id_btn_sdcard);
        mBtnCallPhone = (Button) findViewById(R.id.id_btn_callphone);
        id_btn_camera = (Button) findViewById(R.id.id_btn_camera);

        mBtnSdcard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (!MPermissions.shouldShowRequestPermissionRationale(PermissionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUECT_CODE_SDCARD))
                {
                    MPermissions.requestPermissions(PermissionActivity.this, REQUECT_CODE_SDCARD, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
        });

        mBtnCallPhone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MPermissions.requestPermissions(PermissionActivity.this, REQUECT_CODE_CALL_PHONE, Manifest.permission.CALL_PHONE);
            }
        });

        id_btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MPermissions.shouldShowRequestPermissionRationale(PermissionActivity.this,Manifest.permission.CAMERA,REQUECT_CODE_CAMERA)) {
                }
            }
        });
    }

    @ShowRequestPermissionRationale(REQUECT_CODE_SDCARD)
    public void whyNeedSdCard()
    {
        Toast.makeText(this, "I need write news to sdcard!", Toast.LENGTH_SHORT).show();
//        MPermissions.requestPermissions(PermissionActivity.this, REQUECT_CODE_SDCARD, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @PermissionGrant(REQUECT_CODE_SDCARD)
    public void requestSdcardSuccess()
    {
        Toast.makeText(this, "GRANT ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied(REQUECT_CODE_SDCARD)
    public void requestSdcardFailed()
    {
        Toast.makeText(this, "DENY ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
    }


    @PermissionGrant(REQUECT_CODE_CALL_PHONE)
    public void requestCallPhoneSuccess()
    {
        Toast.makeText(this, "GRANT ACCESS CALL_PHONE!", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied(REQUECT_CODE_CALL_PHONE)
    public void requestCallPhoneFailed()
    {
        Toast.makeText(this, "DENY ACCESS CALL_PHONE!", Toast.LENGTH_SHORT).show();
    }
}

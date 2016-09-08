package com.example.administrator.mytestdemo.stickview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.administrator.mytestdemo.R;
import com.example.administrator.mytestdemo.util.FileUtil;

import java.io.File;

public class StickerViewActivity extends AppCompatActivity {
    private StickerView mStickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_view_activity);
        mStickerView = (StickerView) findViewById(R.id.sticker_view);
        mStickerView.setBackgroundColor(Color.WHITE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 110);
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.haizewang_2);
            Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.haizewang_23);
            Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.haizewang_150);
            mStickerView.addSticker(bitmap);
            mStickerView.addSticker(bitmap1);
            mStickerView.addSticker(bitmap2);
            mStickerView.addSticker(new DialogDrawable());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 110
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.haizewang_2);
            Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.haizewang_23);
            Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.haizewang_150);
            mStickerView.addSticker(bitmap);
            mStickerView.addSticker(bitmap1);
            mStickerView.addSticker(bitmap2);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_save:
                File file = FileUtil.getNewFile(StickerViewActivity.this, "Sticker");
                if (file != null) {
                    mStickerView.save(file);
                    Toast.makeText(StickerViewActivity.this, "saved in " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StickerViewActivity.this, "the file is null", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

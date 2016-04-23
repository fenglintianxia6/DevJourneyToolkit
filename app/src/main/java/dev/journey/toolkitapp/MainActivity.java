package dev.journey.toolkitapp;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mwp.uitoolkit.ReadBitmapFromFileTask;
import com.example.mwp.uitoolkit.WaterMark;
import com.example.mwp.uitoolkit.WriteBitmapToFileTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dev.journey.toolkit.faulttolerant.DialogUtils;
import dev.journey.toolkit.faulttolerant.ToastUtils;
import dev.journey.toolkit.util.StdFileUtils;

public class MainActivity extends AppCompatActivity {
    float density;
    ImageView imageView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        density = getResources().getDisplayMetrics().density;
        imageView = (ImageView) findViewById(R.id.image);
        progressDialog = new ProgressDialog(this);
        getBitmap();

    }

    private void getBitmap() {
        final String filePath = StdFileUtils.getSdCardFile(this, "Download", "text_watermark.jpg").getAbsolutePath();
        ReadBitmapFromFileTask task = new ReadBitmapFromFileTask(this, new ReadBitmapFromFileTask.IReadBitmapFromFileListener() {
            @Override
            public void onStart() {
                progressDialog.setMessage("正在读取图片...");
                DialogUtils.showDialog(MainActivity.this, progressDialog);
            }

            @Override
            public void onSuccess(Bitmap src) {
                int width = src.getWidth();
                int height = src.getHeight();
                int min = Math.min(width, height);
                WaterMark.TextWaterMarkConfig textWaterMarkConfig = new WaterMark.TextWaterMarkConfig()
                        .textColor(getResources().getColor(R.color.water_mark_text_color))
                        .textSize(min / 10);
                Bitmap bitmap = WaterMark.putWaterMark(src, "mwp2016-04-23 16:52:26", textWaterMarkConfig);
                saveBitmap(bitmap, filePath);
            }

            @Override
            public void onFailure(Throwable throwable) {
                DialogUtils.dismissDialog(MainActivity.this, progressDialog);
                ToastUtils.showToast(MainActivity.this, throwable.getMessage());
            }
        }, filePath);
        task.checkAndStart();
    }


    private void saveBitmap(Bitmap bitmap, String srcFilePath) {
        File srcFile = new File(srcFilePath);
        File dir = srcFile.getParentFile();
        File targetFile = new File(dir, "WaterMark_" + srcFile.getName());
        WriteBitmapToFileTask task = new WriteBitmapToFileTask(this, new WriteBitmapToFileTask.IWriteBitmapFromFileListener() {
            @Override
            public void onStart() {
                progressDialog.setMessage("正在保存水印图片...");
                DialogUtils.showDialog(MainActivity.this, progressDialog);
            }

            @Override
            public void onSuccess(File file) {
                DialogUtils.dismissDialog(MainActivity.this, progressDialog);
                ToastUtils.showToast(MainActivity.this, "文件保存在" + file.getAbsolutePath());
                Glide.with(MainActivity.this).load(file).into(imageView);
            }

            @Override
            public void onFailure(Throwable throwable) {
                DialogUtils.dismissDialog(MainActivity.this, progressDialog);
                ToastUtils.showToast(MainActivity.this, throwable.getMessage());
            }
        }, bitmap, targetFile.getAbsolutePath());
        task.checkAndStart();
    }

}

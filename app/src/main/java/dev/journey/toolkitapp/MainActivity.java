package dev.journey.toolkitapp;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import dev.journey.toolkit.util.TimeUtils;
import dev.journey.uitoolkit.ReadBitmapFromFileTask;
import dev.journey.uitoolkit.WaterMark;
import dev.journey.uitoolkit.WriteBitmapToFileTask;

import java.io.File;

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
                Bitmap bitmap = WaterMark.putWaterMark(src, "mwp " + TimeUtils.createTimeStamp(), textWaterMarkConfig);
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
        File targetFile = new File(dir, "WM_AT_" + TimeUtils.createTimeStamp() + "_" + srcFile.getName());
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

package dev.journey.toolkitapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.journey.toolkit.faulttolerant.DialogUtils;
import dev.journey.toolkit.faulttolerant.ToastUtils;
import dev.journey.toolkit.task.FileDirDeleteTask;
import dev.journey.toolkit.task.FileDirLengthCalculateTask;
import dev.journey.toolkit.util.L;
import dev.journey.toolkit.util.StdFileUtils;
import dev.journey.toolkit.util.StringUtils;

public class MainActivity extends AppCompatActivity {
    float density;
    ProgressDialog progressDialog;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        density = getResources().getDisplayMetrics().density;
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        File dir = StdFileUtils.getSdCardDir(MainActivity.this, "QuFenQiBD", false);
        calculateCacheSize(dir);
    }

    private void calculateCacheSize(File dir) {
        FileDirLengthCalculateTask task = new FileDirLengthCalculateTask(this, new FileDirLengthCalculateTask.FileDirLengthCalculateListener() {
            @Override
            public void onStart() {
                textView.setText("正在计算缓存大小");
            }

            @Override
            public void onFailure(Throwable throwable) {
                textView.setText("获取缓存大小失败！");
            }

            @Override
            public void onSuccess(long length) {
                textView.setText(StringUtils.formatDouble(StdFileUtils.toMB(length)) + "MB");
            }
        });
        task.addFileOrDir(dir);
        task.checkAndStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear_cache: {
                File dir = StdFileUtils.getSdCardDir(MainActivity.this, "QuFenQiBD");
                clearCache(dir);
                long length = StdFileUtils.getFileDirectorySize(dir);
                L.d("MainActivity", "after deleteFile, dir length = " + StringUtils.formatDouble(StdFileUtils.toMB(length)) + "MB");
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void clearCache(File dir) {
        FileDirDeleteTask dirDeleteTask = new FileDirDeleteTask(this, new FileDirDeleteTask.IFileDirDeleteListener() {
            @Override
            public void onStart() {
                progressDialog.setMessage("正在清理缓存，请稍候...");
                DialogUtils.showDialog(MainActivity.this, progressDialog);
            }

            @Override
            public void onFailure(Throwable throwable) {
                DialogUtils.dismissDialog(MainActivity.this, progressDialog);
                ToastUtils.showToast(MainActivity.this, throwable.getMessage());
            }

            @Override
            public void onSuccess() {
                DialogUtils.dismissDialog(MainActivity.this, progressDialog);
                ToastUtils.showToast(MainActivity.this, "清理成功");
                File dir = StdFileUtils.getSdCardDir(MainActivity.this, "QuFenQiBD", false);
                long length = StdFileUtils.getFileDirectorySize(dir);
                textView.setText(StringUtils.formatDouble(StdFileUtils.toMB(length)) + "MB");
            }
        }).addFileOrDir(dir);
        dirDeleteTask.checkAndStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
}

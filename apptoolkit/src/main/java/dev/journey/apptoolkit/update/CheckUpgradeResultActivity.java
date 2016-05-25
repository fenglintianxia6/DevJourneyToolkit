package dev.journey.apptoolkit.update;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

public class CheckUpgradeResultActivity extends AppCompatActivity {
    UpgradeInfoProvider provider;
    Config config;

    public static Intent getStartIntent(Context context, UpgradeInfoProvider upgradeInfoProvider, Config config) {
        Intent starter = new Intent(context, CheckUpgradeResultActivity.class);
        starter.putExtra(UpgradeInfoProvider.TAG, upgradeInfoProvider);
        starter.putExtra(Config.TAG, config);
        return starter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new FrameLayout(this));
        provider = (UpgradeInfoProvider) getIntent().getSerializableExtra(UpgradeInfoProvider.TAG);
        config = (Config) getIntent().getSerializableExtra(Config.TAG);
        if (provider == null || config == null) {
            finish();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (provider != null) {
            showUpgradeDialog();
        }
    }

    private void showUpgradeDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("发现新版本：" + provider.getNewVersionName() + "，是否更新?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FileDownloadService.download(CheckUpgradeResultActivity.this, config, provider.getApkDownloadUrl());
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!provider.isForceUpgrade()) {
                            finish();
                        } else {
                            Intent intent = new Intent(CheckUpgradeResultActivity.this, config.getActivityClass());
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("force_finish", true);
                            startActivity(intent);
                        }
                    }
                })
                .setCancelable(false)
                .create().show();
    }

}

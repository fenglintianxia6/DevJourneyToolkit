package dev.journey.apptoolkit.update;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;

import java.io.File;

import dev.journey.toolkit.util.FileDownloadListener;
import dev.journey.toolkit.util.ProgressFileDownloader;
import dev.journey.toolkit.util.StdFileUtils;

/**
 * 处理文件下载
 *
 * @author mwp on 2015年4月8日 下午2:44:17
 */
public class FileDownloadService extends IntentService {
    private static final String SERVICE_NAME = "dev.journey.apptoolkit.update.FileDownloadService";
    private Config config;
    private String url;
    protected int mCurrentProgress;
    private NotificationManagerCompat notificationManagerCompat;
    private NotificationCompat.Builder mBuilder;

    public FileDownloadService() {
        super(SERVICE_NAME);
    }

    final FileDownloadListener fileDownloadListener = new FileDownloadListener() {
        @Override
        public void onDownloadComplete(File f) {
            StdFileUtils.installApk(FileDownloadService.this, f);
        }

        @Override
        public void onFailure(Throwable throwable) {
            if (throwable != null) {
                throwable.printStackTrace();
            }
        }

        @Override
        public void onStart() {
            mBuilder = new Builder(FileDownloadService.this);
            mBuilder.setAutoCancel(false);
            mBuilder.setSmallIcon(config.getSmallIcon());
            mBuilder.setContentTitle("正在下载新版本");
        }


        @Override
        public void update(long bytesRead, long contentLength, boolean done) {
            if (done) {
                notificationManagerCompat.cancel(0);
            } else {
                int progress = (int) (bytesRead * 100 / contentLength);
                if (mCurrentProgress == progress) {
                    return;
                }
                mCurrentProgress = progress;
                mBuilder.setProgress(100, progress, false);
                mBuilder.setContentText("下载进度：" + progress + "%");
                notificationManagerCompat.notify(0, mBuilder.build());
            }
        }
    };

    private void createNotification() {
        notificationManagerCompat = NotificationManagerCompat.from(this);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setAutoCancel(false);
    }


    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        config = (Config) bundle.getSerializable(Config.TAG);
        url = bundle.getString("url");
        if (config != null && !TextUtils.isEmpty(url)) {
            createNotification();
            if (config != null && !TextUtils.isEmpty(url)) {
                File targetDir = StdFileUtils.getSdCardDir(this, "download");
                ProgressFileDownloader downloader = new ProgressFileDownloader(fileDownloadListener);
                ProgressFileDownloader.DownloadRequest request = new ProgressFileDownloader.DownloadRequest()
                        .downloadUrl(url)
                        .fileName(StdFileUtils.getFileNameFromUrl(url))
                        .targetDir(targetDir == null ? null : targetDir.getAbsolutePath());
                downloader.download(request);
            }
        }
    }

    public static void download(Context context, Config config, String url) {
        /**
         * 下载链接或者目标文件夹地址不合法不予下载
         */
        if (config == null || TextUtils.isEmpty(url)) {
            return;
        }
        Intent downloadIntent = new Intent(context, FileDownloadService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Config.TAG, config);
        bundle.putSerializable("url", url);
        downloadIntent.putExtras(bundle);
        context.startService(downloadIntent);
    }
}
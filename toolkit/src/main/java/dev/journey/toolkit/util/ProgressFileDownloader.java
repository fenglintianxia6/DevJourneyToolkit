package dev.journey.toolkit.util;

import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import dev.journey.toolkit.retrofit.ProgressResponseBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 带进度条的下载器
 * Created by mwp on 2016/2/25.
 */
public class ProgressFileDownloader {
    private OkHttpClient client;
    FileDownloadListener fileDownloadListener;

    public ProgressFileDownloader(FileDownloadListener listener) {
        this.fileDownloadListener = listener;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);
        if (fileDownloadListener != null) {
            builder.addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .body(new ProgressResponseBody(originalResponse.body(), fileDownloadListener))
                            .build();
                }
            });
        }
        client = builder.build();
    }

    public static class DownloadRequest {
        private String targetDir;
        private String fileName;
        private String downloadUrl;

        public DownloadRequest targetDir(String targetDir) {
            this.targetDir = targetDir;
            return this;
        }

        public DownloadRequest fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public DownloadRequest downloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
            return this;
        }
    }

    public void download(DownloadRequest downloadRequest) {

        if (fileDownloadListener == null) {
            return;
        }

        String targetDir = downloadRequest.targetDir;
        String downloadUrl = downloadRequest.downloadUrl;
        String fileName = downloadRequest.fileName;

        if (TextUtils.isEmpty(targetDir) || TextUtils.isEmpty(downloadUrl) || TextUtils.isEmpty(fileName)) {
            fileDownloadListener.onFailure(new Exception("targetDir , fileName or downloadUrl can not be empty! targetDir="
                    + targetDir + ";fileName=" + fileName + ";downloadUrl=" + downloadUrl));
            return;
        }
        File dir = new File(targetDir);
        // 检查文件夹是否存在，不存在则创建
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!dir.exists()) {
            /**
             * 创建目录失败
             */
            fileDownloadListener.onFailure(new Exception("can not create file dir:" + dir));
            return;
        }

        final File file = new File(dir, fileName);

        fileDownloadListener.onStart();
        Request request = new Request.Builder().url(downloadUrl).build();
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                byte[] buf = new byte[2048];
                int len = 0;
                is = response.body().byteStream();
                fos = new FileOutputStream(file);
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                fos.flush();
                fileDownloadListener.onDownloadComplete(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fileDownloadListener.onFailure(e);
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                fileDownloadListener.onFailure(e);
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                fileDownloadListener.onFailure(e);
            }
        }
    }

}

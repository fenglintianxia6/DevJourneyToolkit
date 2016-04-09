package dev.journey.toolkit.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 文件下载
 * Created by mwp on 16/4/9.
 */
public class FileDownloader {
    private OkHttpClient client;

    public FileDownloader() {
        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }

    public void download(File targetFile, String downloadUrl) throws Exception {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            Request request = new Request.Builder()
                    .url(downloadUrl)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                byte[] buf = new byte[2048];
                int len = 0;
                is = response.body().byteStream();
                fos = new FileOutputStream(targetFile);
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                fos.flush();
            }
        } catch (Exception e) {
            L.e("download", e);
            throw e;
        } finally {
            try {
                if (is != null) is.close();
            } catch (Exception e) {
                L.e("download", e);
                throw e;
            }
            try {
                if (fos != null) fos.close();
            } catch (Exception e) {
                L.e("download", e);
                throw e;
            }
        }
    }
}

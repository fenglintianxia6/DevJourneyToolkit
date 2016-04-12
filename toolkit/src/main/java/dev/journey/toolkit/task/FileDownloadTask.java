package dev.journey.toolkit.task;

import android.app.Activity;
import android.text.TextUtils;

import java.io.File;

import dev.journey.toolkit.util.FileDownloader;
import dev.journey.toolkit.util.L;
import dev.journey.toolkit.util.StdFileUtils;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 文件下载
 * Created by mwp on 2016/4/11.
 */
public class FileDownloadTask extends AbsTask {
    private IFileDownloadListener listener;
    private String downloadUrl;

    public FileDownloadTask(Activity activity, IFileDownloadListener listener, String downloadUrl) {
        super(activity);
        if (activity != null) {
            this.listener = listener;
            this.downloadUrl = downloadUrl;
        }
    }

    @Override
    protected void onStart() {
        if (TextUtils.isEmpty(downloadUrl)) {
            provideListener().onFailure(new Exception("downloadUrl can not be empty!"));
            return;
        }
        provideListener().onStart();

        Single.create(new Single.OnSubscribe<File>() {
            @Override
            public void call(SingleSubscriber singleSubscriber) {
                if (isCallbackReady()) {
                    try {
                        File targetFile = StdFileUtils.getSdCardFile(getActivity(), "download", getFileNameFromUrl(downloadUrl));
                        FileDownloader fileDownloader = new FileDownloader();
                        fileDownloader.download(targetFile, downloadUrl);
                        singleSubscriber.onSuccess(targetFile);
                        L.d("FileDownloadTask download", "success file=" + targetFile.getAbsolutePath() + ";length=" + targetFile.length());
                    } catch (Exception e) {
                        L.e("FileDownloadTask download", e);
                        singleSubscriber.onError(e);
                    }
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        if (isCallbackReady()) {
                            provideListener().onSuccess(file);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (isCallbackReady()) {
                            provideListener().onFailure(throwable);
                        }
                    }
                });
    }

    private static String getFileNameFromUrl(String url) {
        if (TextUtils.isEmpty(url) || !url.contains("/")) {
            return url;
        }
        return url.substring(url.lastIndexOf("/"), url.length());
    }

    @Override
    public IFileDownloadListener provideListener() {
        return listener;
    }

    public interface IFileDownloadListener extends ITaskListener {

        void onFailure(Throwable throwable);

        void onStart();

        void onSuccess(File file);
    }
}

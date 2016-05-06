package dev.journey.toolkit.task;

import android.app.Activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dev.journey.toolkit.util.StdFileUtils;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by mengweiping on 16/5/5.
 */
public class FileDirLengthCalculateTask extends AbsTask {
    FileDirLengthCalculateListener listener;
    List<File> dirList = new ArrayList<>();

    public FileDirLengthCalculateTask(Activity activity, FileDirLengthCalculateListener listener) {
        super(activity);
        this.listener = listener;
    }

    public FileDirLengthCalculateTask addFileOrDir(File file) {
        if (file != null) {
            dirList.add(file);
        }
        return this;
    }

    @Override
    protected void onStart() {
        if (dirList.isEmpty()) {
            listener.onSuccess(0);
            return;
        }
        listener.onStart();
        Single.create(new Single.OnSubscribe<Long>() {
            @Override
            public void call(SingleSubscriber<? super Long> singleSubscriber) {
                try {
                    long totalSize = 0;
                    for (File dir : dirList) {
                        totalSize += StdFileUtils.getFileDirectorySize(dir);
                    }
                    singleSubscriber.onSuccess(totalSize);
                } catch (Exception e) {
                    singleSubscriber.onError(e);
                }
                dirList.clear();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
            @Override
            public void call(Long value) {
                listener.onSuccess(value);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                listener.onFailure(throwable);
            }
        });
    }

    @Override
    public ITaskListener provideListener() {
        return listener;
    }


    public interface FileDirLengthCalculateListener extends ITaskListener {

        void onStart();

        void onFailure(Throwable throwable);

        void onSuccess(long length);
    }
}

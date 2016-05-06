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
public class FileDirDeleteTask extends AbsTask {
    IFileDirDeleteListener listener;
    List<File> dirList = new ArrayList<>();

    public FileDirDeleteTask(Activity activity, IFileDirDeleteListener listener) {
        super(activity);
        this.listener = listener;
    }

    public FileDirDeleteTask addFileOrDir(File file) {
        if (file != null) {
            dirList.add(file);
        }
        return this;
    }

    @Override
    protected void onStart() {
        if (dirList.isEmpty()) {
            listener.onFailure(new Exception("dirList can not be empty!"));
            return;
        }
        listener.onStart();
        Single.create(new Single.OnSubscribe<Object>() {
            @Override
            public void call(SingleSubscriber<? super Object> singleSubscriber) {
                try {
                    for (File dir : dirList) {
                        StdFileUtils.deleteFile(dir.getAbsolutePath());
                    }
                    singleSubscriber.onSuccess(true);
                } catch (Exception e) {
                    singleSubscriber.onError(e);
                }
                dirList.clear();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                listener.onSuccess();
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


    public interface IFileDirDeleteListener extends ITaskListener {

        void onStart();

        void onFailure(Throwable throwable);

        void onSuccess();
    }
}

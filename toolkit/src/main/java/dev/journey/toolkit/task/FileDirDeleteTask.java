package dev.journey.toolkit.task;

import android.app.Activity;

import java.io.File;

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
    File dir;

    public FileDirDeleteTask(Activity activity, IFileDirDeleteListener listener, File dir) {
        super(activity);
        this.listener = listener;
        this.dir = dir;
    }

    @Override
    protected void onStart() {
        if (!StdFileUtils.isFileOrDirExists(dir)) {
            listener.onFailure(new Exception("dir " + dir + " is not exists"));
            return;
        }
        listener.onStart();
        Single.create(new Single.OnSubscribe<Object>() {
            @Override
            public void call(SingleSubscriber<? super Object> singleSubscriber) {
                boolean deleted = StdFileUtils.deleteFile(dir.getAbsolutePath());
                singleSubscriber.onSuccess(deleted);
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

package com.example.mwp.uitoolkit;

import android.app.Activity;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import dev.journey.toolkit.task.AbsTask;
import dev.journey.toolkit.task.ITaskListener;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by mwp on 16/4/23.
 */
public class WriteBitmapToFileTask extends AbsTask {
    IWriteBitmapFromFileListener listener;
    String filePath;
    Bitmap bitmap;

    public WriteBitmapToFileTask(Activity activity, IWriteBitmapFromFileListener listener, Bitmap bitmap, String filePath) {
        super(activity);
        this.listener = listener;
        this.filePath = filePath;
        this.bitmap = bitmap;
    }

    @Override
    protected void onStart() {
        listener.onStart();

        if (bitmap == null) {
            listener.onFailure(new Exception("bitmap is " + bitmap));
            recycle();
            return;
        }
        Single.create(new Single.OnSubscribe<File>() {
            @Override
            public void call(SingleSubscriber<? super File> singleSubscriber) {
                try {
                    File file = new File(filePath);
                    OutputStream outputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    singleSubscriber.onSuccess(file);
                } catch (Exception e) {
                    singleSubscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        recycle();
                        if (isCallbackReady()) {
                            listener.onSuccess(file);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        recycle();
                        if (isCallbackReady()) {
                            listener.onFailure(throwable);
                        }
                    }
                });
    }


    private void recycle() {
        bitmap = null;
    }

    @Override
    public IWriteBitmapFromFileListener provideListener() {
        return listener;
    }

    public interface IWriteBitmapFromFileListener extends ITaskListener {

        void onStart();

        void onSuccess(File file);

        void onFailure(Throwable throwable);
    }
}

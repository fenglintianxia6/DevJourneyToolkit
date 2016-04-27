package dev.journey.uitoolkit;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import dev.journey.toolkit.task.AbsTask;
import dev.journey.toolkit.task.ITaskListener;
import dev.journey.toolkit.util.StdFileUtils;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by mwp on 16/4/23.
 */
public class ReadBitmapFromFileTask extends AbsTask {
    IReadBitmapFromFileListener listener;
    String filePath;

    public ReadBitmapFromFileTask(Activity activity, IReadBitmapFromFileListener listener, String filePath) {
        super(activity);
        this.listener = listener;
        this.filePath = filePath;
    }

    @Override
    protected void onStart() {
        listener.onStart();

        if (!StdFileUtils.isFileExists(filePath)) {
            listener.onFailure(new Exception("File " + filePath + " is not exists"));
            return;
        }

        Single.create(new Single.OnSubscribe<Bitmap>() {
            @Override
            public void call(SingleSubscriber<? super Bitmap> singleSubscriber) {
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                if (bitmap == null) {
                    singleSubscriber.onError(new Exception("unable to decode stream,filePath=" + filePath));
                } else {
                    singleSubscriber.onSuccess(bitmap);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        if (isCallbackReady()) {
                            listener.onSuccess(bitmap);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (isCallbackReady()) {
                            listener.onFailure(throwable);
                        }
                    }
                });
    }

    @Override
    public IReadBitmapFromFileListener provideListener() {
        return listener;
    }

    public interface IReadBitmapFromFileListener extends ITaskListener {

        void onStart();

        void onSuccess(Bitmap bitmap);

        void onFailure(Throwable throwable);
    }
}

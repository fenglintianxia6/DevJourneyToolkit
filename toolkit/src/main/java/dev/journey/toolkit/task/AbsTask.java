package dev.journey.toolkit.task;

import android.app.Activity;

import java.lang.ref.SoftReference;

/**
 * Created by mwp on 2016/4/11.
 */
public abstract class AbsTask {
    protected SoftReference<Activity> activityRef;

    public AbsTask(Activity activity) {
        if (activity != null) {
            this.activityRef = new SoftReference<Activity>(activity);
        }
    }

    /**
     * 判断是否需要执行task。只有当Activity不为finishing并且监听接口不为null时才继续进行
     */
    public final void checkAndStart() {
        if (isCallbackReady()) {
            onStart();
        }
    }

    public Activity getActivity() {
        if (activityRef != null) {
            return activityRef.get();
        } else {
            return null;
        }
    }

    protected boolean isCallbackReady() {
        return isActivityReady() && provideListener() != null;
    }

    protected boolean isActivityReady() {
        Activity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            return true;
        } else {
            return false;
        }
    }

    protected abstract void onStart();

    public abstract ITaskListener provideListener();


}

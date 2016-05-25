package dev.journey.apptoolkit.update;

import android.app.Activity;

import java.io.Serializable;

/**
 * Created by mengweiping on 16/5/24.
 */
public class Config implements Serializable {
    public static final String TAG = "Config";
    private int smallIcon;
    private Class activityClass;

    public Config smallIcon(int smallIcon) {
        this.smallIcon = smallIcon;
        return this;
    }

    public Class getActivityClass() {
        return activityClass;
    }

    public Config activityClass(Class activityClass) {
        this.activityClass = activityClass;
        return this;
    }

    public int getSmallIcon() {
        return smallIcon;
    }
}

package dev.journey.apptoolkit.update;

import java.io.Serializable;

/**
 * Created by mengweiping on 16/5/24.
 */
public class Config implements Serializable {
    public static final String TAG = "Config";
    private String url;
    private int smallIcon;
    private Class<?> pendingIntentClass;

    public Config url(String url) {
        this.url = url;
        return this;
    }

    public Config smallIcon(int smallIcon) {
        this.smallIcon = smallIcon;
        return this;
    }

    public Config pendingIntentClass(Class<?> pendingIntentClass) {
        this.pendingIntentClass = pendingIntentClass;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public int getSmallIcon() {
        return smallIcon;
    }

    public Class<?> getPendingIntentClass() {
        return pendingIntentClass;
    }
}

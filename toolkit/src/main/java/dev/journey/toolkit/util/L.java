package dev.journey.toolkit.util;

import android.util.Log;

/**
 * Created by mwp on 2016/4/8.
 */
public class L {
    private static boolean sDebug = true;
    private static final String LOG_TAG = "DevJourneyToolkit";

    public static void setDebug(boolean debug) {
        sDebug = debug;
    }

    /**
     * 注意这里的action不是tag
     *
     * @param action
     * @param message
     */
    public static final void d(String action, String message) {
        if (sDebug) {
            Log.d(LOG_TAG, action + ": " + message);
        }
    }

    public static final void e(String action, Exception e) {
        if (e == null) {
            return;
        }
        if (sDebug) {
            Log.d(LOG_TAG, action, e);
        }
    }
}

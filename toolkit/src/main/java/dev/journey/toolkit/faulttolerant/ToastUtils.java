package dev.journey.toolkit.faulttolerant;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by mwp on 16/4/22.
 */
public class ToastUtils {
    public static final int SHOW_DURATION_MILLIS = 3000;
    private static long lastShowTime;
    private static String lastMsg;

    public static void showToast(Context context, String msg) {
        /**
         * 不显示空白的toast
         */
        if (context == null || TextUtils.isEmpty(msg)) {
            return;
        }
        if (TextUtils.isEmpty(lastMsg) || !lastMsg.equals(msg) || lastShowTime + SHOW_DURATION_MILLIS < Calendar.getInstance().getTimeInMillis()) {
            try {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                lastMsg = msg;
                lastShowTime = Calendar.getInstance().getTimeInMillis();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //do nothing
        }
    }
}

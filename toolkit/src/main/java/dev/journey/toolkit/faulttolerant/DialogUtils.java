package dev.journey.toolkit.faulttolerant;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import dev.journey.toolkit.util.L;

/**
 * 对话框有关的容错，放到这里
 * Created by mwp on 2016/4/8.
 */
public class DialogUtils {
    public static void showDialog(Context context, Dialog dialog) {
        if (context == null || dialog == null || dialog.isShowing()) {
            return;
        }

        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                L.d("DialogUtils showDialog", "isFinishing");
                return;
            }
        }
        try {
            dialog.show();
        } catch (Exception e) {
            L.e("DialogUtils showDialog", e);
        }
    }

    public static void dismissDialog(Context context, Dialog dialog) {
        if (context == null || dialog == null || !dialog.isShowing()) {
            return;
        }

        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                L.d("DialogUtils dismissDialog", "isFinishing");
                return;
            }
        }
        try {
            dialog.dismiss();
        } catch (Exception e) {
            L.e("DialogUtils dismissDialog", e);
        }
    }
}

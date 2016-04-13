package dev.journey.toolkit.util;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * Created by mwp on 16/4/13.
 */
public class VersionUtils {
    /**
     * 比较两个版本号的大小
     * 如果version1>version2，返回1，小于则返回-1，相等则返回0
     *
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2) {
        if (TextUtils.isEmpty(version1) || TextUtils.isEmpty(version2)) {
            return 0;
        }
        long a = 0, b = 0;
        int v1len = version1.length();
        int v2len = version2.length();
        int i = 0, j = 0;
        while (i < v1len || j < v2len) {
            a = 0;
            b = 0;
            while (i < v1len && version1.charAt(i) != '.') {
                a = a * 10 + version1.charAt(i) - '0';
                ++i;
            }
            ++i;
            while (j < v2len && version2.charAt(j) != '.') {
                b = b * 10 + version2.charAt(j) - '0';
                ++j;
            }
            ++j;
            if (a > b)
                return 1;
            if (a < b)
                return -1;
        }
        return 0;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersionName(Activity activity) {
        String versionName = "1.0.0";
        try {
            PackageManager manager = activity.getPackageManager();
            PackageInfo info = manager.getPackageInfo(activity.getPackageName(), 0);
            versionName = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }
}

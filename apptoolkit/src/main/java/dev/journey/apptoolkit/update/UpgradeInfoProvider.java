package dev.journey.apptoolkit.update;

import java.io.Serializable;

/**
 * Created by mengweiping on 16/5/24.
 */
public interface UpgradeInfoProvider extends Serializable {
    String TAG = "UpgradeInfoProvider";

    /**
     * 接口返回的最新版本号，如1.1.2
     *
     * @return
     */
    String getNewVersionName();

    /**
     * 是否强制升级
     *
     * @return
     */
    boolean isForceUpgrade();

    /**
     * Apk文件的下载地址
     *
     * @return
     */
    String getApkDownloadUrl();
}

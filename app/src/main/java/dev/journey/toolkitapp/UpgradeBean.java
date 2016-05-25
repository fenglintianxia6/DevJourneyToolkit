package dev.journey.toolkitapp;

import java.io.Serializable;

import dev.journey.apptoolkit.update.UpgradeInfoProvider;

/**
 * Created by mengweiping on 16/5/25.
 */
public class UpgradeBean implements UpgradeInfoProvider {

    /**
     * code : 0
     * data : {"hasnew":0,"version":"1.7.0","force_update":0,"url":"http://static001.qufenqi.com/mobile/android/QuFenQiApp_1.7.0_qufenqi.apk"}
     * message :
     */

    private int code;
    /**
     * hasnew : 0
     * version : 1.7.0
     * force_update : 0
     * url : http://static001.qufenqi.com/mobile/android/QuFenQiApp_1.7.0_qufenqi.apk
     */

    private DataBean data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getNewVersionName() {
        return data == null ? null : data.getVersion();
    }

    @Override
    public boolean isForceUpgrade() {
        return data == null ? false : 1 == data.getForce_update();
    }

    @Override
    public String getApkDownloadUrl() {
        return data == null ? null : data.getUrl();
    }

    public static class DataBean implements Serializable {
        private int hasnew;
        private String version;
        private int force_update;
        private String url;

        public int getHasnew() {
            return hasnew;
        }

        public void setHasnew(int hasnew) {
            this.hasnew = hasnew;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public int getForce_update() {
            return force_update;
        }

        public void setForce_update(int force_update) {
            this.force_update = force_update;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}

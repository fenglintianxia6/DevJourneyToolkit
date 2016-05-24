package dev.journey.apptoolkit.update;

/**
 * Created by mengweiping on 16/5/24.
 */
public interface DataInterceptor {
    /**
     * 升级接口的返回数据，调用者在这里定义数据解析
     *
     * @param data
     */
    UpgradeInfoProvider intercept(Object data);
}

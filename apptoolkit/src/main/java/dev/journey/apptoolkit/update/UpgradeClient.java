package dev.journey.apptoolkit.update;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.io.IOException;
import java.util.Map;

import dev.journey.toolkit.config.ApiConfig;
import dev.journey.toolkit.retrofit.DefaultOkHttpClientBuilder;
import dev.journey.toolkit.retrofit.RetrofitCallback;
import dev.journey.toolkit.retrofit.RetrofitComponentFactory;
import dev.journey.toolkit.util.HttpUtils;
import dev.journey.toolkit.util.L;
import dev.journey.toolkit.util.VersionUtils;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * 检查升级
 * Created by mengweiping on 16/5/24.
 */
public class UpgradeClient {
    public static final long NON_FORCE_UPDATE_TIME_INTERVAL = 1000 * 60 * 60 * 24 * 3;
    UpgradeApiService apiService;
    Context mContext;
    String currentVersionName = "1.0.0";
    DataInterceptor dataInterceptor;

    public UpgradeClient(Activity activity, String baseUrl, DataInterceptor dataInterceptor) {
        currentVersionName = VersionUtils.getVersionName(activity);
        mContext = activity.getApplicationContext();
        this.dataInterceptor = dataInterceptor;
        OkHttpClient okHttpClient = DefaultOkHttpClientBuilder.newInstance(mContext)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request.Builder builder = originalRequest.newBuilder();
                        if (!TextUtils.isEmpty(HttpUtils.defaultUserAgent())) {
                            builder.header(ApiConfig.KEY_USER_AGENT, HttpUtils.defaultUserAgent());
                        }
                        Request newHeaderRequest = builder.build();
                        return chain.proceed(newHeaderRequest);
                    }
                })
                .build();
        Retrofit retrofit = RetrofitComponentFactory.newGsonRxJavaRetrofitBuilder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .build();
        apiService = retrofit.create(UpgradeApiService.class);
    }

    public void checkUpgrade(String url, Map<String, String> map, final Config config) {
        if (dataInterceptor == null) {
            return;
        }
        Call<Object> call = apiService.check(url, map);
        call.enqueue(new RetrofitCallback<Object>() {
            @Override
            public void onDataSuccess(Call<Object> call, Object data) {
                UpgradeInfoProvider upgInfo = dataInterceptor.intercept(data);
                L.d("UpgradeClient", "data = " + data.toString());
                if (upgInfo != null && !TextUtils.isEmpty(upgInfo.getApkDownloadUrl()) && !TextUtils.isEmpty(upgInfo.getNewVersionName())) {
                    String targetVersion = upgInfo.getNewVersionName();
                    int compare = VersionUtils.compareVersion(targetVersion, currentVersionName);
                    boolean forceUpg = upgInfo.isForceUpgrade();
                    boolean needUpdate;
                    if (compare > 0) {
                        if (forceUpg) {
                            needUpdate = true;
                        } else {
                            boolean ignoreExpired = VersionUtils.isVersionIgnoreExpired(mContext, targetVersion, NON_FORCE_UPDATE_TIME_INTERVAL);
                            if (ignoreExpired) {
                                needUpdate = true;
                            } else {
                                needUpdate = false;
                            }
                        }
                    } else {
                        needUpdate = false;
                    }
                    if (needUpdate && mContext != null) {
                        Intent intent = CheckUpgradeResultActivity.getStartIntent(mContext, upgInfo, config);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    } else {
                    }
                } else {
                }
            }
        });
    }

    public static void handleNewIntent(Activity activity, Intent intent) {
        if (intent != null && activity != null && intent.getBooleanExtra("force_finish", false)) {
            activity.finish();
        }
    }
}

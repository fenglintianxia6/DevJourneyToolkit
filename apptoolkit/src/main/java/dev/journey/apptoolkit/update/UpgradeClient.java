package dev.journey.apptoolkit.update;

import android.content.Context;
import android.text.TextUtils;

import java.io.IOException;
import java.util.Map;

import dev.journey.toolkit.config.ApiConfig;
import dev.journey.toolkit.retrofit.DefaultOkHttpClientBuilder;
import dev.journey.toolkit.retrofit.RetrofitCallback;
import dev.journey.toolkit.retrofit.RetrofitComponentFactory;
import dev.journey.toolkit.util.HttpUtils;
import dev.journey.toolkit.util.L;
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
    UpgradeApiService apiService;
    DataInterceptor dataInterceptor;

    public UpgradeClient(Context context, String baseUrl, DataInterceptor dataInterceptor) {
        this.dataInterceptor = dataInterceptor;
        OkHttpClient okHttpClient = DefaultOkHttpClientBuilder.newInstance(context)
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

    public void checkUpgrade(String url, Map<String, String> map) {
        if (dataInterceptor == null) {
            return;
        }
        Call<Object> call = apiService.check(url, map);
        call.enqueue(new RetrofitCallback<Object>() {
            @Override
            public void onDataSuccess(Call<Object> call, Object data) {
                UpgradeInfoProvider provider = dataInterceptor.intercept(data);
                L.d("UpgradeClient", "data = " + data.toString());
            }
        });
    }
}

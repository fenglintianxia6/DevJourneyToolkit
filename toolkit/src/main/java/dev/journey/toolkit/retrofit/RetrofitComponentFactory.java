package dev.journey.toolkit.retrofit;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

/**
 * Created by mwp on 2016/2/25.
 */
public class RetrofitComponentFactory {
    public static Retrofit.Builder createRetrofitBuilder() {
        Retrofit.Builder builder = new Retrofit.Builder().client(GlobalOkHttpManager.getInstance().get())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        return builder;
    }

    public static Retrofit createRetrofit(String baseUrl) {
        return createRetrofitBuilder().baseUrl(baseUrl).build();
    }
}

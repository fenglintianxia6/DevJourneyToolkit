package dev.journey.toolkit.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mwp on 2016/2/25.
 */
public class RetrofitComponentFactory {
    public static Retrofit.Builder newGsonRxJavaRetrofitBuilder() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        return builder;
    }
}

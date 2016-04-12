package dev.journey.toolkit.retrofit;

import android.content.Context;

import okhttp3.OkHttpClient;


/**
 * Created by mwp on 2015/12/28.
 */
public class GlobalOkHttpManager {
    private static GlobalOkHttpManager sHttpResponseFetcher;
    private static boolean sGetInstanceAllowed = false;

    private OkHttpClient mOkHttpClient;

    private GlobalOkHttpManager(Context context) {
        mOkHttpClient = DefaultOkHttpClientBuilder.newInstance(context).build();
    }

    public OkHttpClient get() {
        return mOkHttpClient;
    }

    public static synchronized GlobalOkHttpManager createInstance(Context context) {
        if (sHttpResponseFetcher == null) {
            sHttpResponseFetcher = new GlobalOkHttpManager(context.getApplicationContext());
        }
        setGetInstanceIsAllowed();
        return sHttpResponseFetcher;
    }

    public static GlobalOkHttpManager getInstance() {
        checkInstanceIsAllowed();
        return sHttpResponseFetcher;
    }

    static void setGetInstanceIsAllowed() {
        sGetInstanceAllowed = true;
    }

    private static void checkInstanceIsAllowed() {
        if (!sGetInstanceAllowed) {
            throw new IllegalStateException(
                    "HttpResponseFetcher::createInstance() needs to be called "
                            + "before HttpResponseFetcher::getInstance()");
        }
    }
}

package dev.journey.toolkit.retrofit;

import android.content.Context;
import android.webkit.CookieManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dev.journey.toolkit.util.StdFileUtils;
import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * Created by mwp on 2016/1/21.
 */
public class DefaultOkHttpClientBuilder {
    public static final int DEFAULT_CACHE_SIZE = 10 * 1024 * 1024; // 10 MiB

    public static OkHttpClient.Builder newInstance(Context context) {
        Cache cache = new Cache(StdFileUtils.getDiskCacheDir(context, "okhttp"), DEFAULT_CACHE_SIZE);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .cache(cache)
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> cookies) {
                        if (cookies != null) {
                            CookieUtils.saveToCookieManager(cookies);
                        }
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                        List<Cookie> cookies = new ArrayList<Cookie>();
                        try {
                            URL url = httpUrl.url();
                            String domain = CookieUtils.getDomain(url);
                            if (domain != null) {
                                String cookieOfWebView = CookieManager.getInstance().getCookie(domain);
                                List<Cookie> cookieListOfWebView = CookieUtils.parseCookieList(httpUrl, cookieOfWebView);
                                cookies = CookieUtils.mergeCookie(cookies, cookieListOfWebView);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return cookies;
                    }
                });

        return builder;
    }
}

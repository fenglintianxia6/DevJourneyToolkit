package dev.journey.toolkit.retrofit;

import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dev.journey.toolkit.util.L;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by mwp on 2016/1/14.
 */
public class CookieUtils {

    public static void synCookieToCookieManager() {
        try {
            CookieSyncManager.getInstance().sync();
        } catch (Exception e) {
            L.e("CookieUtils", e);
        }
    }

    public static List<Cookie> parseCookieList(HttpUrl url, String strCookies) {
        if (TextUtils.isEmpty(strCookies)) {
            return null;
        }
        String[] strSingleCookies = strCookies.split(";");
        if (strSingleCookies == null || strSingleCookies.length == 0) {
            return null;
        }
        Cookie cookie;
        List<Cookie> cookieList = new ArrayList<Cookie>();

        for (String setCookie : strSingleCookies) {
            cookie = Cookie.parse(url, setCookie);
            if (cookie != null) {
                cookieList.add(cookie);
            }
        }
        return cookieList;
    }

    public static String getDomain(URL url) {
        String domain = null;
        String host = url.getHost();
        if (host != null) {
            domain = host;
        }
        return domain;
    }

    /**
     * 获取n位的随机数
     *
     * @param n
     * @return
     */
    public static String getRandomStr(int n) {
        Random r = new Random();
        String result = "";
        for (int i = 0; i < n; i++) {
            result += (r.nextInt(9)) % 10;
        }
        return result;
    }

    public static void saveToCookieManager(List<Cookie> cookieList) {
        if (cookieList != null) {
            CookieManager cookieManager = CookieManager.getInstance();
            for (Cookie cookie : cookieList) {
                cookieManager.setCookie(cookie.domain(), cookie.toString());
            }
        }
    }

    public static List<Cookie> mergeCookie(List<Cookie> sourceList, List<Cookie> inputList) {
        if (sourceList == null) {
            sourceList = new ArrayList<>();
        }
        if (inputList == null) {
            return sourceList;
        }
        for (Cookie cookie : inputList) {
            if (!contains(sourceList, cookie)) {
                sourceList.add(cookie);
            }
        }
        return sourceList;
    }

    public static boolean contains(List<Cookie> sourceList, Cookie input) {
        for (Cookie cookie : sourceList) {
            if (cookie.name().equals(input.name())) {
                return true;
            }
        }
        return false;
    }

    public static String getCookieOfWebView(String url) {
        String domain = CookieUtils.getDomainOfUrl(url);
        if (TextUtils.isEmpty(domain)) {
            return null;
        }
        return CookieManager.getInstance().getCookie(domain);
    }

    public static String getDomainOfUrl(String url) {
        return getDomainOfUrl(url, "");
    }

    public static String getDomainOfUrl(String url, String defaultDomain) {
        return getHostOfUrl(url, defaultDomain);
    }

    public static String getHostOfUrl(String url, String defaultDomain) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        String host = null;
        try {
            host = new URL(url).getHost();
            if (!TextUtils.isEmpty(defaultDomain) && !TextUtils.isEmpty(host)) {
                if (host.contains(defaultDomain)) {
                    return defaultDomain;
                }
            }
        } catch (Exception e) {
        }
        return host;
    }

}

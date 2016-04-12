package dev.journey.toolkit.util;

import java.net.FileNameMap;
import java.net.URLConnection;

import okhttp3.internal.Util;
import okhttp3.internal.Version;

/**
 * Created by mwp on 2016/1/21.
 */
public class HttpUtils {
    public static String getContentTypeFor(String filename) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(filename);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    public static String defaultUserAgent() {
        String agent = System.getProperty("http.agent");
        return agent != null ? Util.toHumanReadableAscii(agent) : Version.userAgent();
    }
}

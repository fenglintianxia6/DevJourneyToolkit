package dev.journey.toolkit.config;

import dev.journey.toolkit.util.HttpUtils;
import okhttp3.MediaType;

/**
 * Created by mwp on 2016/1/22.
 */
public class ApiConfig {

    public static final String KEY_USER_AGENT = "User-Agent";
    public static final String DEFAULT_USER_AGENT = HttpUtils.defaultUserAgent();

    public static MediaType MEDIA_TYPE_OCTET_STREAM = MediaType.parse("application/octet-stream");
    public static MediaType MEDIA_TYPE_TEXT_PLAIN = MediaType.parse("text/plain; charset=UTF-8");
}

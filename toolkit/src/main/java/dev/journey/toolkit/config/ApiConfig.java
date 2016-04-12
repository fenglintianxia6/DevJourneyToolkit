package dev.journey.toolkit.config;

import dev.journey.toolkit.util.HttpUtils;

/**
 * Created by mwp on 2016/1/22.
 */
public class ApiConfig {
    public static final String KEY_USER_AGENT = "User-Agent";
    public static final String DEFAULT_USER_AGENT = HttpUtils.defaultUserAgent();
}

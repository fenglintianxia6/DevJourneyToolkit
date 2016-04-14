package dev.journey.faceid.data.service;

import java.util.Map;

import dev.journey.toolkit.config.ApiConfig;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

/**
 * Created by mwp on 16/4/14.
 */
public interface FaceIdApiService {
    @POST
    @Multipart
    Call<Object> verify(@Url String url,
                            @PartMap Map<String, RequestBody> partMap,
                            @Header(ApiConfig.KEY_USER_AGENT) String userAgent);

}

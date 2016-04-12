package dev.journey.toolkit.data.service;

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
 * Created by mwp on 2016/4/11.
 */
public interface FileUploadApiService {
    @POST
    @Multipart
    Call<Object> uploadFile(@Url String url,
                            @PartMap Map<String, RequestBody> partMap,
                            @Header(ApiConfig.KEY_USER_AGENT) String userAgent);
}

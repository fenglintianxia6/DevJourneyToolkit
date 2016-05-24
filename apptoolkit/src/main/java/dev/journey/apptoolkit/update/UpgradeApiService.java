package dev.journey.apptoolkit.update;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by mengweiping on 16/5/24.
 */
public interface UpgradeApiService {
    @GET
    Call<Object> check(@Url String url,
                       @QueryMap Map<String, String> partMap);
}

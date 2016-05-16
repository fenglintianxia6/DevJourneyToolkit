package dev.journey.toolkit.task;

import android.app.Activity;
import android.text.TextUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import dev.journey.toolkit.config.ApiConfig;
import dev.journey.toolkit.data.service.FileUploadApiService;
import dev.journey.toolkit.retrofit.DefaultOkHttpClientBuilder;
import dev.journey.toolkit.retrofit.RetrofitCallback;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mwp on 2016/4/11.
 */
public class FileUploadTask extends AbsTask {

    FileUploadApiService mFileUploadApiService;
    IFileUploadListener listener;
    File file;
    String url;
    String key;

    public FileUploadTask(Builder builder) {
        super(builder.activity);
        this.listener = builder.listener;
        this.file = builder.file;
        this.key = builder.key;
        this.url = builder.url;
        if (getActivity() != null && listener != null) {
            mFileUploadApiService = new Retrofit.Builder()
                    .baseUrl(builder.baseUrl)
                    .client(DefaultOkHttpClientBuilder.newInstance(getActivity()).build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build()
                    .create(FileUploadApiService.class);
        }
    }

    public static class Builder {
        Activity activity;
        String key;
        String baseUrl;
        String url;
        File file;
        IFileUploadListener listener;

        public Builder activity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder file(String key, File file) {
            this.key = key;
            this.file = file;
            return this;
        }

        public Builder listener(IFileUploadListener listener) {
            this.listener = listener;
            return this;
        }

        public FileUploadTask build() {
            return new FileUploadTask(this);
        }
    }

    @Override
    protected void onStart() {
        if (mFileUploadApiService == null) {
            return;
        }

        if (!(file != null && file.isFile() && file.exists())) {
            provideListener().onFailure(new Exception("file is not legal! file=" + file));
            return;
        }

        if (TextUtils.isEmpty(url)) {
            provideListener().onFailure(new Exception("uploadUrl is not legal! url=" + url));
            return;
        }

        provideListener().onStart();
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody fileBody = RequestBody.create(ApiConfig.MEDIA_TYPE_OCTET_STREAM, file);
        map.put(key + "\"; filename=\"" + file.getName(), fileBody);
        Call<Object> call = mFileUploadApiService.uploadFile(url, map, ApiConfig.DEFAULT_USER_AGENT);
        call.enqueue(new RetrofitCallback<Object>() {

            @Override
            public void onFailure(Call<Object> c, Throwable t) {
                if (isCallbackReady()) {
                    provideListener().onFailure(t);
                }
            }

            @Override
            public void onDataSuccess(Call<Object> c,Object data) {
                if (isCallbackReady()) {
                    provideListener().onSuccess(data);
                }
            }
        });
    }

    @Override
    public IFileUploadListener provideListener() {
        return listener;
    }

    public interface IFileUploadListener extends ITaskListener {

        void onStart();

        void onFailure(Throwable throwable);

        void onSuccess(Object data);
    }
}

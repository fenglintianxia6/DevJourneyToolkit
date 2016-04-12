package dev.journey.toolkit.task;

import android.app.Activity;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import dev.journey.toolkit.config.ApiConfig;
import dev.journey.toolkit.data.service.FileUploadApiService;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

/**
 * Created by mwp on 2016/4/11.
 */
public class FileUploadTask extends AbsTask {
    public static final MediaType MEDIA_TYPE_OCTET_STREAM = MediaType.parse("application/octet-stream");

    FileUploadApiService mFileUploadApiService;
    SoftReference<IFileUploadListener> listenerRef;
    File file;
    String uploadUrl;
    String key;

    public FileUploadTask(Activity activity, IFileUploadListener listener, String baseUrl, String uploadUrl, File file) {
        this(activity, listener, baseUrl, uploadUrl, file, "file");
    }

    public FileUploadTask(Activity activity, IFileUploadListener listener, String baseUrl, String uploadUrl, File file, String key) {
        super(activity);
        if (activity != null && listener != null) {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
            mFileUploadApiService = builder.build().create(FileUploadApiService.class);
            listenerRef = new SoftReference<>(listener);
            this.file = file;
            this.uploadUrl = uploadUrl;
            this.key = key;
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

        if (TextUtils.isEmpty(uploadUrl)) {
            provideListener().onFailure(new Exception("uploadUrl is not legal! uploadUrl=" + uploadUrl));
            return;
        }

        provideListener().onStart();
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody fileBody = RequestBody.create(MEDIA_TYPE_OCTET_STREAM, file);
        map.put(key + "\"; filename=\"" + file.getName(), fileBody);
        Call<Object> call = mFileUploadApiService.uploadFile(uploadUrl, map, ApiConfig.DEFAULT_USER_AGENT);
        call.enqueue(new Callback<Object>() {


            @Override
            public void onResponse(Response<Object> response) {
                if (response != null && response.isSuccess()) {
                    Object dataObj = response.body();
                    if (dataObj != null) {
                        this.onDataSuccess(new Gson().toJson(dataObj));
                    } else {
                        this.onFailure(this.buildException("response isSuccess but its body is null!"));
                    }
                } else if (response != null) {
                    String errorMsg = response.code() + ":" + response.message();
                    this.onFailure(this.buildException(errorMsg));
                } else {
                    this.onFailure(this.buildException("response is null!"));
                }
            }

            private Exception buildException(String msg) {
                return new Exception(msg);
            }

            private void onDataSuccess(String data) {
                if (isCallbackReady()) {
                    provideListener().onSuccess(data);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (isCallbackReady()) {
                    provideListener().onFailure(t);
                }
            }
        });
    }

    @Override
    public IFileUploadListener provideListener() {
        return listenerRef != null ? listenerRef.get() : null;
    }

    public interface IFileUploadListener extends ITaskListener {

        void onStart();

        void onFailure(Throwable throwable);

        void onSuccess(String result);
    }
}

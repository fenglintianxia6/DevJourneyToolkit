package dev.journey.faceid.task;

import android.app.Activity;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.journey.faceid.data.service.FaceIdApiService;
import dev.journey.toolkit.config.ApiConfig;
import dev.journey.toolkit.retrofit.RetrofitCallback;
import dev.journey.toolkit.task.AbsTask;
import dev.journey.toolkit.task.ITaskListener;
import dev.journey.toolkit.util.StdFileUtils;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

/**
 * Created by mwp on 2016/4/12.
 */
public class VerifyFaceTask extends AbsTask {
    IVerifyFaceListener listener;
    String delta;
    String url;
    String deltaKey;
    String idCardFilePath;
    String idCardFilePathKey;
    String faceToken;
    String faceTokenKey;
    String imageBestPath;
    String imageBestPathKey;
    List<String> actionImagePathList;
    String actionImagePathListKey;
    FaceIdApiService mFaceIdApiService;

    public VerifyFaceTask(Builder builder) {
        super(builder.activity);

        mFaceIdApiService = new Retrofit.Builder()
                .baseUrl(builder.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(FaceIdApiService.class);

        this.listener = builder.listener;
        this.url = builder.url;
        this.delta = builder.delta;
        this.deltaKey = builder.deltaKey;
        this.idCardFilePath = builder.idCardFilePath;
        this.idCardFilePathKey = builder.idCardFilePathKey;
        this.faceToken = builder.faceToken;
        this.faceTokenKey = builder.faceTokenKey;
        this.imageBestPath = builder.imageBestPath;
        this.imageBestPathKey = builder.imageBestPathKey;
        this.actionImagePathList = builder.actionImagePathList;
        this.actionImagePathListKey = builder.actionImagePathListKey;
    }

    public static class Builder {
        Activity activity;
        IVerifyFaceListener listener;
        String delta;
        String deltaKey;
        String idCardFilePath;
        String idCardFilePathKey;
        String faceToken;
        String faceTokenKey;
        String imageBestPath;
        String imageBestPathKey;
        List<String> actionImagePathList;
        String actionImagePathListKey;
        String baseUrl;
        String url;

        public Builder activity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public Builder listener(IVerifyFaceListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder delta(String key, String delta) {
            this.delta = delta;
            this.deltaKey = key;
            return this;
        }

        public Builder idCardFilePath(String key, String idCardFilePath) {
            this.idCardFilePath = idCardFilePath;
            this.idCardFilePathKey = key;
            return this;
        }

        public Builder faceToken(String key, String faceToken) {
            this.faceToken = faceToken;
            this.faceTokenKey = key;
            return this;
        }

        public Builder imageBestPath(String key, String imageBestPath) {
            this.imageBestPath = imageBestPath;
            this.imageBestPathKey = key;
            return this;
        }

        public Builder actionImagePathList(String key, List<String> actionImagePathList) {
            this.actionImagePathList = actionImagePathList;
            this.actionImagePathListKey = key;
            return this;
        }

        public VerifyFaceTask build() {
            VerifyFaceTask task = new VerifyFaceTask(this);
            return task;
        }
    }

    @Override
    protected void onStart() {

        if (TextUtils.isEmpty(url)) {
            listener.onFailure(new Exception("url can not be empty , url = " + url));
            return;
        }

        listener.onStart();
        Map<String, RequestBody> map = new HashMap<>();
        map.put(deltaKey, RequestBody.create(ApiConfig.MEDIA_TYPE_TEXT_PLAIN, delta));

        if (StdFileUtils.isFileExists(idCardFilePath)) {
            File idCardFile = new File(idCardFilePath);
            RequestBody fileBody = RequestBody.create(ApiConfig.MEDIA_TYPE_OCTET_STREAM, idCardFile);
            map.put(idCardFilePathKey + "\"; filename=\"" + idCardFile.getName(), fileBody);
        }

        if (StdFileUtils.isFileExists(imageBestPath)) {
            File bestImageFile = new File(imageBestPath);
            RequestBody fileBody = RequestBody.create(ApiConfig.MEDIA_TYPE_OCTET_STREAM, bestImageFile);
            map.put(imageBestPathKey + "\"; filename=\"" + bestImageFile.getName(), fileBody);

        }

        if (actionImagePathList != null && !actionImagePathList.isEmpty()) {
            int i = 0;
            for (String filePath : actionImagePathList) {
                if (StdFileUtils.isFileExists(filePath)) {
                    File file = new File(filePath);
                    RequestBody fileBody = RequestBody.create(ApiConfig.MEDIA_TYPE_OCTET_STREAM, file);
                    map.put(actionImagePathListKey + i + "\"; filename=\"" + file.getName(), fileBody);
                    i++;
                }
            }
        }

        Call<Object> call = mFaceIdApiService.verify(url, map, ApiConfig.DEFAULT_USER_AGENT);

        call.enqueue(new RetrofitCallback<Object>() {
            @Override
            public void onFailure(Throwable t) {
                if (isCallbackReady()) {
                    provideListener().onFailure(t);
                }
            }

            @Override
            public void onDataSuccess(Object o) {
                if (isCallbackReady()) {
                    provideListener().onSuccess(new Gson().toJson(o));
                }
            }
        });
    }

    @Override
    public IVerifyFaceListener provideListener() {
        return listener;
    }


    public interface IVerifyFaceListener extends ITaskListener {

        void onStart();

        void onFailure(Throwable t);

        void onSuccess(String s);
    }
}

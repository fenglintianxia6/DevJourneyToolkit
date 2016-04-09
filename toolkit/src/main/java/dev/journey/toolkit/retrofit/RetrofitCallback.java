package dev.journey.toolkit.retrofit;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * 对retrofit的Callback进行二次封装
 * Created by mwp on 2016/4/8.
 */
public abstract class RetrofitCallback<T> implements Callback<T> {
    @Override
    public void onResponse(Response<T> response) {
        if (response != null && response.isSuccess()) {
            T data = response.body();
            if (data != null) {
                onDataSuccess(data);
            } else {
                onFailure(buildException("response isSuccess but its body is null!"));
            }
        } else if (response != null) {
            String errorMsg = response.code() + ":" + response.message();
            onFailure(buildException(errorMsg));
        } else {
            onFailure(buildException("response is null!"));
        }
    }

    private Exception buildException(String msg) {
        return new Exception(msg);
    }

    @Override
    public void onFailure(Throwable t) {

    }

    public abstract void onDataSuccess(T data);
}

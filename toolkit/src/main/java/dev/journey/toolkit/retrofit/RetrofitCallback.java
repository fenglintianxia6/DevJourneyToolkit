package dev.journey.toolkit.retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 对retrofit的Callback进行二次封装
 * Created by mwp on 2016/4/8.
 */
public abstract class RetrofitCallback<T> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response != null && response.isSuccessful()) {
            T data = response.body();
            if (data != null) {
                try {
                    onDataSuccess(call, data);
                } catch (Exception e) {
                    onFailure(call, e);
                }
            } else {
                onFailure(call, buildException("response isSuccess but its body is null!"));
            }
        } else if (response != null) {
            String errorMsg = response.code() + ":" + response.message();
            onFailure(call, buildException(errorMsg));
        } else {
            onFailure(call, buildException("response is null!"));
        }
    }

    private Exception buildException(String msg) {
        return new Exception(msg);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {

    }

    public abstract void onDataSuccess(Call<T> call, T data);
}

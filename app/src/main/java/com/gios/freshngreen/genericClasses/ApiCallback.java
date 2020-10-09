package com.gios.freshngreen.genericClasses;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

abstract public class ApiCallback<T> implements Callback<T> {

    @Override
    public void onResponse(@NotNull Call<T> call, @NotNull Response<T> response) {
        if (response.body() != null) {
            handleResponseData(response.body());
        } else {
            handleError(response);
        }
    }

    @Override
    public void onFailure(@NotNull Call<T> call, @NotNull Throwable t) {
        if (t instanceof Exception) {
            handleException((Exception) t);
        } else {
            //do something else
        }
    }

    abstract protected void handleResponseData(T data);

    abstract protected void handleError(Response<T> response);

    abstract protected void handleException(Exception t);

}
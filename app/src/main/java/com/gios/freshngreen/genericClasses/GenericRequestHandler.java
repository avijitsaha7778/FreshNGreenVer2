package com.gios.freshngreen.genericClasses;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Response;

public abstract class GenericRequestHandler<R> {
    abstract protected Call<R> makeRequest();

    public final LiveData<DataWrapper<R>> doRequest(Context context, FragmentActivity activity) {
        final MutableLiveData<DataWrapper<R>> liveData = new MutableLiveData<>();
        final DataWrapper<R> dataWrapper = new DataWrapper<R>();

        makeRequest().enqueue(new ApiCallback<R>() {
            @Override
            protected void handleResponseData(R data) {
                dataWrapper.setData(data);
                liveData.setValue(dataWrapper);
            }

            @Override
            protected void handleError(Response<R> response) {
                if (response.errorBody() != null) {
                    dataWrapper.setError(new ApiErrorHandler<R>().handleError(response,context,activity));
                }
                liveData.setValue(dataWrapper);
            }

            @Override
            protected void handleException(Exception t) {
                dataWrapper.setApiException(t);
                liveData.setValue(dataWrapper);
            }
        });
        return liveData;
    }
}
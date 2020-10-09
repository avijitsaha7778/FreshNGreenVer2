package com.gios.freshngreen.apis.loginApis;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.genericClasses.GenericRequestHandler;
import com.gios.freshngreen.interfaces.ApiInterface;
import com.gios.freshngreen.responseModel.login.ForgotPasswordModel;
import com.gios.freshngreen.web.ApiClient;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import okhttp3.RequestBody;
import retrofit2.Call;

public class ForgotPasswordApi extends GenericRequestHandler<ForgotPasswordModel> {
    private static ApiInterface authService;
    private Context context;
    private FragmentActivity activity;
    private Map<String, RequestBody> bodyMap;

    public static ForgotPasswordApi createInstance(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity)
    {
        ForgotPasswordApi instance = new ForgotPasswordApi();
        instance.activity = activity;
        instance.context = context;
        instance.bodyMap = bodyMap;
        authService = ApiClient.getApiClient().create(ApiInterface.class);
        return instance;
    }

    public LiveData<DataWrapper<ForgotPasswordModel>> onApiRequest() {
        return doRequest(context,activity);
    }

    @Override
    protected Call<ForgotPasswordModel> makeRequest() {
        return authService.forgotPassword(bodyMap);
    }
}


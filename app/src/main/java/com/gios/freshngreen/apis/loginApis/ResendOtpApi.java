package com.gios.freshngreen.apis.loginApis;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.genericClasses.GenericRequestHandler;
import com.gios.freshngreen.interfaces.ApiInterface;
import com.gios.freshngreen.responseModel.login.ResendOtpModel;
import com.gios.freshngreen.web.ApiClient;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import okhttp3.RequestBody;
import retrofit2.Call;

public class ResendOtpApi extends GenericRequestHandler<ResendOtpModel> {
    private static ApiInterface authService;
    private Context context;
    private FragmentActivity activity;
    private Map<String, RequestBody> bodyMap;

    public static ResendOtpApi createInstance(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity)
    {
        ResendOtpApi instance = new ResendOtpApi();
        instance.activity = activity;
        instance.context = context;
        instance.bodyMap = bodyMap;
        authService = ApiClient.getApiClient().create(ApiInterface.class);
        return instance;
    }

    public LiveData<DataWrapper<ResendOtpModel>> onApiRequest() {
        return doRequest(context,activity);
    }

    @Override
    protected Call<ResendOtpModel> makeRequest() {
        return authService.resendOtp(bodyMap);
    }
}
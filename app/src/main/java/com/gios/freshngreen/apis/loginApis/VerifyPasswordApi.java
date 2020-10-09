package com.gios.freshngreen.apis.loginApis;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.genericClasses.GenericRequestHandler;
import com.gios.freshngreen.interfaces.ApiInterface;
import com.gios.freshngreen.responseModel.login.VerifyOtpModel;
import com.gios.freshngreen.responseModel.login.VerifyPasswordModel;
import com.gios.freshngreen.web.ApiClient;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import okhttp3.RequestBody;
import retrofit2.Call;

public class VerifyPasswordApi extends GenericRequestHandler<VerifyPasswordModel> {
    private static ApiInterface authService;
    private Context context;
    private FragmentActivity activity;
    private Map<String, RequestBody> bodyMap;

    public static VerifyPasswordApi createInstance(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity)
    {
        VerifyPasswordApi instance = new VerifyPasswordApi();
        instance.activity = activity;
        instance.context = context;
        instance.bodyMap = bodyMap;
        authService = ApiClient.getApiClient().create(ApiInterface.class);
        return instance;
    }

    public LiveData<DataWrapper<VerifyPasswordModel>> onApiRequest() {
        return doRequest(context,activity);
    }

    @Override
    protected Call<VerifyPasswordModel> makeRequest() {
        return authService.changePassword(bodyMap);
    }
}
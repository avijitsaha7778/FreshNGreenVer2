package com.gios.freshngreen.apis.loginApis;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.genericClasses.GenericRequestHandler;
import com.gios.freshngreen.interfaces.ApiInterface;
import com.gios.freshngreen.responseModel.login.LoginModel;
import com.gios.freshngreen.web.ApiClient;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import okhttp3.RequestBody;
import retrofit2.Call;

public class LoginApi extends GenericRequestHandler<LoginModel> {
    private static ApiInterface authService;
    private Context context;
    private FragmentActivity activity;
    private Map<String, RequestBody> bodyMap;

    public static LoginApi createInstance(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity)
    {
        LoginApi instance = new LoginApi();
        instance.activity = activity;
        instance.context = context;
        instance.bodyMap = bodyMap;
        authService = ApiClient.getApiClient().create(ApiInterface.class);
        return instance;
    }

    public LiveData<DataWrapper<LoginModel>> onApiRequest() {
        return doRequest(context,activity);
    }

    @Override
    protected Call<LoginModel> makeRequest() {
        return authService.login(bodyMap);
    }
}

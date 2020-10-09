package com.gios.freshngreen.apis.loginApis;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.genericClasses.GenericRequestHandler;
import com.gios.freshngreen.interfaces.ApiInterface;
import com.gios.freshngreen.responseModel.login.SignupModel;
import com.gios.freshngreen.web.ApiClient;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import okhttp3.RequestBody;
import retrofit2.Call;

public class SignUpApi extends GenericRequestHandler<SignupModel> {
    private static ApiInterface authService;
    private Context context;
    private FragmentActivity activity;
    private Map<String, RequestBody> bodyMap;
    
    public static SignUpApi createInstance(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity)
    {
        SignUpApi instance = new SignUpApi();
        instance.activity = activity;
        instance.context = context;
        instance.bodyMap = bodyMap;
        authService = ApiClient.getApiClient().create(ApiInterface.class);
        return instance;
    }

    public LiveData<DataWrapper<SignupModel>> onApiRequest() {
        return doRequest(context,activity);
    }

    @Override
    protected Call<SignupModel> makeRequest() {
        return authService.signUp(bodyMap);
    }
}

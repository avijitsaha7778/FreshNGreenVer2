package com.gios.freshngreen.apis.homeApis;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.genericClasses.GenericRequestHandler;
import com.gios.freshngreen.interfaces.ApiInterface;
import com.gios.freshngreen.responseModel.home.CategoriesModel;
import com.gios.freshngreen.responseModel.login.ForgotPasswordModel;
import com.gios.freshngreen.web.ApiClient;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import okhttp3.RequestBody;
import retrofit2.Call;

public class CategoriesApi extends GenericRequestHandler<CategoriesModel> {
    private static ApiInterface authService;
    private Context context;
    private FragmentActivity activity;
    private Map<String, RequestBody> bodyMap;

    public static CategoriesApi createInstance(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity)
    {
        CategoriesApi instance = new CategoriesApi();
        instance.activity = activity;
        instance.context = context;
        instance.bodyMap = bodyMap;
        authService = ApiClient.getApiClient().create(ApiInterface.class);
        return instance;
    }

    public LiveData<DataWrapper<CategoriesModel>> onApiRequest() {
        return doRequest(context,activity);
    }

    @Override
    protected Call<CategoriesModel> makeRequest() {
        return authService.categories(bodyMap);
    }
}
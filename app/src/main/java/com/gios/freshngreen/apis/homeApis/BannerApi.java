package com.gios.freshngreen.apis.homeApis;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.genericClasses.GenericRequestHandler;
import com.gios.freshngreen.interfaces.ApiInterface;
import com.gios.freshngreen.responseModel.home.BannerModel;
import com.gios.freshngreen.responseModel.home.CategoriesModel;
import com.gios.freshngreen.web.ApiClient;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import okhttp3.RequestBody;
import retrofit2.Call;

public class BannerApi extends GenericRequestHandler<BannerModel> {
    private static ApiInterface authService;
    private Context context;
    private FragmentActivity activity;
    private Map<String, RequestBody> bodyMap;

    public static BannerApi createInstance(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity)
    {
        BannerApi instance = new BannerApi();
        instance.activity = activity;
        instance.context = context;
        instance.bodyMap = bodyMap;
        authService = ApiClient.getApiClient().create(ApiInterface.class);
        return instance;
    }

    public LiveData<DataWrapper<BannerModel>> onApiRequest() {
        return doRequest(context,activity);
    }

    @Override
    protected Call<BannerModel> makeRequest() {
        return authService.banner(bodyMap);
    }
}
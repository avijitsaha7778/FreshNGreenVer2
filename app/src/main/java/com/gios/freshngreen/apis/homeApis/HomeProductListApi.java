package com.gios.freshngreen.apis.homeApis;

import android.content.Context;

import com.gios.freshngreen.apis.productApis.ProductListApi;
import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.genericClasses.GenericRequestHandler;
import com.gios.freshngreen.interfaces.ApiInterface;
import com.gios.freshngreen.responseModel.home.HomeProductListModel;
import com.gios.freshngreen.responseModel.product.ProductModel;
import com.gios.freshngreen.web.ApiClient;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import okhttp3.RequestBody;
import retrofit2.Call;

public class HomeProductListApi extends GenericRequestHandler<HomeProductListModel> {
    private static ApiInterface authService;
    private Context context;
    private FragmentActivity activity;
    private Map<String, RequestBody> bodyMap;

    public static HomeProductListApi createInstance(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity)
    {
        HomeProductListApi instance = new HomeProductListApi();
        instance.activity = activity;
        instance.context = context;
        instance.bodyMap = bodyMap;
        authService = ApiClient.getApiClient().create(ApiInterface.class);
        return instance;
    }

    public LiveData<DataWrapper<HomeProductListModel>> onApiRequest() {
        return doRequest(context,activity);
    }

    @Override
    protected Call<HomeProductListModel> makeRequest() {
        return authService.homeProductList(bodyMap);
    }
}
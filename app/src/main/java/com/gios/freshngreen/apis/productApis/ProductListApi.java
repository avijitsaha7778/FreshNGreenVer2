package com.gios.freshngreen.apis.productApis;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.genericClasses.GenericRequestHandler;
import com.gios.freshngreen.interfaces.ApiInterface;
import com.gios.freshngreen.responseModel.login.LoginModel;
import com.gios.freshngreen.responseModel.product.ProductModel;
import com.gios.freshngreen.web.ApiClient;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import okhttp3.RequestBody;
import retrofit2.Call;

public class ProductListApi extends GenericRequestHandler<ProductModel> {
    private static ApiInterface authService;
    private Context context;
    private FragmentActivity activity;
    private Map<String, RequestBody> bodyMap;

    public static ProductListApi createInstance(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity)
    {
        ProductListApi instance = new ProductListApi();
        instance.activity = activity;
        instance.context = context;
        instance.bodyMap = bodyMap;
        authService = ApiClient.getApiClient().create(ApiInterface.class);
        return instance;
    }

    public LiveData<DataWrapper<ProductModel>> onApiRequest() {
        return doRequest(context,activity);
    }

    @Override
    protected Call<ProductModel> makeRequest() {
        return authService.productList(bodyMap);
    }
}

package com.gios.freshngreen.apis.productApis;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.genericClasses.GenericRequestHandler;
import com.gios.freshngreen.interfaces.ApiInterface;
import com.gios.freshngreen.responseModel.product.ProductDetailsModel;
import com.gios.freshngreen.responseModel.product.RelatedProductModel;
import com.gios.freshngreen.web.ApiClient;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import okhttp3.RequestBody;
import retrofit2.Call;

public class RelatedProductApi extends GenericRequestHandler<RelatedProductModel> {
    private static ApiInterface authService;
    private Context context;
    private FragmentActivity activity;
    private Map<String, RequestBody> bodyMap;

    public static RelatedProductApi createInstance(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity)
    {
        RelatedProductApi instance = new RelatedProductApi();
        instance.activity = activity;
        instance.context = context;
        instance.bodyMap = bodyMap;
        authService = ApiClient.getApiClient().create(ApiInterface.class);
        return instance;
    }

    public LiveData<DataWrapper<RelatedProductModel>> onApiRequest() {
        return doRequest(context,activity);
    }

    @Override
    protected Call<RelatedProductModel> makeRequest() {
        return authService.relatedProduct(bodyMap);
    }
}
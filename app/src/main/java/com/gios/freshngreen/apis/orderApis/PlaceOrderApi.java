package com.gios.freshngreen.apis.orderApis;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.genericClasses.GenericRequestHandler;
import com.gios.freshngreen.interfaces.ApiInterface;
import com.gios.freshngreen.responseModel.order.PlaceOrderModel;
import com.gios.freshngreen.web.ApiClient;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import okhttp3.RequestBody;
import retrofit2.Call;

public class PlaceOrderApi extends GenericRequestHandler<PlaceOrderModel> {
    private static ApiInterface authService;
    private Context context;
    private FragmentActivity activity;
    private Map<String, RequestBody> bodyMap;

    public static PlaceOrderApi createInstance(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity)
    {
        PlaceOrderApi instance = new PlaceOrderApi();
        instance.activity = activity;
        instance.context = context;
        instance.bodyMap = bodyMap;
        authService = ApiClient.getApiClient().create(ApiInterface.class);
        return instance;
    }

    public LiveData<DataWrapper<PlaceOrderModel>> onApiRequest() {
        return doRequest(context,activity);
    }

    @Override
    protected Call<PlaceOrderModel> makeRequest() {
        return authService.placeOrder(bodyMap);
    }
}
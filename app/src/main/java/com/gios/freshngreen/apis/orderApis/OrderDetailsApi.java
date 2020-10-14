package com.gios.freshngreen.apis.orderApis;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.genericClasses.GenericRequestHandler;
import com.gios.freshngreen.interfaces.ApiInterface;
import com.gios.freshngreen.responseModel.order.OrderDetailsModel;
import com.gios.freshngreen.responseModel.order.OrderHistoryModel;
import com.gios.freshngreen.web.ApiClient;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import okhttp3.RequestBody;
import retrofit2.Call;

public class OrderDetailsApi extends GenericRequestHandler<OrderDetailsModel> {
    private static ApiInterface authService;
    private Context context;
    private FragmentActivity activity;
    private Map<String, RequestBody> bodyMap;

    public static OrderDetailsApi createInstance(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity)
    {
        OrderDetailsApi instance = new OrderDetailsApi();
        instance.activity = activity;
        instance.context = context;
        instance.bodyMap = bodyMap;
        authService = ApiClient.getApiClient().create(ApiInterface.class);
        return instance;
    }

    public LiveData<DataWrapper<OrderDetailsModel>> onApiRequest() {
        return doRequest(context,activity);
    }

    @Override
    protected Call<OrderDetailsModel> makeRequest() {
        return authService.orderDetails(bodyMap);
    }
}
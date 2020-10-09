package com.gios.freshngreen.apis.orderApis;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.genericClasses.GenericRequestHandler;
import com.gios.freshngreen.interfaces.ApiInterface;
import com.gios.freshngreen.responseModel.order.OrderHistoryModel;
import com.gios.freshngreen.web.ApiClient;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import okhttp3.RequestBody;
import retrofit2.Call;

public class OrderHistoryApi extends GenericRequestHandler<OrderHistoryModel> {
    private static ApiInterface authService;
    private Context context;
    private FragmentActivity activity;
    private Map<String, RequestBody> bodyMap;

    public static OrderHistoryApi createInstance(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity)
    {
        OrderHistoryApi instance = new OrderHistoryApi();
        instance.activity = activity;
        instance.context = context;
        instance.bodyMap = bodyMap;
        authService = ApiClient.getApiClient().create(ApiInterface.class);
        return instance;
    }

    public LiveData<DataWrapper<OrderHistoryModel>> onApiRequest() {
        return doRequest(context,activity);
    }

    @Override
    protected Call<OrderHistoryModel> makeRequest() {
        return authService.orderHistory(bodyMap);
    }
}

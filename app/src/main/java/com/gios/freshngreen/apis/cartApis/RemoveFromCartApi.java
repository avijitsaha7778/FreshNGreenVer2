package com.gios.freshngreen.apis.cartApis;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.genericClasses.GenericRequestHandler;
import com.gios.freshngreen.interfaces.ApiInterface;
import com.gios.freshngreen.responseModel.cart.AddCartModel;
import com.gios.freshngreen.responseModel.cart.RemoveCartModel;
import com.gios.freshngreen.web.ApiClient;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import okhttp3.RequestBody;
import retrofit2.Call;

public class RemoveFromCartApi extends GenericRequestHandler<RemoveCartModel> {
    private static ApiInterface authService;
    private Context context;
    private FragmentActivity activity;
    private Map<String, RequestBody> bodyMap;

    public static RemoveFromCartApi createInstance(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity)
    {
        RemoveFromCartApi instance = new RemoveFromCartApi();
        instance.activity = activity;
        instance.context = context;
        instance.bodyMap = bodyMap;
        authService = ApiClient.getApiClient().create(ApiInterface.class);
        return instance;
    }

    public LiveData<DataWrapper<RemoveCartModel>> onApiRequest() {
        return doRequest(context,activity);
    }

    @Override
    protected Call<RemoveCartModel> makeRequest() {
        return authService.removeCart(bodyMap);
    }
}

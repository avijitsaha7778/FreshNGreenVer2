package com.gios.freshngreen.apis.orderApis;

import android.content.Context;

import com.gios.freshngreen.apis.profile.GetAreaListApi;
import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.genericClasses.GenericRequestHandler;
import com.gios.freshngreen.interfaces.ApiInterface;
import com.gios.freshngreen.responseModel.order.UpdateAddressModel;
import com.gios.freshngreen.responseModel.profile.GetAreaListModel;
import com.gios.freshngreen.web.ApiClient;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import okhttp3.RequestBody;
import retrofit2.Call;

public class UpdateAddressApi extends GenericRequestHandler<UpdateAddressModel> {
    private static ApiInterface authService;
    private Context context;
    private FragmentActivity activity;
    private Map<String, RequestBody> bodyMap;

    public static UpdateAddressApi createInstance(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity)
    {
        UpdateAddressApi instance = new UpdateAddressApi();
        instance.activity = activity;
        instance.context = context;
        instance.bodyMap = bodyMap;
        authService = ApiClient.getApiClient().create(ApiInterface.class);
        return instance;
    }

    public LiveData<DataWrapper<UpdateAddressModel>> onApiRequest() {
        return doRequest(context,activity);
    }

    @Override
    protected Call<UpdateAddressModel> makeRequest() {
        return authService.updateAddress(bodyMap);
    }
}
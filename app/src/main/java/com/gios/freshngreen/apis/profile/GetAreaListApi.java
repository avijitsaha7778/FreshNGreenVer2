package com.gios.freshngreen.apis.profile;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.genericClasses.GenericRequestHandler;
import com.gios.freshngreen.interfaces.ApiInterface;
import com.gios.freshngreen.responseModel.profile.GetAreaListModel;
import com.gios.freshngreen.responseModel.profile.GetProfileModel;
import com.gios.freshngreen.web.ApiClient;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import okhttp3.RequestBody;
import retrofit2.Call;

public class GetAreaListApi extends GenericRequestHandler<GetAreaListModel> {
    private static ApiInterface authService;
    private Context context;
    private FragmentActivity activity;
    private Map<String, RequestBody> bodyMap;

    public static GetAreaListApi createInstance(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity)
    {
        GetAreaListApi instance = new GetAreaListApi();
        instance.activity = activity;
        instance.context = context;
        instance.bodyMap = bodyMap;
        authService = ApiClient.getApiClient().create(ApiInterface.class);
        return instance;
    }

    public LiveData<DataWrapper<GetAreaListModel>> onApiRequest() {
        return doRequest(context,activity);
    }

    @Override
    protected Call<GetAreaListModel> makeRequest() {
        return authService.areaList(bodyMap);
    }
}

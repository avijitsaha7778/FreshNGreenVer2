package com.gios.freshngreen.apis.profile;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.genericClasses.GenericRequestHandler;
import com.gios.freshngreen.interfaces.ApiInterface;
import com.gios.freshngreen.responseModel.profile.GetProfileModel;
import com.gios.freshngreen.web.ApiClient;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import okhttp3.RequestBody;
import retrofit2.Call;

public class GetProfileApi extends GenericRequestHandler<GetProfileModel> {
    private static ApiInterface authService;
    private Context context;
    private FragmentActivity activity;
    private Map<String, RequestBody> bodyMap;

    public static GetProfileApi createInstance(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity)
    {
        GetProfileApi instance = new GetProfileApi();
        instance.activity = activity;
        instance.context = context;
        instance.bodyMap = bodyMap;
        authService = ApiClient.getApiClient().create(ApiInterface.class);
        return instance;
    }

    public LiveData<DataWrapper<GetProfileModel>> onApiRequest() {
        return doRequest(context,activity);
    }

    @Override
    protected Call<GetProfileModel> makeRequest() {
        return authService.getProfileDetails(bodyMap);
    }
}

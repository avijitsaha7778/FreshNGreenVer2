package com.gios.freshngreen.apis.profile;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.genericClasses.GenericRequestHandler;
import com.gios.freshngreen.interfaces.ApiInterface;
import com.gios.freshngreen.responseModel.profile.UpdateProfileModel;
import com.gios.freshngreen.web.ApiClient;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class UpdateProfileApi extends GenericRequestHandler<UpdateProfileModel> {
    private static ApiInterface authService;
    private Context context;
    private FragmentActivity activity;
    private Map<String, RequestBody> bodyMap;
    private MultipartBody.Part part;

    public static UpdateProfileApi createInstance(Map<String, RequestBody> bodyMap, MultipartBody.Part part, Context context, FragmentActivity activity)
    {
        UpdateProfileApi instance = new UpdateProfileApi();
        instance.activity = activity;
        instance.context = context;
        instance.bodyMap = bodyMap;
        instance.part = part;
        authService = ApiClient.getApiClient().create(ApiInterface.class);
        return instance;
    }

    public LiveData<DataWrapper<UpdateProfileModel>> onApiRequest() {
        return doRequest(context,activity);
    }

    @Override
    protected Call<UpdateProfileModel> makeRequest() {
        return authService.updateProfile(bodyMap, part);
    }
}

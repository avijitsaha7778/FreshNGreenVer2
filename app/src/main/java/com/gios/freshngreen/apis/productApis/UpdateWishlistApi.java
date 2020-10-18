package com.gios.freshngreen.apis.productApis;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.genericClasses.GenericRequestHandler;
import com.gios.freshngreen.interfaces.ApiInterface;
import com.gios.freshngreen.responseModel.wishlist.RemoveWishlistModel;
import com.gios.freshngreen.web.ApiClient;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import okhttp3.RequestBody;
import retrofit2.Call;

public class UpdateWishlistApi extends GenericRequestHandler<RemoveWishlistModel> {
    private static ApiInterface authService;
    private Context context;
    private FragmentActivity activity;
    private Map<String, RequestBody> bodyMap;

    public static UpdateWishlistApi createInstance(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity)
    {
        UpdateWishlistApi instance = new UpdateWishlistApi();
        instance.activity = activity;
        instance.context = context;
        instance.bodyMap = bodyMap;
        authService = ApiClient.getApiClient().create(ApiInterface.class);
        return instance;
    }

    public LiveData<DataWrapper<RemoveWishlistModel>> onApiRequest() {
        return doRequest(context,activity);
    }

    @Override
    protected Call<RemoveWishlistModel> makeRequest() {
        return authService.updateWistlist(bodyMap);
    }
}
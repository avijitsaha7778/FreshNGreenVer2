package com.gios.freshngreen.apis.productApis;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.genericClasses.GenericRequestHandler;
import com.gios.freshngreen.interfaces.ApiInterface;
import com.gios.freshngreen.responseModel.wishlist.AddWishlistModel;
import com.gios.freshngreen.web.ApiClient;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import okhttp3.RequestBody;
import retrofit2.Call;

public class AddWishlistApi extends GenericRequestHandler<AddWishlistModel> {
    private static ApiInterface authService;
    private Context context;
    private FragmentActivity activity;
    private Map<String, RequestBody> bodyMap;

    public static AddWishlistApi createInstance(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity)
    {
        AddWishlistApi instance = new AddWishlistApi();
        instance.activity = activity;
        instance.context = context;
        instance.bodyMap = bodyMap;
        authService = ApiClient.getApiClient().create(ApiInterface.class);
        return instance;
    }

    public LiveData<DataWrapper<AddWishlistModel>> onApiRequest() {
        return doRequest(context,activity);
    }

    @Override
    protected Call<AddWishlistModel> makeRequest() {
        return authService.addWistlist(bodyMap);
    }
}
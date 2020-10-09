package com.gios.freshngreen.apis.wishlistApis;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.genericClasses.GenericRequestHandler;
import com.gios.freshngreen.interfaces.ApiInterface;
import com.gios.freshngreen.responseModel.product.ProductModel;
import com.gios.freshngreen.responseModel.wishlist.GetWishlistModel;
import com.gios.freshngreen.web.ApiClient;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import okhttp3.RequestBody;
import retrofit2.Call;

public class GetWishlistApi  extends GenericRequestHandler<GetWishlistModel> {
    private static ApiInterface authService;
    private Context context;
    private FragmentActivity activity;
    private Map<String, RequestBody> bodyMap;

    public static GetWishlistApi createInstance(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity)
    {
        GetWishlistApi instance = new GetWishlistApi();
        instance.activity = activity;
        instance.context = context;
        instance.bodyMap = bodyMap;
        authService = ApiClient.getApiClient().create(ApiInterface.class);
        return instance;
    }

    public LiveData<DataWrapper<GetWishlistModel>> onApiRequest() {
        return doRequest(context,activity);
    }

    @Override
    protected Call<GetWishlistModel> makeRequest() {
        return authService.getWistlist(bodyMap);
    }
}

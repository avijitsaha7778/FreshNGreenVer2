package com.gios.freshngreen.apis.cartApis;

import android.content.Context;

import com.gios.freshngreen.apis.wishlistApis.GetWishlistApi;
import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.genericClasses.GenericRequestHandler;
import com.gios.freshngreen.interfaces.ApiInterface;
import com.gios.freshngreen.responseModel.cart.GetCartModel;
import com.gios.freshngreen.responseModel.wishlist.GetWishlistModel;
import com.gios.freshngreen.web.ApiClient;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import okhttp3.RequestBody;
import retrofit2.Call;

public class GetCartApi extends GenericRequestHandler<GetCartModel> {
    private static ApiInterface authService;
    private Context context;
    private FragmentActivity activity;
    private Map<String, RequestBody> bodyMap;

    public static GetCartApi createInstance(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity)
    {
        GetCartApi instance = new GetCartApi();
        instance.activity = activity;
        instance.context = context;
        instance.bodyMap = bodyMap;
        authService = ApiClient.getApiClient().create(ApiInterface.class);
        return instance;
    }

    public LiveData<DataWrapper<GetCartModel>> onApiRequest() {
        return doRequest(context,activity);
    }

    @Override
    protected Call<GetCartModel> makeRequest() {
        return authService.getCart(bodyMap);
    }
}
package com.gios.freshngreen.repositories.wishList;

import android.content.Context;

import com.gios.freshngreen.apis.cartApis.AddToCartApi;
import com.gios.freshngreen.apis.productApis.AddWishlistApi;
import com.gios.freshngreen.apis.productApis.RemoveWishlistApi;
import com.gios.freshngreen.apis.productApis.UpdateWishlistApi;
import com.gios.freshngreen.apis.wishlistApis.GetWishlistApi;
import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.responseModel.cart.AddCartModel;
import com.gios.freshngreen.responseModel.wishlist.AddWishlistModel;
import com.gios.freshngreen.responseModel.wishlist.GetWishlistModel;
import com.gios.freshngreen.responseModel.wishlist.RemoveWishlistModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.RequestBody;

public class WishlistRepository {
    private static WishlistRepository instance;
    private LiveData<DataWrapper<GetWishlistModel>> wishlistData;
    private LiveData<DataWrapper<AddWishlistModel>> addWishlistData;
    private LiveData<DataWrapper<RemoveWishlistModel>> removeWishlistData;
    private LiveData<DataWrapper<RemoveWishlistModel>> updateWishlistData;
    private LiveData<DataWrapper<AddCartModel>> addToCartData;


    public static WishlistRepository getInstance() {
        if (instance == null) {
            instance = new WishlistRepository();
        }
        return instance;
    }

    public LiveData<DataWrapper<GetWishlistModel>> getWishlist(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        wishlistData = new MutableLiveData<>();
        wishlistData = GetWishlistApi.createInstance(bodyMap, context, activity).onApiRequest();
        return wishlistData;
    }

    public LiveData<DataWrapper<AddWishlistModel>> addWishlist(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        addWishlistData = new MutableLiveData<>();
        addWishlistData = AddWishlistApi.createInstance(bodyMap, context, activity).onApiRequest();
        return addWishlistData;
    }

    public LiveData<DataWrapper<RemoveWishlistModel>> removeWishlist(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        removeWishlistData = new MutableLiveData<>();
        removeWishlistData = RemoveWishlistApi.createInstance(bodyMap, context, activity).onApiRequest();
        return removeWishlistData;
    }
    public LiveData<DataWrapper<RemoveWishlistModel>> updateWishlist(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        updateWishlistData = new MutableLiveData<>();
        updateWishlistData = UpdateWishlistApi.createInstance(bodyMap, context, activity).onApiRequest();
        return updateWishlistData;
    }

    public LiveData<DataWrapper<AddCartModel>> addToCart(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        addToCartData = new MutableLiveData<>();
        addToCartData = AddToCartApi.createInstance(bodyMap, context, activity).onApiRequest();
        return addToCartData;
    }
}

package com.gios.freshngreen.repositories.cart;

import android.content.Context;

import com.gios.freshngreen.apis.cartApis.AddToCartApi;
import com.gios.freshngreen.apis.cartApis.GetCartApi;
import com.gios.freshngreen.apis.productApis.AddWishlistApi;
import com.gios.freshngreen.apis.productApis.RemoveWishlistApi;
import com.gios.freshngreen.apis.wishlistApis.GetWishlistApi;
import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.responseModel.cart.AddCartModel;
import com.gios.freshngreen.responseModel.cart.GetCartModel;
import com.gios.freshngreen.responseModel.wishlist.AddWishlistModel;
import com.gios.freshngreen.responseModel.wishlist.GetWishlistModel;
import com.gios.freshngreen.responseModel.wishlist.RemoveWishlistModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.RequestBody;

public class CartRepository {
    private static CartRepository instance;
    private LiveData<DataWrapper<GetCartModel>> cartData;
    private LiveData<DataWrapper<AddWishlistModel>> addWishlistData;
    private LiveData<DataWrapper<RemoveWishlistModel>> removeWishlistData;
    private LiveData<DataWrapper<AddCartModel>> addToCartData;


    public static CartRepository getInstance() {
        if (instance == null) {
            instance = new CartRepository();
        }
        return instance;
    }

    public LiveData<DataWrapper<GetCartModel>> getCart(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        cartData = new MutableLiveData<>();
        cartData = GetCartApi.createInstance(bodyMap, context, activity).onApiRequest();
        return cartData;
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

    public LiveData<DataWrapper<AddCartModel>> addToCart(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        addToCartData = new MutableLiveData<>();
        addToCartData = AddToCartApi.createInstance(bodyMap, context, activity).onApiRequest();
        return addToCartData;
    }
}

package com.gios.freshngreen.repositories.product;

import android.content.Context;

import com.gios.freshngreen.apis.cartApis.AddToCartApi;
import com.gios.freshngreen.apis.productApis.AddWishlistApi;
import com.gios.freshngreen.apis.productApis.ProductDetailsApi;
import com.gios.freshngreen.apis.productApis.RelatedProductApi;
import com.gios.freshngreen.apis.productApis.RemoveWishlistApi;
import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.responseModel.cart.AddCartModel;
import com.gios.freshngreen.responseModel.wishlist.AddWishlistModel;
import com.gios.freshngreen.responseModel.product.ProductDetailsModel;
import com.gios.freshngreen.responseModel.product.RelatedProductModel;
import com.gios.freshngreen.responseModel.wishlist.RemoveWishlistModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.RequestBody;

public class ProductDetailsRepository {
    private static ProductDetailsRepository instance;
    private LiveData<DataWrapper<ProductDetailsModel>> productDetailsData;
    private LiveData<DataWrapper<RelatedProductModel>> relatedProductData;
    private LiveData<DataWrapper<AddWishlistModel>> addWishlistData;
    private LiveData<DataWrapper<RemoveWishlistModel>> removeWishlistData;
    private LiveData<DataWrapper<AddCartModel>> addToCartData;



    public static ProductDetailsRepository getInstance() {
        if (instance == null) {
            instance = new ProductDetailsRepository();
        }
        return instance;
    }

    public LiveData<DataWrapper<ProductDetailsModel>> productDetails(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        productDetailsData = new MutableLiveData<>();
        productDetailsData = ProductDetailsApi.createInstance(bodyMap, context, activity).onApiRequest();
        return productDetailsData;
    }
    public LiveData<DataWrapper<RelatedProductModel>> relatedProduct(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        relatedProductData = new MutableLiveData<>();
        relatedProductData = RelatedProductApi.createInstance(bodyMap, context, activity).onApiRequest();
        return relatedProductData;
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

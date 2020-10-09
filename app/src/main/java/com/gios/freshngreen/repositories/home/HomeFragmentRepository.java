package com.gios.freshngreen.repositories.home;

import android.content.Context;

import com.gios.freshngreen.apis.cartApis.AddToCartApi;
import com.gios.freshngreen.apis.homeApis.BannerApi;
import com.gios.freshngreen.apis.homeApis.CategoriesApi;
import com.gios.freshngreen.apis.homeApis.HomeProductListApi;
import com.gios.freshngreen.apis.homeApis.SearchProductApi;
import com.gios.freshngreen.apis.productApis.AddWishlistApi;
import com.gios.freshngreen.apis.productApis.RemoveWishlistApi;
import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.responseModel.cart.AddCartModel;
import com.gios.freshngreen.responseModel.home.BannerModel;
import com.gios.freshngreen.responseModel.home.CategoriesModel;
import com.gios.freshngreen.responseModel.home.HomeProductListModel;
import com.gios.freshngreen.responseModel.home.SearchModel;
import com.gios.freshngreen.responseModel.wishlist.AddWishlistModel;
import com.gios.freshngreen.responseModel.wishlist.RemoveWishlistModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.RequestBody;

public class HomeFragmentRepository {
    private static HomeFragmentRepository instance;
    private LiveData<DataWrapper<CategoriesModel>> categoriesData;
    private LiveData<DataWrapper<BannerModel>> bannerData;
    private LiveData<DataWrapper<HomeProductListModel>> productListData;
    private LiveData<DataWrapper<SearchModel>> searchData;
    private LiveData<DataWrapper<AddWishlistModel>> addWishlistData;
    private LiveData<DataWrapper<RemoveWishlistModel>> removeWishlistData;
    private LiveData<DataWrapper<AddCartModel>> addToCartData;



    public static HomeFragmentRepository getInstance() {
        if (instance == null) {
            instance = new HomeFragmentRepository();
        }
        return instance;
    }

    public LiveData<DataWrapper<CategoriesModel>> categories(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        categoriesData = new MutableLiveData<>();
        categoriesData = CategoriesApi.createInstance(bodyMap, context, activity).onApiRequest();
        return categoriesData;
    }

    public LiveData<DataWrapper<BannerModel>> banner(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        bannerData = new MutableLiveData<>();
        bannerData = BannerApi.createInstance(bodyMap, context, activity).onApiRequest();
        return bannerData;
    }

    public LiveData<DataWrapper<HomeProductListModel>> homeProductList(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        productListData = new MutableLiveData<>();
        productListData = HomeProductListApi.createInstance(bodyMap, context, activity).onApiRequest();
        return productListData;
    }

    public LiveData<DataWrapper<SearchModel>> searchProduct(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        searchData = new MutableLiveData<>();
        searchData = SearchProductApi.createInstance(bodyMap, context, activity).onApiRequest();
        return searchData;
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

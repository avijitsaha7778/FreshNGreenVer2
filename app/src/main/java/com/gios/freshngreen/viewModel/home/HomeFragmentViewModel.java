package com.gios.freshngreen.viewModel.home;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.repositories.home.HomeFragmentRepository;
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
import androidx.lifecycle.ViewModel;
import okhttp3.RequestBody;

public class HomeFragmentViewModel extends ViewModel {
    private HomeFragmentRepository mRepo;
    public LiveData<DataWrapper<CategoriesModel>> LiveData_categories;
    public LiveData<DataWrapper<BannerModel>> LiveData_banner;
    public LiveData<DataWrapper<HomeProductListModel>> LiveData_productList;
    public LiveData<DataWrapper<SearchModel>> LiveData_search;
    public LiveData<DataWrapper<AddWishlistModel>> LiveData_addWishlist;
    public LiveData<DataWrapper<RemoveWishlistModel>> LiveData_removeWishlist;
    public LiveData<DataWrapper<AddCartModel>> LiveData_addToCart;



    public void init() {
        if (mRepo == null)
            mRepo = HomeFragmentRepository.getInstance();
    }

    public LiveData<DataWrapper<CategoriesModel>> categories(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_categories = mRepo.categories(bodyMap, activity, context);
        return LiveData_categories;
    }
    public LiveData<DataWrapper<BannerModel>> banner(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_banner = mRepo.banner(bodyMap, activity, context);
        return LiveData_banner;
    }
    public LiveData<DataWrapper<HomeProductListModel>> homeProductList(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_productList = mRepo.homeProductList(bodyMap, activity,context);
        return LiveData_productList;
    }
    public LiveData<DataWrapper<SearchModel>> searchProduct(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_search = mRepo.searchProduct(bodyMap, activity,context);
        return LiveData_search;
    }

    public LiveData<DataWrapper<AddWishlistModel>> addWishlist(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_addWishlist = mRepo.addWishlist(bodyMap, activity,context);
        return LiveData_addWishlist;
    }


    public LiveData<DataWrapper<RemoveWishlistModel>> removeWishlist(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_removeWishlist = mRepo.removeWishlist(bodyMap, activity,context);
        return LiveData_removeWishlist;
    }

    public LiveData<DataWrapper<AddCartModel>> addToCart(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_addToCart = mRepo.addToCart(bodyMap, activity,context);
        return LiveData_addToCart;
    }
}

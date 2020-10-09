package com.gios.freshngreen.viewModel.product;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.repositories.product.ProductListRepository;
import com.gios.freshngreen.responseModel.cart.AddCartModel;
import com.gios.freshngreen.responseModel.wishlist.AddWishlistModel;
import com.gios.freshngreen.responseModel.product.ProductModel;
import com.gios.freshngreen.responseModel.wishlist.RemoveWishlistModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.RequestBody;

public class ProductListViewModel extends ViewModel {

    private ProductListRepository mRepo;
    public LiveData<DataWrapper<ProductModel>> LiveData_wishlist;
    public LiveData<DataWrapper<AddWishlistModel>> LiveData_addWishlist;
    public LiveData<DataWrapper<RemoveWishlistModel>> LiveData_removeWishlist;
    public LiveData<DataWrapper<AddCartModel>> LiveData_addToCart;




    public void init() {
        if (mRepo == null)
            mRepo = ProductListRepository.getInstance();
    }

    public LiveData<DataWrapper<ProductModel>> productList(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_wishlist = mRepo.productList(bodyMap, activity,context);
        return LiveData_wishlist;
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

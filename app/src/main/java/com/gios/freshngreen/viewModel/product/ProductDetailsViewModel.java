package com.gios.freshngreen.viewModel.product;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.repositories.product.ProductDetailsRepository;
import com.gios.freshngreen.responseModel.cart.AddCartModel;
import com.gios.freshngreen.responseModel.wishlist.AddWishlistModel;
import com.gios.freshngreen.responseModel.product.ProductDetailsModel;
import com.gios.freshngreen.responseModel.product.RelatedProductModel;
import com.gios.freshngreen.responseModel.wishlist.RemoveWishlistModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.RequestBody;

public class ProductDetailsViewModel extends ViewModel {

    private ProductDetailsRepository mRepo;
    public LiveData<DataWrapper<ProductDetailsModel>> LiveData_productDetails;
    public LiveData<DataWrapper<RelatedProductModel>> LiveData_relatedProduct;
    public LiveData<DataWrapper<AddWishlistModel>> LiveData_addWishlist;
    public LiveData<DataWrapper<RemoveWishlistModel>> LiveData_removeWishlist;
    public LiveData<DataWrapper<AddCartModel>> LiveData_addToCart;



    public void init() {
        if (mRepo == null)
            mRepo = ProductDetailsRepository.getInstance();
    }

    public LiveData<DataWrapper<ProductDetailsModel>> productDetails(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_productDetails = mRepo.productDetails(bodyMap, activity,context);
        return LiveData_productDetails;
    }
    public LiveData<DataWrapper<RelatedProductModel>> relatedProduct(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_relatedProduct = mRepo.relatedProduct(bodyMap, activity,context);
        return LiveData_relatedProduct;
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

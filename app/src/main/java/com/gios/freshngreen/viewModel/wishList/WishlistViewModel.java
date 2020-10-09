package com.gios.freshngreen.viewModel.wishList;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.repositories.wishList.WishlistRepository;
import com.gios.freshngreen.responseModel.cart.AddCartModel;
import com.gios.freshngreen.responseModel.wishlist.AddWishlistModel;
import com.gios.freshngreen.responseModel.wishlist.GetWishlistModel;
import com.gios.freshngreen.responseModel.wishlist.RemoveWishlistModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.RequestBody;

public class WishlistViewModel extends ViewModel {

    private WishlistRepository mRepo;
    public LiveData<DataWrapper<GetWishlistModel>> LiveData_getWishList;
    public LiveData<DataWrapper<AddWishlistModel>> LiveData_addWishlist;
    public LiveData<DataWrapper<RemoveWishlistModel>> LiveData_removeWishlist;
    public LiveData<DataWrapper<AddCartModel>> LiveData_addToCart;


    public void init() {
        if (mRepo == null)
            mRepo = WishlistRepository.getInstance();
    }

    public LiveData<DataWrapper<GetWishlistModel>> getWishlist(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_getWishList = mRepo.getWishlist(bodyMap, activity,context);
        return LiveData_getWishList;
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


package com.gios.freshngreen.viewModel.cart;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.repositories.cart.CartRepository;
import com.gios.freshngreen.repositories.login.LoginRepository;
import com.gios.freshngreen.responseModel.cart.AddCartModel;
import com.gios.freshngreen.responseModel.cart.GetCartModel;
import com.gios.freshngreen.responseModel.login.LoginModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.RequestBody;

public class CartViewModel extends ViewModel {

    private CartRepository mRepo;
    public LiveData<DataWrapper<GetCartModel>> LiveData_getCart;
    public LiveData<DataWrapper<AddCartModel>> LiveData_addToCart;


    public void init() {
        if (mRepo == null)
            mRepo = CartRepository.getInstance();
    }

    public LiveData<DataWrapper<GetCartModel>> getCart(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_getCart = mRepo.getCart(bodyMap, activity,context);
        return LiveData_getCart;
    }

    public LiveData<DataWrapper<AddCartModel>> addToCart(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_addToCart = mRepo.addToCart(bodyMap, activity,context);
        return LiveData_addToCart;
    }
}

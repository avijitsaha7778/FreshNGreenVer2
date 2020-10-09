package com.gios.freshngreen.viewModel.order;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.repositories.order.OrderAddressRepository;
import com.gios.freshngreen.repositories.order.OrderPaymentRepository;
import com.gios.freshngreen.responseModel.cart.GetCartModel;
import com.gios.freshngreen.responseModel.order.PlaceOrderModel;
import com.gios.freshngreen.responseModel.order.UpdateAddressModel;
import com.gios.freshngreen.responseModel.profile.GetAreaListModel;
import com.gios.freshngreen.responseModel.profile.GetProfileModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.RequestBody;

public class OrderPaymentViewModel extends ViewModel {

    private OrderPaymentRepository mRepo;
    public LiveData<DataWrapper<GetProfileModel>> LiveData_getProfileDetails;
    public LiveData<DataWrapper<GetCartModel>> LiveData_getCart;
    public LiveData<DataWrapper<PlaceOrderModel>> LiveData_placeOrder;



    public void init() {
        if (mRepo == null)
            mRepo = OrderPaymentRepository.getInstance();
    }

    public LiveData<DataWrapper<GetProfileModel>> getProfileDetails(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_getProfileDetails = mRepo.getProfileDetails(bodyMap, activity,context);
        return LiveData_getProfileDetails;
    }
    public LiveData<DataWrapper<GetCartModel>> getCart(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_getCart = mRepo.getCart(bodyMap, activity,context);
        return LiveData_getCart;
    }
    public LiveData<DataWrapper<PlaceOrderModel>> placeOrder(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_placeOrder = mRepo.placeOrder(bodyMap, activity,context);
        return LiveData_placeOrder;
    }


}
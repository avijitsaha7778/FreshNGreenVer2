package com.gios.freshngreen.repositories.order;

import android.content.Context;

import com.gios.freshngreen.apis.cartApis.GetCartApi;
import com.gios.freshngreen.apis.orderApis.PlaceOrderApi;
import com.gios.freshngreen.apis.orderApis.UpdateAddressApi;
import com.gios.freshngreen.apis.profile.GetAreaListApi;
import com.gios.freshngreen.apis.profile.GetProfileApi;
import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.responseModel.cart.GetCartModel;
import com.gios.freshngreen.responseModel.order.PlaceOrderModel;
import com.gios.freshngreen.responseModel.order.UpdateAddressModel;
import com.gios.freshngreen.responseModel.profile.GetAreaListModel;
import com.gios.freshngreen.responseModel.profile.GetProfileModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.RequestBody;

public class OrderPaymentRepository {
    private static OrderPaymentRepository instance;
    private LiveData<DataWrapper<GetProfileModel>> getProfileData;
    private LiveData<DataWrapper<GetCartModel>> cartData;
    private LiveData<DataWrapper<PlaceOrderModel>> placeOrderData;




    public static OrderPaymentRepository getInstance() {
        if (instance == null) {
            instance = new OrderPaymentRepository();
        }
        return instance;
    }

    public LiveData<DataWrapper<GetProfileModel>> getProfileDetails(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        getProfileData = new MutableLiveData<>();
        getProfileData = GetProfileApi.createInstance(bodyMap, context, activity).onApiRequest();
        return getProfileData;
    }

    public LiveData<DataWrapper<GetCartModel>> getCart(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        cartData = new MutableLiveData<>();
        cartData = GetCartApi.createInstance(bodyMap, context, activity).onApiRequest();
        return cartData;
    }
    public LiveData<DataWrapper<PlaceOrderModel>> placeOrder(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        placeOrderData = new MutableLiveData<>();
        placeOrderData = PlaceOrderApi.createInstance(bodyMap, context, activity).onApiRequest();
        return placeOrderData;
    }
}

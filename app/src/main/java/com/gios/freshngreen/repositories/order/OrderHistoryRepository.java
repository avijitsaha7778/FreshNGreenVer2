package com.gios.freshngreen.repositories.order;

import android.content.Context;

import com.gios.freshngreen.apis.cartApis.AddToCartApi;
import com.gios.freshngreen.apis.orderApis.OrderHistoryApi;
import com.gios.freshngreen.apis.productApis.AddWishlistApi;
import com.gios.freshngreen.apis.productApis.ProductListApi;
import com.gios.freshngreen.apis.productApis.RemoveWishlistApi;
import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.responseModel.cart.AddCartModel;
import com.gios.freshngreen.responseModel.order.OrderHistoryModel;
import com.gios.freshngreen.responseModel.product.ProductModel;
import com.gios.freshngreen.responseModel.wishlist.AddWishlistModel;
import com.gios.freshngreen.responseModel.wishlist.RemoveWishlistModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.RequestBody;

public class OrderHistoryRepository {
    private static OrderHistoryRepository instance;
    private LiveData<DataWrapper<OrderHistoryModel>> orderHistoryData;



    public static OrderHistoryRepository getInstance() {
        if (instance == null) {
            instance = new OrderHistoryRepository();
        }
        return instance;
    }

    public LiveData<DataWrapper<OrderHistoryModel>> orderHistory(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        orderHistoryData = new MutableLiveData<>();
        orderHistoryData = OrderHistoryApi.createInstance(bodyMap, context, activity).onApiRequest();
        return orderHistoryData;
    }
}

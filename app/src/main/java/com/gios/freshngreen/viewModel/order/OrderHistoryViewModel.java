package com.gios.freshngreen.viewModel.order;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.repositories.order.OrderHistoryRepository;
import com.gios.freshngreen.responseModel.cart.AddCartModel;
import com.gios.freshngreen.responseModel.order.OrderHistoryModel;
import com.gios.freshngreen.responseModel.wishlist.AddWishlistModel;
import com.gios.freshngreen.responseModel.wishlist.RemoveWishlistModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.RequestBody;

public class OrderHistoryViewModel  extends ViewModel {

    private OrderHistoryRepository mRepo;
    public LiveData<DataWrapper<OrderHistoryModel>> LiveData_orderHistoryList;




    public void init() {
        if (mRepo == null)
            mRepo = OrderHistoryRepository.getInstance();
    }

    public LiveData<DataWrapper<OrderHistoryModel>> orderHistory(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_orderHistoryList = mRepo.orderHistory(bodyMap, activity,context);
        return LiveData_orderHistoryList;
    }

}
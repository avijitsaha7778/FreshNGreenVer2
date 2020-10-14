package com.gios.freshngreen.viewModel.order;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.repositories.order.OrderHistoryDetailsRepository;
import com.gios.freshngreen.responseModel.order.OrderDetailsModel;
import com.gios.freshngreen.responseModel.order.OrderHistoryModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.RequestBody;

public class OrderHistoryDetailsViewModel extends ViewModel {

    private OrderHistoryDetailsRepository mRepo;
    public LiveData<DataWrapper<OrderDetailsModel>> LiveData_orderDetails;




    public void init() {
        if (mRepo == null)
            mRepo = OrderHistoryDetailsRepository.getInstance();
    }

    public LiveData<DataWrapper<OrderDetailsModel>> orderDetails(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_orderDetails = mRepo.orderDetails(bodyMap, activity,context);
        return LiveData_orderDetails;
    }

}
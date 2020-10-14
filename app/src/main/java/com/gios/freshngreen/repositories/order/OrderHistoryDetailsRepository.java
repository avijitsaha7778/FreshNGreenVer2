package com.gios.freshngreen.repositories.order;

import android.content.Context;

import com.gios.freshngreen.apis.orderApis.OrderDetailsApi;
import com.gios.freshngreen.apis.orderApis.OrderHistoryApi;
import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.responseModel.order.OrderDetailsModel;
import com.gios.freshngreen.responseModel.order.OrderHistoryModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.RequestBody;

public class OrderHistoryDetailsRepository {
    private static OrderHistoryDetailsRepository instance;
    private LiveData<DataWrapper<OrderDetailsModel>> orderDetailsData;



    public static OrderHistoryDetailsRepository getInstance() {
        if (instance == null) {
            instance = new OrderHistoryDetailsRepository();
        }
        return instance;
    }

    public LiveData<DataWrapper<OrderDetailsModel>> orderDetails(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        orderDetailsData = new MutableLiveData<>();
        orderDetailsData = OrderDetailsApi.createInstance(bodyMap, context, activity).onApiRequest();
        return orderDetailsData;
    }
}

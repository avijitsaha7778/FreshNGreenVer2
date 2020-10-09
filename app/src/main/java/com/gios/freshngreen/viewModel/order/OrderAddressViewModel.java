package com.gios.freshngreen.viewModel.order;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.repositories.order.OrderAddressRepository;
import com.gios.freshngreen.responseModel.cart.AddCartModel;
import com.gios.freshngreen.responseModel.cart.GetCartModel;
import com.gios.freshngreen.responseModel.order.UpdateAddressModel;
import com.gios.freshngreen.responseModel.profile.GetAreaListModel;
import com.gios.freshngreen.responseModel.profile.GetProfileModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.RequestBody;

public class OrderAddressViewModel extends ViewModel {

    private OrderAddressRepository mRepo;
    public LiveData<DataWrapper<GetProfileModel>> LiveData_getProfileDetails;
    public LiveData<DataWrapper<GetAreaListModel>> LiveData_getAreaList;
    public LiveData<DataWrapper<UpdateAddressModel>> LiveData_updateAddress;



    public void init() {
        if (mRepo == null)
            mRepo = OrderAddressRepository.getInstance();
    }

    public LiveData<DataWrapper<GetProfileModel>> getProfileDetails(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_getProfileDetails = mRepo.getProfileDetails(bodyMap, activity,context);
        return LiveData_getProfileDetails;
    }

    public LiveData<DataWrapper<GetAreaListModel>> getAreaList(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_getAreaList = mRepo.getAreaList(bodyMap, activity,context);
        return LiveData_getAreaList;
    }
    public LiveData<DataWrapper<UpdateAddressModel>> updateAddress(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_updateAddress = mRepo.updateAddress(bodyMap, activity,context);
        return LiveData_updateAddress;
    }

}

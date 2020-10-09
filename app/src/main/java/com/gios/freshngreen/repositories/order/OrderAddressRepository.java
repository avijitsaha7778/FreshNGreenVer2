package com.gios.freshngreen.repositories.order;

import android.content.Context;

import com.gios.freshngreen.apis.cartApis.AddToCartApi;
import com.gios.freshngreen.apis.cartApis.GetCartApi;
import com.gios.freshngreen.apis.orderApis.UpdateAddressApi;
import com.gios.freshngreen.apis.productApis.AddWishlistApi;
import com.gios.freshngreen.apis.productApis.RemoveWishlistApi;
import com.gios.freshngreen.apis.profile.GetAreaListApi;
import com.gios.freshngreen.apis.profile.GetProfileApi;
import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.repositories.cart.CartRepository;
import com.gios.freshngreen.responseModel.cart.AddCartModel;
import com.gios.freshngreen.responseModel.cart.GetCartModel;
import com.gios.freshngreen.responseModel.order.UpdateAddressModel;
import com.gios.freshngreen.responseModel.profile.GetAreaListModel;
import com.gios.freshngreen.responseModel.profile.GetProfileModel;
import com.gios.freshngreen.responseModel.wishlist.AddWishlistModel;
import com.gios.freshngreen.responseModel.wishlist.RemoveWishlistModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.RequestBody;

public class OrderAddressRepository {
    private static OrderAddressRepository instance;
    private LiveData<DataWrapper<GetProfileModel>> getProfileData;
    private LiveData<DataWrapper<UpdateAddressModel>> updateAddressData;
    private LiveData<DataWrapper<GetAreaListModel>> getAreaListData;




    public static OrderAddressRepository getInstance() {
        if (instance == null) {
            instance = new OrderAddressRepository();
        }
        return instance;
    }

    public LiveData<DataWrapper<GetProfileModel>> getProfileDetails(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        getProfileData = new MutableLiveData<>();
        getProfileData = GetProfileApi.createInstance(bodyMap, context, activity).onApiRequest();
        return getProfileData;
    }
    public LiveData<DataWrapper<GetAreaListModel>> getAreaList(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        getAreaListData = new MutableLiveData<>();
        getAreaListData = GetAreaListApi.createInstance(bodyMap, context, activity).onApiRequest();
        return getAreaListData;
    }
    public LiveData<DataWrapper<UpdateAddressModel>> updateAddress(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        updateAddressData = new MutableLiveData<>();
        updateAddressData = UpdateAddressApi.createInstance(bodyMap, context, activity).onApiRequest();
        return updateAddressData;
    }
}

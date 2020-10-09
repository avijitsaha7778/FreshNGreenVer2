package com.gios.freshngreen.repositories.contactus;

import android.content.Context;

import com.gios.freshngreen.apis.cartApis.AddToCartApi;
import com.gios.freshngreen.apis.contactusApis.ContactUsApi;
import com.gios.freshngreen.apis.productApis.AddWishlistApi;
import com.gios.freshngreen.apis.productApis.ProductListApi;
import com.gios.freshngreen.apis.productApis.RemoveWishlistApi;
import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.repositories.product.ProductListRepository;
import com.gios.freshngreen.responseModel.cart.AddCartModel;
import com.gios.freshngreen.responseModel.contactus.ContactUsModel;
import com.gios.freshngreen.responseModel.product.ProductModel;
import com.gios.freshngreen.responseModel.wishlist.AddWishlistModel;
import com.gios.freshngreen.responseModel.wishlist.RemoveWishlistModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.RequestBody;

public class ContactUsRepository {
    private static ContactUsRepository instance;
    private LiveData<DataWrapper<ContactUsModel>> contactUsData;

    public static ContactUsRepository getInstance() {
        if (instance == null) {
            instance = new ContactUsRepository();
        }
        return instance;
    }

    public LiveData<DataWrapper<ContactUsModel>> contactUs(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        contactUsData = new MutableLiveData<>();
        contactUsData = ContactUsApi.createInstance(bodyMap, context, activity).onApiRequest();
        return contactUsData;
    }

}

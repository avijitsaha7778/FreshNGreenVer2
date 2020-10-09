package com.gios.freshngreen.viewModel.contactus;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.repositories.contactus.ContactUsRepository;
import com.gios.freshngreen.repositories.product.ProductListRepository;
import com.gios.freshngreen.responseModel.cart.AddCartModel;
import com.gios.freshngreen.responseModel.contactus.ContactUsModel;
import com.gios.freshngreen.responseModel.product.ProductModel;
import com.gios.freshngreen.responseModel.wishlist.AddWishlistModel;
import com.gios.freshngreen.responseModel.wishlist.RemoveWishlistModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.RequestBody;

public class ContactUsViewModel  extends ViewModel {

    private ContactUsRepository mRepo;
    public LiveData<DataWrapper<ContactUsModel>> LiveData_contactUs;

    public void init() {
        if (mRepo == null)
            mRepo = ContactUsRepository.getInstance();
    }

    public LiveData<DataWrapper<ContactUsModel>> contactUs(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_contactUs = mRepo.contactUs(bodyMap, activity,context);
        return LiveData_contactUs;
    }

}
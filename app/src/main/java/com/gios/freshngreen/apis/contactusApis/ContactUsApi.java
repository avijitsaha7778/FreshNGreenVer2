package com.gios.freshngreen.apis.contactusApis;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.genericClasses.GenericRequestHandler;
import com.gios.freshngreen.interfaces.ApiInterface;
import com.gios.freshngreen.responseModel.contactus.ContactUsModel;
import com.gios.freshngreen.responseModel.product.ProductModel;
import com.gios.freshngreen.web.ApiClient;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import okhttp3.RequestBody;
import retrofit2.Call;

public class ContactUsApi extends GenericRequestHandler<ContactUsModel> {
    private static ApiInterface authService;
    private Context context;
    private FragmentActivity activity;
    private Map<String, RequestBody> bodyMap;

    public static ContactUsApi createInstance(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity)
    {
        ContactUsApi instance = new ContactUsApi();
        instance.activity = activity;
        instance.context = context;
        instance.bodyMap = bodyMap;
        authService = ApiClient.getApiClient().create(ApiInterface.class);
        return instance;
    }

    public LiveData<DataWrapper<ContactUsModel>> onApiRequest() {
        return doRequest(context,activity);
    }

    @Override
    protected Call<ContactUsModel> makeRequest() {
        return authService.contactUs(bodyMap);
    }
}

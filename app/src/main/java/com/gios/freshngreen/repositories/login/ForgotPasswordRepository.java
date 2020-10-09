package com.gios.freshngreen.repositories.login;

import android.content.Context;


import com.gios.freshngreen.apis.loginApis.ForgotPasswordApi;
import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.responseModel.login.ForgotPasswordModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.RequestBody;

public class ForgotPasswordRepository {
    private static ForgotPasswordRepository instance;
    private LiveData<DataWrapper<ForgotPasswordModel>> forgotPasswordData;

    public static ForgotPasswordRepository getInstance() {
        if (instance == null) {
            instance = new ForgotPasswordRepository();
        }
        return instance;
    }

    public LiveData<DataWrapper<ForgotPasswordModel>> forgotPassword(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        forgotPasswordData = new MutableLiveData<>();
        forgotPasswordData = ForgotPasswordApi.createInstance(bodyMap, context, activity).onApiRequest();
        return forgotPasswordData;
    }
}

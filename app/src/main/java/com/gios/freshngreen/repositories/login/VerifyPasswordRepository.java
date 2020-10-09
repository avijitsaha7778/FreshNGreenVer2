package com.gios.freshngreen.repositories.login;

import android.content.Context;


import com.gios.freshngreen.apis.loginApis.VerifyPasswordApi;
import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.responseModel.login.VerifyPasswordModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.RequestBody;

public class VerifyPasswordRepository {
    private static VerifyPasswordRepository instance;
    public LiveData<DataWrapper<VerifyPasswordModel>> verifyPasswordData;


    public static VerifyPasswordRepository getInstance() {
        if (instance == null) {
            instance = new VerifyPasswordRepository();
        }
        return instance;
    }


    public LiveData<DataWrapper<VerifyPasswordModel>> verifyPassword(Map<String, RequestBody> map, FragmentActivity activity, Context context) {
        verifyPasswordData = new MutableLiveData<>();
        verifyPasswordData = VerifyPasswordApi.createInstance(map, context, activity).onApiRequest();
        return verifyPasswordData;
    }
}

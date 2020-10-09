package com.gios.freshngreen.repositories.login;

import android.content.Context;


import com.gios.freshngreen.apis.loginApis.LoginApi;
import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.responseModel.login.LoginModel;

import java.util.List;
import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LoginRepository {

    private static LoginRepository instance;
    private LiveData<DataWrapper<LoginModel>> loginData;


    public static LoginRepository getInstance() {
        if (instance == null) {
            instance = new LoginRepository();
        }
        return instance;
    }

    public LiveData<DataWrapper<LoginModel>> login(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        loginData = new MutableLiveData<>();
        loginData = LoginApi.createInstance(bodyMap, context, activity).onApiRequest();
        return loginData;
    }

}

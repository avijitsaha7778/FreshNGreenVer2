package com.gios.freshngreen.repositories.login;

import android.content.Context;

import com.gios.freshngreen.apis.loginApis.SignUpApi;
import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.responseModel.login.SignupModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.RequestBody;


public class RegistrationRepository {

    private static RegistrationRepository instance;
    private LiveData<DataWrapper<SignupModel>> signUpResponse;

    public static RegistrationRepository getInstance() {
        if (instance == null) {
            instance = new RegistrationRepository();
        }
        return instance;
    }
    public LiveData<DataWrapper<SignupModel>> signUp(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity) {
        signUpResponse = new MutableLiveData<>();
        signUpResponse = SignUpApi.createInstance(bodyMap, context, activity).onApiRequest();
        return signUpResponse;
    }
}

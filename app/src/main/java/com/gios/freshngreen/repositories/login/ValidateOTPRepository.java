package com.gios.freshngreen.repositories.login;

import android.content.Context;


import com.gios.freshngreen.apis.loginApis.ResendOtpApi;
import com.gios.freshngreen.apis.loginApis.VerifyOtpApi;
import com.gios.freshngreen.apis.loginApis.VerifyOtpForgotPasswordApi;
import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.responseModel.login.ResendOtpModel;
import com.gios.freshngreen.responseModel.login.VerifyOtpForgotPasswordModel;
import com.gios.freshngreen.responseModel.login.VerifyOtpModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.RequestBody;

public class ValidateOTPRepository {
    private static ValidateOTPRepository instance;
    public LiveData<DataWrapper<VerifyOtpModel>> verifyOtpData;
    public LiveData<DataWrapper<VerifyOtpForgotPasswordModel>> verifyOtpForgotPasswordData;
    public LiveData<DataWrapper<ResendOtpModel>> resendOtpData;

    public static ValidateOTPRepository getInstance() {
        if (instance == null) {
            instance = new ValidateOTPRepository();
        }
        return instance;
    }

    public LiveData<DataWrapper<VerifyOtpModel>> verifyOtp(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        verifyOtpData = new MutableLiveData<>();
        verifyOtpData = VerifyOtpApi.createInstance(bodyMap,context, activity).onApiRequest();
        return verifyOtpData;
    }

    public LiveData<DataWrapper<VerifyOtpForgotPasswordModel>> verifyOtpForgotPassword(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        verifyOtpForgotPasswordData = new MutableLiveData<>();
        verifyOtpForgotPasswordData = VerifyOtpForgotPasswordApi.createInstance(bodyMap,context, activity).onApiRequest();
        return verifyOtpForgotPasswordData;
    }

    public LiveData<DataWrapper<ResendOtpModel>> resendOtp(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        resendOtpData = new MutableLiveData<>();
        resendOtpData = ResendOtpApi.createInstance(bodyMap,context, activity).onApiRequest();
        return resendOtpData;
    }

}

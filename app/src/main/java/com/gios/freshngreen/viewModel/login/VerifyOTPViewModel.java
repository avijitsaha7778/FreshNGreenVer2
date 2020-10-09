package com.gios.freshngreen.viewModel.login;

import android.content.Context;


import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.repositories.login.ValidateOTPRepository;
import com.gios.freshngreen.responseModel.login.ResendOtpModel;
import com.gios.freshngreen.responseModel.login.VerifyOtpForgotPasswordModel;
import com.gios.freshngreen.responseModel.login.VerifyOtpModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.RequestBody;

public class VerifyOTPViewModel extends ViewModel {

    private ValidateOTPRepository mRepo;
    public LiveData<DataWrapper<VerifyOtpModel>> LiveData_verifyOtp;
    public LiveData<DataWrapper<VerifyOtpForgotPasswordModel>> LiveData_verifyOtpForgotPassword;
    public LiveData<DataWrapper<ResendOtpModel>> LiveData_resendOtp;

    public void init() {
        if (mRepo == null)
            mRepo = ValidateOTPRepository.getInstance();
    }

    public LiveData<DataWrapper<VerifyOtpModel>> verifyOtp(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity) {
        LiveData_verifyOtp = mRepo.verifyOtp(bodyMap, activity, context);
        return LiveData_verifyOtp;
    }

    public LiveData<DataWrapper<VerifyOtpForgotPasswordModel>> verifyOtpForgotPassword(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity) {
        LiveData_verifyOtpForgotPassword = mRepo.verifyOtpForgotPassword(bodyMap, activity, context);
        return LiveData_verifyOtpForgotPassword;
    }
    public LiveData<DataWrapper<ResendOtpModel>> resendOtp(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity) {
        LiveData_resendOtp = mRepo.resendOtp(bodyMap, activity, context);
        return LiveData_resendOtp;
    }

}

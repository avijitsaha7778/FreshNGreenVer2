package com.gios.freshngreen.viewModel.login;

import android.content.Context;


import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.repositories.login.ForgotPasswordRepository;
import com.gios.freshngreen.responseModel.login.ForgotPasswordModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.RequestBody;

public class ForgotPasswordViewModel extends ViewModel {
    private ForgotPasswordRepository mRepo;
    public LiveData<DataWrapper<ForgotPasswordModel>> LiveData_AWSForgotPassword;

    public void init() {
        if (mRepo == null)
            mRepo = ForgotPasswordRepository.getInstance();
    }

    public LiveData<DataWrapper<ForgotPasswordModel>> forgotPassword(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_AWSForgotPassword = mRepo.forgotPassword(bodyMap, activity, context);
        return LiveData_AWSForgotPassword;
    }

}

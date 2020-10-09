package com.gios.freshngreen.viewModel.login;

import android.content.Context;


import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.repositories.login.VerifyPasswordRepository;
import com.gios.freshngreen.responseModel.login.VerifyPasswordModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.RequestBody;

public class VerifyPasswordViewModel extends ViewModel {
    private VerifyPasswordRepository mRepo;
    public LiveData<DataWrapper<VerifyPasswordModel>> LiveData_verifyPassword;


    public void init() {
        if (mRepo == null)
            mRepo = VerifyPasswordRepository.getInstance();
    }

    public LiveData<DataWrapper<VerifyPasswordModel>> verifyPassword(Map<String, RequestBody> map, Context context, FragmentActivity activity) {
        LiveData_verifyPassword = mRepo.verifyPassword(map, activity, context);
        return LiveData_verifyPassword;
    }

}

package com.gios.freshngreen.viewModel.login;

import android.content.Context;


import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.repositories.login.LoginRepository;
import com.gios.freshngreen.responseModel.login.LoginModel;

import java.util.List;
import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.RequestBody;

public class LoginViewModel extends ViewModel {

    private LoginRepository mRepo;
    public LiveData<DataWrapper<LoginModel>> LiveData_login;

    public void init() {
        if (mRepo == null)
            mRepo = LoginRepository.getInstance();
    }

    public LiveData<DataWrapper<LoginModel>> login(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_login = mRepo.login(bodyMap, activity,context);
        return LiveData_login;
    }
}

package com.gios.freshngreen.viewModel.login;

import android.content.Context;


import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.repositories.login.RegistrationRepository;
import com.gios.freshngreen.responseModel.login.SignupModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.RequestBody;

public class RegistrationViewModel extends ViewModel {

    private RegistrationRepository mRepo;
    public LiveData<DataWrapper<SignupModel>> liveData_signUp;

    public void init() {
        if (mRepo == null)
            mRepo = RegistrationRepository.getInstance();
    }

    public LiveData<DataWrapper<SignupModel>> signUp(Map<String, RequestBody> bodyMap, Context context, FragmentActivity activity) {
        liveData_signUp = mRepo.signUp(bodyMap, context, activity);
        return liveData_signUp;
    }
}

package com.gios.freshngreen.viewModel.profile;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.repositories.login.LoginRepository;
import com.gios.freshngreen.repositories.profile.ProfileRepository;
import com.gios.freshngreen.responseModel.login.LoginModel;
import com.gios.freshngreen.responseModel.profile.GetAreaListModel;
import com.gios.freshngreen.responseModel.profile.GetProfileModel;
import com.gios.freshngreen.responseModel.profile.UpdateProfileModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileViewModel extends ViewModel {

    private ProfileRepository mRepo;
    public LiveData<DataWrapper<GetProfileModel>> LiveData_getProfileDetails;
    public LiveData<DataWrapper<UpdateProfileModel>> LiveData_updateProfileDetails;
    public LiveData<DataWrapper<GetAreaListModel>> LiveData_getAreaList;

    public void init() {
        if (mRepo == null)
            mRepo = ProfileRepository.getInstance();
    }

    public LiveData<DataWrapper<GetProfileModel>> getProfileDetails(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_getProfileDetails = mRepo.getProfileDetails(bodyMap, activity,context);
        return LiveData_getProfileDetails;
    }
    public LiveData<DataWrapper<UpdateProfileModel>> updateProfileDetails(Map<String, RequestBody> bodyMap, MultipartBody.Part part, FragmentActivity activity, Context context) {
        LiveData_updateProfileDetails = mRepo.updateProfileDetails(bodyMap,part, activity,context);
        return LiveData_updateProfileDetails;
    }
    public LiveData<DataWrapper<GetAreaListModel>> getAreaList(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_getAreaList = mRepo.getAreaList(bodyMap, activity,context);
        return LiveData_getAreaList;
    }
}

package com.gios.freshngreen.repositories.profile;

import android.content.Context;

import com.gios.freshngreen.apis.loginApis.LoginApi;
import com.gios.freshngreen.apis.profile.GetAreaListApi;
import com.gios.freshngreen.apis.profile.GetProfileApi;
import com.gios.freshngreen.apis.profile.UpdateProfileApi;
import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.repositories.login.LoginRepository;
import com.gios.freshngreen.responseModel.login.LoginModel;
import com.gios.freshngreen.responseModel.profile.GetAreaListModel;
import com.gios.freshngreen.responseModel.profile.GetProfileModel;
import com.gios.freshngreen.responseModel.profile.UpdateProfileModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileRepository {

    private static ProfileRepository instance;
    private LiveData<DataWrapper<GetProfileModel>> getProfileData;
    private LiveData<DataWrapper<UpdateProfileModel>> updateProfileData;
    private LiveData<DataWrapper<GetAreaListModel>> getAreaListData;


    public static ProfileRepository getInstance() {
        if (instance == null) {
            instance = new ProfileRepository();
        }
        return instance;
    }

    public LiveData<DataWrapper<GetProfileModel>> getProfileDetails(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        getProfileData = new MutableLiveData<>();
        getProfileData = GetProfileApi.createInstance(bodyMap, context, activity).onApiRequest();
        return getProfileData;
    }
    public LiveData<DataWrapper<UpdateProfileModel>> updateProfileDetails(Map<String, RequestBody> bodyMap, MultipartBody.Part part, FragmentActivity activity, Context context) {
        getProfileData = new MutableLiveData<>();
        updateProfileData = UpdateProfileApi.createInstance(bodyMap,part, context, activity).onApiRequest();
        return updateProfileData;
    }
    public LiveData<DataWrapper<GetAreaListModel>> getAreaList(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        getAreaListData = new MutableLiveData<>();
        getAreaListData = GetAreaListApi.createInstance(bodyMap, context, activity).onApiRequest();
        return getAreaListData;
    }

}

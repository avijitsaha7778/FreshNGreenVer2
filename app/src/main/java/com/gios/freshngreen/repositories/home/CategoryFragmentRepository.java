package com.gios.freshngreen.repositories.home;

import android.content.Context;

import com.gios.freshngreen.apis.homeApis.CategoriesApi;
import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.responseModel.home.CategoriesModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.RequestBody;

public class CategoryFragmentRepository {
    private static CategoryFragmentRepository instance;
    private LiveData<DataWrapper<CategoriesModel>> categoriesData;

    public static CategoryFragmentRepository getInstance() {
        if (instance == null) {
            instance = new CategoryFragmentRepository();
        }
        return instance;
    }

    public LiveData<DataWrapper<CategoriesModel>> categories(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        categoriesData = new MutableLiveData<>();
        categoriesData = CategoriesApi.createInstance(bodyMap, context, activity).onApiRequest();
        return categoriesData;
    }
}

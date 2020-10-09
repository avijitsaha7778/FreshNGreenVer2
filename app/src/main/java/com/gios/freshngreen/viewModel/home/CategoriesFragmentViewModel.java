package com.gios.freshngreen.viewModel.home;

import android.content.Context;

import com.gios.freshngreen.genericClasses.DataWrapper;
import com.gios.freshngreen.repositories.home.CategoryFragmentRepository;
import com.gios.freshngreen.repositories.home.HomeFragmentRepository;
import com.gios.freshngreen.responseModel.home.CategoriesModel;

import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.RequestBody;

public class CategoriesFragmentViewModel  extends ViewModel {
    private CategoryFragmentRepository mRepo;
    public LiveData<DataWrapper<CategoriesModel>> LiveData_categories;

    public void init() {
        if (mRepo == null)
            mRepo = CategoryFragmentRepository.getInstance();
    }

    public LiveData<DataWrapper<CategoriesModel>> categories(Map<String, RequestBody> bodyMap, FragmentActivity activity, Context context) {
        LiveData_categories = mRepo.categories(bodyMap, activity, context);
        return LiveData_categories;
    }

}
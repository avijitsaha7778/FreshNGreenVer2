package com.gios.freshngreen.fragments.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gios.freshngreen.R;
import com.gios.freshngreen.activities.HomeActivity;
import com.gios.freshngreen.adapter.CategoryGridAdapter;
import com.gios.freshngreen.adapter.HomeCategoriesHorizontalAdapter;
import com.gios.freshngreen.databinding.FragmentCategoriesBinding;
import com.gios.freshngreen.databinding.FragmentHomeBinding;
import com.gios.freshngreen.dialogs.SubCategoryDialog;
import com.gios.freshngreen.genericClasses.ApiObserver;
import com.gios.freshngreen.responseModel.home.CategoriesModel;
import com.gios.freshngreen.responseModel.home.Category;
import com.gios.freshngreen.responseModel.home.Subcategory;
import com.gios.freshngreen.utils.NetworkUtils;
import com.gios.freshngreen.utils.SharedPref;
import com.gios.freshngreen.utils.SpacesItemDecoration;
import com.gios.freshngreen.viewModel.home.CategoriesFragmentViewModel;
import com.gios.freshngreen.viewModel.home.HomeFragmentViewModel;
import com.gios.freshngreen.viewModel.login.LoginViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static androidx.navigation.Navigation.findNavController;
import static com.gios.freshngreen.utils.Constants.CATEGORYID;
import static com.gios.freshngreen.utils.MyUtilities.showMessage;

public class CategoriesFragment extends Fragment implements HomeCategoriesHorizontalAdapter.HomeCategoriesHorizontalAdapterInterface,
        SubCategoryDialog.SubCategorySelectionInterface {
    private FragmentCategoriesBinding binding;
    private SharedPref sharedPref;
    private String password;
    private String phone;
    private ProgressDialog waitDialog;
    Map<String, RequestBody> bodyMap;
    private CategoriesFragmentViewModel viewModel;
    private SubCategoryDialog mSubCategoryDialog;


    public CategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCategoriesBinding.inflate(inflater, container, false);

        initVar();
        setListeners();
        getCategories();
        return binding.getRoot();
    }

    private void initVar() {
        HomeActivity.setScreenName("Categories");
        sharedPref = new SharedPref(requireActivity());
        viewModel = new ViewModelProvider(this).get(CategoriesFragmentViewModel.class);
        viewModel.init();


        binding.categoriesRecyclerview.setHasFixedSize(true);
        binding.categoriesRecyclerview.setNestedScrollingEnabled(false);
        binding.categoriesRecyclerview.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.category_grid_spacing);
        binding.categoriesRecyclerview.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
    }

    private void setListeners() {
    }

    private void getParams(Map<String, RequestBody> map, String categoryId) {
        RequestBody categoryIdBody = RequestBody.create(categoryId, MediaType.parse("text/plain"));

        map.put(CATEGORYID, categoryIdBody);
    }

    private void getCategories() {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {
            showWaitDialog(requireContext(), "Loading...");
            bodyMap = new HashMap<>();
            getParams(bodyMap, "");

            viewModel.categories(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                    new ApiObserver<CategoriesModel>(new ApiObserver.ChangeListener<CategoriesModel>() {
                        @Override
                        public void onSuccess(CategoriesModel response) {
                            try {
                                if (response != null && response.getStatus()) {
                                    if (response.getCategoryList().size() > 0) {
                                        binding.categoriesRecyclerview.setVisibility(View.VISIBLE);
                                        CategoryGridAdapter adapter
                                                = new CategoryGridAdapter(response.getCategoryList(), CategoriesFragment.this);
                                        binding.categoriesRecyclerview.setAdapter(adapter);
                                    } else {
                                        binding.categoriesRecyclerview.setVisibility(View.GONE);
                                    }

                                } else {
                                    showMessage(requireContext(), binding.getRoot(), response.getError());
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            } finally {
                                closeWaitDialog();
                            }
                        }

                        @Override
                        public void onErrorMessage(String message) {
                            closeWaitDialog();
                            showMessage(requireContext(), binding.getRoot(), message);
                        }

                        @Override
                        public void onFail(Exception exception) {
                            closeWaitDialog();
                            showMessage(requireContext(), binding.getRoot(), exception.getMessage());
                        }
                    }));
        } else {
            showMessage(requireContext(), binding.getRoot(), getString(R.string.check_connections));
        }
    }


    private void showWaitDialog(Context context, String message) {
        try {
            waitDialog = new ProgressDialog(context, R.style.AppCompatAlertDialogStyle);
            waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            waitDialog.setMessage(message);
            waitDialog.setIndeterminate(true);
            waitDialog.setCancelable(false);
            waitDialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void closeWaitDialog() {
        try {
            if (waitDialog.isShowing()) {
                waitDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCategoryClick(Category mCategory) {
        try {
            if (mCategory.getSubcategory() != null && mCategory.getSubcategory().size() > 0) {
                mSubCategoryDialog = new SubCategoryDialog(this, mCategory.getSubcategory());
                try {
                    if (!mSubCategoryDialog.isAdded()) {
                        mSubCategoryDialog.show(getParentFragmentManager(), TAG);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                CategoriesFragmentDirections.ActionCategoriesFragmentToProductListFragment action =
                        CategoriesFragmentDirections.actionCategoriesFragmentToProductListFragment(mCategory.getId(), "");
                action.setCategoryId(mCategory.getId());
                action.setSubCategoryId("");
                findNavController(binding.getRoot()).navigate(action);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onSubCategorySelection(Subcategory mSubcategory) {
        try {
            CategoriesFragmentDirections.ActionCategoriesFragmentToProductListFragment action =
                    CategoriesFragmentDirections.actionCategoriesFragmentToProductListFragment(mSubcategory.getParentId(), mSubcategory.getId());
            action.setCategoryId(mSubcategory.getParentId());
            action.setSubCategoryId(mSubcategory.getId());
            findNavController(binding.getRoot()).navigate(action);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
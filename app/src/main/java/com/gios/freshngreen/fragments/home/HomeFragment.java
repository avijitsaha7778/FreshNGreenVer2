package com.gios.freshngreen.fragments.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gios.freshngreen.R;
import com.gios.freshngreen.activities.HomeActivity;
import com.gios.freshngreen.adapter.BannerSliderAdapter;
import com.gios.freshngreen.adapter.HomeCategoriesHorizontalAdapter;
import com.gios.freshngreen.adapter.HomeProductListAdpater;
import com.gios.freshngreen.adapter.SearchAdapter;
import com.gios.freshngreen.adapter.WishlistAdapter;
import com.gios.freshngreen.databinding.FragmentHomeBinding;
import com.gios.freshngreen.dialogs.SubCategoryDialog;
import com.gios.freshngreen.genericClasses.ApiObserver;
import com.gios.freshngreen.responseModel.cart.AddCartModel;
import com.gios.freshngreen.responseModel.home.BannerModel;
import com.gios.freshngreen.responseModel.home.CategoriesModel;
import com.gios.freshngreen.responseModel.home.Category;
import com.gios.freshngreen.responseModel.home.HomeProductListModel;
import com.gios.freshngreen.responseModel.home.SearchModel;
import com.gios.freshngreen.responseModel.home.SearchProductList;
import com.gios.freshngreen.responseModel.home.Subcategory;
import com.gios.freshngreen.responseModel.wishlist.AddWishlistModel;
import com.gios.freshngreen.responseModel.product.ProductList;
import com.gios.freshngreen.responseModel.wishlist.RemoveWishlistModel;
import com.gios.freshngreen.responseModel.wishlist.WishlistDetail;
import com.gios.freshngreen.utils.MyUtilities;
import com.gios.freshngreen.utils.NetworkUtils;
import com.gios.freshngreen.utils.PicassoImageLoadingService;
import com.gios.freshngreen.utils.SharedPref;
import com.gios.freshngreen.viewModel.home.HomeFragmentViewModel;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import ss.com.bannerslider.Slider;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static androidx.navigation.Navigation.findNavController;
import static com.gios.freshngreen.utils.Constants.ACTION;
import static com.gios.freshngreen.utils.Constants.CATEGORYID;
import static com.gios.freshngreen.utils.Constants.KEYWORD;
import static com.gios.freshngreen.utils.Constants.PRICEID;
import static com.gios.freshngreen.utils.Constants.PRODUCTID;
import static com.gios.freshngreen.utils.Constants.QUANTITY;
import static com.gios.freshngreen.utils.Constants.USERID;
import static com.gios.freshngreen.utils.MyUtilities.showMessage;


public class HomeFragment extends Fragment implements HomeCategoriesHorizontalAdapter.HomeCategoriesHorizontalAdapterInterface,
        SubCategoryDialog.SubCategorySelectionInterface, HomeProductListAdpater.Interface, SearchAdapter.Interface {
    private FragmentHomeBinding binding;
    private SharedPref sharedPref;
    private ProgressDialog waitDialog;
    Map<String, RequestBody> bodyMap;
    private HomeFragmentViewModel viewModel;
    private List<Category> categoryList = new ArrayList<>();
    private SubCategoryDialog mSubCategoryDialog;
    private String searchKey;
    private long typingDelay = 1000;
    private long last_text_edit_time = 0;
    private List<SearchProductList> searchProductList = new ArrayList<>();
    private SearchAdapter mSearchAdapter;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        initVar();
        setListeners();
        if (NetworkUtils.isNetworkAvailable(requireContext())) {
            getCategories();
        } else {
            showMessage(requireContext(), binding.getRoot(), getString(R.string.check_connections));
        }
        return binding.getRoot();
    }


    private void initVar() {
        HomeActivity.setScreenName("Feel Fresh!");
        sharedPref = new SharedPref(requireActivity());
        viewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
        viewModel.init();


        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.categoriesRecyclerview.setHasFixedSize(true);
        binding.categoriesRecyclerview.setNestedScrollingEnabled(false);
        binding.categoriesRecyclerview.setLayoutManager(horizontalLayoutManagaer);


        mSearchAdapter = new SearchAdapter(requireContext(), searchProductList, HomeFragment.this);
        binding.searchProduct.setAdapter(mSearchAdapter);

    }

    private void setListeners() {

        binding.scrolView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                binding.searchProduct.clearFocus();
//                MyUtilities.hideKeyboard(getActivity(),binding.searchProduct);
            }
        });

        binding.homeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.searchProduct.clearFocus();
                return true;
            }
        });

        binding.searchProduct.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    MyUtilities.hideKeyboard(getActivity(), binding.searchProduct);
                }
            }
        });

        Handler handler = new Handler();
        Runnable input_finish_checker = new Runnable() {
            public void run() {
                if (System.currentTimeMillis() > (last_text_edit_time + typingDelay - 200)) {
                    if (NetworkUtils.isNetworkAvailable(requireContext())) {
                        if (searchKey.length() > 2) {
                            if (NetworkUtils.isNetworkAvailable(requireContext())) {
                                searchProduct(searchKey.toString());
                            } else {
                                showMessage(requireContext(), binding.getRoot(), getString(R.string.check_connections));
                            }

                        }
                    } else {
                        showMessage(requireContext(), binding.getRoot(), getString(R.string.check_connections));
                    }
                }
            }
        };

        binding.searchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(input_finish_checker);
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 2) {
                    searchKey = s.toString();
                    last_text_edit_time = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, typingDelay);
                } else {

                }


            }
        });


    }

    private void getParams(Map<String, RequestBody> map, String categoryId) {
        RequestBody categoryIdBody = RequestBody.create(categoryId, MediaType.parse("text/plain"));

        map.put(CATEGORYID, categoryIdBody);
    }

    private void getParamsHomeProductList(Map<String, RequestBody> map, String userId) {
        RequestBody userIdBody = RequestBody.create(userId, MediaType.parse("text/plain"));

        map.put(USERID, userIdBody);
    }

    private void getCategories() {
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
                                    categoryList.clear();
                                    categoryList.addAll(response.getCategoryList());
                                    categoryList.add(new Category("000", "View All", "", null));
                                    binding.categoriesRecyclerview.setVisibility(View.VISIBLE);
                                    HomeCategoriesHorizontalAdapter adapter
                                            = new HomeCategoriesHorizontalAdapter(categoryList, HomeFragment.this);
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
                            if (NetworkUtils.isNetworkAvailable(requireContext())) {
                                getBanner();
                            } else {
                                showMessage(requireContext(), binding.getRoot(), getString(R.string.check_connections));
                            }
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
    }

    private void getBanner() {
        bodyMap = new HashMap<>();
        getParams(bodyMap, "");
        viewModel.banner(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                new ApiObserver<BannerModel>(new ApiObserver.ChangeListener<BannerModel>() {
                    @Override
                    public void onSuccess(BannerModel response) {
                        try {
                            if (response != null && response.getStatus()) {
                                if (response.getBannerList().size() > 0) {
                                    Slider.init(new PicassoImageLoadingService(requireContext()));
                                    binding.bannerSlider.setAdapter(new BannerSliderAdapter(requireContext(), response.getBannerList()));
                                    binding.bannerSlider.setInterval(6000);
                                } else {
                                    binding.bannerSlider.setVisibility(View.GONE);
                                }
                            } else {
                                showMessage(requireContext(), binding.getRoot(), response.getError());
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            if (NetworkUtils.isNetworkAvailable(requireContext())) {
                                getHomeProductList();
                            } else {
                                showMessage(requireContext(), binding.getRoot(), getString(R.string.check_connections));
                            }
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
    }

    private void getHomeProductList() {
        bodyMap = new HashMap<>();
        getParamsHomeProductList(bodyMap, sharedPref.getUserId());
        viewModel.homeProductList(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                new ApiObserver<HomeProductListModel>(new ApiObserver.ChangeListener<HomeProductListModel>() {
                    @Override
                    public void onSuccess(HomeProductListModel response) {
                        try {
                            if (response != null && response.getStatus()) {
                                if (response.getCategoryList().size() > 0) {

                                    for (int i = 0; i < response.getCategoryList().size(); i++) {

                                        if (response.getCategoryList().get(i).getProductList().size() > 0) {
                                            View contentView = View.inflate(getContext(), R.layout.home_recyclerview, null);

                                            TextView categoriesNameLabel = contentView.findViewById(R.id.categoriesName);
                                            Button viewAllBtn = contentView.findViewById(R.id.viewAll);
                                            RecyclerView homeProductRecyclerView = contentView.findViewById(R.id.homeProductRecyclerView);

                                            LinearLayoutManager horizontalLayoutManagaer
                                                    = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
                                            homeProductRecyclerView.setHasFixedSize(true);
                                            homeProductRecyclerView.setNestedScrollingEnabled(false);
                                            homeProductRecyclerView.setLayoutManager(horizontalLayoutManagaer);

                                            HomeProductListAdpater adapter
                                                    = new HomeProductListAdpater(requireContext(),
                                                    response.getCategoryList().get(i).getProductList(), HomeFragment.this, viewAllBtn,
                                                    response.getCategoryList().get(i).getId());
                                            homeProductRecyclerView.setAdapter(adapter);
                                            categoriesNameLabel.setText(response.getCategoryList().get(i).getCategoryName());
                                            binding.homeProductLayout.addView(contentView);
                                        }
                                    }
                                } else {
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
    }


    private void getParamsSearch(Map<String, RequestBody> map, String keyWord, String userId) {
        RequestBody keyWordBody = RequestBody.create(keyWord, MediaType.parse("text/plain"));
        RequestBody userIdBody = RequestBody.create(userId, MediaType.parse("text/plain"));

        map.put(KEYWORD, keyWordBody);
        map.put(USERID, userIdBody);
    }

    private void searchProduct(String keyWord) {
        bodyMap = new HashMap<>();
        getParamsSearch(bodyMap, keyWord, sharedPref.getUserId());

        viewModel.searchProduct(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                new ApiObserver<SearchModel>(new ApiObserver.ChangeListener<SearchModel>() {
                    @Override
                    public void onSuccess(SearchModel response) {
                        try {
                            if (response != null && response.getStatus()) {
                                if (response.getProductList().size() > 0) {
                                    searchProductList.clear();
                                    searchProductList.addAll(response.getProductList());
                                    mSearchAdapter.notifyDataSetChanged();
                                } else {
                                }

                            } else {
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
                        }
                    }

                    @Override
                    public void onErrorMessage(String message) {
                    }

                    @Override
                    public void onFail(Exception exception) {
                    }
                }));
    }

    private void getParamsAddWishlist(Map<String, RequestBody> map, String productId, String defaultPriceId, String userId) {
        RequestBody productIdBody = RequestBody.create(productId, MediaType.parse("text/plain"));
        RequestBody defaultPriceIdBody = RequestBody.create(defaultPriceId, MediaType.parse("text/plain"));
        RequestBody userIdBody = RequestBody.create(userId, MediaType.parse("text/plain"));

        map.put(PRODUCTID, productIdBody);
        map.put(PRICEID, defaultPriceIdBody);
        map.put(USERID, userIdBody);
    }

    private void addWishlist(String productId, String defaultPriceId) {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {

            bodyMap = new HashMap<>();
            getParamsAddWishlist(bodyMap, productId, defaultPriceId,sharedPref.getUserId());

            viewModel.addWishlist(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                    new ApiObserver<AddWishlistModel>(new ApiObserver.ChangeListener<AddWishlistModel>() {
                        @Override
                        public void onSuccess(AddWishlistModel response) {
                            try {
                                if (response != null && response.getStatus()) {
                                    showMessage(requireContext(), binding.getRoot(), "Item added to your wishlist");

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


    private void getParamsRemoveWishlist(Map<String, RequestBody> map, String productId, String userId) {
        RequestBody userIdBody = RequestBody.create(userId, MediaType.parse("text/plain"));
        RequestBody productIdBody = RequestBody.create(productId, MediaType.parse("text/plain"));

        map.put(USERID, userIdBody);
        map.put(PRODUCTID, productIdBody);
    }


    private void removeWishlist(String productId) {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {
            showWaitDialog(requireContext(), "Loading...");
            bodyMap = new HashMap<>();
            getParamsRemoveWishlist(bodyMap, productId, sharedPref.getUserId());

            viewModel.removeWishlist(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                    new ApiObserver<RemoveWishlistModel>(new ApiObserver.ChangeListener<RemoveWishlistModel>() {
                        @Override
                        public void onSuccess(RemoveWishlistModel response) {
                            try {
                                if (response != null && response.getStatus()) {
                                    closeWaitDialog();
                                    showMessage(requireContext(), binding.getRoot(), "Item removed from your wishlist");
                                    /*if (response.getWishlistDetails().size() > 0) {
                                        binding.wishlistRecyclerview.setVisibility(View.VISIBLE);
                                        binding.noProductLayout.setVisibility(View.GONE);

                                        WishlistAdapter adapter
                                                = new WishlistAdapter(requireContext(), response.getWishlistDetails(),
                                                WishlistFragment.this);
                                        binding.wishlistRecyclerview.setAdapter(adapter);
                                    } else {
                                        binding.wishlistRecyclerview.setVisibility(View.GONE);
                                        binding.noProductLayout.setVisibility(View.VISIBLE);
                                    }*/
                                } else {
                                    closeWaitDialog();
                                    showMessage(requireContext(), binding.getRoot(), response.getError());
                                }
                            } catch (Exception ex) {
                                closeWaitDialog();
                                ex.printStackTrace();
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

    private void getParamsAddToCart(Map<String, RequestBody> map, String productId, String defaultPriceId, String userId, String itemQuantity, String action) {
        RequestBody userIdBody = RequestBody.create(userId, MediaType.parse("text/plain"));
        RequestBody productIdBody = RequestBody.create(productId, MediaType.parse("text/plain"));
        RequestBody defaultPriceIdBody = RequestBody.create(defaultPriceId, MediaType.parse("text/plain"));
        RequestBody actionBody = RequestBody.create(action, MediaType.parse("text/plain"));
        RequestBody itemQuantityBody = RequestBody.create(itemQuantity, MediaType.parse("text/plain"));

        map.put(USERID, userIdBody);
        map.put(PRODUCTID, productIdBody);
        map.put(PRICEID, defaultPriceIdBody);
        map.put(ACTION, actionBody);
        map.put(QUANTITY, itemQuantityBody);
    }

    private void addToCart(String productId, String defaultPriceId, String itemQuantity, String action) {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {
            bodyMap = new HashMap<>();
            getParamsAddToCart(bodyMap, productId, defaultPriceId, sharedPref.getUserId(),itemQuantity, action);

            viewModel.addToCart(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                    new ApiObserver<AddCartModel>(new ApiObserver.ChangeListener<AddCartModel>() {
                        @Override
                        public void onSuccess(AddCartModel response) {
                            try {
                                if (response != null && response.getStatus()) {
                                    showMessage(requireContext(), binding.getRoot(), "Item added to your cart");
                                    if(response.getCartDetails().size()>0) {
                                        HomeActivity.setCartValue(response.getCartDetails().size());
                                    }else{
                                        HomeActivity.removeBadge();
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


    private void getParamsUpdateCart(Map<String, RequestBody> map, String productId, String defaultPriceId, String userId, String itemQuantity, String action) {
        RequestBody userIdBody = RequestBody.create(userId, MediaType.parse("text/plain"));
        RequestBody productIdBody = RequestBody.create(productId, MediaType.parse("text/plain"));
        RequestBody defaultPriceIdBody = RequestBody.create(defaultPriceId, MediaType.parse("text/plain"));
        RequestBody actionBody = RequestBody.create(action, MediaType.parse("text/plain"));
        RequestBody itemQuantityBody = RequestBody.create(itemQuantity, MediaType.parse("text/plain"));

        map.put(USERID, userIdBody);
        map.put(PRODUCTID, productIdBody);
        map.put(PRICEID, defaultPriceIdBody);
        map.put(ACTION, actionBody);
        map.put(QUANTITY, itemQuantityBody);
    }

    private void updateCart(String productId, String defaultPriceId, String itemQuantity, String action) {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {
            bodyMap = new HashMap<>();
            getParamsUpdateCart(bodyMap, productId, defaultPriceId, sharedPref.getUserId(),itemQuantity, action);

            viewModel.addToCart(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                    new ApiObserver<AddCartModel>(new ApiObserver.ChangeListener<AddCartModel>() {
                        @Override
                        public void onSuccess(AddCartModel response) {
                            try {
                                if (response != null && response.getStatus()) {
//                                    showMessage(requireContext(), binding.getRoot(), "Item moved to your cart");
                                    if(response.getCartDetails().size()>0) {
                                        HomeActivity.setCartValue(response.getCartDetails().size());
                                    }else{
                                        HomeActivity.removeBadge();
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
        if (mCategory.getCategoryName().equalsIgnoreCase("View All")) {
            findNavController(binding.getRoot()).navigate(R.id.categoriesFragment);
        } else {
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
                HomeFragmentDirections.ActionHomeFragmentToProductListFragment action =
                        HomeFragmentDirections.actionHomeFragmentToProductListFragment(mCategory.getId(), "");
                action.setCategoryId(mCategory.getId());
                action.setSubCategoryId("");
                findNavController(binding.getRoot()).navigate(action);
            }
        }

    }

    @Override
    public void onSubCategorySelection(Subcategory mSubcategory) {
        try {
            HomeFragmentDirections.ActionHomeFragmentToProductListFragment action =
                    HomeFragmentDirections.actionHomeFragmentToProductListFragment(mSubcategory.getParentId(), mSubcategory.getId());
            action.setCategoryId(mSubcategory.getParentId());
            action.setSubCategoryId(mSubcategory.getId());
            findNavController(binding.getRoot()).navigate(action);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onProductListClick(ProductList mProductList) {
        try {
            HomeFragmentDirections.ActionHomeFragmentToProductDetailsFragment action =
                    HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(mProductList);
            action.setProductModel(mProductList);
            findNavController(binding.getRoot()).navigate(action);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onViewAllClick(String categoryId) {
        try {
            if (categoryId != null && !categoryId.isEmpty()) {
                HomeFragmentDirections.ActionHomeFragmentToProductListFragment action =
                        HomeFragmentDirections.actionHomeFragmentToProductListFragment(categoryId, "");
                action.setCategoryId(categoryId);
                action.setSubCategoryId("");
                findNavController(binding.getRoot()).navigate(action);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onAddWishlist(ProductList mProductList) {
        try {
            String defaultPriceId = "";
            if (mProductList != null && mProductList.getId() != null && !mProductList.getId().isEmpty()) {
                for(int i=0; i< mProductList.getPriceDetails().size();i++){
                    if(mProductList.getPriceDetails().get(i).isDefaultPrice()){
                        defaultPriceId = mProductList.getPriceDetails().get(i).getId();
                    }
                }
                addWishlist(mProductList.getId(),defaultPriceId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onRemoveWishlist(ProductList mProductList) {
        try {
            if (mProductList != null && mProductList.getId() != null && !mProductList.getId().isEmpty()) {
                removeWishlist(mProductList.getId());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onAddToCart(ProductList mProductList) {
        try {
            String defaultPriceId = "";

            if (mProductList != null && mProductList.getId() != null && !mProductList.getId().isEmpty()) {
                for(int i=0; i< mProductList.getPriceDetails().size();i++){
                    if(mProductList.getPriceDetails().get(i).isDefaultPrice()){
                        defaultPriceId = mProductList.getPriceDetails().get(i).getId();
                    }
                }
                addToCart(mProductList.getId(), defaultPriceId,"1","insert");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onUpdateCart(ProductList mProductList, String itemQuantity) {
        try {
            String defaultPriceId = "";

            if (mProductList != null && mProductList.getId() != null && !mProductList.getId().isEmpty()) {
                for(int i=0; i< mProductList.getPriceDetails().size();i++){
                    if(mProductList.getPriceDetails().get(i).isDefaultPrice()){
                        defaultPriceId = mProductList.getPriceDetails().get(i).getId();
                    }
                }
                if(Integer.parseInt(itemQuantity) == 0){
                    updateCart(mProductList.getId(), defaultPriceId, itemQuantity, "delete");
                }else {
                    updateCart(mProductList.getId(), defaultPriceId, itemQuantity, "update");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onSearchItemClick(SearchProductList mSearchProductList) {
        try {
            ProductList mProductList = new ProductList();
            if (mSearchProductList != null) {
                mProductList.setId(mSearchProductList.getId());
                mProductList.setProductName(mSearchProductList.getProductName());

                HomeFragmentDirections.ActionHomeFragmentToProductDetailsFragment action =
                        HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(mProductList);
                action.setProductModel(mProductList);
                findNavController(binding.getRoot()).navigate(action);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
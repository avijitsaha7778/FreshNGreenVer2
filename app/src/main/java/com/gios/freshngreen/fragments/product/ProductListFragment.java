package com.gios.freshngreen.fragments.product;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gios.freshngreen.R;
import com.gios.freshngreen.activities.HomeActivity;
import com.gios.freshngreen.adapter.ProductListAdapter;
import com.gios.freshngreen.databinding.FragmentProductListBinding;
import com.gios.freshngreen.dialogs.PriceDialog;
import com.gios.freshngreen.dialogs.SortDialog;
import com.gios.freshngreen.genericClasses.ApiObserver;
import com.gios.freshngreen.responseModel.cart.AddCartModel;
import com.gios.freshngreen.responseModel.product.PriceDetail;
import com.gios.freshngreen.responseModel.wishlist.AddWishlistModel;
import com.gios.freshngreen.responseModel.product.ProductList;
import com.gios.freshngreen.responseModel.product.ProductModel;
import com.gios.freshngreen.responseModel.wishlist.RemoveWishlistModel;
import com.gios.freshngreen.responseModel.wishlist.WishlistDetail;
import com.gios.freshngreen.utils.NetworkUtils;
import com.gios.freshngreen.utils.SharedPref;
import com.gios.freshngreen.utils.SpacesItemDecoration;
import com.gios.freshngreen.viewModel.product.ProductListViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static androidx.navigation.Navigation.findNavController;
import static com.gios.freshngreen.utils.Constants.ACTION;
import static com.gios.freshngreen.utils.Constants.CATEGORYID;
import static com.gios.freshngreen.utils.Constants.PRICEID;
import static com.gios.freshngreen.utils.Constants.PRODUCTID;
import static com.gios.freshngreen.utils.Constants.QUANTITY;
import static com.gios.freshngreen.utils.Constants.SUBCATEGORYID;
import static com.gios.freshngreen.utils.Constants.USERID;
import static com.gios.freshngreen.utils.MyUtilities.showMessage;

public class ProductListFragment extends Fragment implements ProductListAdapter.Interface, SortDialog.Interface{
    private FragmentProductListBinding binding;
    private SharedPref sharedPref;
    private ProductListViewModel viewModel;
    private ProgressDialog waitDialog;
    Map<String, RequestBody> bodyMap;
    private String categoryId;
    private String subCategoryId;
    private String sortVal = "";
    private List<ProductList> productList = new ArrayList<>();


    public ProductListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProductListBinding.inflate(inflater, container, false);

        try {
            if (getArguments() != null) {
                categoryId = getArguments().getString("categoryId");
                subCategoryId = getArguments().getString("subCategoryId");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        initVar();
        setListeners();
        getProductList();
        return binding.getRoot();
    }

    private void initVar() {
        HomeActivity.setScreenName("Select, Buy & Enjoy!");

        sharedPref = new SharedPref(requireActivity());
        viewModel = new ViewModelProvider(this).get(ProductListViewModel.class);
        viewModel.init();

        binding.productListRecyclerview.setHasFixedSize(true);
        binding.productListRecyclerview.setNestedScrollingEnabled(false);
        binding.productListRecyclerview.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.productlist_grid_spacing);
        binding.productListRecyclerview.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

    }

    private void setListeners() {
        binding.sort.setOnClickListener(v -> {
            SortDialog mSortDialog = new SortDialog(this, binding, sortVal);
            mSortDialog.show(requireActivity().getSupportFragmentManager(), "sort");
        });

        binding.refresh.setOnClickListener(v -> {
            sortVal = "";
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (Build.VERSION.SDK_INT >= 26) {
                ft.setReorderingAllowed(false);
            }
            ft.detach(this).attach(this).commit();
        });
    }

    private void getParams(Map<String, RequestBody> map, String categoryId, String subCategoryId, String userId) {
        RequestBody categoryIdBody = RequestBody.create(categoryId, MediaType.parse("text/plain"));
        RequestBody subCategoryIdBody = RequestBody.create(subCategoryId, MediaType.parse("text/plain"));
        RequestBody userIdBody = RequestBody.create(userId, MediaType.parse("text/plain"));

        map.put(CATEGORYID, categoryIdBody);
        map.put(SUBCATEGORYID, subCategoryIdBody);
        map.put(USERID, userIdBody);
    }

    private void getProductList() {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {

            showWaitDialog(requireContext(), "Loading...");
            bodyMap = new HashMap<>();
            getParams(bodyMap, categoryId, subCategoryId, sharedPref.getUserId());

            viewModel.productList(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                    new ApiObserver<ProductModel>(new ApiObserver.ChangeListener<ProductModel>() {
                        @Override
                        public void onSuccess(ProductModel response) {
                            try {
                                if (response != null && response.getStatus()) {
                                    if (response.getProductList().size() > 0) {
                                        binding.productListRecyclerview.setVisibility(View.VISIBLE);
                                        binding.noProductLayout.setVisibility(View.GONE);

                                        productList.clear();
                                        productList.addAll(response.getProductList());

                                        ProductListAdapter adapter
                                                = new ProductListAdapter(requireContext(), productList, ProductListFragment.this);
                                        binding.productListRecyclerview.setAdapter(adapter);
                                    } else {
                                        binding.productListRecyclerview.setVisibility(View.GONE);
                                        binding.noProductLayout.setVisibility(View.VISIBLE);
                                        HomeActivity.setScreenName("Oops!");
                                    }

                                } else {
                                    showMessage(requireContext(), binding.getRoot(), response.getError());
                                    binding.productListRecyclerview.setVisibility(View.GONE);
                                    binding.noProductLayout.setVisibility(View.VISIBLE);
                                    HomeActivity.setScreenName("Oops!");
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                binding.productListRecyclerview.setVisibility(View.GONE);
                                binding.noProductLayout.setVisibility(View.VISIBLE);
                                HomeActivity.setScreenName("Oops!");
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

    private void getParamsAddWishlist(Map<String, RequestBody> map, String productId, String defaultPriceId, String userId) {
        RequestBody productIdBody = RequestBody.create(productId, MediaType.parse("text/plain"));
        RequestBody defaultPriceIdBody = RequestBody.create(defaultPriceId, MediaType.parse("text/plain"));
        RequestBody userIdBody = RequestBody.create(userId, MediaType.parse("text/plain"));

        map.put(PRODUCTID, productIdBody);
        map.put(PRICEID, defaultPriceIdBody);
        map.put(USERID, userIdBody);
    }

    private void addWishlist(String productId, String defaultPriceId) {
        if(NetworkUtils.isNetworkAvailable(requireContext())) {

            bodyMap = new HashMap<>();
            getParamsAddWishlist(bodyMap, productId, defaultPriceId, sharedPref.getUserId());

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
        }else{
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
                                    if(response.getCartDetails().size()>0) {
                                        HomeActivity.setCartValue(response.getCartDetails().size());
                                    }
                                    showMessage(requireContext(), binding.getRoot(), "Item added to your cart");
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
                                    if(response.getCartDetails().size()>0) {
                                        HomeActivity.setCartValue(response.getCartDetails().size());
                                    }else{
                                        HomeActivity.removeBadge();
                                    }
//                                    showMessage(requireContext(), binding.getRoot(), "Item moved to your cart");
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
    public void onProductListClick(ProductList mProductList) {
        try {
            ProductListFragmentDirections.ActionProductListFragmentToProductDetailsFragment action =
                    ProductListFragmentDirections.actionProductListFragmentToProductDetailsFragment(mProductList);
            action.setProductModel(mProductList);
            findNavController(binding.getRoot()).navigate(action);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onAddWishlist(ProductList mProductList) {
        try{
            String defaultPriceId = "";
            if(mProductList !=null && mProductList.getId() != null && !mProductList.getId().isEmpty()){
                for(int i=0; i< mProductList.getPriceDetails().size();i++){
                    if(mProductList.getPriceDetails().get(i).isDefaultPrice()){
                        defaultPriceId = mProductList.getPriceDetails().get(i).getId();
                    }
                }
                addWishlist(mProductList.getId(), defaultPriceId);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    @Override
    public void onRemoveWishlist(ProductList mProductList) {
//        Toast.makeText(requireContext(), "onRemoveWishlist  "+mProductList.getProductName(), Toast.LENGTH_SHORT).show();
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
                addToCart(mProductList.getId(),defaultPriceId,"1","insert");
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
                    updateCart(mProductList.getId(),defaultPriceId, itemQuantity, "delete");
                }else {
                    updateCart(mProductList.getId(),defaultPriceId, itemQuantity, "update");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onSortClick(FragmentProductListBinding binding, String sortType) {
        ProductListAdapter adapter;
        sortVal = sortType;
        switch (sortType) {
            case "priceLowToHigh":

                Collections.sort(productList, new Comparator<ProductList>() {
                    public int compare(ProductList obj1, ProductList obj2) {
                        return Integer.valueOf(obj1.getPriceDetails().get(0).getActualPrice()).compareTo(Integer.valueOf(obj2.getPriceDetails().get(0).getActualPrice()));
                    }
                });
                adapter = new ProductListAdapter(requireContext(), productList, ProductListFragment.this);
                binding.productListRecyclerview.setAdapter(adapter);
                break;
            case "priceHighToLow":
                Collections.sort(productList, new Comparator<ProductList>() {
                    public int compare(ProductList obj1, ProductList obj2) {
                        return Integer.valueOf(obj2.getPriceDetails().get(0).getActualPrice()).compareTo(Integer.valueOf(obj1.getPriceDetails().get(0).getActualPrice()));
                    }
                });

                adapter = new ProductListAdapter(requireContext(), productList, ProductListFragment.this);
                binding.productListRecyclerview.setAdapter(adapter);
                break;
            case "aToZ":
                Collections.sort(productList, new Comparator<ProductList>() {
                    public int compare(ProductList obj1, ProductList obj2) {
                        return obj1.getProductName().compareToIgnoreCase(obj2.getProductName()); // To compare string values
                    }
                });
                adapter = new ProductListAdapter(requireContext(), productList, ProductListFragment.this);
                binding.productListRecyclerview.setAdapter(adapter);
                break;
            case "zToA":
                Collections.sort(productList, new Comparator<ProductList>() {
                    public int compare(ProductList obj1, ProductList obj2) {
                        return obj2.getProductName().compareToIgnoreCase(obj1.getProductName()); // To compare string values
                    }
                });

                adapter = new ProductListAdapter(requireContext(), productList, ProductListFragment.this);
                binding.productListRecyclerview.setAdapter(adapter);
                break;
        }

    }

    @Override
    public void onSortCancelClick(FragmentProductListBinding binding) {

    }

}
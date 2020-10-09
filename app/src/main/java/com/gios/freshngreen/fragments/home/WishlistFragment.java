package com.gios.freshngreen.fragments.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gios.freshngreen.R;
import com.gios.freshngreen.activities.HomeActivity;
import com.gios.freshngreen.adapter.WishlistAdapter;
import com.gios.freshngreen.databinding.FragmentWishlistBinding;
import com.gios.freshngreen.genericClasses.ApiObserver;
import com.gios.freshngreen.responseModel.cart.AddCartModel;
import com.gios.freshngreen.responseModel.product.ProductList;
import com.gios.freshngreen.responseModel.wishlist.GetWishlistModel;
import com.gios.freshngreen.responseModel.wishlist.RemoveWishlistModel;
import com.gios.freshngreen.responseModel.wishlist.WishlistDetail;
import com.gios.freshngreen.utils.NetworkUtils;
import com.gios.freshngreen.utils.SharedPref;
import com.gios.freshngreen.utils.SpacesItemDecoration;
import com.gios.freshngreen.viewModel.wishList.WishlistViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static androidx.navigation.Navigation.findNavController;
import static com.gios.freshngreen.utils.Constants.ACTION;
import static com.gios.freshngreen.utils.Constants.PRODUCTID;
import static com.gios.freshngreen.utils.Constants.QUANTITY;
import static com.gios.freshngreen.utils.Constants.USERID;
import static com.gios.freshngreen.utils.MyUtilities.showMessage;


public class WishlistFragment extends Fragment implements WishlistAdapter.Interface {
    private FragmentWishlistBinding binding;
    private ProgressDialog waitDialog;
    private SharedPref sharedPref;
    private WishlistViewModel viewModel;
    Map<String, RequestBody> bodyMap;


    public WishlistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWishlistBinding.inflate(inflater, container, false);

        initVar();
        setListeners();
        getWishlist();
        return binding.getRoot();
    }

    private void initVar() {
        HomeActivity.setScreenName("Wishlist");

        sharedPref = new SharedPref(requireActivity());
        viewModel = new ViewModelProvider(this).get(WishlistViewModel.class);
        viewModel.init();

        binding.wishlistRecyclerview.setHasFixedSize(true);
        binding.wishlistRecyclerview.setNestedScrollingEnabled(false);
        binding.wishlistRecyclerview.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.productlist_grid_spacing);
        binding.wishlistRecyclerview.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
    }

    private void setListeners() {
    }

    private void getParams(Map<String, RequestBody> map, String userId) {
        RequestBody userIdBody = RequestBody.create(userId, MediaType.parse("text/plain"));
        map.put(USERID, userIdBody);
    }

    private void getWishlist() {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {

            showWaitDialog(requireContext(), "Loading...");
            bodyMap = new HashMap<>();
            getParams(bodyMap, sharedPref.getUserId());

            viewModel.getWishlist(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                    new ApiObserver<GetWishlistModel>(new ApiObserver.ChangeListener<GetWishlistModel>() {
                        @Override
                        public void onSuccess(GetWishlistModel response) {
                            try {
                                if (response != null && response.getStatus()) {
                                    if (response.getWishlistDetails().size() > 0) {
                                        binding.wishlistRecyclerview.setVisibility(View.VISIBLE);
                                        binding.noProductLayout.setVisibility(View.GONE);

                                        WishlistAdapter adapter
                                                = new WishlistAdapter(requireContext(), response.getWishlistDetails(),
                                                WishlistFragment.this);
                                        binding.wishlistRecyclerview.setAdapter(adapter);
                                    } else {
                                        binding.wishlistRecyclerview.setVisibility(View.GONE);
                                        binding.noProductLayout.setVisibility(View.VISIBLE);
                                    }

                                } else {
                                    showMessage(requireContext(), binding.getRoot(), response.getError());
                                    binding.wishlistRecyclerview.setVisibility(View.GONE);
                                    binding.noProductLayout.setVisibility(View.VISIBLE);
                                    HomeActivity.setScreenName("Oops!");
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                binding.wishlistRecyclerview.setVisibility(View.GONE);
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
                            HomeActivity.setScreenName("Oops!");
                        }

                        @Override
                        public void onFail(Exception exception) {
                            closeWaitDialog();
                            showMessage(requireContext(), binding.getRoot(), exception.getMessage());
                            HomeActivity.setScreenName("Oops!");
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

    private void removeWishlist(String productId,  boolean isMoveToCart) {
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
                                    if (isMoveToCart) {
                                        addToCart(productId,response.getWishlistDetails(),"1","insert");
                                    } else {
                                        closeWaitDialog();
                                        showMessage(requireContext(), binding.getRoot(), "Item removed from your wishlist");
                                        if (response.getWishlistDetails().size() > 0) {
                                            binding.wishlistRecyclerview.setVisibility(View.VISIBLE);
                                            binding.noProductLayout.setVisibility(View.GONE);

                                            WishlistAdapter adapter
                                                    = new WishlistAdapter(requireContext(), response.getWishlistDetails(),
                                                    WishlistFragment.this);
                                            binding.wishlistRecyclerview.setAdapter(adapter);
                                        } else {
                                            binding.wishlistRecyclerview.setVisibility(View.GONE);
                                            binding.noProductLayout.setVisibility(View.VISIBLE);
                                        }
                                    }
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

    private void getParamsAddToCart(Map<String, RequestBody> map, String productId, String userId, String itemQuantity, String action) {
        RequestBody userIdBody = RequestBody.create(userId, MediaType.parse("text/plain"));
        RequestBody productIdBody = RequestBody.create(productId, MediaType.parse("text/plain"));
        RequestBody actionBody = RequestBody.create(action, MediaType.parse("text/plain"));
        RequestBody itemQuantityBody = RequestBody.create(itemQuantity, MediaType.parse("text/plain"));

        map.put(USERID, userIdBody);
        map.put(PRODUCTID, productIdBody);
        map.put(ACTION, actionBody);
        map.put(QUANTITY, itemQuantityBody);
    }

    private void addToCart(String productId, List<WishlistDetail> wishlistDetails, String itemQuantity, String action) {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {
            bodyMap = new HashMap<>();
            getParamsAddToCart(bodyMap, productId, sharedPref.getUserId(),itemQuantity, action);

            viewModel.addToCart(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                    new ApiObserver<AddCartModel>(new ApiObserver.ChangeListener<AddCartModel>() {
                        @Override
                        public void onSuccess(AddCartModel response) {
                            try {
                                if (response != null && response.getStatus()) {
                                    showMessage(requireContext(), binding.getRoot(), "Item moved to your cart");
                                    if(response.getCartDetails().size()>0) {
                                        HomeActivity.setCartValue(response.getCartDetails().size());
                                    }else{
                                        HomeActivity.removeBadge();
                                    }
                                    if (wishlistDetails.size() > 0) {
                                        binding.wishlistRecyclerview.setVisibility(View.VISIBLE);
                                        binding.noProductLayout.setVisibility(View.GONE);

                                        WishlistAdapter adapter
                                                = new WishlistAdapter(requireContext(), wishlistDetails,
                                                WishlistFragment.this);
                                        binding.wishlistRecyclerview.setAdapter(adapter);
                                    } else {
                                        binding.wishlistRecyclerview.setVisibility(View.GONE);
                                        binding.noProductLayout.setVisibility(View.VISIBLE);
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


    private void getParamsUpdateCart(Map<String, RequestBody> map, String productId, String userId, String itemQuantity, String action) {
        RequestBody userIdBody = RequestBody.create(userId, MediaType.parse("text/plain"));
        RequestBody productIdBody = RequestBody.create(productId, MediaType.parse("text/plain"));
        RequestBody actionBody = RequestBody.create(action, MediaType.parse("text/plain"));
        RequestBody itemQuantityBody = RequestBody.create(itemQuantity, MediaType.parse("text/plain"));

        map.put(USERID, userIdBody);
        map.put(PRODUCTID, productIdBody);
        map.put(ACTION, actionBody);
        map.put(QUANTITY, itemQuantityBody);
    }

    private void updateCart(String productId, String itemQuantity, String action) {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {
            bodyMap = new HashMap<>();
            getParamsUpdateCart(bodyMap, productId, sharedPref.getUserId(),itemQuantity, action);

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
    public void onProductListClick(WishlistDetail mWishlistDetail) {
        try {
            ProductList mProductList = new ProductList();
            mProductList.setProductName(mWishlistDetail.getProductName());
            mProductList.setId(mWishlistDetail.getProductId());
            mProductList.setQuantity(mWishlistDetail.getQuantity());
            mProductList.setWishlistFlag(mWishlistDetail.getWishlistFlag());
            mProductList.setDescription(mWishlistDetail.getDescription());

            WishlistFragmentDirections.ActionWishlistFragmentToProductDetailsFragment action =
                    WishlistFragmentDirections.actionWishlistFragmentToProductDetailsFragment(mProductList);
            action.setProductModel(mProductList);
            findNavController(binding.getRoot()).navigate(action);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onRemoveWishlist(WishlistDetail mWishlistDetail) {
        try {
            if (mWishlistDetail != null && mWishlistDetail.getProductId() != null && !mWishlistDetail.getProductId().isEmpty()) {
                removeWishlist(mWishlistDetail.getProductId(), false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onMoveToCart(WishlistDetail mWishlistDetail) {
        try {
            if (mWishlistDetail != null && mWishlistDetail.getProductId() != null && !mWishlistDetail.getProductId().isEmpty()) {
                removeWishlist(mWishlistDetail.getProductId(), true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onUpdateCart(WishlistDetail mWishlistDetail, String itemQuantity) {
        try {
            if (mWishlistDetail != null && mWishlistDetail.getProductId() != null && !mWishlistDetail.getProductId().isEmpty()) {
                updateCart(mWishlistDetail.getProductId(), itemQuantity,"update");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
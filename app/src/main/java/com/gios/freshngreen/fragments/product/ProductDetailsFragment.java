package com.gios.freshngreen.fragments.product;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gios.freshngreen.R;
import com.gios.freshngreen.activities.HomeActivity;
import com.gios.freshngreen.adapter.RelatedProductListAdapter;
import com.gios.freshngreen.adapter.WishlistAdapter;
import com.gios.freshngreen.databinding.FragmentProductDetailsBinding;
import com.gios.freshngreen.dialogs.PriceDialog;
import com.gios.freshngreen.dialogs.PriceDialogForDetails;
import com.gios.freshngreen.fragments.home.WishlistFragment;
import com.gios.freshngreen.genericClasses.ApiObserver;
import com.gios.freshngreen.responseModel.cart.AddCartModel;
import com.gios.freshngreen.responseModel.product.PriceDetail;
import com.gios.freshngreen.responseModel.wishlist.AddWishlistModel;
import com.gios.freshngreen.responseModel.product.ProductDetail;
import com.gios.freshngreen.responseModel.product.ProductDetailsModel;
import com.gios.freshngreen.responseModel.product.ProductList;
import com.gios.freshngreen.responseModel.product.RelatedProductModel;
import com.gios.freshngreen.responseModel.wishlist.RemoveWishlistModel;
import com.gios.freshngreen.responseModel.wishlist.WishlistDetail;
import com.gios.freshngreen.utils.NetworkUtils;
import com.gios.freshngreen.utils.SharedPref;
import com.gios.freshngreen.viewModel.product.ProductDetailsViewModel;
import com.squareup.picasso.Picasso;
import com.varunest.sparkbutton.SparkEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.gios.freshngreen.utils.Constants.ACTION;
import static com.gios.freshngreen.utils.Constants.CATEGORYID;
import static com.gios.freshngreen.utils.Constants.PRICEID;
import static com.gios.freshngreen.utils.Constants.PRODUCTID;
import static com.gios.freshngreen.utils.Constants.QUANTITY;
import static com.gios.freshngreen.utils.Constants.SUBCATEGORYID;
import static com.gios.freshngreen.utils.Constants.USERID;
import static com.gios.freshngreen.utils.MyUtilities.showMessage;

public class ProductDetailsFragment extends Fragment implements RelatedProductListAdapter.Interface, PriceDialogForDetails.SelectPriceInterface {
    private FragmentProductDetailsBinding binding;
    private SharedPref sharedPref;
    private String productId = "";
    private String categoryId = "";
    private String subCategoryId = "";
    private ProductDetailsViewModel viewModel;
    private ProgressDialog waitDialog;
    Map<String, RequestBody> bodyMap;
    private ProductList mProductList;
    private int cartQuantityValue = 0;
    private int priceArrayItemPos = 0;
    private ProductDetail mProductDetail;


    public ProductDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false);

        try {
            if (getArguments() != null) {
                mProductList = (ProductList) getArguments().get("productModel");
                productId = mProductList.getId();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        initVar();
        setListeners();
        if (mProductList != null) {
            getProductDetails(mProductList.getId());
        }
        return binding.getRoot();
    }

    private void initVar() {
        if (mProductList != null && !mProductList.getProductName().isEmpty()) {
            HomeActivity.setScreenName(mProductList.getProductName());
        }
        sharedPref = new SharedPref(requireActivity());
        viewModel = new ViewModelProvider(this).get(ProductDetailsViewModel.class);
        viewModel.init();

        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.relatedProductRecyclerView.setHasFixedSize(true);
        binding.relatedProductRecyclerView.setNestedScrollingEnabled(false);
        binding.relatedProductRecyclerView.setLayoutManager(horizontalLayoutManagaer);
    }

    private void setListeners() {

    }

    private void getParamsForDetails(Map<String, RequestBody> map, String productId, String userId) {
        RequestBody productIdBody = RequestBody.create(productId, MediaType.parse("text/plain"));
        RequestBody userIdBody = RequestBody.create(userId, MediaType.parse("text/plain"));

        map.put(PRODUCTID, productIdBody);
        map.put(USERID, userIdBody);
    }

    private void getProductDetails(String productId) {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {

            showWaitDialog(requireContext(), "Loading...");
            bodyMap = new HashMap<>();
            getParamsForDetails(bodyMap, productId, sharedPref.getUserId());

            viewModel.productDetails(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                    new ApiObserver<ProductDetailsModel>(new ApiObserver.ChangeListener<ProductDetailsModel>() {
                        @Override
                        public void onSuccess(ProductDetailsModel response) {
                            try {
                                if (response != null && response.getStatus()) {
                                    if (response.getProductDetails().size() > 0) {
                                        mProductDetail = response.getProductDetails().get(0);
                                        setProductDescription(mProductDetail);
                                        categoryId = response.getProductDetails().get(0).getCategoryId();
                                        subCategoryId = response.getProductDetails().get(0).getSubcategoryId();

                                    } else {
                                        HomeActivity.setScreenName("Oops!");
                                    }

                                } else {
                                    showMessage(requireContext(), binding.getRoot(), response.getError());
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            } finally {
                                getRelatedProductList(productId, categoryId, subCategoryId);
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


    private void getParams(Map<String, RequestBody> map, String productId, String categoryId, String subCategoryId, String userId) {
        RequestBody productIdBody = RequestBody.create(productId, MediaType.parse("text/plain"));
        RequestBody categoryIdBody = RequestBody.create(categoryId, MediaType.parse("text/plain"));
        RequestBody subCategoryIdBody = RequestBody.create(subCategoryId, MediaType.parse("text/plain"));
        RequestBody userIdBody = RequestBody.create(userId, MediaType.parse("text/plain"));

        map.put(PRODUCTID, productIdBody);
        map.put(CATEGORYID, categoryIdBody);
        map.put(SUBCATEGORYID, subCategoryIdBody);
        map.put(USERID, userIdBody);
    }

    private void getRelatedProductList(String productId, String categoryId, String subCategoryId) {
        if(NetworkUtils.isNetworkAvailable(requireContext())) {

            bodyMap = new HashMap<>();
        getParams(bodyMap, productId, categoryId, subCategoryId, sharedPref.getUserId());

        viewModel.relatedProduct(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                new ApiObserver<RelatedProductModel>(new ApiObserver.ChangeListener<RelatedProductModel>() {
                    @Override
                    public void onSuccess(RelatedProductModel response) {
                        try {
                            if (response != null && response.getStatus()) {
                                if (response.getRelatedProductList().size() > 0) {
                                    binding.noProductLayout.setVisibility(View.GONE);
                                    binding.relatedProductRecyclerView.setVisibility(View.VISIBLE);

                                    RelatedProductListAdapter adapter
                                            = new RelatedProductListAdapter(requireContext(),
                                            response.getRelatedProductList(), ProductDetailsFragment.this);
                                    binding.relatedProductRecyclerView.setAdapter(adapter);
                                } else {
                                    binding.relatedProductRecyclerView.setVisibility(View.GONE);
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
        }else{
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

    private void addWishlist(String productIdWishlist, String defaultPriceId) {
        if(NetworkUtils.isNetworkAvailable(requireContext())) {

            bodyMap = new HashMap<>();
        getParamsAddWishlist(bodyMap, productIdWishlist, defaultPriceId, sharedPref.getUserId());

        viewModel.addWishlist(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                new ApiObserver<AddWishlistModel>(new ApiObserver.ChangeListener<AddWishlistModel>() {
                    @Override
                    public void onSuccess(AddWishlistModel response) {
                        try {
                            if (response != null && response.getStatus()) {
                                showMessage(requireContext(), binding.getRoot(), "Item added to your wishlist");
                                getProductDetails(productId);

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


    private void removeWishlist(String productIdWishlist) {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {
            showWaitDialog(requireContext(), "Loading...");
            bodyMap = new HashMap<>();
            getParamsRemoveWishlist(bodyMap, productIdWishlist, sharedPref.getUserId());

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

    private void addToCart(String productIdCart, String defaultPriceId, String itemQuantity, String action) {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {
            bodyMap = new HashMap<>();
            getParamsAddToCart(bodyMap, productIdCart, defaultPriceId, sharedPref.getUserId(),itemQuantity, action);

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

    private void updateCart(String productIdCart, String defaultPriceId, String itemQuantity, String action) {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {
            bodyMap = new HashMap<>();
            getParamsUpdateCart(bodyMap, productIdCart, defaultPriceId, sharedPref.getUserId(),itemQuantity, action);

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


    private void setProductDescription(ProductDetail mProductDetail) {
        try {
//            cartQuantityValue = Integer.parseInt(mProductDetail.getItemAddToCart());

            binding.wishListIcon.setEventListener(new SparkEventListener(){
                @Override
                public void onEvent(ImageView button, boolean buttonState) {
                    if (buttonState) {
                        // Button is active
                        String defaultPriceId = "";
                        for(int i=0; i< mProductDetail.getPriceDetails().size();i++){
                            if(mProductDetail.getPriceDetails().get(i).isDefaultPrice()){
                                defaultPriceId = mProductDetail.getPriceDetails().get(i).getId();
                            }
                        }
                        addWishlist(mProductDetail.getId(),defaultPriceId);
                    } else {
                        // Button is inactive
                        removeWishlist(mProductDetail.getId());
                    }
                }
                @Override
                public void onEventAnimationEnd(ImageView button, boolean buttonState) {
                }
                @Override
                public void onEventAnimationStart(ImageView button, boolean buttonState) {
                }
            });

            binding.add.setOnClickListener(v -> {
                String defaultPriceId = "";
                if(cartQuantityValue < Integer.parseInt(mProductDetail.getQuantity())) {
                    cartQuantityValue++;
                    binding.cartQuantity.setText(String.valueOf(cartQuantityValue));
                    for(int i=0; i< mProductDetail.getPriceDetails().size();i++){
                        if(mProductDetail.getPriceDetails().get(i).isDefaultPrice()){
                            defaultPriceId = mProductDetail.getPriceDetails().get(i).getId();
                        }
                    }
                    updateCart(mProductDetail.getId(), defaultPriceId, String.valueOf(cartQuantityValue), "update");
                }
            });


            binding.addToCart.setOnClickListener(v -> {
                String defaultPriceId = "";

                binding.cartQuantityLayout.setVisibility(View.VISIBLE);
                binding.addToCart.setVisibility(View.GONE);
                cartQuantityValue++;
                binding.cartQuantity.setText(String.valueOf(cartQuantityValue));
                for(int i=0; i< mProductDetail.getPriceDetails().size();i++){
                    if(mProductDetail.getPriceDetails().get(i).isDefaultPrice()){
                        defaultPriceId = mProductDetail.getPriceDetails().get(i).getId();
                    }
                }
                addToCart(mProductDetail.getId(),defaultPriceId, "1","insert");

            });

            binding.remove.setOnClickListener(v -> {
                String defaultPriceId = "";

                for(int i=0; i< mProductDetail.getPriceDetails().size();i++){
                    if(mProductDetail.getPriceDetails().get(i).isDefaultPrice()){
                        defaultPriceId = mProductDetail.getPriceDetails().get(i).getId();
                    }
                }
                if (cartQuantityValue <= 1) {
                    binding.cartQuantityLayout.setVisibility(View.GONE);
                    binding.addToCart.setVisibility(View.VISIBLE);
                    cartQuantityValue = 0;
                    updateCart(mProductDetail.getId(),defaultPriceId, "0", "delete");
                } else {
                    cartQuantityValue--;
                    binding.cartQuantity.setText(String.valueOf(cartQuantityValue));
                    updateCart(mProductDetail.getId(),defaultPriceId, String.valueOf(cartQuantityValue), "update");
                }
            });

            binding.unitLayout.setOnClickListener(v -> {
                if(mProductDetail.getPriceDetails().size() > 1) {
                    PriceDialogForDetails mPriceDialog = new PriceDialogForDetails(requireContext(), this, mProductDetail);
                    mPriceDialog.show(getParentFragmentManager(), TAG);
                }
            });

            if (mProductDetail.getImage() != null && !mProductDetail.getImage().isEmpty()) {
                Picasso.get().load(mProductDetail.getImage()).into(binding.productImg);
            }

            if (mProductDetail != null && mProductDetail.getProductName() != null) {
                binding.productName.setText(mProductDetail.getProductName());
            }

            for(int i=0; i< mProductDetail.getPriceDetails().size();i++){
                if(mProductDetail.getPriceDetails().get(i).isDefaultPrice()){
                    priceArrayItemPos = i;
                }
            }

            if(mProductDetail != null && mProductDetail.getPriceDetails().size() > 1) {
                binding.dropdownArrow.setVisibility(View.VISIBLE);
                binding.unitLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.weight_background));

                if (mProductDetail.getPriceDetails().get(priceArrayItemPos).getActualPrice() != null) {
                    binding.sellingPrice.setText(String.format("%s%s", getResources().getString(R.string.rs),
                            mProductDetail.getPriceDetails().get(priceArrayItemPos).getActualPrice()));
                }

                if (mProductDetail.getPriceDetails().get(priceArrayItemPos).getWeight() != null) {
//                    holder.unit.setText(String.format("%s/ Unit", mProductDetail.getPriceDetails().get(priceArrayItemPos).getWeight()));
                    binding.unit.setText(String.format("%s / %s", getResources().getString(R.string.rs) +" "+ mProductDetail.getPriceDetails().get(priceArrayItemPos).getActualPrice(),
                            mProductDetail.getPriceDetails().get(priceArrayItemPos).getWeight()));
                }

                if (mProductDetail.getPriceDetails().get(priceArrayItemPos).getRetailPrice() != null && !mProductDetail.getPriceDetails().get(priceArrayItemPos).getRetailPrice().isEmpty()) {
                    binding.productPrice.setText(String.format("%s%s", getResources().getString(R.string.rs),
                            mProductDetail.getPriceDetails().get(priceArrayItemPos).getRetailPrice()));
                    binding.productPrice.setPaintFlags(binding.productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    binding.productPrice.setText("");
                }

                if (mProductDetail.getPriceDetails().get(priceArrayItemPos).getActualPrice() != null &&
                        mProductDetail.getPriceDetails().get(priceArrayItemPos).getRetailPrice() != null
                        && !mProductDetail.getPriceDetails().get(priceArrayItemPos).getRetailPrice().isEmpty())
                {
                    binding.discount.setText(String.format("%s%% Off",
                            String.valueOf(calculateDiscount((int) Double.parseDouble(mProductDetail.getPriceDetails().get(priceArrayItemPos).getRetailPrice()),
                                    (int) Double.parseDouble(mProductDetail.getPriceDetails().get(priceArrayItemPos).getActualPrice())))));
                } else {
                    binding.discount.setText("");
                }

            }
            else{
                binding.dropdownArrow.setVisibility(View.GONE);
                binding.unitLayout.setBackground(null);

                if (mProductDetail.getPriceDetails().get(priceArrayItemPos).getActualPrice() != null) {
                    binding.sellingPrice.setText(String.format("%s%s", getResources().getString(R.string.rs),
                            mProductDetail.getPriceDetails().get(priceArrayItemPos).getActualPrice()));
                }

                if (mProductDetail.getPriceDetails().get(priceArrayItemPos).getWeight() != null) {
                    binding.unit.setText(String.format("%s", mProductDetail.getPriceDetails().get(priceArrayItemPos).getWeight()));
                }

                if (mProductDetail.getPriceDetails().get(priceArrayItemPos).getRetailPrice() != null && !mProductDetail.getPriceDetails().get(priceArrayItemPos).getRetailPrice().isEmpty()) {
                    binding.productPrice.setText(String.format("%s%s", getResources().getString(R.string.rs),
                            mProductDetail.getPriceDetails().get(priceArrayItemPos).getRetailPrice()));
                    binding.productPrice.setPaintFlags(binding.productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    binding.productPrice.setText("");
                }

                if (mProductDetail.getPriceDetails().get(priceArrayItemPos).getActualPrice() != null &&
                        mProductDetail.getPriceDetails().get(priceArrayItemPos).getRetailPrice() != null
                        && !mProductDetail.getPriceDetails().get(priceArrayItemPos).getRetailPrice().isEmpty())
                {
                    binding.discount.setText(String.format("%s%% Off",
                            String.valueOf(calculateDiscount((int) Double.parseDouble(mProductDetail.getPriceDetails().get(priceArrayItemPos).getRetailPrice()),
                                    (int) Double.parseDouble(mProductDetail.getPriceDetails().get(priceArrayItemPos).getActualPrice())))));
                } else {
                    binding.discount.setText("");
                }
            }


            if (mProductDetail != null && mProductDetail.getDescription() != null && !mProductDetail.getDescription().isEmpty()) {
                binding.description.setText(mProductDetail.getDescription().toString().trim());
            } else {
                binding.description.setText("");
            }


            if(mProductDetail != null && mProductDetail.getWishlistFlag() != null && mProductDetail.getWishlistFlag()) {
                binding.wishListIcon.setChecked(true);
            }else {
                binding.wishListIcon.setChecked(false);
            }

            if(mProductDetail != null && mProductDetail.getQuantity() != null && Integer.parseInt(mProductDetail.getQuantity()) == 0) {
                binding.outOfStock.setVisibility(View.VISIBLE);
                binding.addToCart.setVisibility(View.GONE);
                binding.cartQuantityLayout.setVisibility(View.GONE);
            }else {
                if(mProductDetail != null && mProductDetail.getItemAddToCart() != null && Integer.parseInt(mProductDetail.getItemAddToCart()) >= 1) {
                    binding.cartQuantityLayout.setVisibility(View.VISIBLE);
                    binding.addToCart.setVisibility(View.GONE);
                    binding.outOfStock.setVisibility(View.GONE);
                    cartQuantityValue = Integer.parseInt(mProductDetail.getItemAddToCart());
                    binding.cartQuantity.setText(String.valueOf(cartQuantityValue ));
                }else {
                    cartQuantityValue = Integer.parseInt(mProductDetail.getItemAddToCart());
                    binding.cartQuantityLayout.setVisibility(View.GONE);
                    binding.outOfStock.setVisibility(View.GONE);
                    binding.addToCart.setVisibility(View.VISIBLE);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void selectPriceCallback(PriceDetail mPriceDetail) {
        if(mPriceDetail != null) {
            for (int i = 0; i < mProductDetail.getPriceDetails().size(); i++) {
                if (mProductDetail.getPriceDetails().get(i).getId().equalsIgnoreCase(mPriceDetail.getId())) {
                    mProductDetail.getPriceDetails().get(i).setDefaultPrice(true);
                }else{
                    mProductDetail.getPriceDetails().get(i).setDefaultPrice(false);
                }
            }
            setProductDescription(mProductDetail);


           /* if (mPriceDetail.getActualPrice() != null) {
                binding.sellingPrice.setText(String.format("%s%s", getResources().getString(R.string.rs),
                        mPriceDetail.getActualPrice()));
            }

            if (mPriceDetail.getWeight() != null) {
//                    holder.unit.setText(String.format("%s/ Unit", mPriceDetail.getWeight()));
                binding.unit.setText(String.format("%s / %s", getResources().getString(R.string.rs) +" "+ mPriceDetail.getActualPrice(),
                        mPriceDetail.getWeight()));
            }

            if (mPriceDetail.getRetailPrice() != null && !mPriceDetail.getRetailPrice().isEmpty()) {
                binding.productPrice.setText(String.format("%s%s", getResources().getString(R.string.rs),
                        mPriceDetail.getRetailPrice()));
                binding.productPrice.setPaintFlags(binding.productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                binding.productPrice.setText("");
            }

            if (mPriceDetail.getActualPrice() != null &&
                    mPriceDetail.getRetailPrice() != null
                    && !mPriceDetail.getRetailPrice().isEmpty())
            {
                binding.discount.setText(String.format("%s%% Off",
                        String.valueOf(calculateDiscount((int) Double.parseDouble(mPriceDetail.getRetailPrice()),
                                (int) Double.parseDouble(mPriceDetail.getActualPrice())))));
            } else {
                binding.discount.setText("");
            }*/

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

    private int calculateDiscount(float retailPrice, float actualPrice) {
        int discount = 0;
        if(retailPrice > 0) {
            try {

                float res1 = retailPrice / actualPrice;
                float res2 = 100 / res1;
                discount = (int) ((Integer) 100 - res2);
                if(discount <1){
                    discount = 0;
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return discount;
    }

    @Override
    public void onProductListClick(ProductList mProductList) {
        binding.scrollView.smoothScrollTo(0, 0);
        getProductDetails(mProductList.getId());
        productId = mProductList.getId();
        HomeActivity.setScreenName(mProductList.getProductName());

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


}
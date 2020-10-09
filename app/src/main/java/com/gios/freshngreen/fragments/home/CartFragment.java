package com.gios.freshngreen.fragments.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gios.freshngreen.R;
import com.gios.freshngreen.activities.HomeActivity;
import com.gios.freshngreen.adapter.CartAdapter;
import com.gios.freshngreen.adapter.WishlistAdapter;
import com.gios.freshngreen.databinding.FragmentCartBinding;
import com.gios.freshngreen.genericClasses.ApiObserver;
import com.gios.freshngreen.responseModel.cart.AddCartModel;
import com.gios.freshngreen.responseModel.cart.CartDetail;
import com.gios.freshngreen.responseModel.cart.GetCartModel;
import com.gios.freshngreen.responseModel.wishlist.GetWishlistModel;
import com.gios.freshngreen.utils.MyUtilities;
import com.gios.freshngreen.utils.NetworkUtils;
import com.gios.freshngreen.utils.SharedPref;
import com.gios.freshngreen.utils.SpacesItemDecoration;
import com.gios.freshngreen.viewModel.cart.CartViewModel;
import com.gios.freshngreen.viewModel.login.LoginViewModel;
import com.gios.freshngreen.viewModel.wishList.WishlistViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static androidx.navigation.Navigation.findNavController;
import static com.gios.freshngreen.utils.Constants.ACTION;
import static com.gios.freshngreen.utils.Constants.PRODUCTID;
import static com.gios.freshngreen.utils.Constants.QUANTITY;
import static com.gios.freshngreen.utils.Constants.USERID;
import static com.gios.freshngreen.utils.MyUtilities.showMessage;

public class CartFragment extends Fragment implements CartAdapter.Interface {
    private FragmentCartBinding binding;
    private SharedPref sharedPref;
    private CartViewModel viewModel;
    private ProgressDialog waitDialog;
    Map<String, RequestBody> bodyMap;
    private CartAdapter adapter;
    private List<CartDetail> cartDetailsList = new ArrayList<>();
    private float cartTotalAmount = 0;


    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false);

        initVar();
        setListeners();
        getCart();
        return binding.getRoot();
    }

    private void initVar() {
        HomeActivity.setScreenName("Cart");

        sharedPref = new SharedPref(requireActivity());
        viewModel = new ViewModelProvider(this).get(CartViewModel.class);
        viewModel.init();

        binding.cartRecyclerview.setHasFixedSize(true);
        binding.cartRecyclerview.setNestedScrollingEnabled(false);
        binding.cartRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new CartAdapter(requireContext(), cartDetailsList,CartFragment.this);
        binding.cartRecyclerview.setAdapter(adapter);

    }

    private void setListeners() {
        binding.placeOrder.setOnClickListener(v->{
            int outOfStockCount = 0;
            if(cartDetailsList.size()>0) {
                for (int i = 0; i < cartDetailsList.size(); i++) {
                    if(Integer.parseInt(cartDetailsList.get(i).getQuantity()) < 1){
                        outOfStockCount++;
                    }

                }

                if(outOfStockCount > 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("CONFIRM ORDER");
                    builder.setMessage("1 or more item(s) in your cart is out of stock. Do you want to remove them?");
                    builder.setCancelable(false);

                    builder.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    removeOutOfStockItem("clearoutofstock");
                                    dialog.cancel();
                                }
                            });

                    builder.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder.create();
                    alert11.show();
                }else{
                    try {
                        CartFragmentDirections.ActionCartFragmentToOrderAddressFragment action =
                                CartFragmentDirections.actionCartFragmentToOrderAddressFragment(String.valueOf(cartTotalAmount));
                            action.setTotalAmount(String.valueOf(cartTotalAmount));
                            findNavController(binding.getRoot()).navigate(action);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }else{
                MyUtilities.showMessage(requireContext(),binding.getRoot(),"Please add some item first!!");
            }

        });
    }

    private void getParams(Map<String, RequestBody> map, String userId) {
        RequestBody userIdBody = RequestBody.create(userId, MediaType.parse("text/plain"));
        map.put(USERID, userIdBody);
    }

    private void getCart() {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {

            showWaitDialog(requireContext(), "Loading...");
            bodyMap = new HashMap<>();
            getParams(bodyMap, sharedPref.getUserId());

            viewModel.getCart(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                    new ApiObserver<GetCartModel>(new ApiObserver.ChangeListener<GetCartModel>() {
                        @Override
                        public void onSuccess(GetCartModel response) {
                            try {
                                if (response != null && response.getStatus()) {
                                    if (response.getCartDetails().size() > 0) {
                                        HomeActivity.setCartValue(response.getCartDetails().size());
                                        binding.totalText.setText(response.getCartDetails().size() +
                                                " Item " + getResources().getString(R.string.rs) + "  "
                                                + String.valueOf(calculateTotalCartValue(response.getCartDetails())));

                                        binding.savedText.setText("Saved " + getResources().getString(R.string.rs) + "  "
                                                + String.valueOf(calculateTotalSavedValue(response.getCartDetails())));

                                        binding.cartRecyclerview.setVisibility(View.VISIBLE);
                                        binding.noProductLayout.setVisibility(View.GONE);

                                        cartDetailsList.clear();
                                        cartDetailsList.addAll(response.getCartDetails());
                                        adapter.notifyDataSetChanged();

                                    } else {
                                        HomeActivity.removeBadge();

                                        binding.totalText.setText("0 item");
                                        binding.savedText.setVisibility(View.GONE);

                                        cartDetailsList.clear();
                                        cartDetailsList.addAll(response.getCartDetails());
                                        adapter.notifyDataSetChanged();

                                        binding.cartRecyclerview.setVisibility(View.GONE);
                                        binding.noProductLayout.setVisibility(View.VISIBLE);
                                    }

                                } else {
                                    showMessage(requireContext(), binding.getRoot(), response.getError());
                                    binding.cartRecyclerview.setVisibility(View.GONE);
                                    binding.noProductLayout.setVisibility(View.VISIBLE);
                                    HomeActivity.setScreenName("Oops!");
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                binding.cartRecyclerview.setVisibility(View.GONE);
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
            if (action.equals("delete")) {
                showWaitDialog(requireContext(), "Refreshing...");
            }

            bodyMap = new HashMap<>();
            getParamsUpdateCart(bodyMap, productId, sharedPref.getUserId(),itemQuantity, action);

            viewModel.addToCart(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                    new ApiObserver<AddCartModel>(new ApiObserver.ChangeListener<AddCartModel>() {
                        @Override
                        public void onSuccess(AddCartModel response) {
                            try {
                                if (response != null && response.getStatus()) {
                                    if (action.equals("delete")) {
                                        showMessage(requireContext(), binding.getRoot(), "Item removed.");
                                    }
                                    if (response.getCartDetails().size() > 0) {
                                        HomeActivity.setCartValue(response.getCartDetails().size());

                                        binding.totalText.setText(response.getCartDetails().size() +
                                                " Item " + getResources().getString(R.string.rs) + "  "
                                                + String.valueOf(calculateTotalCartValue(response.getCartDetails())));

                                        binding.savedText.setText("Saved " + getResources().getString(R.string.rs) + "  "
                                                + String.valueOf(calculateTotalSavedValue(response.getCartDetails())));

                                        binding.cartRecyclerview.setVisibility(View.VISIBLE);
                                        binding.noProductLayout.setVisibility(View.GONE);

                                        cartDetailsList.clear();
                                        cartDetailsList.addAll(response.getCartDetails());
                                        adapter.notifyDataSetChanged();

                                    } else {
                                        HomeActivity.removeBadge();

                                        binding.totalText.setText("0 item");
                                        binding.savedText.setVisibility(View.GONE);


                                        cartDetailsList.clear();
                                        cartDetailsList.addAll(response.getCartDetails());
                                        adapter.notifyDataSetChanged();

                                        binding.cartRecyclerview.setVisibility(View.GONE);
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

    private void getParamsremoveOutOfStockItem(Map<String, RequestBody> map, String userId, String action) {
        RequestBody userIdBody = RequestBody.create(userId, MediaType.parse("text/plain"));
        RequestBody actionBody = RequestBody.create(action, MediaType.parse("text/plain"));

        map.put(USERID, userIdBody);
        map.put(ACTION, actionBody);
    }

    private void removeOutOfStockItem(String action) {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {
            showWaitDialog(requireContext(), "Removing out of stock items...");
            bodyMap = new HashMap<>();
            getParamsremoveOutOfStockItem(bodyMap, sharedPref.getUserId(), action);

            viewModel.addToCart(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                    new ApiObserver<AddCartModel>(new ApiObserver.ChangeListener<AddCartModel>() {
                        @Override
                        public void onSuccess(AddCartModel response) {
                            try {
                                if (response != null && response.getStatus()) {
                                    if (response.getCartDetails().size() > 0) {
                                        HomeActivity.setCartValue(response.getCartDetails().size());

                                        binding.totalText.setText(response.getCartDetails().size() +
                                                " Item " + getResources().getString(R.string.rs) + "  "
                                                + String.valueOf(calculateTotalCartValue(response.getCartDetails())));

                                        binding.savedText.setText("Saved " + getResources().getString(R.string.rs) + "  "
                                                + String.valueOf(calculateTotalSavedValue(response.getCartDetails())));

                                        binding.cartRecyclerview.setVisibility(View.VISIBLE);
                                        binding.noProductLayout.setVisibility(View.GONE);

                                        cartDetailsList.clear();
                                        cartDetailsList.addAll(response.getCartDetails());
                                        adapter.notifyDataSetChanged();

                                    } else {
                                        HomeActivity.removeBadge();

                                        binding.totalText.setText("0 item");
                                        binding.savedText.setVisibility(View.GONE);


                                        cartDetailsList.clear();
                                        cartDetailsList.addAll(response.getCartDetails());
                                        adapter.notifyDataSetChanged();

                                        binding.cartRecyclerview.setVisibility(View.GONE);
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
    public void onRemoveFromCart(CartDetail mCartDetail) {
        try {
            if (mCartDetail != null && mCartDetail.getProductId() != null && !mCartDetail.getProductId().isEmpty()) {
                updateCart(mCartDetail.getProductId(), "0", "delete");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onUpdateCart(CartDetail mCartDetail, String itemQuantity) {
        try {
            if (mCartDetail != null && mCartDetail.getProductId() != null && !mCartDetail.getProductId().isEmpty()) {
                    updateCart(mCartDetail.getProductId(), itemQuantity, "update");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private float calculateTotalCartValue(List<CartDetail> cartDetails){
        float totalValue = 0;
        try{
            for(int i=0; i<cartDetails.size(); i++)
            {
                Float itemVal = ((Float.parseFloat(cartDetails.get(i).getActualPrice())) *
                        Integer.parseInt(cartDetails.get(i).getCartQuantity()));
                totalValue = totalValue + itemVal;
            }
            cartTotalAmount = totalValue;
            return totalValue;
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return totalValue;
    }

    private float calculateTotalSavedValue(List<CartDetail> cartDetails){
        float savedValue = 0;
        try{
            for(int i=0; i<cartDetails.size(); i++)
            {
                Float itemVal = ((Float.parseFloat(cartDetails.get(i).getRetailPrice()) -
                        Float.parseFloat(cartDetails.get(i).getActualPrice())) *
                        Integer.parseInt(cartDetails.get(i).getCartQuantity()));
                savedValue = savedValue + itemVal;
                if(savedValue <0){
                    savedValue = 0;
                }
            }
            return savedValue;
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return savedValue;
    }
}
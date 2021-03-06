package com.gios.freshngreen.fragments.order;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gios.freshngreen.R;
import com.gios.freshngreen.activities.HomeActivity;
import com.gios.freshngreen.adapter.OrderHistoryAdapter;
import com.gios.freshngreen.adapter.ProductListAdapter;
import com.gios.freshngreen.databinding.FragmentOrderHistoryBinding;
import com.gios.freshngreen.databinding.FragmentProductListBinding;
import com.gios.freshngreen.dialogs.SortDialog;
import com.gios.freshngreen.fragments.product.ProductListFragment;
import com.gios.freshngreen.fragments.product.ProductListFragmentDirections;
import com.gios.freshngreen.genericClasses.ApiObserver;
import com.gios.freshngreen.responseModel.cart.AddCartModel;
import com.gios.freshngreen.responseModel.order.OrderHistoryModel;
import com.gios.freshngreen.responseModel.order.OrderList;
import com.gios.freshngreen.responseModel.product.ProductList;
import com.gios.freshngreen.responseModel.product.ProductModel;
import com.gios.freshngreen.responseModel.wishlist.AddWishlistModel;
import com.gios.freshngreen.responseModel.wishlist.RemoveWishlistModel;
import com.gios.freshngreen.utils.NetworkUtils;
import com.gios.freshngreen.utils.SharedPref;
import com.gios.freshngreen.utils.SpacesItemDecoration;
import com.gios.freshngreen.viewModel.order.OrderHistoryViewModel;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static androidx.navigation.Navigation.findNavController;
import static com.gios.freshngreen.utils.Constants.USERID;
import static com.gios.freshngreen.utils.MyUtilities.showMessage;

public class OrderHistoryFragment extends Fragment implements OrderHistoryAdapter.Interface {
    private FragmentOrderHistoryBinding binding;
    private SharedPref sharedPref;
    private OrderHistoryViewModel viewModel;
    private ProgressDialog waitDialog;
    Map<String, RequestBody> bodyMap;


    public OrderHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderHistoryBinding.inflate(inflater, container, false);

        initVar();
        setListeners();
        getorderHistory();
        return binding.getRoot();
    }

    private void initVar() {
        HomeActivity.setScreenName("Order History");

        sharedPref = new SharedPref(requireActivity());
        viewModel = new ViewModelProvider(this).get(OrderHistoryViewModel.class);
        viewModel.init();

        binding.orderListRecyclerview.setHasFixedSize(true);
        binding.orderListRecyclerview.setNestedScrollingEnabled(false);
        binding.orderListRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));

    }

    private void setListeners() {
    }

    private void getParams(Map<String, RequestBody> map, String userId) {
        RequestBody userIdBody = RequestBody.create(userId, MediaType.parse("text/plain"));

        map.put(USERID, userIdBody);
    }

    private void getorderHistory() {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {

            showWaitDialog(requireContext(), "Loading...");
            bodyMap = new HashMap<>();
            getParams(bodyMap, sharedPref.getUserId());

            viewModel.orderHistory(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                    new ApiObserver<OrderHistoryModel>(new ApiObserver.ChangeListener<OrderHistoryModel>() {
                        @Override
                        public void onSuccess(OrderHistoryModel response) {
                            try {
                                if (response != null && response.getStatus()) {
                                    if (response.getOrderList().size() > 0) {
                                        binding.orderListRecyclerview.setVisibility(View.VISIBLE);
                                        binding.noProductLayout.setVisibility(View.GONE);

                                        OrderHistoryAdapter adapter
                                                = new OrderHistoryAdapter(requireContext(), response.getOrderList(), OrderHistoryFragment.this);
                                        binding.orderListRecyclerview.setAdapter(adapter);
                                    } else {
                                        binding.orderListRecyclerview.setVisibility(View.GONE);
                                        binding.noProductLayout.setVisibility(View.VISIBLE);
                                        HomeActivity.setScreenName("Oops!");
                                    }

                                } else {
                                    showMessage(requireContext(), binding.getRoot(), response.getError());
                                    binding.orderListRecyclerview.setVisibility(View.GONE);
                                    binding.noProductLayout.setVisibility(View.VISIBLE);
                                    HomeActivity.setScreenName("Oops!");
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                binding.orderListRecyclerview.setVisibility(View.GONE);
                                binding.noProductLayout.setVisibility(View.VISIBLE);
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
    public void onClickItem(OrderList mOrderList) {
        try {
            OrderHistoryFragmentDirections.ActionOrderHistoryFragmentToOrderHistoryDetailsFragment action =
                    OrderHistoryFragmentDirections.actionOrderHistoryFragmentToOrderHistoryDetailsFragment(mOrderList);
            action.setOrderItem(mOrderList);
            findNavController(binding.getRoot()).navigate(action);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
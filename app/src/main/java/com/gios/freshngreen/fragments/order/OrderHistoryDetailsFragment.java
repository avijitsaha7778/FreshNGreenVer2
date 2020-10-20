package com.gios.freshngreen.fragments.order;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gios.freshngreen.R;
import com.gios.freshngreen.activities.HomeActivity;
import com.gios.freshngreen.adapter.OrderDetailsAdapter;
import com.gios.freshngreen.adapter.OrderHistoryAdapter;
import com.gios.freshngreen.databinding.FragmentOrderHistoryDetailsBinding;
import com.gios.freshngreen.fragments.home.CartFragmentDirections;
import com.gios.freshngreen.genericClasses.ApiObserver;
import com.gios.freshngreen.responseModel.order.OrderDetailsModel;
import com.gios.freshngreen.responseModel.order.OrderHistoryModel;
import com.gios.freshngreen.responseModel.order.OrderList;
import com.gios.freshngreen.utils.NetworkUtils;
import com.gios.freshngreen.utils.SharedPref;
import com.gios.freshngreen.viewModel.order.OrderHistoryDetailsViewModel;
import com.gios.freshngreen.viewModel.order.OrderHistoryViewModel;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static androidx.navigation.Navigation.findNavController;
import static com.gios.freshngreen.utils.Constants.ORDERID;
import static com.gios.freshngreen.utils.Constants.USERID;
import static com.gios.freshngreen.utils.MyUtilities.showMessage;

public class OrderHistoryDetailsFragment extends Fragment {
    private FragmentOrderHistoryDetailsBinding binding;
    private SharedPref sharedPref;
    private OrderHistoryDetailsViewModel viewModel;
    private ProgressDialog waitDialog;
    Map<String, RequestBody> bodyMap;
    private OrderList mOrderList;


    public OrderHistoryDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderHistoryDetailsBinding.inflate(inflater, container, false);


        try {
            if (getArguments() != null) {
                mOrderList = (OrderList) getArguments().getSerializable("orderItem");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        initVar();
        setListeners();
        getorderDetails();
        return binding.getRoot();
    }

    private void initVar() {
        HomeActivity.setScreenName("Order Details");

        sharedPref = new SharedPref(requireActivity());
        viewModel = new ViewModelProvider(this).get(OrderHistoryDetailsViewModel.class);
        viewModel.init();

        binding.orderListRecyclerview.setHasFixedSize(true);
        binding.orderListRecyclerview.setNestedScrollingEnabled(false);
        binding.orderListRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.orderId.setText(mOrderList.getOrderId());
        binding.amount.setText(String.format("%s %s", getResources().getString(R.string.rs), mOrderList.getNettotal()));
        binding.date.setText(formatDate(mOrderList.getOrderTime(),"yyyy-MM-dd hh:mm:ss","EEEE, dd MMMM yyyy"));
        binding.time.setText(formatDate(mOrderList.getOrderTime(),"yyyy-MM-dd hh:mm:ss","hh:mm a"));
        binding.paymentMode.setText(mOrderList.getPaymentMethod());

        if(Integer.parseInt(mOrderList.getDeliveryCharge()) == 0){
            binding.deliveryCharge.setText("Free");
        }else if(Integer.parseInt(mOrderList.getDeliveryCharge()) > 0){
            binding.deliveryCharge.setText(String.format("%s %s", getResources().getString(R.string.rs), mOrderList.getDeliveryCharge()));
        }


    }

    private void setListeners() {
    }

    private void getParams(Map<String, RequestBody> map, String orderId) {
        RequestBody orderIdBody = RequestBody.create(orderId, MediaType.parse("text/plain"));

        map.put(ORDERID, orderIdBody);
    }

    private void getorderDetails() {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {

            showWaitDialog(requireContext(), "Loading...");
            bodyMap = new HashMap<>();
            getParams(bodyMap, mOrderList.getId());

            viewModel.orderDetails(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                    new ApiObserver<OrderDetailsModel>(new ApiObserver.ChangeListener<OrderDetailsModel>() {
                        @Override
                        public void onSuccess(OrderDetailsModel response) {
                            try {
                                if (response != null && response.getStatus()) {
                                    if (response.getOrderItemList().size() > 0) {

                                        OrderDetailsAdapter adapter
                                                = new OrderDetailsAdapter(requireContext(), response.getOrderItemList());
                                        binding.orderListRecyclerview.setAdapter(adapter);
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

    private String formatDate(String date, String sInputFormat, String sOutputFormat) {
        Date newDate = null;
        String returnDate = "";
        SimpleDateFormat inputFormat = new SimpleDateFormat(sInputFormat);
        SimpleDateFormat outputFormat = new SimpleDateFormat(sOutputFormat);
        try {
            newDate = inputFormat.parse(date);
            returnDate = outputFormat.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnDate;
    }

}
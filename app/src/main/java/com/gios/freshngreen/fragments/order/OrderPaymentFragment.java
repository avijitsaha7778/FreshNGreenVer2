package com.gios.freshngreen.fragments.order;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.gios.freshngreen.R;
import com.gios.freshngreen.activities.HomeActivity;
import com.gios.freshngreen.activities.LoginActivity;
import com.gios.freshngreen.activities.SplashScreenActivity;
import com.gios.freshngreen.databinding.FragmentOrderAddressBinding;
import com.gios.freshngreen.databinding.FragmentOrderPaymentBinding;
import com.gios.freshngreen.dialogs.EditAddressDialog;
import com.gios.freshngreen.genericClasses.ApiObserver;
import com.gios.freshngreen.responseModel.cart.CartDetail;
import com.gios.freshngreen.responseModel.cart.GetCartModel;
import com.gios.freshngreen.responseModel.order.PlaceOrderModel;
import com.gios.freshngreen.responseModel.order.UpdateAddressModel;
import com.gios.freshngreen.responseModel.profile.AreaList;
import com.gios.freshngreen.responseModel.profile.GetAreaListModel;
import com.gios.freshngreen.responseModel.profile.GetProfileModel;
import com.gios.freshngreen.responseModel.profile.PinList;
import com.gios.freshngreen.responseModel.profile.UserDetail;
import com.gios.freshngreen.utils.MyUtilities;
import com.gios.freshngreen.utils.NetworkUtils;
import com.gios.freshngreen.utils.SharedPref;
import com.gios.freshngreen.viewModel.order.OrderAddressViewModel;
import com.gios.freshngreen.viewModel.order.OrderPaymentViewModel;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.gios.freshngreen.utils.Constants.ADDRESS;
import static com.gios.freshngreen.utils.Constants.AREA;
import static com.gios.freshngreen.utils.Constants.CITY;
import static com.gios.freshngreen.utils.Constants.DELIVERY_CHARGE;
import static com.gios.freshngreen.utils.Constants.LANDARK;
import static com.gios.freshngreen.utils.Constants.LOGIN;
import static com.gios.freshngreen.utils.Constants.NET_TOTAL;
import static com.gios.freshngreen.utils.Constants.PAY_METHOD;
import static com.gios.freshngreen.utils.Constants.PAY_STATUS;
import static com.gios.freshngreen.utils.Constants.PINCODE;
import static com.gios.freshngreen.utils.Constants.SUB_TOTAL;
import static com.gios.freshngreen.utils.Constants.TYPE;
import static com.gios.freshngreen.utils.Constants.USERID;
import static com.gios.freshngreen.utils.MyUtilities.showMessage;

public class OrderPaymentFragment extends Fragment{
    private FragmentOrderPaymentBinding binding;
    private SharedPref sharedPref;
    private OrderPaymentViewModel viewModel;
    private ProgressDialog waitDialog;
    Map<String, RequestBody> bodyMap;
    private List<AreaList> areaList = new ArrayList<>();
    private List<PinList> pinList = new ArrayList<>();
    private UserDetail userDetail;
    private RadioButton radioPayButton;
    private String subTotalAmt = "", deliveryChangeAmt = "", netTotalAmt = "";


    public OrderPaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderPaymentBinding.inflate(inflater, container, false);


        initVar();
        setListeners();
        if (NetworkUtils.isNetworkAvailable(requireContext())) {
            getCart();
        } else {
            showMessage(requireContext(), binding.getRoot(), getString(R.string.check_connections));
        }
        return binding.getRoot();
    }

    private void initVar() {
        HomeActivity.setScreenName("Payment");

        sharedPref = new SharedPref(requireActivity());
        viewModel = new ViewModelProvider(this).get(OrderPaymentViewModel.class);
        viewModel.init();


    }

    private void setListeners() {
        binding.proceedPayment.setOnClickListener(v -> {
            try {
                int selectedId = binding.radioGroup.getCheckedRadioButtonId();
                radioPayButton = (RadioButton)requireActivity().findViewById(selectedId);
                if(radioPayButton !=null && !radioPayButton.getText().toString().isEmpty()){
//                    MyUtilities.showMessage(requireContext(),binding.getRoot(),radioPayButton.getText().toString());
                    placeOrder("false", radioPayButton.getText().toString(),
                            subTotalAmt, deliveryChangeAmt, netTotalAmt);
                }else{
                    MyUtilities.showMessage(requireContext(),binding.getRoot(),"Please Select Payment Mode");
                }
            }catch (Exception ex){
                ex.printStackTrace();
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
                                        setSummery(response.getCartDetails());

                                    } else {
                                    }

                                } else {
                                    showMessage(requireContext(), binding.getRoot(), response.getError());
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            } finally {
//                                closeWaitDialog();
                                getProfileDetails();

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

    private void getProfileDetails() {
        bodyMap = new HashMap<>();
        getParams(bodyMap, sharedPref.getUserId());

        viewModel.getProfileDetails(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                new ApiObserver<GetProfileModel>(new ApiObserver.ChangeListener<GetProfileModel>() {
                    @Override
                    public void onSuccess(GetProfileModel response) {
                        try {
                            if (response != null && response.getStatus()) {
                                if (response.getUserDetails().size() > 0) {
                                   /* userDetail = response.getUserDetails().get(0);
                                    binding.name.setText(response.getUserDetails().get(0).getFullName());
                                    binding.address.setText(String.format("%s,\n%s,\n%s,\n%s,\nPincode- %s", response.getUserDetails().get(0).getAddress(),
                                            response.getUserDetails().get(0).getArea(), response.getUserDetails().get(0).getLandmark(),
                                            response.getUserDetails().get(0).getCity(), response.getUserDetails().get(0).getPin()));
                                    if(sharedPref!= null && sharedPref.getMobile() != null && sharedPref.getMobile().length() > 0) {
                                        binding.phone.setText(sharedPref.getMobile());
                                    }*/
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


    private void getParamsplaceOrder(Map<String, RequestBody> map, String userId, String payStatus, String payMethod,
                                     String subTotal, String deliveryChange, String netTotal) {
        RequestBody userIdBody = RequestBody.create(userId, MediaType.parse("text/plain"));
        RequestBody payStatusBody = RequestBody.create(payStatus, MediaType.parse("text/plain"));
        RequestBody payMethodBody = RequestBody.create(payMethod, MediaType.parse("text/plain"));
        RequestBody subTotalBody = RequestBody.create(subTotal, MediaType.parse("text/plain"));
        RequestBody deliveryChangeBody = RequestBody.create(deliveryChange, MediaType.parse("text/plain"));
        RequestBody netTotalBody = RequestBody.create(netTotal, MediaType.parse("text/plain"));

        map.put(USERID, userIdBody);
        map.put(PAY_STATUS, payStatusBody);
        map.put(PAY_METHOD, payMethodBody);
        map.put(SUB_TOTAL, subTotalBody);
        map.put(DELIVERY_CHARGE, deliveryChangeBody);
        map.put(NET_TOTAL, netTotalBody);


    }

    private void placeOrder( String payStatus, String payMethod,
                             String subTotal, String deliveryChange, String netTotal) {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {

            showWaitDialog(requireContext(), "Placing your Order...");
            bodyMap = new HashMap<>();
            getParamsplaceOrder(bodyMap, sharedPref.getUserId(), payStatus, payMethod,
                    subTotal, deliveryChange, netTotal);

            viewModel.placeOrder(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                    new ApiObserver<PlaceOrderModel>(new ApiObserver.ChangeListener<PlaceOrderModel>() {
                        @Override
                        public void onSuccess(PlaceOrderModel response) {
                            try {
                                if (response != null && response.getStatus()) {
                                    if (response.getOrderId().length() > 0) {
                                        MyUtilities.showMessage(requireContext(),binding.getRoot(),
                                                "Your Order Placed Successfully.");

                                        HomeActivity.removeBadge();

                                        new Handler().postDelayed(() -> {
                                            requireActivity().onBackPressed();
                                            requireActivity().onBackPressed();
                                            requireActivity().onBackPressed();
                                        }, Snackbar.LENGTH_LONG + 200);

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

    private void setSummery(List<CartDetail> cartDetail) {
        try{
            float totalAmt = 0;
            if(cartDetail != null){
                if(cartDetail.size() > 0){
                    binding.orderItems.setText(String.valueOf(cartDetail.size()));
                    binding.subTotal.setText(String.format("%s %s", getResources().getString(R.string.rs), String.valueOf(calculateTotalCartValue(cartDetail))));
                    subTotalAmt = String.valueOf(calculateTotalCartValue(cartDetail));

                    if(calculateTotalCartValue(cartDetail) > 499){
                        binding.deliveryCharge.setText("Free");
                        totalAmt = (int) calculateTotalCartValue(cartDetail);
                        deliveryChangeAmt = "0";
                    }else{
                        binding.deliveryCharge.setText(String.format("%s  20", getResources().getString(R.string.rs)));
                        totalAmt = calculateTotalCartValue(cartDetail) + 20;
                        deliveryChangeAmt = "20";
                    }
                    binding.total.setText(String.format("%s %s", getResources().getString(R.string.rs), String.valueOf(totalAmt)));
                    netTotalAmt = String.valueOf(totalAmt);

                }
            }
        }catch (Exception ex){
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
            return totalValue;
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return totalValue;
    }

}
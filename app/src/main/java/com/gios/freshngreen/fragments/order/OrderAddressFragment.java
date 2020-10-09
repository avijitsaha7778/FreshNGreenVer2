package com.gios.freshngreen.fragments.order;

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
import com.gios.freshngreen.databinding.FragmentCartBinding;
import com.gios.freshngreen.databinding.FragmentOrderAddressBinding;
import com.gios.freshngreen.databinding.FragmentProfileBinding;
import com.gios.freshngreen.dialogs.EditAddressDialog;
import com.gios.freshngreen.fragments.home.CartFragment;
import com.gios.freshngreen.fragments.home.CartFragmentDirections;
import com.gios.freshngreen.genericClasses.ApiObserver;
import com.gios.freshngreen.responseModel.cart.AddCartModel;
import com.gios.freshngreen.responseModel.cart.CartDetail;
import com.gios.freshngreen.responseModel.cart.GetCartModel;
import com.gios.freshngreen.responseModel.order.UpdateAddressModel;
import com.gios.freshngreen.responseModel.profile.AreaList;
import com.gios.freshngreen.responseModel.profile.GetAreaListModel;
import com.gios.freshngreen.responseModel.profile.GetProfileModel;
import com.gios.freshngreen.responseModel.profile.PinList;
import com.gios.freshngreen.responseModel.profile.UpdateProfileModel;
import com.gios.freshngreen.responseModel.profile.UserDetail;
import com.gios.freshngreen.utils.MyUtilities;
import com.gios.freshngreen.utils.NetworkUtils;
import com.gios.freshngreen.utils.SharedPref;
import com.gios.freshngreen.viewModel.cart.CartViewModel;
import com.gios.freshngreen.viewModel.order.OrderAddressViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static androidx.navigation.Navigation.findNavController;
import static com.gios.freshngreen.utils.Constants.ACTION;
import static com.gios.freshngreen.utils.Constants.ADDRESS;
import static com.gios.freshngreen.utils.Constants.ANNIVERSARY;
import static com.gios.freshngreen.utils.Constants.AREA;
import static com.gios.freshngreen.utils.Constants.CITY;
import static com.gios.freshngreen.utils.Constants.DOB;
import static com.gios.freshngreen.utils.Constants.EMAIL;
import static com.gios.freshngreen.utils.Constants.FULL_NAME;
import static com.gios.freshngreen.utils.Constants.LANDARK;
import static com.gios.freshngreen.utils.Constants.PINCODE;
import static com.gios.freshngreen.utils.Constants.PRODUCTID;
import static com.gios.freshngreen.utils.Constants.QUANTITY;
import static com.gios.freshngreen.utils.Constants.USERID;
import static com.gios.freshngreen.utils.MyUtilities.showMessage;

public class OrderAddressFragment extends Fragment implements EditAddressDialog.EditAddressDialogInterface {
    private FragmentOrderAddressBinding binding;
    private SharedPref sharedPref;
    private OrderAddressViewModel viewModel;
    private ProgressDialog waitDialog;
    Map<String, RequestBody> bodyMap;
    private String cartTotalAmount;
    private List<AreaList> areaList = new ArrayList<>();
    private List<PinList> pinList = new ArrayList<>();
    private UserDetail userDetail;


    public OrderAddressFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrderAddressBinding.inflate(inflater, container, false);

        try {
            if (getArguments() != null) {
                cartTotalAmount = getArguments().getString("totalAmount");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        initVar();
        setListeners();
        if (NetworkUtils.isNetworkAvailable(requireContext())) {
            getProfileDetails();
        } else {
            showMessage(requireContext(), binding.getRoot(), getString(R.string.check_connections));
        }
        return binding.getRoot();
    }

    private void initVar() {
        HomeActivity.setScreenName("Delivery Address");
        binding.totalText.setText(String.format("%s %s", getResources().getString(R.string.rs), cartTotalAmount));

        sharedPref = new SharedPref(requireActivity());
        viewModel = new ViewModelProvider(this).get(OrderAddressViewModel.class);
        viewModel.init();


    }

    private void setListeners() {
        binding.editAddress.setOnClickListener(v -> {
            try {
                EditAddressDialog mEditAddressDialog = new EditAddressDialog(this, userDetail, areaList, pinList);
                mEditAddressDialog.show(requireActivity().getSupportFragmentManager(), "edit_address");
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });

        binding.editAddressIcon.setOnClickListener(v -> {
            try {
                EditAddressDialog mEditAddressDialog = new EditAddressDialog(this, userDetail, areaList, pinList);
                mEditAddressDialog.show(requireActivity().getSupportFragmentManager(), "edit_address");
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });

         binding.proceedPayment.setOnClickListener(v -> {
             try {
                 if(!binding.address.getText().toString().isEmpty()) {
                     findNavController(binding.getRoot()).navigate(R.id.action_orderAddressFragment_to_orderPaymentFragment);
                 }else{
                     MyUtilities.showMessage(requireContext(),binding.getRoot(),"Please add Delivery Address");
                 }
             } catch (Exception ex) {
                 ex.printStackTrace();
             }
        });

    }

    private void getParams(Map<String, RequestBody> map, String userId) {
        RequestBody userIdBody = RequestBody.create(userId, MediaType.parse("text/plain"));

        map.put(USERID, userIdBody);
    }

    private void getProfileDetails() {
        showWaitDialog(requireContext(), "Loading...");
        bodyMap = new HashMap<>();
        getParams(bodyMap, sharedPref.getUserId());

        viewModel.getProfileDetails(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                new ApiObserver<GetProfileModel>(new ApiObserver.ChangeListener<GetProfileModel>() {
                    @Override
                    public void onSuccess(GetProfileModel response) {
                        try {
                            if (response != null && response.getStatus()) {
                                if (response.getUserDetails().size() > 0) {
                                    userDetail = response.getUserDetails().get(0);
                                    binding.name.setText(response.getUserDetails().get(0).getFullName());
                                    if(response.getUserDetails().get(0).getAddress() != null && !response.getUserDetails().get(0).getAddress().isEmpty()) {
                                        binding.address.setText(String.format("%s,\n%s,\n%s,\n%s,\nPincode- %s", response.getUserDetails().get(0).getAddress(),
                                                response.getUserDetails().get(0).getArea(), response.getUserDetails().get(0).getLandmark(),
                                                response.getUserDetails().get(0).getCity(), response.getUserDetails().get(0).getPin()));
                                    }
                                    if(sharedPref!= null && sharedPref.getMobile() != null && sharedPref.getMobile().length() > 0) {
                                        binding.phone.setText(sharedPref.getMobile());
                                    }
                                } else {
                                }

                            } else {
                                showMessage(requireContext(), binding.getRoot(), response.getError());
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            getAreaList();
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

    private void getAreaList() {
//        showWaitDialog(requireContext(), "Loading...");
        bodyMap = new HashMap<>();
        getParams(bodyMap, sharedPref.getUserId());

        viewModel.getAreaList(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                new ApiObserver<GetAreaListModel>(new ApiObserver.ChangeListener<GetAreaListModel>() {
                    @Override
                    public void onSuccess(GetAreaListModel response) {
                        try {
                            if (response != null && response.getStatus()) {
                                if (response.getAreaList() != null && response.getAreaList().size() > 0) {
                                    areaList.clear();
                                    areaList.addAll(response.getAreaList());
                                }
                                if (response.getPinList() != null && response.getPinList().size() > 0) {
                                    pinList.clear();
                                    pinList.addAll(response.getPinList());
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

    private void getParamsupdateAddress(Map<String, RequestBody> map, String userId, String address, String landMark, String area, String pin, String city) {

        RequestBody userIdBody = RequestBody.create(userId, MediaType.parse("text/plain"));
        RequestBody addressBody = RequestBody.create(address, MediaType.parse("text/plain"));
        RequestBody landMarkBody = RequestBody.create(landMark, MediaType.parse("text/plain"));
        RequestBody areaBody = RequestBody.create(area, MediaType.parse("text/plain"));
        RequestBody pinBody = RequestBody.create(pin, MediaType.parse("text/plain"));
        RequestBody cityBody = RequestBody.create(city, MediaType.parse("text/plain"));


        map.put(USERID, userIdBody);
        map.put(ADDRESS, addressBody);
        map.put(LANDARK, landMarkBody);
        map.put(AREA, areaBody);
        map.put(PINCODE, pinBody);
        map.put(CITY, cityBody);
    }

    private void updateAddress(String address, String landMark, String area, String pin, String city) {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {
            showWaitDialog(requireContext(), "Loading...");
            bodyMap = new HashMap<>();

            getParamsupdateAddress(bodyMap, sharedPref.getUserId(), address, landMark, area, pin, city);

            viewModel.updateAddress(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                    new ApiObserver<UpdateAddressModel>(new ApiObserver.ChangeListener<UpdateAddressModel>() {
                        @Override
                        public void onSuccess(UpdateAddressModel response) {
                            try {
                                if (response != null && response.getStatus()) {
                                    if (response.getUserAddressDetails().size() > 0) {
                                        binding.address.setText(String.format("%s,\n%s,\n%s,\n%s,\nPincode- %s", response.getUserAddressDetails().get(0).getAddress(),
                                                response.getUserAddressDetails().get(0).getArea(), response.getUserAddressDetails().get(0).getLandmark(),
                                                response.getUserAddressDetails().get(0).getCity(), response.getUserAddressDetails().get(0).getPin()));
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

    @Override
    public void onAddressClick(String address1, String address2, String area, String city, String pinCode, String landmark) {
        try{
            String address;
            if (address2.isEmpty()) {
                address = address1;
            } else {
                address = address1 + ", " + address2;
            }
            updateAddress(address, landmark, area, pinCode, city);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onCancelClick() {

    }
}
package com.gios.freshngreen.fragments.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import com.gios.freshngreen.R;
import com.gios.freshngreen.databinding.FragmentForgotPasswordBinding;
import com.gios.freshngreen.genericClasses.ApiObserver;
import com.gios.freshngreen.responseModel.login.ForgotPasswordModel;
import com.gios.freshngreen.responseModel.login.LoginModel;
import com.gios.freshngreen.utils.MyUtilities;
import com.gios.freshngreen.utils.NetworkUtils;
import com.gios.freshngreen.utils.SharedPref;
import com.gios.freshngreen.viewModel.login.ForgotPasswordViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.gios.freshngreen.utils.Constants.IS_CONNECTED;
import static com.gios.freshngreen.utils.Constants.PASSWORD;
import static com.gios.freshngreen.utils.Constants.PHONE;
import static com.gios.freshngreen.utils.Constants.STATUS;
import static com.gios.freshngreen.utils.MyUtilities.showMessage;


public class ForgotPasswordFragment extends Fragment {

    private FragmentForgotPasswordBinding binding;
    private ForgotPasswordViewModel viewModel;
    private String phone;
    private ProgressDialog waitDialog;
    Map<String, RequestBody> bodyMap;
    private SharedPref sharedPref;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        initVar();
        listeners();
        return binding.getRoot();

    }

    private void initVar() {
        viewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);
        viewModel.init();
        sharedPref = new SharedPref(requireActivity());
    }

    private void listeners() {
        try {
            binding.continueButton.setOnClickListener(v -> {
                binding.phone.clearFocus();
                phone = Objects.requireNonNull(binding.phone.getText()).toString().trim();
                if (NetworkUtils.isNetworkAvailable(requireContext())) {
                    if (validate(phone)) {
                        forgotPassword(phone);
                    }
                }
                else {
                    showMessage(requireContext(), binding.getRoot(), getString(R.string.check_connections));
                }
            });

            binding.homeLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    binding.phone.clearFocus();
                    return true;
                }
            });

            binding.phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        MyUtilities.hideKeyboard(getActivity(),binding.phone);
                    }
                }
            });

        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getParams(Map<String, RequestBody> map, String phone) {
        RequestBody phoneBody = RequestBody.create(phone, MediaType.parse("text/plain"));
        map.put(PHONE, phoneBody);
    }



    private void forgotPassword(String phone) {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {

            showWaitDialog(requireContext(),"Sending...");
        bodyMap = new HashMap<>();
        getParams(bodyMap, phone);

        viewModel.forgotPassword(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                new ApiObserver<ForgotPasswordModel>(new ApiObserver.ChangeListener<ForgotPasswordModel>() {
                    @Override
                    public void onSuccess(ForgotPasswordModel response) {
                        try {
                            if (response != null && response.getStatus())
                            {
                                OtpFragment otpFragment = new OtpFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("otp", response.getOtp());
                                bundle.putString("userId", response.getUserId());
                                bundle.putString("mobile", response.getPhoneNo());
                                otpFragment.setArguments(bundle);
                                bundle.putBoolean(STATUS, true);
                                getParentFragmentManager().beginTransaction().add(R.id.fragment_container, otpFragment).addToBackStack("ForgotPasswordFragment").commit();
                            }
                            else if(response != null && !response.getError().isEmpty()){
                                showMessage(requireContext(), binding.getRoot(), response.getError());
                            }
                            else {
                                showMessage(requireContext(), binding.getRoot(), getResources().getString(R.string.something_went_wrong));
                            }
                        }catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        finally {
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

    private boolean validate(String phone) {
        boolean isValidate = true;
        if (phone != null && phone.isEmpty()) {
            isValidate = false;
            showMessage(requireContext(), binding.getRoot(), "Please enter mobile number");
        } else if (phone != null && phone.length() != 10) {
            isValidate = false;
            showMessage(requireContext(), binding.getRoot(), "Mobile number must be 10 digits");
        }
        return isValidate;
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


}
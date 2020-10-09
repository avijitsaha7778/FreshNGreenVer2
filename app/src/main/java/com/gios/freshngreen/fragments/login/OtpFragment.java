package com.gios.freshngreen.fragments.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gios.freshngreen.R;
import com.gios.freshngreen.activities.HomeActivity;
import com.gios.freshngreen.databinding.FragmentOtpBinding;
import com.gios.freshngreen.genericClasses.ApiObserver;
import com.gios.freshngreen.responseModel.login.ResendOtpModel;
import com.gios.freshngreen.responseModel.login.VerifyOtpForgotPasswordModel;
import com.gios.freshngreen.responseModel.login.VerifyOtpModel;
import com.gios.freshngreen.utils.MyUtilities;
import com.gios.freshngreen.utils.NetworkUtils;
import com.gios.freshngreen.utils.SharedPref;
import com.gios.freshngreen.viewModel.login.VerifyOTPViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.gios.freshngreen.utils.Constants.FULLNAME;
import static com.gios.freshngreen.utils.Constants.OTP;
import static com.gios.freshngreen.utils.Constants.PHONE;
import static com.gios.freshngreen.utils.Constants.STATUS;
import static com.gios.freshngreen.utils.Constants.USERID;
import static com.gios.freshngreen.utils.MyUtilities.showMessage;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtpFragment extends Fragment {
    private FragmentOtpBinding binding;
    private List<TextView> emailOtpTexts;
    private List<TextView> mobileOtpTexts;
    private String otp;
    private String userId;
    private String mobile;
    private SharedPref sharedPref;
    private boolean fromForgotPasswordScreen = false;
    private VerifyOTPViewModel viewModel;
    Map<String, RequestBody> bodyMap;
    private ProgressDialog waitDialog;

    public OtpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOtpBinding.inflate(inflater, container, false);


        try {
            if (getArguments() != null) {
                otp = getArguments().getString("otp");
                userId = getArguments().getString("userId");
                mobile = getArguments().getString("mobile");
                fromForgotPasswordScreen = getArguments().getBoolean(STATUS);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        initVar();
        setListeners();
        return binding.getRoot();
    }

    private void initVar() {
        viewModel = new ViewModelProvider(this).get(VerifyOTPViewModel.class);
        viewModel.init();
        sharedPref = new SharedPref(requireContext());
        binding.otp.setText(otp);
    }


    private void setListeners() {
        try {
            binding.resendOtp.setOnClickListener(v -> {
                if (NetworkUtils.isNetworkAvailable(requireContext()))
                    reSendOtp();
                else
                    showMessage(requireContext(), binding.getRoot(), getString(R.string.check_connections));
            });

            binding.continueButton.setOnClickListener(v -> {
                binding.otp.clearFocus();
                String otp = binding.otp.getText().toString();
                if (otp.length() == 4) {
                    if (fromForgotPasswordScreen) {
                        if (NetworkUtils.isNetworkAvailable(requireContext())) {
                            verifyOtpForgotPassword(otp);
                        } else {
                            showMessage(requireContext(), binding.getRoot(), getString(R.string.check_connections));
                        }
                    } else {
                        if (NetworkUtils.isNetworkAvailable(requireContext())) {
                            verifyOtp(otp);
                        } else {
                            showMessage(requireContext(), binding.getRoot(), getString(R.string.check_connections));
                        }
                    }
                } else {
                    showMessage(requireContext(), binding.getRoot(), getString(R.string.enter_valid_otp));
                }
            });


            binding.homeLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    binding.otp.clearFocus();
                    return true;
                }
            });

            binding.otp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        MyUtilities.hideKeyboard(getActivity(), binding.otp);
                    }
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void getParams(Map<String, RequestBody> map, String otp, String phone, String userId) {
        RequestBody otpBody = RequestBody.create(otp, MediaType.parse("text/plain"));
        RequestBody phoneBody = RequestBody.create(phone, MediaType.parse("text/plain"));
        RequestBody userIdBody = RequestBody.create(userId, MediaType.parse("text/plain"));

        map.put(OTP, otpBody);
        map.put(PHONE, phoneBody);
        map.put(USERID, userIdBody);
    }


    private void verifyOtp(String otp) {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {
            try {
                showWaitDialog(requireContext(), "Verifying OTP ...");
                bodyMap = new HashMap<>();
                getParams(bodyMap, otp, mobile, userId);

                viewModel.verifyOtp(bodyMap, requireContext(), requireActivity()).observe(requireActivity(),
                        new ApiObserver<VerifyOtpModel>(new ApiObserver.ChangeListener<VerifyOtpModel>() {
                            @Override
                            public void onSuccess(VerifyOtpModel response) {
                                try {
                                    if (response != null && response.getStatus()) {
                                        sharedPref.setMobile(mobile);
                                        sharedPref.setUserId(userId);
                                        startActivity(new Intent(requireActivity(), HomeActivity.class));
                                        requireActivity().finish();
                                    } else if (response != null && !response.getStatus() && !response.getError().isEmpty()) {
                                        showMessage(requireContext(), binding.getRoot(), response.getError());
                                    } else {
                                        showMessage(requireContext(), binding.getRoot(), getResources().getString(R.string.something_went_wrong));
                                        requireActivity().onBackPressed();
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
            } catch (Exception ex) {
                ex.printStackTrace();
                closeWaitDialog();
            }
        } else {
            showMessage(requireContext(), binding.getRoot(), getString(R.string.check_connections));
        }
    }

    private void verifyOtpForgotPassword(String otp) {
        try {
            showWaitDialog(requireContext(), "Verifying OTP ...");
            bodyMap = new HashMap<>();
            getParams(bodyMap, otp, mobile, userId);

            viewModel.verifyOtpForgotPassword(bodyMap, requireContext(), requireActivity()).observe(requireActivity(),
                    new ApiObserver<VerifyOtpForgotPasswordModel>(new ApiObserver.ChangeListener<VerifyOtpForgotPasswordModel>() {
                        @Override
                        public void onSuccess(VerifyOtpForgotPasswordModel response) {
                            try {
                                if (response != null && response.getStatus()) {
                                    VerifyPasswordFragment verifyPasswordFragment = new VerifyPasswordFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("userId", response.getUserId());
                                    verifyPasswordFragment.setArguments(bundle);
                                    getParentFragmentManager().beginTransaction().add(R.id.fragment_container, verifyPasswordFragment)
                                            .addToBackStack("OtpFragment").commit();
                                } else if (response != null && !response.getStatus() && !response.getError().isEmpty()) {
                                    showMessage(requireContext(), binding.getRoot(), response.getError());
                                } else {
                                    showMessage(requireContext(), binding.getRoot(), getResources().getString(R.string.something_went_wrong));
                                    requireActivity().onBackPressed();
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
        } catch (Exception ex) {
            ex.printStackTrace();
            closeWaitDialog();
        }
    }

    private void getParamsResendOtp(Map<String, RequestBody> map, String name, String phone, String userId) {
        RequestBody nameBody = RequestBody.create(name, MediaType.parse("text/plain"));
        RequestBody phoneBody = RequestBody.create(phone, MediaType.parse("text/plain"));
        RequestBody userIdBody = RequestBody.create(userId, MediaType.parse("text/plain"));

        map.put(FULLNAME, nameBody);
        map.put(PHONE, phoneBody);
        map.put(USERID, userIdBody);
    }

    private void reSendOtp() {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {

            try {
                showWaitDialog(requireContext(), "Sending OTP ...");
                bodyMap = new HashMap<>();
                getParamsResendOtp(bodyMap, sharedPref.getName(), sharedPref.getMobile(), sharedPref.getUserId());

                viewModel.resendOtp(bodyMap, requireContext(), requireActivity()).observe(requireActivity(),
                        new ApiObserver<ResendOtpModel>(new ApiObserver.ChangeListener<ResendOtpModel>() {
                            @Override
                            public void onSuccess(ResendOtpModel response) {
                                try {
                                    if (response != null && response.getStatus()) {
                                        showMessage(requireContext(), binding.getRoot(), getResources().getString(R.string.otp_sent_success));
                                    } else {
                                        showMessage(requireContext(), binding.getRoot(), getResources().getString(R.string.something_went_wrong));
                                        requireActivity().onBackPressed();
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
            } catch (Exception ex) {
                ex.printStackTrace();
                closeWaitDialog();
            }
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


    private void showErrorMessage(String message) {
        showMessage(requireContext(), binding.getRoot(), message);
    }

}

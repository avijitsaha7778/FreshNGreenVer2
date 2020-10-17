package com.gios.freshngreen.fragments.login;

import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.gios.freshngreen.R;
import com.gios.freshngreen.activities.HomeActivity;
import com.gios.freshngreen.databinding.FragmentLoginBinding;
import com.gios.freshngreen.genericClasses.ApiObserver;
import com.gios.freshngreen.responseModel.login.LoginModel;
import com.gios.freshngreen.responseModel.login.SignupModel;
import com.gios.freshngreen.utils.MyUtilities;
import com.gios.freshngreen.utils.NetworkUtils;
import com.gios.freshngreen.utils.SharedPref;
import com.gios.freshngreen.viewModel.login.LoginViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;


import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.gios.freshngreen.utils.Constants.FULLNAME;
import static com.gios.freshngreen.utils.Constants.PASSWORD;
import static com.gios.freshngreen.utils.Constants.PHONE;
import static com.gios.freshngreen.utils.MyUtilities.showMessage;
import static com.gios.freshngreen.utils.Validations.checkPasswordValidates;
import static com.gios.freshngreen.utils.Validations.checkPhoneValidates;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private SharedPref sharedPref;
    private String password;
    private String phone;
    private LoginViewModel viewModel;
    private ProgressDialog waitDialog;
    Map<String, RequestBody> bodyMap;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        initVar();
        setListeners();
        return binding.getRoot();
    }

    private void initVar() {
        sharedPref = new SharedPref(requireActivity());
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModel.init();
    }

    private void setListeners() {
        binding.continueButton.setOnClickListener(v -> {
            phone = Objects.requireNonNull(binding.phone.getText()).toString().trim();
            password = Objects.requireNonNull(binding.password.getText()).toString().trim();

            if (NetworkUtils.isNetworkAvailable(requireContext())) {
                if (validate(phone, password)) {
                    login(phone, password);
                }
            } else
                showMessage(requireContext(), binding.getRoot(), getString(R.string.check_connections));
        });

        binding.signUp.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new RegisterFragment()).commit();
        });

        binding.forgotPassword.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new ForgotPasswordFragment()).addToBackStack("LoginFragment").commit();
        });


        binding.homeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.phone.clearFocus();
                binding.password.clearFocus();
                return true;
            }
        });

        binding.phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    MyUtilities.hideKeyboard(getActivity(), binding.phone);
                }
            }
        });
        binding.password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    MyUtilities.hideKeyboard(getActivity(), binding.password);
                }
            }
        });


    }

    private void getParams(Map<String, RequestBody> map, String phone, String password) {
        RequestBody phoneBody = RequestBody.create(phone, MediaType.parse("text/plain"));
        RequestBody passwordBody = RequestBody.create(password, MediaType.parse("text/plain"));

        map.put(PHONE, phoneBody);
        map.put(PASSWORD, passwordBody);
    }


    private void login(String phone, String password) {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {

            showWaitDialog(requireContext(), "Signing in...");
            bodyMap = new HashMap<>();
            getParams(bodyMap, phone, password);

            viewModel.login(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                    new ApiObserver<LoginModel>(new ApiObserver.ChangeListener<LoginModel>() {
                        @Override
                        public void onSuccess(LoginModel response) {
                            try {
                                if (response != null && response.getStatus()) {
                                    sharedPref.setName(response.getLoginUserDetails().getName());

                                    if (response.getLoginUserDetails().getPhoneVerified().equalsIgnoreCase("INACTIVE")) {
                                        OtpFragment otpFragment = new OtpFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("userId", response.getLoginUserDetails().getUserId());
                                        bundle.putString("mobile", response.getLoginUserDetails().getPhoneNo());
                                        otpFragment.setArguments(bundle);
                                        getParentFragmentManager().beginTransaction().add(R.id.fragment_container, otpFragment).addToBackStack("registerFragment").commit();

                                    } else if (response.getLoginUserDetails().getPhoneVerified().equalsIgnoreCase("ACTIVE")) {
                                        sharedPref.setMobile(response.getLoginUserDetails().getPhoneNo());
                                        sharedPref.setUserId(response.getLoginUserDetails().getUserId());
                                        startActivity(new Intent(requireActivity(), HomeActivity.class));
                                        requireActivity().finish();
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


    private boolean validate(String phone, String password) {
        boolean isValidate = true;
        if (phone != null && phone.isEmpty()) {
            isValidate = false;
            showMessage(requireContext(), binding.getRoot(), "Please enter mobile number");
        } else if (phone != null && phone.length() != 10) {
            isValidate = false;
            showMessage(requireContext(), binding.getRoot(), "Mobile number must be 10 digits");
        } else if (password != null && password.isEmpty()) {
            isValidate = false;
            showMessage(requireContext(), binding.getRoot(), "Please enter password");
        } else if (password != null && password.length() < 8) {
            isValidate = false;
            showMessage(requireContext(), binding.getRoot(), "Password must contain 8 characters");
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

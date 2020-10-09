package com.gios.freshngreen.fragments.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import com.gios.freshngreen.R;
import com.gios.freshngreen.databinding.FragmentRegisterBinding;
import com.gios.freshngreen.genericClasses.ApiObserver;
import com.gios.freshngreen.responseModel.login.SignupModel;
import com.gios.freshngreen.utils.MyUtilities;
import com.gios.freshngreen.utils.NetworkUtils;
import com.gios.freshngreen.utils.SharedPref;
import com.gios.freshngreen.viewModel.login.RegistrationViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.gios.freshngreen.utils.Constants.FULLNAME;
import static com.gios.freshngreen.utils.Constants.IS_CONNECTED;
import static com.gios.freshngreen.utils.Constants.PASSWORD;
import static com.gios.freshngreen.utils.Constants.PHONE;
import static com.gios.freshngreen.utils.MyUtilities.showMessage;
import static com.gios.freshngreen.utils.Validations.checkConfirmPasswordValidates;
import static com.gios.freshngreen.utils.Validations.checkEmailValidates;
import static com.gios.freshngreen.utils.Validations.checkPasswordValidates;
import static com.gios.freshngreen.utils.Validations.checkPhoneValidates;
import static com.gios.freshngreen.utils.Validations.isNameValidates;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;
    private RegistrationViewModel viewModel;
    Map<String, RequestBody> bodyMap;
    private ProgressDialog waitDialog;
    private String name;


    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false);


        initViews();
        setListeners();
        return binding.getRoot();
    }

    private void initViews() {
        try {
            viewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
            viewModel.init();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setListeners() {
        binding.continueButton.setOnClickListener(v -> {
            if (NetworkUtils.isNetworkAvailable(requireContext())) {
                signUp();
            } else {
                showMessage(requireContext(), binding.getRoot(), getString(R.string.check_connections));
            }
        });

        binding.signIn.setOnClickListener(v ->
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new LoginFragment())
                        .commit());

        binding.homeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.phone.clearFocus();
                binding.name.clearFocus();
                binding.password.clearFocus();
                binding.confirmPassword.clearFocus();
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
        binding.name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    MyUtilities.hideKeyboard(getActivity(), binding.name);
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
        binding.confirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    MyUtilities.hideKeyboard(getActivity(), binding.confirmPassword);
                }
            }
        });
    }


    private void getParams(Map<String, RequestBody> map, String name, String phone, String password) {
        RequestBody nameBody = RequestBody.create(name, MediaType.parse("text/plain"));
        RequestBody phoneBody = RequestBody.create(phone, MediaType.parse("text/plain"));
        RequestBody passwordBody = RequestBody.create(password, MediaType.parse("text/plain"));

        map.put(FULLNAME, nameBody);
        map.put(PHONE, phoneBody);
        map.put(PASSWORD, passwordBody);
    }

    private void signUp() {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {
            name = Objects.requireNonNull(binding.name.getText()).toString().trim();
            String phone = Objects.requireNonNull(binding.phone.getText()).toString().trim();
            String password = Objects.requireNonNull(binding.password.getText()).toString().trim();

            if (validateAllValues()) {
                showWaitDialog(requireContext(), "Signing up...");
                bodyMap = new HashMap<>();
                getParams(bodyMap, name, phone, password);

                viewModel.signUp(bodyMap, requireContext(), requireActivity()).observe(requireActivity(),
                        new ApiObserver<SignupModel>(new ApiObserver.ChangeListener<SignupModel>() {
                            @Override
                            public void onSuccess(SignupModel response) {
                                try {
                                    if (response != null && response.getStatus()) {
                                        SharedPref mSharedPref = new SharedPref(requireContext());
                                        mSharedPref.setName(name);

                                        OtpFragment otpFragment = new OtpFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("otp", response.getOtp());
                                        bundle.putString("userId", response.getUserId());
                                        bundle.putString("mobile", response.getPhoneNo());
                                        otpFragment.setArguments(bundle);
                                        getParentFragmentManager().beginTransaction().add(R.id.fragment_container, otpFragment).addToBackStack("registerFragment").commit();
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
        } else {
            showMessage(requireContext(), binding.getRoot(), getString(R.string.check_connections));
        }
    }

    private boolean validateAllValues() {
        boolean isValidate = true;
        if (binding.name.getText().toString().isEmpty()) {
            isValidate = false;
            showMessage(requireContext(), binding.getRoot(), "Please enter name");
        } else if (binding.phone.getText().toString().isEmpty()) {
            isValidate = false;
            showMessage(requireContext(), binding.getRoot(), "Please enter mobile number");
        } else if (binding.phone.getText().toString().length() != 10) {
            isValidate = false;
            showMessage(requireContext(), binding.getRoot(), "Mobile number must be 10 digits");
        } else if (binding.password.getText().toString().isEmpty()) {
            isValidate = false;
            showMessage(requireContext(), binding.getRoot(), "Please enter password");
        } else if (binding.confirmPassword.getText().toString().isEmpty()) {
            isValidate = false;
            showMessage(requireContext(), binding.getRoot(), "Please enter confirm password");
        } else if (binding.password.getText().toString().length() < 8) {
            isValidate = false;
            showMessage(requireContext(), binding.getRoot(), "Password must contain 8 characters");
        } else if (!binding.confirmPassword.getText().toString().equalsIgnoreCase(binding.password.getText().toString())) {
            isValidate = false;
            showMessage(requireContext(), binding.getRoot(), "Password and confirm password does not match");
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

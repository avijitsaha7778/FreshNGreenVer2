package com.gios.freshngreen.fragments.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.gios.freshngreen.R;
import com.gios.freshngreen.activities.LoginActivity;
import com.gios.freshngreen.activities.SplashScreenActivity;
import com.gios.freshngreen.databinding.FragmentVerifyPasswordBinding;
import com.gios.freshngreen.genericClasses.ApiObserver;
import com.gios.freshngreen.responseModel.login.ForgotPasswordModel;
import com.gios.freshngreen.responseModel.login.VerifyPasswordModel;
import com.gios.freshngreen.utils.MyUtilities;
import com.gios.freshngreen.utils.NetworkUtils;
import com.gios.freshngreen.utils.SharedPref;
import com.gios.freshngreen.viewModel.login.VerifyPasswordViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.gios.freshngreen.utils.Constants.LOGIN;
import static com.gios.freshngreen.utils.Constants.PASSWORD;
import static com.gios.freshngreen.utils.Constants.PHONE;
import static com.gios.freshngreen.utils.Constants.STATUS;
import static com.gios.freshngreen.utils.Constants.TYPE;
import static com.gios.freshngreen.utils.Constants.USERID;
import static com.gios.freshngreen.utils.MyUtilities.showMessage;

public class VerifyPasswordFragment extends Fragment {

    private FragmentVerifyPasswordBinding binding;
    private VerifyPasswordViewModel viewModel;
    private String email;
    private String userId;
    private String password;
    private ProgressDialog waitDialog;
    Map<String, RequestBody> bodyMap;
    private SharedPref sharedPref;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVerifyPasswordBinding.inflate(inflater, container, false);

        try {
            if (getArguments() != null) {
                userId = getArguments().getString("userId");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        initVar();
        setListeners();

        return binding.getRoot();
    }

    private void initVar() {
        viewModel = new ViewModelProvider(this).get(VerifyPasswordViewModel.class);
        viewModel.init();
        sharedPref = new SharedPref(requireActivity());
    }

    private void setListeners() {
        binding.continueButton.setOnClickListener(v -> {
            binding.password.clearFocus();
            binding.confirmPassword.clearFocus();
            password = Objects.requireNonNull(binding.password.getText()).toString().trim();

            if (NetworkUtils.isNetworkAvailable(requireContext())) {
                if (validateAllValues()) {
                    changePassword(password);
                }
            } else {
                showMessage(requireContext(), binding.getRoot(), getString(R.string.check_connections));
            }
        });


        binding.homeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.password.clearFocus();
                binding.confirmPassword.clearFocus();
                return true;
            }
        });

        binding.password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    MyUtilities.hideKeyboard(getActivity(),binding.password);
                }
            }
        });
        binding.confirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    MyUtilities.hideKeyboard(getActivity(),binding.confirmPassword);
                }
            }
        });
    }

    private void getParams(Map<String, RequestBody> map, String userId, String password) {
        RequestBody phoneBody = RequestBody.create(password, MediaType.parse("text/plain"));
        RequestBody userIdBody = RequestBody.create(userId, MediaType.parse("text/plain"));
        map.put(PASSWORD, phoneBody);
        map.put(USERID, userIdBody);
    }

    private void changePassword(String password) {
        if(NetworkUtils.isNetworkAvailable(requireContext())) {

            showWaitDialog(requireContext(),"Sending...");
        bodyMap = new HashMap<>();
        getParams(bodyMap, userId, password);

        viewModel.verifyPassword(bodyMap, requireContext(), requireActivity()).observe(requireActivity(),
                new ApiObserver<VerifyPasswordModel>(new ApiObserver.ChangeListener<VerifyPasswordModel>() {
                    @Override
                    public void onSuccess(VerifyPasswordModel response) {
                        try {
                            if (response != null && response.getStatus())
                            {
                                showMessage(requireContext(), binding.getRoot(), "Password changed successfully");
                                sharedPref.clearAllPrefs(requireContext());

                                startActivity(new Intent(requireActivity(), LoginActivity.class).putExtra(TYPE,LOGIN));
                                requireActivity().finish();
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
        }else{
            showMessage(requireContext(), binding.getRoot(), getString(R.string.check_connections));
        }
    }


    private boolean validateAllValues() {
        boolean isValidate = true;
        if(binding.password.getText().toString().isEmpty()){
            isValidate = false;
            showMessage(requireContext(),binding.getRoot(),"Please enter password");
        }else if(binding.confirmPassword.getText().toString().isEmpty()){
            isValidate = false;
            showMessage(requireContext(),binding.getRoot(),"Please enter confirm password");
        }else if(binding.password.getText().toString().length() < 8){
            isValidate = false;
            showMessage(requireContext(),binding.getRoot(),"Password must contain 8 characters");
        }
        else if(!binding.confirmPassword.getText().toString().equalsIgnoreCase(binding.password.getText().toString())){
            isValidate = false;
            showMessage(requireContext(),binding.getRoot(),"Password and confirm password does not match");
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
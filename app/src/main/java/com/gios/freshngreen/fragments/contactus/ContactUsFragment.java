package com.gios.freshngreen.fragments.contactus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gios.freshngreen.R;
import com.gios.freshngreen.activities.HomeActivity;
import com.gios.freshngreen.adapter.ProductListAdapter;
import com.gios.freshngreen.databinding.FragmentContactUsBinding;
import com.gios.freshngreen.databinding.FragmentProductListBinding;
import com.gios.freshngreen.dialogs.SortDialog;
import com.gios.freshngreen.fragments.product.ProductListFragment;
import com.gios.freshngreen.fragments.product.ProductListFragmentDirections;
import com.gios.freshngreen.genericClasses.ApiObserver;
import com.gios.freshngreen.responseModel.cart.AddCartModel;
import com.gios.freshngreen.responseModel.contactus.ContactDetail;
import com.gios.freshngreen.responseModel.contactus.ContactUsModel;
import com.gios.freshngreen.responseModel.product.ProductList;
import com.gios.freshngreen.responseModel.product.ProductModel;
import com.gios.freshngreen.responseModel.wishlist.AddWishlistModel;
import com.gios.freshngreen.responseModel.wishlist.RemoveWishlistModel;
import com.gios.freshngreen.utils.NetworkUtils;
import com.gios.freshngreen.utils.SharedPref;
import com.gios.freshngreen.utils.SpacesItemDecoration;
import com.gios.freshngreen.viewModel.contactus.ContactUsViewModel;

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
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static androidx.navigation.Navigation.findNavController;
import static com.gios.freshngreen.utils.Constants.ACTION;
import static com.gios.freshngreen.utils.Constants.CATEGORYID;
import static com.gios.freshngreen.utils.Constants.PRODUCTID;
import static com.gios.freshngreen.utils.Constants.QUANTITY;
import static com.gios.freshngreen.utils.Constants.SUBCATEGORYID;
import static com.gios.freshngreen.utils.Constants.USERID;
import static com.gios.freshngreen.utils.MyUtilities.showMessage;

public class ContactUsFragment extends Fragment {
    private FragmentContactUsBinding binding;
    private SharedPref sharedPref;
    private ContactUsViewModel viewModel;
    private ProgressDialog waitDialog;
    Map<String, RequestBody> bodyMap;


    public ContactUsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentContactUsBinding.inflate(inflater, container, false);


        initVar();
        setListeners();
        getContactInfo();
        return binding.getRoot();
    }

    private void initVar() {
        try {
            HomeActivity.setScreenName("Contact Us");

            sharedPref = new SharedPref(requireActivity());
            viewModel = new ViewModelProvider(this).get(ContactUsViewModel.class);
            viewModel.init();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setListeners() {
        binding.phoneLayout.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + binding.phone.getText().toString().trim()));
                startActivity(intent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }


    private void getParams(Map<String, RequestBody> map, String userId) {
        try {
            RequestBody userIdBody = RequestBody.create(userId, MediaType.parse("text/plain"));
            map.put(USERID, userIdBody);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getContactInfo() {
        try {
            if (NetworkUtils.isNetworkAvailable(requireContext())) {

                showWaitDialog(requireContext(), "Loading...");
                bodyMap = new HashMap<>();
                getParams(bodyMap, sharedPref.getUserId());

                viewModel.contactUs(bodyMap, requireActivity(), requireContext()).observe(requireActivity(),
                        new ApiObserver<ContactUsModel>(new ApiObserver.ChangeListener<ContactUsModel>() {
                            @Override
                            public void onSuccess(ContactUsModel response) {
                                try {
                                    if (response != null && response.getStatus()) {
                                        if (response.getContactDetails().size() > 0) {
                                            ContactDetail mContactDetail = response.getContactDetails().get(0);
                                            if (mContactDetail != null && mContactDetail.getAddress() != null && !mContactDetail.getAddress().isEmpty()) {
                                                binding.address.setText(mContactDetail.getAddress());
                                            } else {
                                                binding.address.setText("-");
                                            }

                                            if (mContactDetail != null && mContactDetail.getPhoneNo() != null && !mContactDetail.getPhoneNo().isEmpty()) {
                                                binding.phone.setText(mContactDetail.getPhoneNo());
                                            } else {
                                                binding.phone.setText("-");
                                            }

                                            if (mContactDetail != null && mContactDetail.getEmailId() != null && !mContactDetail.getEmailId().isEmpty()) {
                                                binding.email.setText(mContactDetail.getEmailId());
                                            } else {
                                                binding.email.setText("-");
                                            }
                                        }

                                    } else {
                                        showMessage(requireContext(), binding.getRoot(), response.getError());
                                        HomeActivity.setScreenName("Oops!");
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
        } catch (Exception ex) {
            ex.printStackTrace();
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

}
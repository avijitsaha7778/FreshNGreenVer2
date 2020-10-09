package com.gios.freshngreen.fragments.home;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.gios.freshngreen.R;
import com.gios.freshngreen.activities.HomeActivity;
import com.gios.freshngreen.adapter.HomeCategoriesHorizontalAdapter;
import com.gios.freshngreen.databinding.FragmentHomeBinding;
import com.gios.freshngreen.databinding.FragmentLoginBinding;
import com.gios.freshngreen.databinding.FragmentProfileBinding;
import com.gios.freshngreen.dialogs.EditAddressDialog;
import com.gios.freshngreen.dialogs.EditProfileDialog;
import com.gios.freshngreen.dialogs.PhotoChooserDialog;
import com.gios.freshngreen.fragments.login.ForgotPasswordFragment;
import com.gios.freshngreen.fragments.login.RegisterFragment;
import com.gios.freshngreen.genericClasses.ApiObserver;
import com.gios.freshngreen.responseModel.home.CategoriesModel;
import com.gios.freshngreen.responseModel.home.Category;
import com.gios.freshngreen.responseModel.login.LoginModel;
import com.gios.freshngreen.responseModel.profile.AreaList;
import com.gios.freshngreen.responseModel.profile.GetAreaListModel;
import com.gios.freshngreen.responseModel.profile.GetProfileModel;
import com.gios.freshngreen.responseModel.profile.PinList;
import com.gios.freshngreen.responseModel.profile.UpdateProfileModel;
import com.gios.freshngreen.responseModel.profile.UserDetail;
import com.gios.freshngreen.utils.CircleTransform;
import com.gios.freshngreen.utils.MyUtilities;
import com.gios.freshngreen.utils.NetworkUtils;
import com.gios.freshngreen.utils.SharedPref;
import com.gios.freshngreen.viewModel.home.HomeFragmentViewModel;
import com.gios.freshngreen.viewModel.login.LoginViewModel;
import com.gios.freshngreen.viewModel.profile.ProfileViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static com.gios.freshngreen.utils.Constants.ADDRESS;
import static com.gios.freshngreen.utils.Constants.ANNIVERSARY;
import static com.gios.freshngreen.utils.Constants.AREA;
import static com.gios.freshngreen.utils.Constants.CATEGORYID;
import static com.gios.freshngreen.utils.Constants.CITY;
import static com.gios.freshngreen.utils.Constants.DOB;
import static com.gios.freshngreen.utils.Constants.EMAIL;
import static com.gios.freshngreen.utils.Constants.FULLNAME;
import static com.gios.freshngreen.utils.Constants.FULL_NAME;
import static com.gios.freshngreen.utils.Constants.LANDARK;
import static com.gios.freshngreen.utils.Constants.PINCODE;
import static com.gios.freshngreen.utils.Constants.PROFILE_IMAGE;
import static com.gios.freshngreen.utils.Constants.USERID;
import static com.gios.freshngreen.utils.ImageFilePath.getPath;
import static com.gios.freshngreen.utils.MyUtilities.getResizedBitmap;
import static com.gios.freshngreen.utils.MyUtilities.showMessage;


public class ProfileFragment extends Fragment implements EditProfileDialog.EditProfileDialogInterface,
        EditAddressDialog.EditAddressDialogInterface, PhotoChooserDialog.Interface {
    String[] PERMISSIONS_CAMERA = {
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE

    };

    String[] PERMISSIONS_FILE_PICK = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final int REQUEST_IMAGE_CAPTURE = 100;
    private static final int REQUEST_IMAGE_GALLERY = 200;
    private final int PERMISSION_REQUEST_CAMERA = 300;
    private final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 400;
    private String currentImagePath = "";
    private String imageFilepath = "";


    private FragmentProfileBinding binding;
    private SharedPref sharedPref;
    private String password;
    private String phone;
    private ProfileViewModel viewModel;
    private ProgressDialog waitDialog;
    Map<String, RequestBody> bodyMap;

    private int mYear;
    private int mMonth;
    private int mDay;
    private String dobVal;
    private String anniversaryDateVal;
    private UserDetail userDetail;
    private List<AreaList> areaList = new ArrayList<>();
    private List<PinList> pinList = new ArrayList<>();


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);

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
        HomeActivity.setScreenName("Profile");

        sharedPref = new SharedPref(requireContext());
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        viewModel.init();

        if (!sharedPref.getMobile().isEmpty()) {
            binding.mobileNumber.setText(sharedPref.getMobile());
        }
    }

    private void setListeners() {
        dateOfBirthSelection();
        dateOfMarriageSelection();

        binding.editIcon.setOnClickListener(v -> {
            EditProfileDialog mEditProfileDialog = new EditProfileDialog(this, binding, userDetail);
            mEditProfileDialog.show(requireActivity().getSupportFragmentManager(), "edit_profile");
        });

        binding.editAddressIcon.setOnClickListener(v -> {
            EditAddressDialog mEditAddressDialog = new EditAddressDialog(this, userDetail, areaList, pinList);
            mEditAddressDialog.show(requireActivity().getSupportFragmentManager(), "edit_address");
        });

        binding.profileImg.setOnClickListener(v -> {
            PhotoChooserDialog mPhotoChooserDialog = new PhotoChooserDialog(this, binding);
            mPhotoChooserDialog.show(requireActivity().getSupportFragmentManager(), "camera_chooser");
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
                                    sharedPref.setName(userDetail.getFullName());
                                    setUpProfile(response.getUserDetails().get(0));
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

    private void getParamsUpdate(Map<String, RequestBody> map, String userId, String fullName, String email, String address, String landMark, String area, String pin
            , String city, String dob, String anniversaryDate) {

        try {
            RequestBody userIdBody = RequestBody.create(userId, MediaType.parse("text/plain"));
            RequestBody fullNameBody = RequestBody.create(fullName, MediaType.parse("text/plain"));
            RequestBody emailBody = RequestBody.create(email, MediaType.parse("text/plain"));
            RequestBody addressBody = RequestBody.create(address, MediaType.parse("text/plain"));
            RequestBody landMarkBody = RequestBody.create(landMark, MediaType.parse("text/plain"));
            RequestBody areaBody = RequestBody.create(area, MediaType.parse("text/plain"));
            RequestBody pinBody = RequestBody.create(pin, MediaType.parse("text/plain"));
            RequestBody cityBody = RequestBody.create(city, MediaType.parse("text/plain"));
            RequestBody dobBody = RequestBody.create(dob, MediaType.parse("text/plain"));
            RequestBody anniversaryDateBody = RequestBody.create(anniversaryDate, MediaType.parse("text/plain"));


            map.put(USERID, userIdBody);
            map.put(FULL_NAME, fullNameBody);
            map.put(EMAIL, emailBody);
            map.put(ADDRESS, addressBody);
            map.put(LANDARK, landMarkBody);
            map.put(AREA, areaBody);
            map.put(PINCODE, pinBody);
            map.put(CITY, cityBody);
            map.put(DOB, dobBody);
            map.put(ANNIVERSARY, anniversaryDateBody);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateProfileDetails(String fullName, String email, String address, String landMark, String area, String pin
            , String city, String dob, String anniversaryDate) {
        if (NetworkUtils.isNetworkAvailable(requireContext())) {
            showWaitDialog(requireContext(), "Loading...");
            bodyMap = new HashMap<>();
            MultipartBody.Part part;
            if (imageFilepath != null && !imageFilepath.isEmpty()) {
                part = MyUtilities.convertIntoMultipart(imageFilepath);
            } else {
                RequestBody fileReqBody = RequestBody.create(MediaType.parse("text/plain"), "");
                part = MultipartBody.Part.createFormData("userImage", "", fileReqBody);
            }

            getParamsUpdate(bodyMap, sharedPref.getUserId(), fullName, email, address, landMark, area, pin, city, dob, anniversaryDate);

            viewModel.updateProfileDetails(bodyMap, part, requireActivity(), requireContext()).observe(requireActivity(),
                    new ApiObserver<UpdateProfileModel>(new ApiObserver.ChangeListener<UpdateProfileModel>() {
                        @Override
                        public void onSuccess(UpdateProfileModel response) {
                            try {
                                if (response != null && response.getStatus()) {
                                    if (response.getUserDetails().size() > 0) {
                                        imageFilepath = "";
                                        userDetail = response.getUserDetails().get(0);
                                        sharedPref.setName(userDetail.getFullName());
                                        showMessage(requireContext(), binding.getRoot(), "Updated Successfully.");
                                        setUpProfile(response.getUserDetails().get(0));
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

    private void setUpProfile(UserDetail userDetail) {
        try {
            binding.progressBar.setVisibility(View.VISIBLE);
            if (userDetail != null && userDetail.getFullName() != null && !userDetail.getFullName().isEmpty()) {
                binding.userName.setText(userDetail.getFullName());
            }
            if (userDetail != null && userDetail.getEmailId() != null && !userDetail.getEmailId().isEmpty()) {
                binding.email.setText(userDetail.getEmailId());
            }
            if (userDetail != null && userDetail.getAddress() != null && !userDetail.getAddress().isEmpty()) {
                binding.address.setText(userDetail.getAddress() + ",\n" + userDetail.getArea() + ",\n" +
                        userDetail.getLandmark() + ",\n" + userDetail.getCity() + ",\n" +
                        "Pincode- " + userDetail.getPin());
            }
            if (userDetail != null && userDetail.getDateOfBirth() != null && !userDetail.getDateOfBirth().isEmpty()) {
                binding.dob.setText(formatDate(userDetail.getDateOfBirth(), "yyyy-MM-dd", "dd MMMM yyyy"));
            }
            if (userDetail != null && userDetail.getDateOfAnniversary() != null && !userDetail.getDateOfAnniversary().isEmpty()) {
                binding.anniversaryDate.setText(formatDate(userDetail.getDateOfAnniversary(), "yyyy-MM-dd", "dd MMMM yyyy"));
            }
            if (userDetail != null && userDetail.getProfileImage() != null && !userDetail.getProfileImage().isEmpty()) {
                Picasso.get().load(userDetail.getProfileImage()).transform(new CircleTransform()).into(binding.profileImg,
                        new Callback() {
                            @Override
                            public void onSuccess() {
                                binding.progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void dateOfBirthSelection() {
        try {
            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            c.set(mYear, mMonth, mDay);
            DatePickerDialog.OnDateSetListener mDateSetListener =
                    (view, year, monthOfYear, dayOfMonth) -> {
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                        updateDisplay();
                    };
            final DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(), R.style.DatePickerDialogTheme,
                    mDateSetListener,
                    mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());

            binding.editDobIcon.setOnClickListener(view -> {
                datePickerDialog.show();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateDisplay() {
        try {
            String mDayRefined = mDay < 10 ? "0" + mDay : mDay + "";
            String mMonthRefined = (mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1) + "";
            // Month is 0 based so add 1
            dobVal = mYear + "-" +
                    mMonthRefined + "-" +
                    mDayRefined + "";

//            binding.dob.setText(formatDate(dobVal,"yyyy-MM-dd","dd MMMM yyyy"));
            String areaId = "";
            String pinId = "";
            if (userDetail.getArea() != null) {
                for (AreaList mAreaList : areaList) {
                    if (userDetail.getArea().equals(mAreaList.getAreaName())) {
                        areaId = mAreaList.getId();
                    }
                }
            }
            if (userDetail.getPin() != null) {
                for (PinList mPinList : pinList) {
                    if (userDetail.getPin().equals(mPinList.getPin())) {
                        pinId = mPinList.getId();
                    }
                }
            }

            updateProfileDetails(userDetail.getFullName(), userDetail.getEmailId(), userDetail.getAddress(),
                    userDetail.getLandmark(), areaId, pinId, userDetail.getCity()
                    , dobVal
                    , userDetail.getDateOfAnniversary());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void dateOfMarriageSelection() {
        try {
            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            c.set(mYear, mMonth, mDay);
            DatePickerDialog.OnDateSetListener mDateSetListener =
                    (view, year, monthOfYear, dayOfMonth) -> {
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                        updateDisplayMarriage();
                    };
            final DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(), R.style.DatePickerDialogTheme,
                    mDateSetListener,
                    mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());

            binding.editAnniversaryIcon.setOnClickListener(view -> {
                datePickerDialog.show();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateDisplayMarriage() {
        try {
            String mDayRefined = mDay < 10 ? "0" + mDay : mDay + "";
            String mMonthRefined = (mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1) + "";
            // Month is 0 based so add 1
            anniversaryDateVal = mYear + "-" +
                    mMonthRefined + "-" +
                    mDayRefined + "";

//            binding.anniversaryDate.setText(formatDate(anniversaryDateVal,"yyyy-MM-dd","dd MMMM yyyy"));

            String areaId = "";
            String pinId = "";
            if (userDetail.getArea() != null) {
                for (AreaList mAreaList : areaList) {
                    if (userDetail.getArea().equals(mAreaList.getAreaName())) {
                        areaId = mAreaList.getId();
                    }
                }
            }
            if (userDetail.getPin() != null) {
                for (PinList mPinList : pinList) {
                    if (userDetail.getPin().equals(mPinList.getPin())) {
                        pinId = mPinList.getId();
                    }
                }
            }

            updateProfileDetails(userDetail.getFullName(), userDetail.getEmailId(), userDetail.getAddress(),
                    userDetail.getLandmark(), areaId, pinId, userDetail.getCity()
                    , userDetail.getDateOfBirth()
                    , anniversaryDateVal);
        } catch (Exception ex) {
            ex.printStackTrace();
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
    public void onEditClick(FragmentProfileBinding binding, UserDetail userDetail, String name, String email) {
        this.imageFilepath = imageFilepath;
        String areaId = "";
        String pinId = "";
        if (userDetail.getArea() != null) {
            for (AreaList mAreaList : areaList) {
                if (userDetail.getArea().equals(mAreaList.getAreaName())) {
                    areaId = mAreaList.getId();
                }
            }
        }
        if (userDetail.getPin() != null) {
            for (PinList mPinList : pinList) {
                if (userDetail.getPin().equals(mPinList.getPin())) {
                    pinId = mPinList.getId();
                }
            }
        }
        updateProfileDetails(name, email, userDetail.getAddress(),
                userDetail.getLandmark(),areaId, pinId, userDetail.getCity()
                , formatDate(binding.dob.getText().toString(), "dd MMMM yyyy", "yyyy-MM-dd")
                , formatDate(binding.anniversaryDate.getText().toString(), "dd MMMM yyyy", "yyyy-MM-dd"));

    }

    @Override
    public void onAddressClick(String address1, String address2, String area, String city,
                               String pinCode, String landmark) {
        String address;
        if (address2.isEmpty()) {
            address = address1;
        } else {
            address = address1 + ", " + address2;
        }
        updateProfileDetails(binding.userName.getText().toString(), binding.email.getText().toString(), address,
                landmark, area, pinCode, city, formatDate(binding.dob.getText().toString(), "dd MMMM yyyy", "yyyy-MM-dd")
                , formatDate(binding.anniversaryDate.getText().toString(), "dd MMMM yyyy", "yyyy-MM-dd"));

    }

    @Override
    public void onCancelClick() {
    }

    @Override
    public void onCancelClick(FragmentProfileBinding binding) {

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void requestPermissionFILE_PICK() {
        this.requestPermissions(PERMISSIONS_FILE_PICK, PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
    }

    private void requestPermissionCAMERA() {
        this.requestPermissions(PERMISSIONS_CAMERA, PERMISSION_REQUEST_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Snackbar.make(binding.getRoot(), "Need Camera Permission to open Camera", Snackbar.LENGTH_LONG)
                            .setAction("Enable", view -> requestPermissionCAMERA())
                            .show();
                }
                break;

            case PERMISSION_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    Snackbar.make(binding.getRoot(), "Need Storage Permission to open File Manager", Snackbar.LENGTH_LONG)
                            .setAction("Enable", view -> requestPermissionFILE_PICK())
                            .show();
                }
                break;
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }

    private void openCamera() {
        Intent intent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {

            File imageFile = null;

            try {
                imageFile = MyUtilities.getImageFile(requireActivity(), ".jpg");
                currentImagePath = "file:" + imageFile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (imageFile != null) {
                Uri imageUri = FileProvider.getUriForFile(requireActivity(), "com.gios.freshngreen.fileProvider", imageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    try {
                        Uri uri = Uri.parse(currentImagePath);
                        File f = new File(Objects.requireNonNull(uri.getPath()));
//                        imageFilepath = uri.getPath();

                        InputStream inputStream;
                        try {

                            ExifInterface ei = new ExifInterface(uri.getPath());
                            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                    ExifInterface.ORIENTATION_UNDEFINED);

                            inputStream = requireActivity().getContentResolver().openInputStream(uri);
                            Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);
                            selectedImage = getResizedBitmap(selectedImage, 1000);

                            Bitmap rotatedBitmap = null;
                            switch (orientation) {

                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    rotatedBitmap = rotateImage(selectedImage, 90);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    rotatedBitmap = rotateImage(selectedImage, 180);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    rotatedBitmap = rotateImage(selectedImage, 270);
                                    break;

                                case ExifInterface.ORIENTATION_NORMAL:
                                default:
                                    rotatedBitmap = selectedImage;
                            }


                            Uri tempUri = getImageUri(requireContext(), rotatedBitmap);
                            imageFilepath = getRealPathFromURI(tempUri);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }


                        Picasso.get().load(uri).transform(new CircleTransform()).into(binding.profileImg);

                        String areaId = "";
                        String pinId = "";
                        if (userDetail.getArea() != null) {
                            for (AreaList mAreaList : areaList) {
                                if (userDetail.getArea().equals(mAreaList.getAreaName())) {
                                    areaId = mAreaList.getId();
                                }
                            }
                        }
                        if (userDetail.getPin() != null) {
                            for (PinList mPinList : pinList) {
                                if (userDetail.getPin().equals(mPinList.getPin())) {
                                    pinId = mPinList.getId();
                                }
                            }
                        }

                        updateProfileDetails(userDetail.getFullName(), userDetail.getEmailId(), userDetail.getAddress(),
                                userDetail.getLandmark(), areaId, pinId, userDetail.getCity(),
                                userDetail.getDateOfBirth(), userDetail.getDateOfAnniversary());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;

                case REQUEST_IMAGE_GALLERY:
                    try {
                        if (data != null) {
                            Uri uri = data.getData();
                            if (uri != null) {
                                String path = getPath(requireContext(), uri);
//                                imageFilepath = path;

                                InputStream inputStream;
                                try {
                                    inputStream = requireActivity().getContentResolver().openInputStream(uri);
                                    Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);
                                    selectedImage = getResizedBitmap(selectedImage, 1000);

                                    Uri tempUri = getImageUri(requireContext(), selectedImage);
                                    imageFilepath = getRealPathFromURI(tempUri);

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }


                                Picasso.get().load(uri).transform(new CircleTransform()).into(binding.profileImg);

                                String areaId = "";
                                String pinId = "";
                                if (userDetail.getArea() != null) {
                                    for (AreaList mAreaList : areaList) {
                                        if (userDetail.getArea().equals(mAreaList.getAreaName())) {
                                            areaId = mAreaList.getId();
                                        }
                                    }
                                }
                                if (userDetail.getPin() != null) {
                                    for (PinList mPinList : pinList) {
                                        if (userDetail.getPin().equals(mPinList.getPin())) {
                                            pinId = mPinList.getId();
                                        }
                                    }
                                }
                                updateProfileDetails(userDetail.getFullName(), userDetail.getEmailId(), userDetail.getAddress(),
                                        userDetail.getLandmark(), areaId, pinId, userDetail.getCity(),
                                        userDetail.getDateOfBirth(), userDetail.getDateOfAnniversary());

                            }
                        } else {
                            showMessage(requireContext(), binding.getRoot(), "Upload Error");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
            }
        }
    }

    @Override
    public void onCameraOptionClick(FragmentProfileBinding binding, String option) {
        switch (option) {
            case "camera":
                if (hasPermissions(requireContext(), PERMISSIONS_CAMERA)) {
                    openCamera();
                } else {
                    requestPermissionCAMERA();
                }
                break;
            case "gallery":
                if (hasPermissions(getActivity(), PERMISSIONS_FILE_PICK)) {
                    openGallery();
                } else {
                    requestPermissionFILE_PICK();
                }
                break;

        }

    }


    @Override
    public void onCameraOptionCancelClick(FragmentProfileBinding binding) {

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = requireActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
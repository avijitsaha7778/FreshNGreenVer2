package com.gios.freshngreen.dialogs;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gios.freshngreen.R;
import com.gios.freshngreen.databinding.FragmentProfileBinding;
import com.gios.freshngreen.responseModel.profile.UserDetail;
import com.gios.freshngreen.utils.CircleTransform;
import com.gios.freshngreen.utils.MyUtilities;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static android.app.Activity.RESULT_OK;
import static com.gios.freshngreen.utils.ImageFilePath.getPath;
import static com.gios.freshngreen.utils.MyUtilities.showMessage;

public class EditProfileDialog extends BottomSheetDialogFragment {


    private EditProfileDialog.EditProfileDialogInterface mInterface;
    private FragmentProfileBinding binding;
    private UserDetail userDetail;


    public EditProfileDialog(EditProfileDialog.EditProfileDialogInterface mInterface, FragmentProfileBinding binding, UserDetail userDetail) {
        this.mInterface = mInterface;
        this.binding = binding;
        this.userDetail = userDetail;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (IllegalStateException ignored) {

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.edit_profile_dialog, null);
        dialog.setContentView(contentView);

        AppCompatEditText etEmail = contentView.findViewById(R.id.etEmail);
        AppCompatEditText etName = contentView.findViewById(R.id.etName);
        Button editBtn = (Button)contentView.findViewById(R.id.editBtn);

        etName.setText(userDetail.getFullName());
        etEmail.setText(userDetail.getEmailId());
        Pattern pattern = Patterns.EMAIL_ADDRESS;


        editBtn.setOnClickListener(v -> {
            if(etName.getText().toString().trim().isEmpty()){
                etName.setError("Please enter your name");
            }
            else if(etEmail.getText().toString().trim().isEmpty()){
                etEmail.setError("Please enter your email address");
            }
            else if (!pattern.matcher(etEmail.getText().toString().trim()).matches()) {
                etEmail.setError("Please enter a valid email address");
            }else {
                mInterface.onEditClick(binding, userDetail,
                        etName.getText().toString().trim(), etEmail.getText().toString().trim());
                dismiss();
            }
        });


    }

    public interface EditProfileDialogInterface {
        void onEditClick(FragmentProfileBinding binding,UserDetail userDetail, String name, String email);
        void onCancelClick(FragmentProfileBinding binding);
    }

    @Override
    public void onCancel(DialogInterface dialog)
    {
        super.onCancel(dialog);
        mInterface.onCancelClick(binding);
    }




}
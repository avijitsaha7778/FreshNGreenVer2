package com.gios.freshngreen.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import com.gios.freshngreen.R;
import com.gios.freshngreen.responseModel.profile.UserDetail;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class PhotoChooserDialog extends BottomSheetDialogFragment {


    private PhotoChooserDialog.Interface mInterface;
    private com.gios.freshngreen.databinding.FragmentProfileBinding binding;


    public PhotoChooserDialog(PhotoChooserDialog.Interface mInterface, com.gios.freshngreen.databinding.FragmentProfileBinding binding) {
        this.mInterface = mInterface;
        this.binding = binding;
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
        View contentView = View.inflate(getContext(), R.layout.photo_chooser, null);
        dialog.setContentView(contentView);

        ConstraintLayout camera = dialog.findViewById(R.id.camera);
        ConstraintLayout gallery = dialog.findViewById(R.id.gallery);



        camera.setOnClickListener(v -> {
                mInterface.onCameraOptionClick(binding, "camera");
                dismiss();
        });

        gallery.setOnClickListener(v -> {
                mInterface.onCameraOptionClick(binding,"gallery");
                dismiss();
        });


    }

    public interface Interface {
        void onCameraOptionClick(com.gios.freshngreen.databinding.FragmentProfileBinding binding, String option);
        void onCameraOptionCancelClick(com.gios.freshngreen.databinding.FragmentProfileBinding binding);
    }

    @Override
    public void onCancel(DialogInterface dialog)
    {
        super.onCancel(dialog);
        mInterface.onCameraOptionCancelClick(binding);
    }




}